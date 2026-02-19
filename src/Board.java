public class Board {

    private final Piece[][] board = new Piece[8][8];

    public Board() {
        setupBoard();
    }

    private void setupBoard() {
        // Pawns
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(true, 6, i);
            board[1][i] = new Pawn(false, 1, i);
        }

        // Rooks
        board[7][0] = new Rook(true, 7, 0);
        board[7][7] = new Rook(true, 7, 7);
        board[0][0] = new Rook(false, 0, 0);
        board[0][7] = new Rook(false, 0, 7);

        // Knights
        board[7][1] = new Knight(true, 7, 1);
        board[7][6] = new Knight(true, 7, 6);
        board[0][1] = new Knight(false, 0, 1);
        board[0][6] = new Knight(false, 0, 6);

        // Bishops
        board[7][2] = new Bishop(true, 7, 2);
        board[7][5] = new Bishop(true, 7, 5);
        board[0][2] = new Bishop(false, 0, 2);
        board[0][5] = new Bishop(false, 0, 5);

        // Queens
        board[7][3] = new Queen(true, 7, 3);
        board[0][3] = new Queen(false, 0, 3);

        // Kings
        board[7][4] = new King(true, 7, 4);
        board[0][4] = new King(false, 0, 4);
    }

    public boolean makeMove(int sr, int sc, int er, int ec) {
        Piece piece = board[sr][sc];

        if (piece == null) {
            System.out.println("No piece at start position.");
            return false;
        }

        if (!piece.isValidMove(er, ec, board)) {
            System.out.println("Invalid move!");
            return false;
        }

        // Save state (important later for undo during check validation)
        Piece captured = board[er][ec];

        // Make move
        board[er][ec] = piece;
        board[sr][sc] = null;
        piece.setPosition(er, ec);

        // If move leaves your king in check → undo
        if (isKingInCheck(piece.isWhite)) {
            board[sr][sc] = piece;
            board[er][ec] = captured;
            piece.setPosition(sr, sc);
            System.out.println("You cannot leave your king in check!");
            return false;
        }

        return true;
    }

    public void printBoard() {
        System.out.println();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == null) System.out.print(". ");
                else System.out.print(board[r][c].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isKingInCheck(boolean whiteKing) {
        int kingRow = -1, kingCol = -1;

        // Find the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.isWhite == whiteKing) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        // Check if any opponent piece attacks the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.isWhite != whiteKing) {
                    if (p.isValidMove(kingRow, kingCol, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean resolvesCheck(int sr, int sc, int er, int ec, boolean whiteKing) {
        Piece moving = board[sr][sc];
        Piece captured = board[er][ec];

        // Make temporary move
        board[er][ec] = moving;
        board[sr][sc] = null;
        int oldRow = moving.row;
        int oldCol = moving.col;
        moving.setPosition(er, ec);

        boolean stillInCheck = isKingInCheck(whiteKing);

        // Undo move
        board[sr][sc] = moving;
        board[er][ec] = captured;
        moving.setPosition(oldRow, oldCol);

        return !stillInCheck;
    }

    public boolean isCheckmate(boolean whiteKing) {
        if (!isKingInCheck(whiteKing)) return false;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.isWhite == whiteKing) {
                    for (int er = 0; er < 8; er++) {
                        for (int ec = 0; ec < 8; ec++) {
                            if (p.isValidMove(er, ec, board)) {
                                if (resolvesCheck(r, c, er, ec, whiteKing)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    
}
