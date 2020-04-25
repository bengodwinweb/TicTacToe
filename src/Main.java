import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String response;

        // Ask the User if they would like to play
        System.out.println("Start new game? Y / N");

        // Parse response from the user, repeat if response is invalid
        while (true) {
            response = scan.nextLine().toLowerCase();
            if (response.equals("y") || response.equals("n")) break;
            System.out.println("Invalid selection - please enter Y or N");
        }

        // while the response remains "Y", continue playing tic tac toe
        while (response.equals("y")) {
            playGame();

            System.out.println("\nPlay again? y / n");
            while (true) {
                response = scan.nextLine().toLowerCase();
                if (response.equals("y") || response.equals("n")) break;
                System.out.println("Invalid selection - please enter Y or N");
            }
        }

        System.out.println("\nGoodbye!");
    }

    public static void playGame() {
        Game game = new Game();
        Scanner scan = new Scanner(System.in);
        int selectedSpace;

        System.out.println("\nNew Game Started!");
        Game.printKeyMap(); // prints keyMap for the User so they know how to refer to spaces
        System.out.println();

        // loop until the game is finished
        while (game.getGameState() == GameState.UNFINISHED) {

            // switch based on which player went last
            switch (game.getLastPlayed()) {
                case COMPUTER:
                    // User's turn
                    System.out.println(game.getLastMove()); // print last move that happened
                    System.out.println("\nYour Turn");
                    System.out.println("Where would you like to place your marker?"); // prompt user for their next move
                    while (true) {
                        try {
                            selectedSpace = Integer.parseInt(scan.nextLine());
                            if (selectedSpace >= 1 && selectedSpace <= 9) break;
                        } catch (NumberFormatException e) {
                        }
                        System.out.println("Please enter a valid integer between 1 and 9");
                    }
                    game.enterMove(selectedSpace); // enter that move to the board
                    break;
                case USER:
                    // Computer's turn
                    System.out.println(game.getLastMove()); // print last move that happened
                    System.out.println("\nComputer's Turn");
                    game.computerMove(); // ask for computer move
                    break;
            }

            // print the current board
            System.out.println(game);
        }

        // print the appropriate message based on the result of the game
        switch (game.getGameState()) {
            case PLAYER_WON:
                System.out.println("\nYou won!! :)");
                break;
            case COMPUTER_WON:
                System.out.println("\nComputer won :(");
                break;
            case DRAW:
                System.out.println("\nIt was a Draw :/");
                break;
        }

    }
}
