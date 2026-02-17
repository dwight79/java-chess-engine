import pieces.*;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public Board() {
        initialize();
    }

    private void initialize() {
        board[6][0] = new Pawn(true, 6, 0);
        board[1][0] = new Pawn(false, 1, 0);
    }

    public Piece[][] getBoard() {
        return board;
    }
}