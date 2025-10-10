package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType pType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pType = type;
    }
    //override allows us to overwrite Object methods (equal, toString, hashCode, etc.)
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && pType == that.pType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pType);
    }

    @Override
    public String toString() {
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            if (pType.equals(PieceType.KING)) {
                return "K";
            } else if (pType.equals(PieceType.QUEEN)) {
                return "Q";
            } else if (pType.equals(PieceType.ROOK)) {
                return "R";
            } else if (pType.equals(PieceType.KNIGHT)) {
                return "N";
            } else if (pType.equals(PieceType.BISHOP)) {
                return "B";
            } else {
                return "P";
            }
        } else {
            if (pType.equals(PieceType.KING)) {
                return "k";
            } else if (pType.equals(PieceType.QUEEN)) {
                return "q";
            } else if (pType.equals(PieceType.ROOK)) {
                return "r";
            } else if (pType.equals(PieceType.KNIGHT)) {
                return "n";
            } else if (pType.equals(PieceType.BISHOP)) {
                return "b";
            } else {
                return "p";
            }
        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        //implement a class for each piece that can return a collection of valid moves and call them here
        if (myPiece.getPieceType() == PieceType.KING) {
            List moves = KingMove.kingMoves(board, myPosition, color);
            return moves;
        } else if (myPiece.getPieceType() == PieceType.QUEEN) {
            List moves = QueenMove.queenMoves(board, myPosition, color);
            return moves;
        } else if (myPiece.getPieceType() == PieceType.ROOK) {
            List moves = RookMove.rookMoves(board, myPosition, color);
            return moves;
        } else if (myPiece.getPieceType() == PieceType.BISHOP) {
            List moves = BishopMove.bishopMoves(board, myPosition, color);
            return moves;
        } else if (myPiece.getPieceType() == PieceType.KNIGHT) {
            List moves = KnightMove.knightMoves(board, myPosition, color);
            return moves;
        } else {
            List moves = PawnMove.pawnMoves(board, myPosition, color);
            return moves;
        }
    }
}
