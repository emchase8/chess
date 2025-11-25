package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import dataaccess.SQLAuthDAO;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

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
                case CONNECT -> connect(ctx.session, temp.getUser(command.getAuthToken()), command);
                case LEAVE -> leave(ctx.session, temp.getUser(command.getAuthToken()), command);
                case MAKE_MOVE -> makeMove(ctx.session, temp.getUser(command.getAuthToken()), (MakeMoveCommand) command);
                case RESIGN -> resign(ctx.session, temp.getUser(command.getAuthToken()), command);
            }
        } catch (DataAccessException e) {
            sendMessage(ctx.session, gameID, e.getMessage());
        } catch (Exception e) {
            sendMessage(ctx.session, gameID, "Error: " + e.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed :)");
    }

}
