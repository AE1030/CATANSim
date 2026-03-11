package Catan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CommandParser using regex-based parsing.
 * Covers boundary values and edge cases.
 */
class CommandParserTest {

    @Test
    void parseBasicCommandsWithVariations() {
        assertEquals(CommandParser.CommandType.ROLL, CommandParser.parse("roll").type);
        assertEquals(CommandParser.CommandType.ROLL, CommandParser.parse("ROLL").type);
        assertEquals(CommandParser.CommandType.ROLL, CommandParser.parse("  roll  ").type);
        assertEquals(CommandParser.CommandType.GO, CommandParser.parse("go").type);
        assertEquals(CommandParser.CommandType.GO, CommandParser.parse(" GO ").type);
        assertEquals(CommandParser.CommandType.LIST, CommandParser.parse("list").type);
        assertEquals(CommandParser.CommandType.LIST, CommandParser.parse("LIST").type);
    }

    @Test
    void parseBuildSettlementBoundaryZero() {
        CommandParser.Command cmd = CommandParser.parse("build settlement 0");
        assertEquals(CommandParser.CommandType.BUILD_SETTLEMENT, cmd.type);
        assertEquals(0, cmd.args[0]);
    }

    @Test
    void parseNullInput() {
        assertEquals(CommandParser.CommandType.INVALID, CommandParser.parse(null).type);
    }

    @Test
    void parseBuildRoadMissingSecondNode() {
        assertEquals(CommandParser.CommandType.INVALID, CommandParser.parse("build road 5").type);
    }
}
