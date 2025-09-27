package chess;

import java.util.*;
import java.util.ArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard my_board;
    private TeamColor whose_turn;

    public ChessGame() {
        my_board = new ChessBoard();
        my_board.resetBoard();
        whose_turn = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return whose_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        whose_turn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(my_board, chessGame.my_board) && whose_turn == chessGame.whose_turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(my_board, whose_turn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
         if (my_board.getPiece(startPosition) != null) {
             ChessPiece piece = my_board.getPiece(startPosition);
             Collection<ChessMove> moves = piece.pieceMoves(my_board, startPosition);
             List<ChessMove> valid = new ArrayList();
             for (ChessMove move : moves) {
                 my_board.addPiece(startPosition, null);
                 my_board.addPiece(move.getEndPosition(), piece);
                 if (!isInCheck(piece.getTeamColor())) {
                     valid.add(move);
                 }
                 my_board.addPiece(startPosition, piece);
                 my_board.addPiece(move.getEndPosition(), null);
             }
             return valid;
         } else {
             return null;
         }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        if (valid.contains(move)) {
            ChessPiece piece = my_board.getPiece(move.getStartPosition());
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                ChessPiece.PieceType promote = move.getPromotionPiece();
                if (piece.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
                    my_board.addPiece(move.getStartPosition(), null);
                    ChessPiece upgrade = new ChessPiece(piece.getTeamColor(), promote);
                    my_board.addPiece(move.getEndPosition(), upgrade);
                } else if (piece.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
                    my_board.addPiece(move.getStartPosition(), null);
                    ChessPiece upgrade = new ChessPiece(piece.getTeamColor(), promote);
                    my_board.addPiece(move.getEndPosition(), upgrade);
                } else {
                    my_board.addPiece(move.getStartPosition(), null);
                    my_board.addPiece(move.getEndPosition(), piece);
                }
            } else {
                my_board.addPiece(move.getStartPosition(), null);
                my_board.addPiece(move.getEndPosition(), piece);
            }
        } else {
            throw new InvalidMoveException("Sorry, you can not make that move. Try again :)");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_position = new ChessPosition(1,1);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition current_pos = new ChessPosition(row, col);
                ChessPiece current_piece = my_board.getPiece(current_pos);
                if (current_piece != null && current_piece.getPieceType() == ChessPiece.PieceType.KING && current_piece.getTeamColor() == teamColor) {
                    king_position = current_pos;
                }
            }
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition current = new ChessPosition(row, col);
                ChessPiece opponent = my_board.getPiece(current);
                if (opponent != null && opponent.pieceMoves(my_board,current).contains(king_position) && opponent.getTeamColor() != teamColor) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        my_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return my_board;
    }
}
