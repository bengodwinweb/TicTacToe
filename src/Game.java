import java.util.*;

public class Game {

    public static Map<Integer, Character> gameSymbols;

    private int[][] board;
    private ArrayList<Integer> emptySpaces;

    private GameState gameState;
    private Player lastPlayed;
    private String lastMove;

    // initializes a static hashmap for the symbols that will be used to represent each user or no value when printing the board
    static {
        gameSymbols = new HashMap<>();
        gameSymbols.put(0, ' ');
        gameSymbols.put(1, 'X');
        gameSymbols.put(2, 'O');
    }

    public Game() {
        this.board = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        this.gameState = GameState.UNFINISHED;
        this.lastPlayed = Player.COMPUTER;
        this.lastMove = "New Game";
        this.emptySpaces = new ArrayList<>();
        // initialize the empty spaces list to include all spaces
        for (int i = 1; i <= 9; i++) {
            emptySpaces.add(i);
        }
    }

    // method for master class to enter a move for the User
    public void enterMove(int space) {
        // do not process the move if it is not the user's turn
        if (lastPlayed != Player.COMPUTER) {
            lastMove = "Invalid move - not player's turn";
            return;
        }
        // do not process the move if the selected space is already full
        if (emptySpaces.indexOf(space) < 0) {
            lastMove = "Invalid move - not an empty space";
            return;
        }
        // do not process the move if the game is finished
        if (gameState != GameState.UNFINISHED) {
            lastMove = "Invalid move - game already finished";
            return;
        }

        // set the value of the specified space to 1 (the user value)
        int row = (space - 1) / 3;
        int col = (space - 1) % 3;
        board[row][col] = 1;

        // remove the space from the emptySpaces list
        emptySpaces.remove((Integer) space);

        this.lastMove = "Player placed marker at space " + space;
        this.lastPlayed = Player.USER;
        this.gameState = GameStateLogic.checkGameState(board, emptySpaces);
    }

    // method for master class to call for the computer to make an automated move
    public int computerMove() {
        // do not process if it is not the computer's turn
        if (lastPlayed != Player.USER) {
            lastMove = "Invalid move - not computer's turn";
            return -1;
        }
        // do not process if the game is finished
        if (gameState != GameState.UNFINISHED) {
            lastMove = "Invalid move - game already finished";
            return -1;
        }

        SimGame aGame = createGameBoard();

        // look for a winning offensive move and defensive move to save an opponent win
        int winningCpuMove = aGame.winningMove(Player.COMPUTER);
        int winningPlayerMove = aGame.winningMove(Player.USER);
        int selectedSpace;

        if (winningCpuMove == -1 && winningPlayerMove == -1) { // no winning move, perform simulated games to select best move

            // 1. Create new array, same size as empty spaces, and assign a starting weight of zero for each space
            int[] weightArray = new int[emptySpaces.size()];
            for (int i = 0; i < weightArray.length; i++) {
                weightArray[i] = 0;
            }

            // 2. for each index in emptySpaces, sim 25 games, adding the result of each to the weightArray index for that space
            // if the sim is a win, the weight will increase by 1, loss will decrease by 1, draw will stay the same
            int numberOfSims = 25;

            for (int i = 0; i < emptySpaces.size(); i++) {
                for (int j = 0; j < numberOfSims; j++) {
                    SimGame gameBoard = createGameBoard();
                    gameBoard.addMove(emptySpaces.get(i), Player.COMPUTER);
                    int weight = gameBoard.simulate();
                    weightArray[i] += weight;
                }
            }

            // 3. select the index with the highest weight and get the corresponding empty space from emptySpaces
            int maxIndex = 0;
            for (int i = 1; i < weightArray.length; i++) {
                if (weightArray[i] > weightArray[maxIndex]) maxIndex = i;
            }

            selectedSpace = emptySpaces.get(maxIndex);

        } else { // there is a winning move, select that space
            if (winningCpuMove > -1) selectedSpace = winningCpuMove; // if there is a winning computer move, use that
            else selectedSpace = winningPlayerMove; // if there is not a winning computer move, defend against a winning player move
        }

        // 4. place a computer marker at the selected space, return the space number
        int row = (selectedSpace - 1) / 3;
        int col = (selectedSpace - 1) % 3;
        board[row][col] = 2;

        emptySpaces.remove((Integer) selectedSpace);

        this.lastMove = "Computer placed marker at space " + selectedSpace;
        this.lastPlayed = Player.COMPUTER;
        this.gameState = GameStateLogic.checkGameState(board, emptySpaces);

        return selectedSpace;
    }

    private SimGame createGameBoard() {
        return new SimGame(board, emptySpaces, lastPlayed);
    }

    // used to print out the current tic-tac-toe board for the user
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("\n");

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                sb.append(' ').append(gameSymbols.get(board[i][j])).append(' ');

                if (j < board[0].length - 1) {
                    sb.append('|');
                }
            }

            if (i < board.length - 1) {
                sb.append("\n-----------\n");
            }
        }

        sb.append('\n');

        return sb.toString();
    }

    // prints a version of the tic tac toe board showing the space numbers for each space
    public static void printKeyMap() {
        StringBuilder sb = new StringBuilder().append('\n');
        int boardSize = 3;
        int current = 1;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(' ').append(current).append(' ');

                if (j < boardSize - 1) {
                    sb.append('|');
                }

                current++;
            }
            if (i < boardSize - 1) {
                sb.append("\n-----------\n");
            }
        }

        System.out.println(sb.toString());
    }

    // used for debugging to see the current state of the game
    public void printState() {
        System.out.println("\nLast Move: " + this.lastPlayed);
        System.out.println("Game State: " + this.gameState);
        System.out.println("Empty Spaces:");
        for (int space : emptySpaces) {
            System.out.print(space + "\t");
        }
        System.out.println();
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getLastPlayed() {
        return lastPlayed;
    }

    public String getLastMove() {
        return lastMove;
    }
}
