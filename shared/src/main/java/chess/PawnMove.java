package chess;

import java.util.List;
import java.util.ArrayList;

public class PawnMove {

    public static boolean[] validatePawn(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor team) {
        boolean[] resultsPawn = new boolean[2];
        int rowPawn = myPosition.getRow();
        int colPawn = myPosition.getColumn();
        if (1<=rowPawn && rowPawn<=8 && 1<=colPawn && colPawn<=8 && board.getPiece(myPosition) == null) {
            resultsPawn[0] = true;
            resultsPawn[1] = false;
            return resultsPawn;
        } else if (1<=rowPawn && rowPawn<=8 && 1<=colPawn && colPawn<=8 && board.getPiece(myPosition).getTeamColor() != team) {
            resultsPawn[0] = true;
            resultsPawn[1] = true;
            return resultsPawn;
        } else {
            resultsPawn[0] = false;
            resultsPawn[1] = false;
            return resultsPawn;
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
                if (validatePawn(up2, board, team)[0] && !validatePawn(up2, board, team)[1] && validatePawn(up1, board, team)[0] && !validatePawn(up1, board, team)[1]) {
                    moves.add(new ChessMove(myPosition, up2, null));
                }
            }
            if (validatePawn(up1, board, team)[0] && !validatePawn(up1, board, team)[1]) {
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
            if (validatePawn(left, board, team)[1]) {
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
            if (validatePawn(right, board, team)[1]) {
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
                if (validatePawn(down2, board, team)[0] && !validatePawn(down2, board, team)[1] && validatePawn(down1, board, team)[0] && !validatePawn(down1, board, team)[1]) {
                    moves.add(new ChessMove(myPosition, down2, null));
                }
            }
            if (validatePawn(down1, board, team)[0] && !validatePawn(down1, board, team)[1]) {
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
            if (validatePawn(left, board, team)[1]) {
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
            if (validatePawn(right, board, team)[1]) {
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
