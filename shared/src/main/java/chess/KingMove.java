package chess;

import java.util.List;
import java.util.ArrayList;

public class KingMove {

    public static boolean validate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (1 <= row && row <= 8 && 1 <= col && col <=8 && board.getPiece(myPosition) == null) {
            return true;
        } else if (1 <= row && row <= 8 && 1 <= col && col <=8 && board.getPiece(myPosition).getTeamColor() != team) {
            return true;
        } else {
            return false;
        }
    }

    public static List kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int upRow = row + 1;
        int downRow = row - 1;
        int leftCol = col - 1;
        int rightCol = col + 1;
        for (int i : List.of(leftCol, col, rightCol)) {
            for (int j : List.of(upRow, downRow)) {
                ChessPosition myNew = new ChessPosition(j, i);
                if (validate(myNew, board, team)) {
                    moves.add(new ChessMove(myPosition, myNew, null));
                }
            }
        }
        ChessPosition right = new ChessPosition(row, rightCol);
        if (validate(right, board, team)) {
            moves.add(new ChessMove(myPosition, right, null));
        }
        ChessPosition left = new ChessPosition(row, leftCol);
        if (validate(left, board, team)) {
            moves.add(new ChessMove(myPosition, left, null));
        }

        return moves;
    }
}
