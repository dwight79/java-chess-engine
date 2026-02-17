package pieces;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite, int row, int col) {
        super(isWhite, row, col);
    }

    @Override
    public boolean isValidMove(int newRow, int newCol, Piece[][] board) {

        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);

        if (row == newRow || col == newCol) {
            return new Rook(isWhite, row, col)
                    .isValidMove(newRow, newCol, board);
        }

        if (rowDiff == colDiff) {
            return new Bishop(isWhite, row, col)
                    .isValidMove(newRow, newCol, board);
        }

        return false;
    }

    @Override
    public List<int[]> generateMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();

        moves.addAll(new Rook(isWhite, row, col)
                .generateMoves(board));

        moves.addAll(new Bishop(isWhite, row, col)
                .generateMoves(board));

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "Q" : "q";
    }
}