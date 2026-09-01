import java.nio.file.Path;

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
                if (executeCommand(input)) {
                    break;
                }
            } catch (DaddyException exception) {
                ui.showError(exception.getMessage());
            }
        }

        ui.close();
    }

    /**
     * Executes one recognized command.
     *
     * @param input the complete command entered by the user
     * @return whether the command ends the chat session
     * @throws DaddyException if the command is invalid or cannot be completed
     */
    private static boolean executeCommand(String input) throws DaddyException {
        CommandType commandType = parser.parse(input);
        String arguments = parser.getArguments(input, commandType);
        switch (commandType) {
        case BYE:
            return execute(new ExitCommand());
        case LIST:
        case LIST_ON:
            return execute(parser.parseListCommand(commandType, arguments));
        case TODO:
        case DEADLINE:
        case EVENT:
            return execute(parser.parseAddCommand(commandType, arguments));
        case MARK:
        case UNMARK:
            return execute(parser.parseTaskStateCommand(commandType, arguments));
        case DELETE:
            return execute(parser.parseDeleteCommand(arguments));
        case MARK_MISSING_ARGUMENT:
            throw new DaddyException("MARK?! Mark what... Try: mark 1 (after adding a task first).");
        case UNMARK_MISSING_ARGUMENT:
            throw new DaddyException("UNMARK?! Unmark what... Try: unmark 1 (after adding a task first).");
        case DELETE_MISSING_ARGUMENT:
            throw new DaddyException("DELETE?! Delete what... Try: delete 1 (choose a task number first).");
        case UNKNOWN:
            throw new DaddyException("Daddy has no clue what '" + input
                    + "' means. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
        throw new IllegalStateException("Unhandled command type: " + commandType);
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
