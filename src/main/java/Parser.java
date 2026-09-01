import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Recognizes Daddy commands and extracts their argument text.
 */
public class Parser {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");

    /**
     * Determines the type of a command without changing its argument text.
     *
     * @param input the complete command entered by the user
     * @return the recognized command type, or {@link CommandType#UNKNOWN}
     */
    public CommandType parse(String input) {
        String lowerCaseInput = input.toLowerCase();
        if (input.equalsIgnoreCase("bye")) {
            return CommandType.BYE;
        } else if (input.equalsIgnoreCase("list")) {
            return CommandType.LIST;
        } else if (lowerCaseInput.startsWith("list on ")) {
            return CommandType.LIST_ON;
        } else if (input.equalsIgnoreCase("todo") || lowerCaseInput.startsWith("todo ")) {
            return CommandType.TODO;
        } else if (input.equalsIgnoreCase("mark")) {
            return CommandType.MARK_MISSING_ARGUMENT;
        } else if (input.equalsIgnoreCase("unmark")) {
            return CommandType.UNMARK_MISSING_ARGUMENT;
        } else if (input.equalsIgnoreCase("deadline") || lowerCaseInput.startsWith("deadline ")) {
            return CommandType.DEADLINE;
        } else if (input.equalsIgnoreCase("event") || lowerCaseInput.startsWith("event ")) {
            return CommandType.EVENT;
        } else if (input.equalsIgnoreCase("delete")) {
            return CommandType.DELETE_MISSING_ARGUMENT;
        } else if (lowerCaseInput.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (lowerCaseInput.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (lowerCaseInput.startsWith("mark ")) {
            return CommandType.MARK;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Extracts and trims the text after a command keyword.
     *
     * @param input the complete command entered by the user
     * @param commandType the recognized command type
     * @return the argument text, or an empty string when the command has no arguments
     */
    public String getArguments(String input, CommandType commandType) {
        return switch (commandType) {
        case LIST_ON -> input.substring(8).trim();
        case TODO -> input.length() == 4 ? "" : input.substring(5).trim();
        case DEADLINE -> input.length() == 8 ? "" : input.substring(9).trim();
        case EVENT -> input.length() == 5 ? "" : input.substring(6).trim();
        case MARK -> input.substring(5).trim();
        case UNMARK, DELETE -> input.substring(7).trim();
        default -> "";
        };
    }

    /**
     * Creates a task from the details of a task-creation command.
     *
     * @param commandType the type of task to create
     * @param taskDetails the task description and date details, when applicable
     * @return the task described by the command
     * @throws DaddyException if the task details are incomplete or incorrectly formatted
     */
    public Task parseTask(CommandType commandType, String taskDetails) throws DaddyException {
        return switch (commandType) {
        case TODO -> parseTodo(taskDetails);
        case DEADLINE -> parseDeadline(taskDetails);
        case EVENT -> parseEvent(taskDetails);
        default -> throw new IllegalArgumentException("command does not create a task");
        };
    }

    /** Creates a todo from its description. */
    private Task parseTodo(String description) throws DaddyException {
        if (description.isEmpty()) {
            throw new DaddyException("Hmm, what are you thinking of doing today? You need a description "
                    + "for that. Try: todo borrow book.");
        }
        return new Todo(description);
    }

    /** Creates a deadline from its description and {@code /by} date or time. */
    private Task parseDeadline(String taskDetails) throws DaddyException {
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
            return new Deadline(description, parseDateTime(deadline));
        } catch (DateTimeParseException exception) {
            throw new DaddyException("That deadline date needs dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: deadline return book /by 02-12-2019 1800");
        }
    }

    /** Creates an event from its description and {@code /from} and {@code /to} times. */
    private Task parseEvent(String taskDetails) throws DaddyException {
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
            return new Event(description, parseDateTime(from), parseDateTime(to));
        } catch (DateTimeParseException exception) {
            throw new DaddyException("Event times need dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: event meeting /from 02-12-2019 1400 /to 02-12-2019 1600");
        }
    }

    /** Parses a date or date-time accepted in task-creation commands. */
    private LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            return LocalDate.parse(value, DATE_FORMAT).atTime(LocalTime.MIDNIGHT);
        }
    }
}
