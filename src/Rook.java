import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {

        // Rook must move in straight line
        if (newRow != row && newCol != col) {
            return false;
        }

        // Check path is clear
        if (newRow == row) {
            int step = (newCol > col) ? 1 : -1;
            for (int c = col + step; c != newCol; c += step) {
                if (board[row][c] != null) return false;
            }
        } else {
            int step = (newRow > row) ? 1 : -1;
            for (int r = row + step; r != newRow; r += step) {
                if (board[r][col] != null) return false;
            }
        }

        // Can't capture own piece
        if (board[newRow][newCol] != null &&
            board[newRow][newCol].isWhite == this.isWhite) {
            return false;
        }

        return true;
    }

    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        // Directions: up, down, left, right
        int[][] directions = {
                {-1, 0}, {1, 0},
                {0, -1}, {0, 1}
        };

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {

                if (board[r][c] == null) {
                    moves.add(new int[]{r, c});
                } else {
                    if (board[r][c].isWhite != this.isWhite) {
                        moves.add(new int[]{r, c}); // capture
                    }
                    break; // blocked
                }

                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "R" : "r";
    }
}
