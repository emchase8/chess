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
        int up_row = row + 1;
        int down_row = row - 1;
        int left_col = col - 1;
        int right_col = col + 1;
        for (int i : List.of(left_col, col, right_col)) {
            for (int j : List.of(up_row, down_row)) {
                ChessPosition my_new = new ChessPosition(j, i);
                if (validate(my_new, board, team)) {
                    moves.add(new ChessMove(myPosition, my_new, null));
                }
            }
        }
        ChessPosition right = new ChessPosition(row, right_col);
        if (validate(right, board, team)) {
            moves.add(new ChessMove(myPosition, right, null));
        }
        ChessPosition left = new ChessPosition(row, left_col);
        if (validate(left, board, team)) {
            moves.add(new ChessMove(myPosition, left, null));
        }

        return moves;
    }
}
