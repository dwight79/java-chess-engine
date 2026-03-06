import java.util.Scanner;

// To run the program:
// javac -cp lib/sqlite-jdbc.jar -d out src/*.java && java -cp out:lib/sqlite-jdbc.jar ChessGame

public class ChessGame {

    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        boolean whiteTurn = true;

        System.out.println("Welcome to Console Chess!");
        System.out.println("Enter moves like e2e4 (type 'exit' to quit)\n");

        while (true) {
            board.printBoard();

            System.out.print((whiteTurn ? "White" : "Black") + " to move: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("Game ended.");
                break;
            }

            int[] move = Notation.parseToArray(input);
            if (move == null) {
                System.out.println("Invalid notation. Use e2e4.");
                continue;
            }

            Piece selected = board.getPiece(move[0], move[1]);

            if (selected == null) {
                System.out.println("No piece at that square.");
                continue;
            }

            if (selected.isWhite != whiteTurn) {
                System.out.println("That's not your piece!");
                continue;
            }

            if (!board.makeMove(move[0], move[1], move[2], move[3])) {
                continue;
            }

            // Pawn promotion (before switching turns)
            if (board.hasPromotionPending()) {
                System.out.print("Promote pawn to (Q/R/B/N): ");
                char choice = scanner.nextLine().trim().toLowerCase().charAt(0);
                board.promotePawn(choice);
            }

            // Switch turns AFTER successful move
            whiteTurn = !whiteTurn;

            // Check
            if (board.isKingInCheck(whiteTurn) && (board.isCheckmate(whiteTurn) == false)) {
                System.out.println("Check!");
            }

            board.recordPosition();

            // Checkmate
            if (board.isCheckmate(whiteTurn)) {
                board.printBoard();
                if (whiteTurn) {
                    System.out.println("Black won by checkmate (0-1)");
                } else {
                    System.out.println("White won by checkmate (1-0)");
                }
                break;
            }

            if (board.isStalemate(whiteTurn)) {
                board.printBoard();
                System.out.println("Stalemate! The game is a draw! (½-½)");
                break;
            }

            // Threefold repetition
            if (board.isThreefoldRepetition()) {
                board.printBoard();
                System.out.println("Threefold repetition detected! The game is a draw! (½-½)");
                break;
            }
        }

        scanner.close();
    }
}
