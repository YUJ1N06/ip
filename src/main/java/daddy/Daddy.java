package daddy;

import java.nio.file.Path;

import daddy.command.Command;
import daddy.exception.DaddyException;
import daddy.parser.Parser;
import daddy.storage.Storage;
import daddy.task.TaskList;
import daddy.ui.Ui;

/**
 * Starts Daddy and coordinates command parsing, task storage, and console interaction.
 */
public class Daddy {
    /** Stores the tasks available during the current application session. */
    private static final TaskList tasks = new TaskList();
    /** Saves tasks in the current file and migrates tasks from the legacy file when needed. */
    private static final Storage storage = new Storage(
            Path.of("data", "daddy.txt"), Path.of("data", "duke.txt"));
    /** Reads commands and displays all messages for the user. */
    private static final Ui ui = new Ui();
    /** Converts user-entered command text into executable commands. */
    private static final Parser parser = new Parser();

    /** Prevents instantiation because this class only contains application entry-point methods. */
    private Daddy() {
    }

    /**
     * Starts the Daddy command-line application.
     *
     * @param args command-line arguments, which Daddy does not use
     */
    public static void main(String[] args) {
        ui.showGreeting();
        loadTasks();
        runChatLoop();
    }

    /**
     * Repeatedly reads and executes commands until an exit command is received.
     */
    private static void runChatLoop() {
        while (true) {
            String input = ui.readCommand();

            try {
                Command command = parser.parseCommand(input);
                if (execute(command)) {
                    break;
                }
            } catch (DaddyException exception) {
                ui.showError(exception.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Executes a command object using Daddy's collaborating objects.
     *
     * @param command the command to execute
     * @return whether the command ends the chat session
     * @throws DaddyException if the command cannot be completed
     */
    private static boolean execute(Command command) throws DaddyException {
        command.execute(tasks, ui, storage);
        return command.isExit();
    }

    /**
     * Loads saved tasks and displays any loading or migration messages.
     */
    private static void loadTasks() {
        for (String message : storage.loadInto(tasks)) {
            ui.showError(message);
        }
    }

}
