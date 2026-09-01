package daddy;

import java.nio.file.Path;

import daddy.command.Command;
import daddy.exception.DaddyException;
import daddy.parser.Parser;
import daddy.storage.Storage;
import daddy.task.TaskList;
import daddy.ui.Ui;

public class Daddy {
    private static final TaskList tasks = new TaskList();
    private static final Storage storage = new Storage(Path.of("data", "duke.txt"));
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();

    public static void main(String[] args) {
        ui.showGreeting();
        loadTasks();
        runChatLoop();
    }

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

    private static void loadTasks() {
        for (String message : storage.loadInto(tasks)) {
            ui.showError(message);
        }
    }

}
