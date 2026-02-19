import java.util.Scanner;

public class ChessGame {

    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Console Chess!");
        System.out.println("Enter moves like e2e4 (type 'exit' to quit)\n");

        while (true) {
            board.printBoard();

            System.out.print("Enter move: ");
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

            board.makeMove(move[0], move[1], move[2], move[3]);
        }

        scanner.close();
    }
}
