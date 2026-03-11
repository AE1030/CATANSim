package Catan;

import java.util.List;
import java.util.Scanner;

/**
 * Demonstrator class for the Catan game simulator.
 * Runs a game with one human player and three AI players.
 *
 * The human player (Player 1) interacts via console commands:
 *   - "roll"                    : Roll the dice and collect resources
 *   - "list"                    : List cards currently in hand
 *   - "build settlement <nodeId>" : Build a settlement at a node
 *   - "build city <nodeId>"     : Upgrade a settlement to a city
 *   - "build road <from>,<to>"  : Build a road between two nodes
 *   - "go"                      : End turn / proceed to next AI turn
 *
 * AI players (Players 2-4) take their turns automatically.
 * Between AI turns, the human must type "go" to step forward (R2.4).
 *
 * The Robber activates when a 7 is rolled (R2.5):
 *   - Players with more than 7 cards discard half (rounded down)
 *   - The Robber moves to a random tile, blocking its production
 *   - A random adjacent player gives a card to the roller
 *
 * Game state is exported to game_state.json after each turn (R2.3)
 * for integration with the external visualizer (R2.2).
 */
public class Demonstrator {
    public static void main(String[] args) throws Exception {
        // Load game configuration (number of turns)
        Config cfg = Config.load("Code/src-gen/config.txt");

        // Set up the board
        Board board = new Board();

        // Create a shared Scanner for console input
        Scanner scanner = new Scanner(System.in);

        // Player 1 is human, Players 2-4 are AI agents
        List<Player> players = List.of(
                new HumanPlayer(1, scanner),
                new AIPlayer(2),
                new AIPlayer(3),
                new AIPlayer(4)
        );

        // Create and run the game, passing the scanner for step-forward functionality
        Game game = new Game(cfg.turns, board, players, scanner);

        System.out.println("=== CATAN SIMULATOR ===");
        System.out.println("You are Player 1 (Human). Players 2-4 are AI.");
        System.out.println("Type 'go' to step through AI turns.");
        System.out.println("On your turn: roll, list, build, go.");
        System.out.println("========================\n");

        game.run();

        scanner.close();
    }
}