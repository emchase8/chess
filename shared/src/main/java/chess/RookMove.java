package chess;

import java.util.List;
import java.util.ArrayList;

public class RookMove {

    public static boolean[] validateRook(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        boolean[] resultsRook = new boolean[2];
        int rowRook = myPosition.getRow();
        int colRook = myPosition.getColumn();
        if (1<=rowRook && rowRook<=8 && 1<=colRook && colRook<=8 && board.getPiece(myPosition) == null) {
            resultsRook[0] = true;
            resultsRook[1] = false;
            return resultsRook;
        } else if (1<=rowRook && rowRook<=8 && 1<=colRook && colRook<=8 && board.getPiece(myPosition).getTeamColor() != team) {
            resultsRook[0] = true;
            resultsRook[1] = true;
            return resultsRook;
        } else {
            resultsRook[0] = false;
            resultsRook[1] = false;
            return resultsRook;
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
            ChessPosition testUp = new ChessPosition(up1, col);
            if (validateRook(testUp, board, team)[1]) {
                moves.add(new ChessMove(myPosition, testUp, null));
                break;
            } else if (validateRook(testUp, board, team)[0]) {
                moves.add(new ChessMove(myPosition, testUp, null));
                up1++;
            } else {
                break;
            }
        }
        while (down1 >= 1) {
            ChessPosition testDown = new ChessPosition(down1, col);
            if (validateRook(testDown, board, team)[1]) {
                moves.add(new ChessMove(myPosition, testDown, null));
                break;
            } else if (validateRook(testDown, board, team)[0]) {
                moves.add(new ChessMove(myPosition, testDown, null));
                down1--;
            } else {
                break;
            }
        }
        while (right1 <= 8) {
            ChessPosition testRight = new ChessPosition(row, right1);
            if (validateRook(testRight, board, team)[1]) {
                moves.add(new ChessMove(myPosition, testRight, null));
                break;
            } else if (validateRook(testRight, board, team)[0]) {
                moves.add(new ChessMove(myPosition, testRight, null));
                right1++;
            } else {
                break;
            }
        }
        while (left1 >= 1) {
            ChessPosition testLeft = new ChessPosition(row, left1);
            if (validateRook(testLeft, board, team)[1]) {
                moves.add(new ChessMove(myPosition, testLeft, null));
                break;
            } else if (validateRook(testLeft, board, team)[0]) {
                moves.add(new ChessMove(myPosition, testLeft, null));
                left1--;
            } else {
                break;
            }
        }
        return moves;
    }
}
