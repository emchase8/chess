package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebsocketFacade extends Endpoint {

    Session session;
    NotificationHandler nHandler;

    public WebsocketFacade(String url, NotificationHandler nHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.nHandler = nHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    nHandler.notify(notification);
                }
            });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(Integer gameID, String user, String connectType, ChessGame.TeamColor joinedAs) throws Exception {
        try {
            var connectCommand = new ConnectCommand(UserGameCommand.CommandType.CONNECT, gameID, user, connectType, joinedAs);
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCommand));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void leave(Integer gameID, String user) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, gameID, user);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void resign(Integer gameID, String user) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, gameID, user);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void move(Integer gameID, String user, String game, ChessMove move, boolean inCheck, boolean inCheckmate, boolean inStalemate, ChessGame.TeamColor joinedAs) throws Exception {
        try {
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, gameID, user, game, move, inCheck, inCheckmate, inStalemate, joinedAs);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
