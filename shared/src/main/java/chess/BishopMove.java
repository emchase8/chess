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
        int up1 = row+1;
        int down1 = row-1;
        int left1 = col-1;
        int right1 = col+1;
        while (up1 <=8 && right1 <=8) {
            ChessPosition test1 = new ChessPosition(up1, right1);
            if (validate(test1, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test1, null));
                break;
            } else if (validate(test1, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test1, null));
                up1++;
                right1++;
            } else {
                break;
            }
        }
        up1 = row+1;
        down1 = row-1;
        left1 = col-1;
        right1 = col+1;
        while (up1 <=8 && left1 >= 1) {
            ChessPosition test2 = new ChessPosition(up1, left1);
            if (validate(test2, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test2, null));
                break;
            } else if (validate(test2, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test2, null));
                up1++;
                left1--;
            } else {
                break;
            }
        }
        up1 = row+1;
        down1 = row-1;
        left1 = col-1;
        right1 = col+1;
        while (down1 >=1 && right1 <=8) {
            ChessPosition test3 = new ChessPosition(down1, right1);
            if (validate(test3, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test3, null));
                break;
            } else if (validate(test3, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test3, null));
                down1--;
                right1++;
            } else {
                break;
            }
        }
        up1 = row+1;
        down1 = row-1;
        left1 = col-1;
        right1 = col+1;
        while (down1 >=1 && left1 >= 1) {
            ChessPosition test4 = new ChessPosition(down1, left1);
            if (validate(test4, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test4, null));
                break;
            } else if (validate(test4, board, team)[0]) {
                moves.add(new ChessMove(myPosition, test4, null));
                down1--;
                left1--;
            } else {
                break;
            }
        }
        return moves;
    }
}
