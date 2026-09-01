import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Daddy {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                if (executeCommand(input)) {
                    break;
                }
            } catch (DaddyException exception) {
                ui.showError(exception.getMessage());
            }
        }

        scanner.close();
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
            ui.showExit();
            return true;
        case LIST:
            ui.showTaskList(tasks);
            return false;
        case LIST_ON:
            listTasksOnDate(arguments);
            return false;
        case TODO:
        case DEADLINE:
        case EVENT:
            addTask(parser.parseTask(commandType, arguments));
            return false;
        case MARK:
            markTask(arguments);
            return false;
        case UNMARK:
            unmarkTask(arguments);
            return false;
        case DELETE:
            deleteTask(arguments);
            return false;
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

    private static void listTasksOnDate(String dateText) throws DaddyException {
        try {
            LocalDate date = LocalDate.parse(dateText, DATE_FORMAT);
            ui.showTasksOnDate(date, tasks.getTasksOccurringOn(date));
        } catch (DateTimeParseException exception) {
            throw new DaddyException("That date needs dd-MM-yyyy format. Try: list on 02-12-2019");
        }
    }

    /**
     * Adds a parsed task, persists the list, and displays the confirmation.
     *
     * @param task the task to add
     * @throws DaddyException if the updated task list cannot be saved
     */
    private static void addTask(Task task) throws DaddyException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void markTask(String taskNumber) throws DaddyException {
        int taskIndex = getTaskIndex(taskNumber, "mark");
        Task task = tasks.markTaskAsDone(taskIndex);
        storage.save(tasks);
        ui.showTaskMarked(task);
    }

    private static void unmarkTask(String taskNumber) throws DaddyException {
        int taskIndex = getTaskIndex(taskNumber, "unmark");
        Task task = tasks.markTaskAsNotDone(taskIndex);
        storage.save(tasks);
        ui.showTaskUnmarked(task);
    }

    private static void deleteTask(String taskNumber) throws DaddyException {
        int taskIndex = getTaskIndex(taskNumber, "delete");
        Task removedTask = tasks.removeTaskAt(taskIndex);
        storage.save(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Converts a one-based task number into a valid zero-based task index.
     *
     * @param taskNumber the task number entered by the user
     * @param command the command that uses the task number
     * @return the matching zero-based task index
     * @throws DaddyException if the supplied task number is invalid
     */
    private static int getTaskIndex(String taskNumber, String command) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (!tasks.hasTaskAt(taskIndex)) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
            }
            return taskIndex;
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try " + command + " 1, for example.");
        }
    }

    private static void loadTasks() {
        for (String message : storage.loadInto(tasks)) {
            ui.showError(message);
        }
    }

}
