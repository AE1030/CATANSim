package Catan;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Player hierarchy (AIPlayer/HumanPlayer) and resource management.
 */
class PlayerTest {

    @Test
    void payThrowsWhenInsufficient() {
        Player p = new AIPlayer(1);
        assertThrows(IllegalStateException.class, () -> p.pay(Cost.road()));
    }

    @Test
    void addResourcesNegativeCountThrows() {
        Player p = new AIPlayer(1);
        assertThrows(IllegalArgumentException.class,
                () -> p.addResources(ResourceType.BRICK, -1));
    }

    @Test
    void discardHalfCardsWhenOverSeven() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.BRICK, 5);
        p.addResources(ResourceType.LUMBER, 5);
        assertEquals(10, p.handSize());

        p.discardHalfCards(new Random(42));
        assertEquals(5, p.handSize());
    }

    @Test
    void discardHalfCardsNoEffectAtBoundarySeven() {
        Player p = new AIPlayer(1);
        p.addResources(ResourceType.BRICK, 7);
        assertEquals(7, p.handSize());

        p.discardHalfCards(new Random(42));
        assertEquals(7, p.handSize());
    }
}
