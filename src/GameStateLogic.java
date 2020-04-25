import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameStateLogic {
    private static Map<Integer, GameState> player;

    static {
        player = new HashMap<>();
        player.put(1, GameState.PLAYER_WON);
        player.put(2, GameState.COMPUTER_WON);
    }

    public static GameState checkGameState(int[][] board, ArrayList<Integer> emptySpaces) {
        // check rows, if there is a winner get the value from a cell in the winning row and get the player from it
        if (checkRows(board) > -1) return player.get(board[checkRows(board)][0]);

        // check columns, if there is a winner get the value from a cell in the winning column and get the player from it
        if (checkColumns(board) > -1) return player.get(board[0][checkColumns(board)]);

        // check diagonals, if there is a winner get the value from the center cell and get the player from it
        if (checkDiagonal(board)) return player.get(board[1][1]);

        // no winner, check for a draw. This method will either return Draw or Unfinished
        return checkDraw(board, emptySpaces);
    }

    private static int checkRows(int[][] board) {
        // for each row, if the first box is not 0, and all boxes match, then there is a win in that row, return the row number
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][0] == board[i][2]) return i;
        }
        return -1;
    }

    private static int checkColumns(int[][] board) {
        // for each column, if the first top box is not 0, and all boxes match, then there is a win in that column, return the column number
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[0][i] == board[2][i]) return i;
        }
        return -1;
    }

    private static boolean checkDiagonal(int[][] board) {
        // if all three boxes are equal and one is not equal to 0, there is a winner, return true
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) return true; // top left to bottom right diag
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[0][2] == board[2][0]) return true; // top right to bottom left
        return false;
    }

    private static GameState checkDraw(int[][] board, ArrayList<Integer> emptySpaces) {
        // if there are 3 or more empty spaces left game is unfinished
        if (emptySpaces.size() > 2) return GameState.UNFINISHED;

        // if there are 0 spaces left and no winner was caught above, then the result is a draw
        if (emptySpaces.size() == 0) return GameState.DRAW;

        // if there are two spaces left, check the result when the two spaces are filled in both ways
        // if both results are a draw, then the game is done and return draw
        if (emptySpaces.size() == 2) {
            int space1 = emptySpaces.get(0);
            int space2 = emptySpaces.get(1);

            SimGame game1 = new SimGame(board, emptySpaces);
            game1.addMove(space1, Player.COMPUTER);
            game1.addMove(space2, Player.USER);
            if (game1.getGameState() != GameState.DRAW) return GameState.UNFINISHED;

            SimGame game2 = new SimGame(board, emptySpaces);
            game2.addMove(space1, Player.USER);
            game2.addMove(space2, Player.COMPUTER);
            if (game2.getGameState() != GameState.DRAW) return GameState.UNFINISHED;
        }

        // if there is one space left, check the result when the space is filled in both ways
        // if both results are a draw, then the game is done and return draw
        if (emptySpaces.size() == 1) {
            SimGame game1 = new SimGame(board, emptySpaces);
            game1.addMove(emptySpaces.get(0), Player.COMPUTER);
            if (game1.getGameState() != GameState.DRAW) return GameState.UNFINISHED;

            SimGame game2 = new SimGame(board, emptySpaces);
            game2.addMove(emptySpaces.get(0), Player.USER);
            if (game2.getGameState() != GameState.DRAW) return GameState.UNFINISHED;
        }

        return GameState.DRAW;
    }

}
