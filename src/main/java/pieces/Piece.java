package pieces;

import java.util.List;

public abstract class Piece {

    protected boolean isWhite;
    protected int row;
    protected int col;

    public Piece(boolean isWhite, int row, int col) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns true if the move is valid for this piece.
     */
    public abstract boolean isValidMove(int newRow, int newCol, Piece[][] board);

    /**
     * Optional (better for real engines):
     * Generates all legal moves for this piece.
     */
    public abstract List<int[]> generateMoves(Piece[][] board);

    /**
     * Useful for printing the board.
     */
    public abstract String getSymbol();
}