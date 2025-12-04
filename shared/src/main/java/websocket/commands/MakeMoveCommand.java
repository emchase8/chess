package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private final ChessMove move;
    private final boolean inCheck;
    private final boolean inCheckmate;
    private final boolean inStalemate;
    private final String game;

    public MakeMoveCommand(CommandType commandType, Integer gameID, String user, String game, ChessMove move, boolean inCheck, boolean inCheckmate, boolean inStalemate, ChessGame.TeamColor joinedAs) {
        super(commandType, gameID, user);
        this.move = move;
        this.inCheck = inCheck;
        this.inCheckmate = inCheckmate;
        this.inStalemate = inStalemate;
        this.game = game;
    }

    public ChessMove getMove() {
        return move;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public boolean isInCheckmate() {
        return inCheckmate;
    }

    public boolean isInStalemate() {
        return inStalemate;
    }

    public String getGame() {
        return game;
    }
}
