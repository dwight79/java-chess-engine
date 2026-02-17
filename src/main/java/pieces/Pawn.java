package pieces;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {
        int direction = isWhite ? -1 : 1;

        if (col == newCol && board[newRow][newCol] == null) {
            return newRow == row + direction;
        }

        return false;
    }

    @Override
    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        int direction = isWhite ? -1 : 1;
        int forwardRow = row + direction;

        if (forwardRow >= 0 && forwardRow < 8) {
            if (board[forwardRow][col] == null) {
                moves.add(new int[]{forwardRow, col});
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "P" : "p";
    }
}
