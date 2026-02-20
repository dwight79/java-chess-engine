public abstract class Piece {
    protected boolean isWhite;
    protected int row, col;
    protected boolean hasMoved = false;

    public Piece(boolean isWhite, int row, int col) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        hasMoved = true;
    }

    public abstract boolean isValidMove(int newRow, int newCol, Piece[][] board);

    public abstract String getSymbol();
}
