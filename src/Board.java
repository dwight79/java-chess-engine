import java.util.*;

public class Board {

    private final Piece[][] board = new Piece[8][8];
    public static Pawn enPassantPawn;
    private Pawn promotionPawn = null;
    private int promotionRow;
    private int promotionCol;

    // In Piece.java (or Board.java constants)
    public static final String RESET = "\u001B[0m";

    // Foreground piece colors
    public static final String WHITE_PIECE = "\u001B[97m";  // bright white
    public static final String BLACK_PIECE = "\u001B[30m";  // black

    // Background square colors
    public static final String WHITE_SQUARE = "\u001B[48;5;228m"; // white background
    public static final String BLACK_SQUARE = "\u001B[45m"; // dark background (like chessboard)

    // Add a history map to count positions
    private Map<String, Integer> positionHistory = new HashMap<>();
    private int halfMoveClock = 0; // for 50-move rule

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
        Pawn enPassantCapturedPawn = null;
        int enPassantCapturedRow = -1;
        int enPassantCapturedCol = -1;

        Piece piece = board[sr][sc];

        if (piece == null) {
            System.out.println("No piece at start position.");
            return false;
        }

        // Castling attempt
        if (piece instanceof King && Math.abs(ec - sc) == 2) {
            return handleCastling(sr, sc, er, ec);
        }

        if (!piece.isValidMove(er, ec, board)) {
            System.out.println("Invalid move!");
            return false;
        }

        // En passant capture
        if (piece instanceof Pawn && board[er][ec] == null && sc != ec) {
            Piece sidePiece = board[sr][ec];
            if (sidePiece instanceof Pawn) {
                enPassantCapturedPawn = (Pawn) sidePiece;
                enPassantCapturedRow = sr;
                enPassantCapturedCol = ec;
                board[sr][ec] = null;
            }
        }

        // Save state
        Piece captured = board[er][ec];

        // Make move
        board[er][ec] = piece;
        board[sr][sc] = null;
        piece.setPosition(er, ec);

        if (isKingInCheck(piece.isWhite)) {
            board[sr][sc] = piece;
            board[er][ec] = captured;
            piece.setPosition(sr, sc);

            // Restore en passant captured pawn
            if (enPassantCapturedPawn != null) {
                board[enPassantCapturedRow][enPassantCapturedCol] = enPassantCapturedPawn;
            }

            System.out.println("You cannot leave your king in check!");
            return false;
        }

        // 50-move rule bookkeeping
        if (piece instanceof Pawn || captured != null || enPassantCapturedPawn != null) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        // EN PASSANT bookkeeping
        enPassantPawn = null;
        if (piece instanceof Pawn && Math.abs(er - sr) == 2) {
            enPassantPawn = (Pawn) piece;
        }

        // Pawn promotion detection
        if (piece instanceof Pawn) {
            if ((piece.isWhite && er == 0) || (!piece.isWhite && er == 7)) {
                promotionPawn = (Pawn) piece;
                promotionRow = er;
                promotionCol = ec;
            }
        }

