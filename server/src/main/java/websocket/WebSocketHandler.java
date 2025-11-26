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
        } catch (DataAccessException e) {
            //not quite sure what these are for???
            sendMessage(ctx.session, gameID, e.getMessage());
        } catch (Exception e) {
            sendMessage(ctx.session, gameID, "Error: " + e.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed :)");
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException {
        connections.add(command.getGameID(), session);
        var msg = "";
        var notify = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        if (command.getConnectType().toLowerCase().equals("observe")) {
            msg = String.format("%s is now observing the game.", username);
            notify.setServerMessageType(ServerMessage.ServerMessageType.NOTIFICATION);
            notify.setMessage(msg);
        } else if (command.getConnectType().toLowerCase().equals("join")) {
            //MAKE IT NOTIFICATION FOR OBSERVES BUT LOAD GAME FOR PLAYERS? might be just get rid of else statement at the end?
            if (command.getJoinedAs() == ChessGame.TeamColor.WHITE) {
                msg = String.format("%s joined as the white player.", username);
            } else {
                msg = String.format("%s joined as the black player.", username);
            }
            notify.setServerMessageType(ServerMessage.ServerMessageType.LOAD_GAME);
            notify.setMessage(msg);
            try {
                SQLGameDAO temp = new SQLGameDAO();
                notify.setJsonGame(temp.getGame(command.getGameID()));
            } catch (DataAccessException e) {
                notify.setServerMessageType(ServerMessage.ServerMessageType.ERROR);
                notify.setMessage(e.getMessage());
            }
        } else {
            notify.setMessage("Error: I'm not sure how you got here, please contact the dev team.");
        }
        if (notify.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            connections.broadcastGameOne(command.getGameID(), session, notify);
        } else {
            connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
        }
    }

    private void leave(Session session, String username, UserGameCommand command) throws IOException {
        var msg = String.format("%s is leaving the game.", username);
        var notify = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notify.setMessage(msg);
        connections.broadcastExcludeCurrent(command.getGameID(), session, notify);
        connections.remove(command.getGameID(), session);
    }
}
