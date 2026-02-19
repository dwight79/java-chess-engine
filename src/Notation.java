public class Notation {

    // Converts "e2e4" into int[4] = {6, 4, 4, 4} (rows 0-7, top to bottom)
    public static int[] parseToArray(String move) {
        if (move.length() != 4) return null;

        int sc = move.charAt(0) - 'a';
        int sr = 8 - (move.charAt(1) - '0');
        int ec = move.charAt(2) - 'a';
        int er = 8 - (move.charAt(3) - '0');

        if (sr < 0 || sr > 7 || sc < 0 || sc > 7 || er < 0 || er > 7 || ec < 0 || ec > 7) {
            return null;
        }

        return new int[]{sr, sc, er, ec};
    }
}