        return true;
    }

    public void printBoard() {
        System.out.println();

        for (int r = 0; r < 8; r++) {
            // Print row numbers if desired
            System.out.print((8 - r) + " ");

            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];

                // Choose square color
                String bg = ((r + c) % 2 == 0) ? WHITE_SQUARE : BLACK_SQUARE;

                String symbol = " "; // default empty square
                String fg = "";      // default foreground

                if (p != null) {
                    symbol = p.getSymbol();
                    fg = p.isWhite ? WHITE_PIECE : BLACK_PIECE;
                }

                System.out.print(bg + fg + " " + symbol + " " + RESET);
            }
            System.out.println();
    }

    // Print column letters
    System.out.print("  ");
    for (char c = 'a'; c <= 'h'; c++) {
        System.out.print(" " + c + " ");
    }
    System.out.println("\n");
    }

    public Piece getPiece(int r, int c) {
        return board[r][c];
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

    public boolean isSquareUnderAttack(int r, int c, boolean byWhite) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board[i][j];
                if (p != null && p.isWhite == byWhite) {
                    if (p.isValidMove(r, c, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean handleCastling(int sr, int sc, int er, int ec) {

        King king = (King) board[sr][sc];
        if (king.hasMoved()) {
            System.out.println("Sorry, your king has already moved");
            return false;
        }

        boolean white = king.isWhite;

        // King cannot be in check
        if (isSquareUnderAttack(sr, sc, !white)) {
            System.out.println("You cannot castle if you're king is in check");
            return false;
        }

        // King-side or Queen-side
        if (ec > sc) {
            // KING-SIDE
            Piece rook = board[sr][7];
            if (!(rook instanceof Rook) || rook.hasMoved()) { 
                System.out.println("Castling not allowed: the rook has moved.");
                return false;
            }

            if (board[sr][5] != null || board[sr][6] != null) {
                System.out.println("Castling not allowed: pieces in the way");
                return false;
            }

            if (isSquareUnderAttack(sr, 5, !white) || isSquareUnderAttack(sr, 6, !white)) {
                System.out.println("Castling not allowed through check.");
                return false;
            }

            // Move king
            board[sr][6] = king;
            board[sr][sc] = null;
            king.setPosition(sr, 6);

            // Move rook
            board[sr][5] = rook;
            board[sr][7] = null;
            rook.setPosition(sr, 5);

            return true;

        } else {
            // QUEEN-SIDE
            Piece rook = board[sr][0];
            if (!(rook instanceof Rook) || rook.hasMoved()) {
                System.out.println("Castling not allowed: the rook has moved.");
                return false;
            }

            if (board[sr][1] != null ||
                board[sr][2] != null ||
                board[sr][3] != null) {
                    System.out.println("Castling not allowed: pieces in the way");
                return false;
            }

            if (isSquareUnderAttack(sr, 3, !white) ||
                isSquareUnderAttack(sr, 2, !white)) {
                    System.out.println("Castling not allowed through check.");
                    return false;
                }

            // Move king
            board[sr][2] = king;
            board[sr][sc] = null;
            king.setPosition(sr, 2);

            // Move rook
            board[sr][3] = rook;
            board[sr][0] = null;
            rook.setPosition(sr, 3);

            return true;
        }
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

    public boolean isStalemate(boolean whiteTurn) {
        // If king is in check, it cannot be stalemate
        if (isKingInCheck(whiteTurn)) return false;

        // Loop over all pieces of this color
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p != null && p.isWhite == whiteTurn) {
                    // Check every possible destination
                    for (int er = 0; er < 8; er++) {
                        for (int ec = 0; ec < 8; ec++) {
                            if (p.isValidMove(er, ec, board)) {
                                if (resolvesCheck(r, c, er, ec, whiteTurn)) {
                                    // Found at least one legal move → not stalemate
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        // No legal moves and king is not in check → stalemate
        return true;
    }

    // Call this after each move
    public void recordPosition() {
        String key = generateBoardKey();
        positionHistory.put(key, positionHistory.getOrDefault(key, 0) + 1);
    }

    public boolean isThreefoldRepetition() {
        String key = generateBoardKey();
        return positionHistory.getOrDefault(key, 0) >= 3;
    }

    // Simple key generation
    private String generateBoardKey() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p == null) sb.append(".");
                else sb.append(p.getSymbol()).append(p.isWhite ? "w" : "b");
            }
        }

        // Include whose turn and castling rights if needed
        // For simplicity, just include en passant pawn column
        sb.append(enPassantPawn != null ? enPassantPawn.col : "-");

        return sb.toString();
    }

    // Insufficient material detection, checks if checkmate is impossible
    public boolean isInsufficientMaterial() {

        int whiteBishops = 0;
        int blackBishops = 0;
        int whiteKnights = 0;
        int blackKnights = 0;
        int otherPieces = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                Piece p = board[r][c];
                if (p == null) continue;

                // Ignore kings
                if (p instanceof King) continue;

                if (p instanceof Bishop) {
                    if (p.isWhite) whiteBishops++;
                    else blackBishops++;
                }
                else if (p instanceof Knight) {
                    if (p.isWhite) whiteKnights++;
                    else blackKnights++;
                }
                else {
                    // Any pawn, rook, or queen means mate is possible
                    otherPieces++;
                }
            }
        }

        // If pawns, rooks, or queens exist → mate possible
        if (otherPieces > 0) return false;

        int totalBishops = whiteBishops + blackBishops;
        int totalKnights = whiteKnights + blackKnights;

        // King vs King
        if (totalBishops == 0 && totalKnights == 0) return true;

        // King + Bishop vs King
        if (totalBishops == 1 && totalKnights == 0) return true;

        // King + Knight vs King
        if (totalBishops == 0 && totalKnights == 1) return true;

        return false;
    }

    public boolean isFiftyMoveRule() {
        return halfMoveClock >= 100;
    }

    public boolean hasPromotionPending() {
        return promotionPawn != null;
    }

    public void promotePawn(char choice) {
        if (promotionPawn == null) return;

        boolean white = promotionPawn.isWhite;
        Piece newPiece;

        switch (Character.toLowerCase(choice)) {
            case 'q': newPiece = new Queen(white, promotionRow, promotionCol); break;
            case 'r': newPiece = new Rook(white, promotionRow, promotionCol); break;
            case 'b': newPiece = new Bishop(white, promotionRow, promotionCol); break;
            case 'n': newPiece = new Knight(white, promotionRow, promotionCol); break;
            default:
                System.out.println("Invalid choice. Defaulting to Queen.");
                newPiece = new Queen(white, promotionRow, promotionCol);
        }

        board[promotionRow][promotionCol] = newPiece;
        promotionPawn = null;
    }
}
