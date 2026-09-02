package daddy.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import daddy.command.AddCommand;
import daddy.command.DeleteCommand;
import daddy.command.ExitCommand;
import daddy.command.FindCommand;
import daddy.command.ListCommand;
import daddy.command.ListOnDateCommand;
import daddy.command.MarkCommand;
import daddy.command.UnmarkCommand;
import daddy.exception.DaddyException;

/**
 * Tests conversion of command text into command objects and user-facing parsing errors.
 */
class ParserTest {
    /**
     * Verifies that every supported command keyword creates its corresponding command class.
     *
     * @throws DaddyException if a valid test command is rejected
     */
    @Test
    void parseCommand_supportedCommands_createsExpectedCommandTypes() throws DaddyException {
        Parser parser = new Parser();

        assertInstanceOf(ExitCommand.class, parser.parseCommand("bye"));
        assertInstanceOf(ListCommand.class, parser.parseCommand("list"));
        assertInstanceOf(ListOnDateCommand.class, parser.parseCommand("list on 02-12-2026"));
        assertInstanceOf(AddCommand.class, parser.parseCommand("todo read book"));
        assertInstanceOf(AddCommand.class, parser.parseCommand("deadline return book /by 02-12-2026 1800"));
        assertInstanceOf(AddCommand.class,
                parser.parseCommand("event meeting /from 02-12-2026 1400 /to 02-12-2026 1600"));
        assertInstanceOf(MarkCommand.class, parser.parseCommand("mark 1"));
        assertInstanceOf(UnmarkCommand.class, parser.parseCommand("unmark 1"));
        assertInstanceOf(DeleteCommand.class, parser.parseCommand("delete 1"));
        assertInstanceOf(FindCommand.class, parser.parseCommand("find book"));
    }

    /**
     * Verifies that incomplete status commands explain which task number is required.
     */
    @Test
    void parseCommand_missingTaskNumber_reportsSpecificCorrection() {
        Parser parser = new Parser();

        assertEquals("MARK?! Mark what... Try: mark 1 (after adding a task first).",
                parseError(parser, "mark").getMessage());
        assertEquals("UNMARK?! Unmark what... Try: unmark 1 (after adding a task first).",
                parseError(parser, "unmark").getMessage());
        assertEquals("DELETE?! Delete what... Try: delete 1 (choose a task number first).",
                parseError(parser, "delete").getMessage());
        assertEquals("FIND?! Find what... Try: find book.",
                parseError(parser, "find").getMessage());
    }

    /**
     * Verifies that malformed arguments retain their command-specific error messages.
     */
    @Test
    void parseCommand_invalidArguments_reportsHelpfulErrors() {
        Parser parser = new Parser();

        assertEquals("'abc' is not a task number. Try mark 1, for example.",
                parseError(parser, "mark abc").getMessage());
        assertEquals("That deadline date needs dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                        + "Try: deadline return book /by 02-12-2019 1800",
                parseError(parser, "deadline return book /by tomorrow").getMessage());
        assertEquals("Daddy has no clue what 'dance' means. "
                        + "Try todo, deadline, event, list, find, mark, unmark, delete, or bye.",
                parseError(parser, "dance").getMessage());
    }

    /**
     * Parses input that is expected to fail and returns its user-facing exception.
     *
     * @param parser the parser under test
     * @param input the invalid command text
     * @return the exception produced for the invalid command
     */
    private DaddyException parseError(Parser parser, String input) {
        return assertThrows(DaddyException.class, () -> parser.parseCommand(input));
    }
}
