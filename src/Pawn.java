public class Pawn extends Piece {

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int dir = isWhite ? -1 : 1;

        // Normal forward move
        if (col == newCol && board[newRow][newCol] == null) {
            if (row + dir == newRow) return true;
            if ((row == 6 && isWhite || row == 1 && !isWhite)
                    && row + 2 * dir == newRow
                    && board[row + dir][col] == null) {
                return true;
            }
        }

        // Normal capture
        if (Math.abs(col - newCol) == 1
                && row + dir == newRow
                && board[newRow][newCol] != null
                && board[newRow][newCol].isWhite != isWhite) {
            return true;
        }

        // EN PASSANT
        if (Math.abs(col - newCol) == 1 && row + dir == newRow && board[newRow][newCol] == null) {
            Piece sidePawn = board[row][newCol];
            if (sidePawn instanceof Pawn && sidePawn.isWhite != isWhite) {
                Pawn p = (Pawn) sidePawn;
                if (p == Board.enPassantPawn) {  // see note below
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "P" : "p";
    }
}
