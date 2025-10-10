package chess;

import java.util.List;
import java.util.ArrayList;

public class BishopMove {

    public static boolean[] validateBishop(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        boolean[] resultsBishop = new boolean[2];
        int rowBishop = myPosition.getRow();
        int colBishop = myPosition.getColumn();
        if (1<=rowBishop && rowBishop<=8 && 1<=colBishop && colBishop<=8 && board.getPiece(myPosition) == null) {
            resultsBishop[0] = true;
            resultsBishop[1] = false;
            return resultsBishop;
        } else if (1<=rowBishop && rowBishop<=8 && 1<=colBishop && colBishop<=8 && board.getPiece(myPosition).getTeamColor() != team) {
            resultsBishop[0] = true;
            resultsBishop[1] = true;
            return resultsBishop;
        } else {
            resultsBishop[0] = false;
            resultsBishop[1] = false;
            return resultsBishop;
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
            if (validateBishop(test1, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test1, null));
                break;
            } else if (validateBishop(test1, board, team)[0]) {
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
            if (validateBishop(test2, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test2, null));
                break;
            } else if (validateBishop(test2, board, team)[0]) {
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
            if (validateBishop(test3, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test3, null));
                break;
            } else if (validateBishop(test3, board, team)[0]) {
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
            if (validateBishop(test4, board, team)[1]) {
                moves.add(new ChessMove(myPosition, test4, null));
                break;
            } else if (validateBishop(test4, board, team)[0]) {
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
