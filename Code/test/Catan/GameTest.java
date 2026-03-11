package Catan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Game logic flow: construction validation, round lifecycle,
 * robber mechanics, resource production, and board placement rules.
 */
class GameTest {

    private Board board;
    private List<Player> players;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setupDefaultMap();
        players = List.of(
                new AIPlayer(1),
                new AIPlayer(2),
                new AIPlayer(3),
                new AIPlayer(4)
        );
    }

    // ===== Construction boundary/edge cases =====

    @Test
    void constructorRejectsBoundaryRounds() {
        assertThrows(IllegalArgumentException.class, () -> new Game(0, board, players));
        assertThrows(IllegalArgumentException.class, () -> new Game(8193, board, players));
        assertDoesNotThrow(() -> new Game(1, board, players));
        assertDoesNotThrow(() -> new Game(8192, board, players));
    }

    @Test
    void constructorRejectsWrongPlayerCount() {
        List<Player> two = List.of(new AIPlayer(1), new AIPlayer(2));
        assertThrows(IllegalArgumentException.class, () -> new Game(10, board, two));
    }

    // ===== Dice =====

    @Test
    void rollDiceAlwaysInRange() {
        Game game = new Game(10, board, players);
        for (int i = 0; i < 500; i++) {
            int roll = game.rollDice();
            assertTrue(roll >= 2 && roll <= 12, "Roll out of range: " + roll);
        }
    }

    // ===== Win / finish conditions =====

    @Test
    void winnerDetectedAtTenVp() {
        Game game = new Game(100, board, players);
        Player p = game.getPlayers().get(0);
        p.addVictoryPoints(10);
        assertNotNull(game.winner());
        assertEquals(p.getId(), game.winner().getId());
        assertTrue(game.isFinished());
    }

    @Test
    void gameFinishesWhenMaxRoundsReached() {
        Game game = new Game(1, board, players);
        game.run();
        assertTrue(game.isFinished());
    }

    // ===== Resource production =====

    @Test
    void produceResourcesGivesCardsToSettlementOwner() {
        Player p = players.get(0);
        // Tile 0 is LUMBER with token 10, nodes include node 4
        board.placeSettlementInitial(p, board.getNode(4));

        int before = p.handSize();
        board.produceResources(10);
        assertTrue(p.handSize() > before,
                "Player should receive resources from production");
    }

    @Test
    void produceResourcesBlockedByRobber() {
        Player p = players.get(0);
        board.placeSettlementInitial(p, board.getNode(4));
        board.getRobber().moveTo(0); // block tile 0 (LUMBER, token 10)

        int before = p.handSize();
        board.produceResources(10);
        assertEquals(before, p.handSize(),
                "Robber should block production on its tile");
    }

    // ===== Robber flow =====

    @Test
    void handleRobberMovesToDifferentTile() {
        Game game = new Game(10, board, players);
        int before = board.getRobber().getTileId();
        game.handleRobber(game.getPlayers().get(0));
        assertNotEquals(before, board.getRobber().getTileId());
    }

    // ===== Placement rules =====

    @Test
    void distanceRuleBlocksAdjacentSettlement() {
        Player p = players.get(0);
        board.placeSettlementInitial(p, board.getNode(0));

        for (int nbId : BoardTopology.nodeNeighbors.get(0)) {
            assertFalse(board.canPlaceSettlement(p, board.getNode(nbId)),
                    "Distance rule violated at node " + nbId);
        }
    }

    @Test
    void cannotUpgradeCityAgain() {
        Player p = players.get(0);
        Node node = board.getNode(0);
        board.placeSettlementInitial(p, node);
        assertTrue(board.upgradeToCity(p, node));
        assertFalse(board.canUpgradeToCity(p, node),
                "Cannot upgrade a city — only settlements");
    }
}
