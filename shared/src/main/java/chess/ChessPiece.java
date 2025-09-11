package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType p_type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        p_type = type;
    }
    //override allows us to overwrite Object methods (equal, toString, hashCode, etc.)
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && p_type == that.p_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, p_type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color.name() +
                ", p_type=" + p_type.name() +
                '}';
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
//        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return p_type;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece my_piece = board.getPiece(myPosition);
        //implement a class for each piece that can return a collection of valid moves and call them here
        if (my_piece.getPieceType() == PieceType.KING) {
            //king stuff
        } else if (my_piece.getPieceType() == PieceType.QUEEN) {
            //queen stuff
        } else if (my_piece.getPieceType() == PieceType.ROOK) {
            //rook stuff
        } else if (my_piece.getPieceType() == PieceType.BISHOP) {
            //bishop stuff
        } else if (my_piece.getPieceType() == PieceType.KNIGHT) {
            //knight stuff
        } else {
            //pawn stuff
        }
        //PLACEHOLDER, DELETE BEFORE TURNING IN!!!!!!
        return java.util.List.of();
//        throw new RuntimeException("Not implemented");
    }
}
