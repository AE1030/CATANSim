package Catan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TileTest {

    @Test
    void producesOnMatchingRoll() {
        Tile tile = new Tile(0, ResourceType.LUMBER, 8);
        assertTrue(tile.producesOn(8));
    }

    @Test
    void doesNotProduceOnDifferentRoll() {
        Tile tile = new Tile(0, ResourceType.LUMBER, 8);
        assertFalse(tile.producesOn(6));
    }

    @Test
    void desertNeverProduces() {
        Tile tile = new Tile(16, ResourceType.DESERT, 0);
        assertFalse(tile.producesOn(0));
        assertFalse(tile.producesOn(7));
    }

    @Test
    void isDesert() {
        Tile desert = new Tile(16, ResourceType.DESERT, 0);
        Tile lumber = new Tile(0, ResourceType.LUMBER, 10);
        assertTrue(desert.isDesert());
        assertFalse(lumber.isDesert());
    }

    @Test
    void gettersReturnConstructorValues() {
        Tile tile = new Tile(5, ResourceType.WOOL, 12);
        assertEquals(5, tile.getTileId());
        assertEquals(ResourceType.WOOL, tile.getResourceType());
        assertEquals(12, tile.getNumberToken());
    }
}
