import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Daddy {
    private static final String INDENT = "    ";
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private static final Path BACKUP_FILE = Path.of("data", "duke.txt.backup");
    private static final Path CORRUPTED_FILE = Path.of("data", "duke.txt.corrupt");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        printGreeting();
        loadTasks();
        runChatLoop();
    }

    private static void printGreeting() {
        String banner = " ____                      _       _       \n"
                + "|  _ \\   __ _   __| |   __| |  _   _ \n"
                + "| | | | / _` | / _` |  / _` | | | | |\n"
                + "| | | || (_| || (_| | | (_| | | |_| |\n"
                + "| |_| | \\__,_| \\__,_|  \\__,_|  \\__, |\n"
                + "|____/                          |___/ \n";
        System.out.println(INDENT + "____________________________________________________________");
        printIndentedBanner(banner);
        System.out.println(INDENT + "Hello, little one.");
        System.out.println(INDENT + "What can I assist you with today ;)?");
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void runChatLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equalsIgnoreCase("bye")) {
                    printExit();
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    printList();
                } else if (input.toLowerCase().startsWith("list on ")) {
                    listTasksOnDate(input.substring(8).trim());
                } else if (input.equalsIgnoreCase("todo") || input.toLowerCase().startsWith("todo ")) {
                    addTodo(input.length() == 4 ? "" : input.substring(5).trim());
                } else if (input.equalsIgnoreCase("mark")) {
                    throw new DaddyException("MARK?! Mark what... Try: mark 1 (after adding a task first).");
                } else if (input.equalsIgnoreCase("unmark")) {
                    throw new DaddyException("UNMARK?! Unmark what... Try: unmark 1 (after adding a task first).");
                } else if (input.equalsIgnoreCase("deadline")) {
                    throw new DaddyException("What deadlines are coming up? Better hurry, add them and get them "
                            + "done ASAP. Try: deadline return book /by Sunday.");
                } else if (input.equalsIgnoreCase("event")) {
                    throw new DaddyException("What exciting event are you planning? Give it a name so Daddy can "
                            + "keep track. Try: event meeting /from Mon 2pm /to 4pm.");
                } else if (input.equalsIgnoreCase("delete")) {
                    throw new DaddyException("DELETE?! Delete what... Try: delete 1 (choose a task number first).");
                } else if (input.toLowerCase().startsWith("deadline ")) {
                    addDeadline(input.substring(9).trim());
                } else if (input.toLowerCase().startsWith("event ")) {
                    addEvent(input.substring(6).trim());
                } else if (input.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(input.substring(7).trim());
                } else if (input.toLowerCase().startsWith("delete ")) {
                    deleteTask(input.substring(7).trim());
                } else if (input.toLowerCase().startsWith("mark ")) {
                    markTask(input.substring(5).trim());
                } else {
                    throw new DaddyException("Daddy has no clue what '" + input
                            + "' means. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
                }
            } catch (DaddyException exception) {
                printError(exception.getMessage());
            }
        }

        scanner.close();
    }

    private static void printList() {
        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(INDENT + " " + (i + 1) + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
        }
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void listTasksOnDate(String dateText) throws DaddyException {
        try {
            LocalDate date = LocalDate.parse(dateText, DATE_FORMAT);
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " Tasks occurring on " + date + ":");
            int displayedTaskNumber = 1;
            for (Task task : tasks) {
                if (task.occursOn(date)) {
                    System.out.println(INDENT + " " + displayedTaskNumber + "." + task.getTypeIcon()
                            + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
                    displayedTaskNumber++;
                }
            }
            if (displayedTaskNumber == 1) {
                System.out.println(INDENT + " No deadlines or events found for that date.");
            }
            System.out.println(INDENT + "____________________________________________________________");
        } catch (DateTimeParseException exception) {
            throw new DaddyException("That date needs dd-MM-yyyy format. Try: list on 02-12-2019");
        }
    }

    private static void addTodo(String description) throws DaddyException {
        if (description.isEmpty()) {
            throw new DaddyException("Hmm, what are you thinking of doing today? You need a description "
                    + "for that. Try: todo borrow book.");
        }
        tasks.add(new Todo(description));
        saveTasks();

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks.get(tasks.size() - 1).getTypeIcon() + "["
                + tasks.get(tasks.size() - 1).getStatusIcon() + "] " + description);
        System.out.println(INDENT + " Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(INDENT + "____________________________________________________________");
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
            tasks.add(new Deadline(description, parseDateTime(deadline)));
        } catch (DateTimeParseException exception) {
            throw new DaddyException("That deadline date needs dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: deadline return book /by 02-12-2019 1800");
        }
        saveTasks();

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks.get(tasks.size() - 1).getTypeIcon() + "["
                + tasks.get(tasks.size() - 1).getStatusIcon() + "] "
                + tasks.get(tasks.size() - 1).getDisplayDescription());
        System.out.println(INDENT + " Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(INDENT + "____________________________________________________________");
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
            tasks.add(new Event(description, parseDateTime(from), parseDateTime(to)));
        } catch (DateTimeParseException exception) {
            throw new DaddyException("Event times need dd-MM-yyyy or dd-MM-yyyy HHmm format. "
                    + "Try: event meeting /from 02-12-2019 1400 /to 02-12-2019 1600");
        }
        saveTasks();

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks.get(tasks.size() - 1).getTypeIcon() + "["
                + tasks.get(tasks.size() - 1).getStatusIcon() + "] "
                + tasks.get(tasks.size() - 1).getDisplayDescription());
        System.out.println(INDENT + " Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void markTask(String taskNumber) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
            }

            tasks.get(taskIndex).markAsDone();
            saveTasks();
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " Nice! I've marked this task as done:");
            System.out.println(INDENT + "   [X] " + tasks.get(taskIndex).getDisplayDescription());
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try mark 1, for example.");
        }
    }

    private static void unmarkTask(String taskNumber) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
            }

            tasks.get(taskIndex).markAsNotDone();
            saveTasks();
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " OK, I've marked this task as not done yet:");
            System.out.println(INDENT + "   [ ] " + tasks.get(taskIndex).getDisplayDescription());
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try unmark 1, for example.");
        }
    }

    private static void deleteTask(String taskNumber) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
            }

            Task removedTask = tasks.remove(taskIndex);
            saveTasks();
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " Noted. I've removed this task:");
            System.out.println(INDENT + "   " + removedTask.getTypeIcon() + "[" + removedTask.getStatusIcon()
                    + "] " + removedTask.getDisplayDescription());
            System.out.println(INDENT + " Now you have " + tasks.size() + " tasks in the list.");
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try delete 1, for example.");
        }
    }

    private static void printError(String message) {
        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " " + message);
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void loadTasks() {
        if (!Files.exists(DATA_FILE)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            List<String> corruptedLines = new ArrayList<>();
            for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
                String line = lines.get(lineNumber);
                if (line.isBlank()) {
                    continue;
                }
                try {
                    String[] fields = line.split("\\|", -1);
                    Task task = createTask(fields);
                    if ("1".equals(fields[1])) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException exception) {
                    corruptedLines.add("line " + (lineNumber + 1) + ": " + line);
                    printError("Daddy skipped corrupted data on line " + (lineNumber + 1)
                            + ". Expected T|0|description, D|0|description|date, or "
                            + "E|0|description|from|to.");
                }
            }
            if (!corruptedLines.isEmpty()) {
                Files.copy(DATA_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
                Files.write(CORRUPTED_FILE, corruptedLines);
                printError("Daddy saved the original file as data/duke.txt.backup and "
                        + "the bad records as data/duke.txt.corrupt so you can fix them.");
            }
        } catch (IOException | RuntimeException exception) {
            printError("Daddy couldn't load the task list. Check data/duke.txt and try again.");
        }
    }

    private static Task createTask(String[] fields) {
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("invalid task record");
        }
        return switch (fields[0]) {
        case "T" -> {
            if (fields.length != 3) {
                throw new IllegalArgumentException("invalid todo record");
            }
            yield new Todo(fields[2]);
        }
        case "D" -> {
            if (fields.length != 4) {
                throw new IllegalArgumentException("invalid deadline record");
            }
            yield new Deadline(fields[2], parseDateTime(fields[3]));
        }
        case "E" -> {
            if (fields.length != 5) {
                throw new IllegalArgumentException("invalid event record");
            }
            yield new Event(fields[2], parseDateTime(fields[3]), parseDateTime(fields[4]));
        }
        default -> throw new IllegalArgumentException("unknown task type");
        };
    }

    private static void saveTasks() throws DaddyException {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                String description = task.getDescription().replace("|", " ");
                String status = task.getStatusIcon().equals("X") ? "1" : "0";
                String line = switch (task.getType()) {
                case TODO -> "T|" + status + "|" + description;
                case DEADLINE -> "D|" + status + "|" + description
                        + "|" + ((Deadline) task).getDeadline();
                case EVENT -> "E|" + status + "|" + description
                        + "|" + ((Event) task).getFrom() + "|" + ((Event) task).getTo();
                case GENERAL -> "T|" + status + "|" + description;
                };
                lines.add(line);
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException exception) {
            throw new DaddyException("Daddy couldn't save your tasks. Check that the data folder is writable.");
        }
    }

    private static LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value, DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            return LocalDate.parse(value, DATE_FORMAT).atTime(LocalTime.MIDNIGHT);
        }
    }

    private static void printExit() {
        System.out.println(INDENT + "Bye. See you soon :)");
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void printIndentedBanner(String banner) {
        for (String line : banner.split("\n")) {
            System.out.println(INDENT + line);
        }
    }
}
