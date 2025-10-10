package chess;

import java.util.List;
import java.util.ArrayList;

public class RookMove {

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

    public static List rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int up1 = row+1;
        int down1 = row-1;
        int left1 = col-1;
        int right1 = col+1;
        while (up1 <=8) {
            ChessPosition test_up = new ChessPosition(up1, col);
            if (validate(test_up, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_up, null));
                break;
            } else if (validate(test_up, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_up, null));
                up1++;
            } else {
                break;
            }
        }
        while (down1 >= 1) {
            ChessPosition test_down = new ChessPosition(down1, col);
            if (validate(test_down, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_down, null));
                break;
            } else if (validate(test_down, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_down, null));
                down1--;
            } else {
                break;
            }
        }
        while (right1 <= 8) {
            ChessPosition test_right = new ChessPosition(row, right1);
            if (validate(test_right, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_right, null));
                break;
            } else if (validate(test_right, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_right, null));
                right1++;
            } else {
                break;
            }
        }
        while (left1 >= 1) {
            ChessPosition test_left = new ChessPosition(row, left1);
            if (validate(test_left, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test_left, null));
                break;
            } else if (validate(test_left, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test_left, null));
                left1--;
            } else {
                break;
            }
        }
        return moves;
    }
}
