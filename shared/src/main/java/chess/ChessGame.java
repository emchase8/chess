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

    private ChessBoard myBoard;
    private TeamColor whoseTurn;

    public ChessGame() {
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        whoseTurn = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return whoseTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        whoseTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(myBoard, chessGame.myBoard) && whoseTurn == chessGame.whoseTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myBoard, whoseTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void resetBoard(ChessPosition startPosition, ChessPiece piece, ChessPiece capture, ChessMove move) {
        myBoard.addPiece(startPosition, piece);
        if (capture != null) {
            myBoard.addPiece(move.getEndPosition(), capture);
        } else {
            myBoard.addPiece(move.getEndPosition(), null);
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (myBoard.getPiece(startPosition) != null) {
             ChessPiece piece = myBoard.getPiece(startPosition);
             Collection<ChessMove> moves = piece.pieceMoves(myBoard, startPosition);
             List<ChessMove> valid = new ArrayList();
             if (isInCheck(piece.getTeamColor())) {
                 for (ChessMove move : moves) {
                     myBoard.addPiece(startPosition, null);
                     ChessPiece capture = null;
                     if (myBoard.getPiece(move.getEndPosition())!=null) {
                         capture = myBoard.getPiece(move.getEndPosition());
                     }
                     myBoard.addPiece(move.getEndPosition(), piece);
                     if (isInCheck(piece.getTeamColor())) {
                         resetBoard(startPosition, piece, capture, move);
                     } else {
                         resetBoard(startPosition, piece, capture, move);
                         valid.add(move);
                     }
                 }
             } else {
                 for (ChessMove move : moves) {
                     myBoard.addPiece(startPosition, null);
                     ChessPiece capture = null;
                     if (myBoard.getPiece(move.getEndPosition())!=null) {
                         capture = myBoard.getPiece(move.getEndPosition());
                     }
                     myBoard.addPiece(move.getEndPosition(), piece);
                     if (!isInCheck(piece.getTeamColor())) {
                         valid.add(move);
                     }
                     myBoard.addPiece(startPosition, piece);
                     if (capture != null) {
                         myBoard.addPiece(move.getEndPosition(), capture);
                     } else {
                         myBoard.addPiece(move.getEndPosition(), null);
                     }
                 }
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
        if (valid != null && valid.contains(move)) {
            ChessPiece piece = myBoard.getPiece(move.getStartPosition());
            ChessGame.TeamColor team = piece.getTeamColor();
            if (getTeamTurn() == team) {
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    ChessPiece.PieceType promote = move.getPromotionPiece();
                    if (piece.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
                        myBoard.addPiece(move.getStartPosition(), null);
                        ChessPiece upgradeWhite = new ChessPiece(piece.getTeamColor(), promote);
                        myBoard.addPiece(move.getEndPosition(), upgradeWhite);
                    } else if (piece.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
                        myBoard.addPiece(move.getStartPosition(), null);
                        ChessPiece upgradeBlack = new ChessPiece(piece.getTeamColor(), promote);
                        myBoard.addPiece(move.getEndPosition(), upgradeBlack);
                    } else {
                        myBoard.addPiece(move.getStartPosition(), null);
                        myBoard.addPiece(move.getEndPosition(), piece);
                    }
                } else {
                    myBoard.addPiece(move.getStartPosition(), null);
                    myBoard.addPiece(move.getEndPosition(), piece);
                }
                if (team == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                } else {
                    setTeamTurn(TeamColor.WHITE);
                }
            } else {
                throw new InvalidMoveException("Sorry, you can not make that move. Try again :)");
            }
        } else {
            throw new InvalidMoveException("Sorry, you can not make that move. Try again :)");
        }
    }

    public ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPosition(1,1);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece currentPiece = myBoard.getPiece(currentPos);
                if (currentPiece != null && currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                    kingPosition = currentPos;
                    break;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition current = new ChessPosition(row, col);
                ChessPiece opponent = myBoard.getPiece(current);
                if (opponent != null && opponent.getTeamColor() != teamColor) {
                    Collection<ChessMove> opMoves = opponent.pieceMoves(myBoard,current);
                    List<ChessPosition> ends = new ArrayList<>();
                    for (ChessMove move : opMoves) {
                        ends.add(move.getEndPosition());
                    }
                    if (ends.contains(kingPosition)) {
                        return true;
                    }
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
        ChessPosition kingPosition = getKingPosition(teamColor);
        if (isInCheck(teamColor) && validMoves(kingPosition).isEmpty()) {
            for (int rowCM = 1; rowCM <= 8; rowCM++) {
                for (int colCM = 1; colCM <= 8; colCM++) {
                    ChessPosition currentPos = new ChessPosition(rowCM, colCM);
                    ChessPiece currentPiece = myBoard.getPiece(currentPos);
                    if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> moves = validMoves(currentPos);
                        if (!moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor)) {
            return false;
        }
        if (isInCheck(teamColor) && !isInCheckmate(teamColor)) {
            return true;
        }
        for (int rowSM = 1; rowSM <= 8; rowSM++) {
            for (int colSM = 1; colSM <= 8; colSM++) {
                ChessPosition currentPos = new ChessPosition(rowSM, colSM);
                ChessPiece currentPiece = myBoard.getPiece(currentPos);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(currentPos);
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myBoard;
    }
}
