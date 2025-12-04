package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDAO;
import io.javalin.websocket.*;
import websocket.commands.ConnectCommand;
import websocket.commands.*;
import dataaccess.SQLAuthDAO;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
            //IN MAKING WS FACADE, FIGURE OUT HOW TO PASS THE USERNAME INTO THE COMMAND SO I DON'T CALL THE DAO HERE!!!
            SQLAuthDAO temp = new SQLAuthDAO();
            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, temp.getUser(command.getAuthToken()), (ConnectCommand) command);
                //sends a message to all OTHER clients in the game
                case LEAVE -> leave(ctx.session, temp.getUser(command.getAuthToken()), command);
                //look at specs to see who needs to be notified when
                case MAKE_MOVE -> makeMove(ctx.session, temp.getUser(command.getAuthToken()), (MakeMoveCommand) command);
                //sends a message to ALL clients in that game
                case RESIGN -> resign(ctx.session, temp.getUser(command.getAuthToken()), command);
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
        notify.setMessage(msg);
        connections.broadcastOnlyCurrent(gameID, session, notify);
    }

    //NOTHING in ws handler can touch the DAOs, only server/server facade can do that
    private void connect(Session session, String username, ConnectCommand command) throws IOException {
        connections.add(command.getGameID(), session);
        var msg = "";
        var notify = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        if (command.getConnectType().toLowerCase().equals("observe")) {
            msg = String.format("%s is now observing the game.", username);
            notify.setMessage(msg);
        } else if (command.getConnectType().toLowerCase().equals("join")) {
            if (command.getJoinedAs() == ChessGame.TeamColor.WHITE) {
                msg = String.format("%s joined as the white player.", username);
            } else {
                msg = String.format("%s joined as the black player.", username);
            }
            notify.setMessage(msg);
        } else {
            notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
            notify.setMessage("Error: I'm not sure how you got here, please contact the dev team.");
        }
        if (notify.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            //THE SERVER FACADE WILL RETURN THE JSON CHESS GAME TO THE CLIENT
            //THE CLIENT WILL SEND THIS JSON STR IN THE CONNECT_COMMAND SO THAT WE CAN STICK IT IN THE LOAD_GAME NOTIFY
            //THE WS FACADE WILL TAKE CARE OF THE PRINTING OF THE BOARD?
            //WILL DELETE THE DAO EVENTUALLY
            try {
                SQLGameDAO temp = new SQLGameDAO();
                notify.setJsonGame(temp.getJsonGame(command.getGameID()));
            } catch (DataAccessException e) {
                notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
                notify.setMessage(e.getMessage());
            }
            connections.broadcastGameOne(command.getGameID(), session, notify);
            notify.setServerMessageType(ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
        } else if (notify.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            //IF THE DAOs ARE NOT ACCESSED IN HERE, WHEN WOULD I HAVE AN ERROR SERVER MESSAGE TYPE
            connections.broadcastOnlyCurrent(command.getGameID(), session, notify);
        }
    }

    //NOTHING in ws handler can touch the DAOs, only server/server facade can do that
    private void leave(Session session, String username, UserGameCommand command) throws IOException {
        var msg = String.format("%s is leaving the game.", username);
        var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notify.setMessage(msg);
        //CLIENT WILL CALL SERVER FACADE TO REMOVE THE PLAYER FROM THE GAME IN THE DB
        //WILL DELETE EVENTUALLY
        try {
            SQLGameDAO temp = new SQLGameDAO();
            String team = temp.getPlayerTeam(command.getGameID(), username);
            temp.removePlayer(command.getGameID(), team);
            notify.setMessage(msg);
        } catch (DataAccessException e) {
            notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
            notify.setMessage(e.getMessage());
        }
        if (notify.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            //IF THE DAOs ARE NOT ACCESSED IN HERE, WHEN WOULD I HAVE AN ERROR SERVER MESSAGE TYPE
            connections.broadcastOnlyCurrent(command.getGameID(), session, notify);
        } else {
            connections.remove(command.getGameID(), session);
            connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
        }
    }

    //NOTHING in ws handler can touch the DAOs, only server/server facade can do that
    private void resign(Session session, String username, UserGameCommand command) throws IOException {
        var msg = String.format("%s is resigning from the game. This game is now over.", username);
        var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        //CLIENT WILL CALL SERVER FACADE TO RESIGN THE GAME IN THE DB
        //WILL DELETE EVENTUALLY
        try {
            SQLGameDAO temp = new SQLGameDAO();
            String jsonGame = temp.getJsonGame(command.getGameID());
            var serializer = new Gson();
            var game = serializer.fromJson(jsonGame, ChessGame.class);
            game.setGameActive(false);
            var newJsonGame = serializer.toJson(game);
            temp.updateGame(command.getGameID(), newJsonGame);
            notify.setMessage(msg);
        } catch (DataAccessException e) {
            notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
            notify.setMessage(e.getMessage());
        }
        if (notify.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            //IF THE DAOs ARE NOT ACCESSED IN HERE, WHEN WOULD I HAVE AN ERROR SERVER MESSAGE TYPE
            connections.broadcastOnlyCurrent(command.getGameID(), session, notify);
        } else {
            connections.remove(command.getGameID(), session);
            connections.broadcastIncludingCurrentUser(command.getGameID(), notify);
        }
    }
}
