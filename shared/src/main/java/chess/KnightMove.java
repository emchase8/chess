package chess;

import java.util.List;
import java.util.ArrayList;

public class KnightMove {

    public static boolean validate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (1 <= row && row <= 8 && 1 <= col && col <= 8 && board.getPiece(myPosition) == null) {
            return true;
        } else if (1 <= row && row <= 8 && 1 <= col && col <= 8 && board.getPiece(myPosition).getTeamColor() != team) {
            return true;
        } else {
            return false;
        }
    }

    public static List knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int up_2 = row+2;
        int up_1 = row+1;
        int down_1 = row-1;
        int down_2 = row-2;
        int left_2 = col-2;
        int left_1 = col-1;
        int right_1 = col+1;
        int right_2 = col+2;
        for (int i: List.of(up_1, down_1)) {
            ChessPosition test_1 = new ChessPosition(i, left_2);
            if (validate(test_1, board, team)) {
                moves.add(new ChessMove(myPosition, test_1, null));
            }
        }
        for (int i: List.of(up_2, down_2)) {
            ChessPosition test_2 = new ChessPosition(i, left_1);
            if (validate(test_2, board, team)) {
                moves.add(new ChessMove(myPosition, test_2, null));
            }
        }
        for (int i: List.of(up_2, down_2)) {
            ChessPosition test_3 = new ChessPosition(i, right_1);
            if (validate(test_3, board, team)) {
                moves.add(new ChessMove(myPosition, test_3, null));
            }
        }
        for (int i: List.of(up_1, down_1)) {
            ChessPosition test_4 = new ChessPosition(i, right_2);
            if (validate(test_4, board, team) && right_2 <= 8 && right_2 >= 1) {
                moves.add(new ChessMove(myPosition, test_4, null));
            }
        }
        return moves;
    }
}
