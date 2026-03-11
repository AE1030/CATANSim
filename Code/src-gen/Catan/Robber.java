package Catan;

/**
 * Represents the Robber piece in Catan.
 * The Robber starts on the desert tile and blocks resource production
 * on whatever tile it occupies. When a 7 is rolled, the Robber is moved.
 */
public class Robber {
    private int tileId;

    public Robber(int initialTileId) {
        this.tileId = initialTileId;
    }

    public int getTileId() {
        return tileId;
    }

    public void moveTo(int newTileId) {
        this.tileId = newTileId;
    }

    public boolean isOnTile(int tileId) {
        return this.tileId == tileId;
    }
}
