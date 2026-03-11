package Catan;

import java.util.*;

/**
 * Computer-controlled player that uses a greedy strategy.
 * Prioritizes building settlements, then roads, then cities.
 */
public class AIPlayer extends Player {
    private final Random rng = new Random();

    public AIPlayer(int id) {
        super(id);
    }

    @Override
    public String takeTurn(Game game) {
        Board board = game.getBoard();
        return tryBuildOnce(board);
    }

    private String tryBuildOnce(Board board) {
        String action;

        if (hasResources(Cost.settlement()) && !(action = buildSettlement(board)).startsWith("PASS")) {
            return action;
        }
        if (hasResources(Cost.road()) && !(action = buildRoad(board)).startsWith("PASS")) {
            return action;
        }
        if (hasResources(Cost.city()) && !(action = buildCity(board)).startsWith("PASS")) {
            return action;
        }
        return pass();
    }

    private String buildSettlement(Board board) {
        List<Node> spots = new ArrayList<>();
        for (Node n : board.getNodes()) {
            if (board.canPlaceSettlement(this, n)) {
                spots.add(n);
            }
        }
        if (spots.isEmpty()) {
            return pass();
        }

        Node at = spots.get(rng.nextInt(spots.size()));
        pay(Cost.settlement());
        return board.placeSettlement(this, at)
                ? "BUILD SETTLEMENT at node " + at.getNodeId()
                : pass();
    }

    private String buildCity(Board board) {
        List<Node> spots = new ArrayList<>();
        for (Node n : board.getNodes()) {
            if (board.canUpgradeToCity(this, n)) {
                spots.add(n);
            }
        }
        if (spots.isEmpty()) {
            return pass();
        }

        Node at = spots.get(rng.nextInt(spots.size()));
        pay(Cost.city());
        return board.upgradeToCity(this, at)
                ? "BUILD CITY at node " + at.getNodeId()
                : pass();
    }

    private String buildRoad(Board board) {
        List<int[]> edges = new ArrayList<>();

        for (Node a : board.getNodes()) {
            int aId = a.getNodeId();
            for (int bId : BoardTopology.nodeNeighbors.get(aId)) {
                if (aId >= bId) {
                    continue;
                }
                Node b = board.getNode(bId);
                if (board.canPlaceRoad(this, a, b)) {
                    edges.add(new int[]{aId, bId});
                }
            }
        }
        if (edges.isEmpty()) {
            return pass();
        }

        int[] e = edges.get(rng.nextInt(edges.size()));
        Node a = board.getNode(e[0]);
        Node b = board.getNode(e[1]);

        pay(Cost.road());
        return board.placeRoad(this, a, b)
                ? "BUILD ROAD " + a.getNodeId() + "-" + b.getNodeId()
                : pass();
    }

    private String pass() {
        return "PASS";
    }
}
