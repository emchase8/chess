package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand {

    private final String connectType;
    private final ChessGame.TeamColor joinedAs;

    public ConnectCommand(CommandType commandType, String authToken, Integer gameID, String user, String connectType, ChessGame.TeamColor joinedAs) {
        super(commandType, authToken, gameID, user);
        this.connectType = connectType;
        this.joinedAs = joinedAs;
    }

    public String getConnectType() {
        return connectType;
    }

    public ChessGame.TeamColor getJoinedAs() {
        return joinedAs;
    }
}
