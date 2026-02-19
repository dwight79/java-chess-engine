import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {

        // Must move diagonally
        if (Math.abs(newRow - row) != Math.abs(newCol - col)) {
            return false;
        }

        int rowStep = Integer.compare(newRow, row);
        int colStep = Integer.compare(newCol, col);

        int r = row + rowStep;
        int c = col + colStep;

        while (r != newRow || c != newCol) {

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
        return (board[newRow][newCol] == null) || (board[newRow][newCol].isWhite != this.isWhite);
    }

    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        int[][] directions = {
                {-1,-1},{-1,1},{1,-1},{1,1}
        };

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c] == null) {
                    moves.add(new int[]{r, c});
                } else {
                    if (board[r][c].isWhite != this.isWhite) {
                        moves.add(new int[]{r, c});
                    }
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "B" : "b";
    }
}