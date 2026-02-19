import java.util.Scanner;

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

            // Attempt move
            if (!board.makeMove(move[0], move[1], move[2], move[3])) {
                continue; // failed move → same player's turn
            }

            // Switch turns AFTER successful move
            whiteTurn = !whiteTurn;

            // Check
            if (board.isKingInCheck(whiteTurn) && (board.isCheckmate(whiteTurn) == false)) {
                System.out.println("Check!");
            }

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
        }

        scanner.close();
    }
}
