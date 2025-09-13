package chess;

import java.util.List;
import java.util.ArrayList;

public class BishopMove {

    public static boolean[] validate(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        boolean[] results = new boolean[2];
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (1<=row && row<=8 && 1<=col && col<=8 && board.getPiece(myPosition) == null) {
            results[0] = true;
            results[1] = false;
            return results;
        } else if (1<=row && row<=8 && 1<=col && col<=8 && board.getPiece(myPosition).getTeamColor() != team) {
            results[0] = true;
            results[1] = true;
            return results;
        } else {
            results[0] = false;
            results[1] = false;
            return results;
        }
    }

    public static List bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int up_1 = row+1;
        int down_1 = row-1;
        int left_1 = col-1;
        int right_1 = col+1;
        while (up_1 <=8 && right_1 <=8) {
            ChessPosition test_1 = new ChessPosition(up_1, right_1);
            if (validate(test_1, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_1, null));
                break;
            } else if (validate(test_1, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_1, null));
                up_1++;
                right_1++;
            } else {
                break;
            }
        }
        up_1 = row+1;
        down_1 = row-1;
        left_1 = col-1;
        right_1 = col+1;
        while (up_1 <=8 && left_1 >= 1) {
            ChessPosition test_2 = new ChessPosition(up_1, left_1);
            if (validate(test_2, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_2, null));
                break;
            } else if (validate(test_2, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_2, null));
                up_1++;
                left_1--;
            } else {
                break;
            }
        }
        up_1 = row+1;
        down_1 = row-1;
        left_1 = col-1;
        right_1 = col+1;
        while (down_1 >=1 && right_1 <=8) {
            ChessPosition test_3 = new ChessPosition(down_1, right_1);
            if (validate(test_3, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_3, null));
                break;
            } else if (validate(test_3, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_3, null));
                down_1--;
                right_1++;
            } else {
                break;
            }
        }
        up_1 = row+1;
        down_1 = row-1;
        left_1 = col-1;
        right_1 = col+1;
        while (down_1 >=1 && left_1 >= 1) {
            ChessPosition test_4 = new ChessPosition(down_1, left_1);
            if (validate(test_4, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_4, null));
                break;
            } else if (validate(test_4, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_4, null));
                down_1--;
                left_1--;
            } else {
                break;
            }
        }
        return moves;
    }
}
