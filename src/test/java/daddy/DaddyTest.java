package daddy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import daddy.storage.Storage;

/**
 * Tests Daddy's command-response API used by the graphical interface.
 */
class DaddyTest {
    /** Provides an isolated directory for each test's saved task data. */
    @TempDir
    Path temporaryDirectory;

    /**
     * Verifies that graphical responses use the same commands, messages, and exit state as the CLI.
     */
    @Test
    void getResponse_validCommands_returnsFormattedMessagesAndTracksExit() {
        Storage storage = new Storage(temporaryDirectory.resolve("daddy.txt"),
                temporaryDirectory.resolve("duke.txt"));
        Daddy daddy = new Daddy(storage);

        String addResponse = daddy.getResponse("todo read book");
        String listResponse = daddy.getResponse("list");

        assertTrue(addResponse.contains("Got it. I've added this task:"));
        assertTrue(listResponse.contains("1.[T][ ] read book"));
        assertFalse(daddy.isExitRequested());

        String exitResponse = daddy.getResponse("bye");

        assertTrue(exitResponse.contains("Bye. See you soon :)"));
        assertTrue(daddy.isExitRequested());
    }
}
