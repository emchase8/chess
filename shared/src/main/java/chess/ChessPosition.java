package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int pRow;
    private int pCol;

    public ChessPosition(int row, int col) {
        pRow = row;
        pCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return pRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return pCol;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return pRow == that.pRow && pCol == that.pCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pRow, pCol);
    }

    @Override
    public String toString() {
        return "[" + pRow + "," + pCol + "]";
    }
}
