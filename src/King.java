import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);

        if (rowDiff <= 1 && colDiff <= 1) {
            return board[newRow][newCol] == null ||
                   board[newRow][newCol].isWhite != this.isWhite;
        }

        if (!hasMoved && newRow == row && Math.abs(newCol - col) == 2) {
            return true; // Board will fully validate
        }

        return false;
    }

    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {

                if (r >= 0 && r < 8 && c >= 0 && c < 8) {

                    if (r == row && c == col) continue;

                    if (board[r][c] == null ||
                        board[r][c].isWhite != this.isWhite) {
                        moves.add(new int[]{r, c});
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "K" : "k";
    }
}