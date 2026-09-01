import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Daddy {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
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
            addTodo(arguments);
            return false;
        case DEADLINE:
            addDeadline(arguments);
            return false;
        case EVENT:
            addEvent(arguments);
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

    private static void addTodo(String description) throws DaddyException {
        if (description.isEmpty()) {
            throw new DaddyException("Hmm, what are you thinking of doing today? You need a description "
                    + "for that. Try: todo borrow book.");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    private static void addDeadline(String taskDetails) throws DaddyException {
        int byIndex = taskDetails.indexOf(" /by ");
        String description = byIndex >= 0 ? taskDetails.substring(0, byIndex).trim() : taskDetails;
        String deadline = byIndex >= 0 ? taskDetails.substring(byIndex + 5).trim() : "";
        if (description.isEmpty()) {
            throw new DaddyException("What deadlines are coming up? Better hurry, add them and get them "
                    + "done ASAP. Try: deadline return book /by Sunday.");
        }
        if (deadline.isEmpty()) {
            throw new DaddyException("A deadline needs a /by date or time. Try: deadline return book /by Sunday");
        }
        try {
            Task task = new Deadline(description, parseDateTime(deadline));
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.size());
        } catch (DateTimeParseException exception) {
            throw new DaddyException("That deadline date needs dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: deadline return book /by 02-12-2019 1800");
        }
    }

    private static void addEvent(String taskDetails) throws DaddyException {
        int fromIndex = taskDetails.indexOf(" /from ");
        int toIndex = taskDetails.indexOf(" /to ");
        String description = fromIndex >= 0 ? taskDetails.substring(0, fromIndex).trim() : taskDetails;
        String from = fromIndex >= 0 && toIndex > fromIndex
                ? taskDetails.substring(fromIndex + 7, toIndex).trim() : "";
        String to = toIndex >= 0 ? taskDetails.substring(toIndex + 5).trim() : "";
        if (description.isEmpty()) {
            throw new DaddyException("What exciting event are you planning? Give it a name so Daddy can "
                    + "keep track. Try: event meeting /from Mon 2pm /to 4pm.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new DaddyException("An event needs both /from and /to times. Try: event meeting /from Mon 2pm /to 4pm");
        }
        try {
            Task task = new Event(description, parseDateTime(from), parseDateTime(to));
            tasks.add(task);
            storage.save(tasks);
            ui.showTaskAdded(task, tasks.size());
        } catch (DateTimeParseException exception) {
            throw new DaddyException("Event times need dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: event meeting /from 02-12-2019 1400 /to 02-12-2019 1600");
        }
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

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            return LocalDate.parse(value, DATE_FORMAT).atTime(LocalTime.MIDNIGHT);
        }
    }

}
