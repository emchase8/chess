package chess;

import java.util.List;
import java.util.ArrayList;

public class PawnMove {

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

    public static List pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor team) {
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (team == ChessGame.TeamColor.WHITE) {
            ChessPosition up1 = new ChessPosition(row+1, col);
            if (row == 2) {
                ChessPosition up2 = new ChessPosition(row+2, col);
                if (validate(up2, board, team)[0] && !validate(up2, board, team)[1] && validate(up1, board, team)[0] && !validate(up1, board, team)[1]) {
                    moves.add(new ChessMove(myPosition, up2, null));
                }
            }
            if (validate(up1, board, team)[0] && !validate(up1, board, team)[1]) {
                if ((row+1)==8) {
                    moves.add(new ChessMove(myPosition, up1, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, up1, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, up1, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, up1, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, up1, null));
                }
            }
            ChessPosition left = new ChessPosition(row+1, col-1);
            if (validate(left, board, team)[1]) {
                if ((row+1)==8) {
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, left, null));
                }
            }
            ChessPosition right = new ChessPosition(row+1, col+1);
            if (validate(right, board, team)[1]) {
                if ((row+1)==8) {
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, right, null));
                }
            }
        } else {
            ChessPosition down1 = new ChessPosition(row-1, col);
            if (row == 7) {
                ChessPosition down2 = new ChessPosition(row-2, col);
                if (validate(down2, board, team)[0] && !validate(down2, board, team)[1] && validate(down1, board, team)[0] && !validate(down1, board, team)[1]) {
                    moves.add(new ChessMove(myPosition, down2, null));
                }
            }
            if (validate(down1, board, team)[0] && !validate(down1, board, team)[1]) {
                if ((row-1)==1) {
                    moves.add(new ChessMove(myPosition, down1, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, down1, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, down1, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, down1, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, down1, null));
                }
            }
            ChessPosition left = new ChessPosition(row-1, col-1);
            if (validate(left, board, team)[1]) {
                if ((row-1)==1) {
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, left, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, left, null));
                }
            }
            ChessPosition right = new ChessPosition(row-1, col+1);
            if (validate(right, board, team)[1]) {
                if ((row-1)==1) {
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, right, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, right, null));
                }
            }
        }
        return moves;
    }

}
