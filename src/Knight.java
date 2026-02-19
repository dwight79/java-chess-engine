import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);

        if (!((rowDiff == 2 && colDiff == 1) ||
              (rowDiff == 1 && colDiff == 2))) {
            return false;
        }

        return board[newRow][newCol] == null ||
               board[newRow][newCol].isWhite != this.isWhite;
    }

    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        int[][] offsets = {
                {-2,-1},{-2,1},{-1,-2},{-1,2},
                {1,-2},{1,2},{2,-1},{2,1}
        };

        for (int[] offset : offsets) {
            int r = row + offset[0];
            int c = col + offset[1];

            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c] == null ||
                    board[r][c].isWhite != this.isWhite) {
                    moves.add(new int[]{r, c});
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "N" : "n";
    }
}