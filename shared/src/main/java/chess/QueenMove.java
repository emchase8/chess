package chess;

import java.util.List;
import java.util.ArrayList;

public class QueenMove {
    public static List queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        moves.addAll(RookMove.rookMoves(board, myPosition, team));
        moves.addAll(BishopMove.bishopMoves(board, myPosition, team));
        return moves;
    }
}
