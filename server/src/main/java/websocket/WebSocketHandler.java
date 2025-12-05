package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.NotAuthException;
import io.javalin.websocket.*;
import model.requests.LeaveRequest;
import model.requests.MoveRequest;
import model.requests.ResignRequest;
import model.results.*;
import service.AuthService;
import service.GameService;
import websocket.commands.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    private String getUsername(String authToken) throws NotAuthException {
        AuthService temp = new AuthService();
        return temp.getUser(authToken);
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected :)");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        int gameID = -1;
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, command);
                //sends a message to all OTHER clients in the game
                case LEAVE -> leave(ctx.session, command);
                //look at specs to see who needs to be notified when
                case MAKE_MOVE -> makeMove(ctx.session, new Gson().fromJson(ctx.message(), MakeMoveCommand.class));
                //sends a message to ALL clients in that game
                case RESIGN -> resign(ctx.session, command);
            }
        } catch (Exception e) {
            //WATCH FOR THIS ERROR!!!
            try {
                sendMessage(ctx.session, gameID, "Error: " + e.getMessage());
            } catch (IOException ex) {
                System.out.println("Error: Something is really broken.");
            }
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed :)");
    }

    private void sendMessage(Session session, int gameID, String msg) throws IOException {
        var notify = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        notify.setErrorMessage(msg);
        connections.broadcastOnlyCurrent(gameID, session, notify);
    }

    private void connect(Session session, UserGameCommand command) throws IOException {
        connections.add(command.getGameID(), session);
        var msg = "";
        String username = "";
        try {
            username = getUsername(command.getAuthToken());
            var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            GameService temp = new GameService();
            ConnectResult result = temp.connectGame(command.getGameID(), command.getAuthToken());
            if (!result.message().isEmpty()) {
                notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
                notify.setErrorMessage(result.message());
                connections.broadcastOnlyCurrent(command.getGameID(), session, notify);
            } else {
                if (result.connectionType().toLowerCase().equals("observe")) {
                    msg = String.format("%s is now observing the game.\n", username);
                    notify.setMessage(msg);
                } else if (result.connectionType().toLowerCase().equals("join")) {
                    if (result.joinedAs() == ChessGame.TeamColor.WHITE) {
                        msg = String.format("%s joined as the white player.\n", username);
                    } else {
                        msg = String.format("%s joined as the black player.\n", username);
                    }
                    notify.setMessage(msg);
                } else {
                    notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
                    notify.setErrorMessage("Error: I'm not sure how you got here, please contact the dev team.\n");
                }
                if (notify.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
                    notify.setServerMessageType(ServerMessage.ServerMessageType.LOAD_GAME);
                    notify.setMessage(null);
                    notify.setGame(result.jsonGame());
                    connections.broadcastGameOne(command.getGameID(), session, notify);
                }
            }
        } catch (NotAuthException n) {
            try {
                sendMessage(session, command.getGameID(), "Error: " + n.getMessage());
            } catch (IOException ex) {
                System.out.println("Error: Something is really broken.");
            }
        }
    }

    private void makeMove(Session session, MakeMoveCommand command) throws IOException {
        String username = "";
        try {
            username = getUsername(command.getAuthToken());
            var msg = String.format("%s has made a move.\n", username);
            var notify = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            GameService inst = new GameService();
            MoveResult result = inst.move(new MoveRequest(command.getAuthToken(), command.getGameID(), command.getMove()));
            if (!result.message().isEmpty()) {
                notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
                notify.setErrorMessage(result.message());
                connections.broadcastOnlyCurrent(command.getGameID(), session, notify);
            } else {
                if (result.inCheck()) {
                    msg += "The other team is now in check.\n";
                } else if (result.inCheckmate()) {
                    msg += String.format("The other team is in checkmate. %s wins!\n", username);
                } else if (result.inStalemate()) {
                    msg += "This game is now in stalemate and is over.\n";
                }
                notify.setGame(result.jsonGame());
                //set to notify and then back
                connections.broadcastGameAll(command.getGameID(), notify);
                notify.setServerMessageType(ServerMessage.ServerMessageType.NOTIFICATION);
                notify.setGame(null);
                notify.setMessage(msg);
                connections.broadcastGameExculdeCurrent(command.getGameID(), session, notify);
            }
        } catch (NotAuthException n) {
            try {
                sendMessage(session, command.getGameID(), "Error: " + n.getMessage());
            } catch (IOException ex) {
                System.out.println("Error: Something is really broken.");
            }
        }
    }

    private void leave(Session session, UserGameCommand command) throws IOException {
        String username = "";
        try {
            username = getUsername(command.getAuthToken());
            GameService inst = new GameService();
            MostBasicResult resultLeave = inst.leaveGameService(new LeaveRequest(command.getAuthToken(), command.getGameID()));
            if (resultLeave.message().isEmpty()) {
                var msg = String.format("%s is leaving the game.\n", username);
                var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notify.setMessage(msg);
                connections.remove(command.getGameID(), session);
                connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
            } else {
                try {
                    sendMessage(session, command.getGameID(), "Error: " + resultLeave.message());
                } catch (IOException ex) {
                    System.out.println("Error: Something is really broken.");
                }
            }
        } catch (NotAuthException e) {
            try {
                sendMessage(session, command.getGameID(), "Error: " + e.getMessage());
            } catch (IOException ex) {
                System.out.println("Error: Something is really broken.");
            }
        }
    }

    private void resign(Session session, UserGameCommand command) throws IOException {
        String username = "";
        try {
            username = getUsername(command.getAuthToken());
            GameService inst = new GameService();
            MostBasicResult resultResign = inst.resignGameService(new ResignRequest(command.getAuthToken(), command.getGameID()));
            if (resultResign.message().isEmpty()) {
                var msg = String.format("%s is resigning from the game. This game is now over.\n", username);
                var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notify.setMessage(msg);
                connections.broadcastIncludingCurrentUser(command.getGameID(), notify);
                connections.remove(command.getGameID(), session);
            } else {
                try {
                    sendMessage(session, command.getGameID(), "Error: " + resultResign.message());
                } catch (IOException ex) {
                    System.out.println("Error: Something is really broken.");
                }
            }
        } catch (NotAuthException e) {
            try {
                sendMessage(session, command.getGameID(), "Error: " + e.getMessage());
            } catch (IOException ex) {
                System.out.println("Error: Something is really broken.");
            }
        }
    }
}
