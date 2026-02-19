import java.util.ArrayList;
import java.util.List;

public class Player {

    private boolean isWhite;
    private List<Piece> capturedPieces;
    private String name;

    public Player(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
        this.capturedPieces = new ArrayList<>();
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getName() {
        return name;
    }

    public void capturePiece(Piece piece) {
        capturedPieces.add(piece);
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public void printCapturedPieces() {
        if (capturedPieces.isEmpty()) {
            System.out.println(name + " has not captured any pieces.");
            return;
        }

        System.out.println(name + "'s captured pieces:");
        for (Piece piece : capturedPieces) {
            System.out.println(piece.getClass().getSimpleName());
        }
    }
}