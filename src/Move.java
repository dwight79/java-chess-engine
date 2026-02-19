package core;

public class Move {

    private final int startRow;
    private final int startCol;
    private final int endRow;
    private final int endCol;

    private final boolean isCapture;
    private final boolean isPromotion;
    private final boolean isCastling;
    private final boolean isEnPassant;

    public Move(int startRow, int startCol, int endRow, int endCol) {
        this(startRow, startCol, endRow, endCol, false, false, false, false);
    }

    public Move(int startRow, int startCol,
                int endRow, int endCol,
                boolean isCapture,
                boolean isPromotion,
                boolean isCastling,
                boolean isEnPassant) {

        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;

        this.isCapture = isCapture;
        this.isPromotion = isPromotion;
        this.isCastling = isCastling;
        this.isEnPassant = isEnPassant;
    }

    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public int getEndRow() { return endRow; }
    public int getEndCol() { return endCol; }

    public boolean isCapture() { return isCapture; }
    public boolean isPromotion() { return isPromotion; }
    public boolean isCastling() { return isCastling; }
    public boolean isEnPassant() { return isEnPassant; }

    @Override
    public String toString() {
        return "(" + startRow + "," + startCol + ") -> (" 
                + endRow + "," + endCol + ")";
    }
}