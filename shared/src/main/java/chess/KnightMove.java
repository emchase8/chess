package chess;

import java.util.List;
import java.util.ArrayList;

public class KnightMove {

    public static boolean validateKnight(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        int rowKnight = myPosition.getRow();
        int colKnight = myPosition.getColumn();
        if (1 <= rowKnight && rowKnight <= 8 && 1 <= colKnight && colKnight <= 8 && board.getPiece(myPosition) == null) {
            return true;
        } else if (1 <= rowKnight && rowKnight <= 8 && 1 <= colKnight && colKnight <= 8 && board.getPiece(myPosition).getTeamColor() != team) {
            return true;
        } else {
            return false;
        }
    }

    public static List knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int up2 = row+2;
        int up1 = row+1;
        int down1 = row-1;
        int down2 = row-2;
        int left2 = col-2;
        int left1 = col-1;
        int right1 = col+1;
        int right2 = col+2;
        for (int i: List.of(up1, down1)) {
            ChessPosition test1 = new ChessPosition(i, left2);
            if (validateKnight(test1, board, team)) {
                moves.add(new ChessMove(myPosition, test1, null));
            }
        }
        for (int i: List.of(up2, down2)) {
            ChessPosition test2 = new ChessPosition(i, left1);
            if (validateKnight(test2, board, team)) {
                moves.add(new ChessMove(myPosition, test2, null));
            }
        }
        for (int i: List.of(up2, down2)) {
            ChessPosition test3 = new ChessPosition(i, right1);
            if (validateKnight(test3, board, team)) {
                moves.add(new ChessMove(myPosition, test3, null));
            }
        }
        for (int i: List.of(up1, down1)) {
            ChessPosition test4 = new ChessPosition(i, right2);
            if (validateKnight(test4, board, team) && right2 <= 8 && right2 >= 1) {
                moves.add(new ChessMove(myPosition, test4, null));
            }
        }
        return moves;
    }
}
