package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;

    public MakeMoveCommand(CommandType commandType, Integer gameID, String authToken, ChessMove move) {
        super(commandType, gameID, authToken);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
