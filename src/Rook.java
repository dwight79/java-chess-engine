import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int er, int ec, Piece[][] board) {
        if (row != er && col != ec) return false;

        int rowStep = Integer.compare(er, row);
        int colStep = Integer.compare(ec, col);

        int r = row + rowStep;
        int c = col + colStep;

        while (r != er || c != ec) {
            // ✅ bounds check FIRST
            if (r < 0 || r >= 8 || c < 0 || c >= 8) {
                return false;
            }

            if (board[r][c] != null) {
                return false;
            }

            r += rowStep;
            c += colStep;
        }

        // Destination square
        return board[er][ec] == null || board[er][ec].isWhite != isWhite;
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
