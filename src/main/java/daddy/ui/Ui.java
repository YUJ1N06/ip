package daddy.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import daddy.task.Task;
import daddy.task.TaskList;

/**
 * Handles all console output shown to a Daddy user.
 */
public class Ui {
    /** Indents every line shown in Daddy's console interface. */
    private static final String INDENT = "    ";
    /** Separates distinct messages in Daddy's console interface. */
    private static final String DIVIDER = "____________________________________________________________";
    /** Reads commands entered through standard input. */
    private final Scanner scanner;
    /** Receives each complete line that Daddy displays. */
    private final Consumer<String> output;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.output = System.out::println;
    }

    /**
     * Creates an interface that sends displayed lines to a supplied output receiver.
     *
     * @param output the receiver for each complete displayed line
     */
    public Ui(Consumer<String> output) {
        this.scanner = null;
        this.output = output;
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the complete command line
     */
    public String readCommand() {
        if (scanner == null) {
            throw new IllegalStateException("this UI does not read console commands");
        }
        return scanner.nextLine();
    }

    /** Releases the input stream used by this interface. */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /** Displays Daddy's greeting banner. */
    public void showGreeting() {
        String banner = " ____                      _       _       \n"
                + "|  _ \\   __ _   __| |   __| |  _   _ \n"
                + "| | | | / _\u0060 | / _\u0060 |  / _\u0060 | | | | |\n"
                + "| | | || (_| || (_| | | (_| | | |_| |\n"
                + "| |_| | \\__,_| \\__,_|  \\__,_|  \\__, |\n"
                + "|____/                          |___/ \n";
        printDivider();
        printIndentedBanner(banner);
        printLine(INDENT + "Hello, little one.");
        printLine(INDENT + "What can I assist you with today ;)?");
        printDivider();
    }

    /**
     * Displays every task in its numbered list order.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(TaskList tasks) {
        printDivider();
        printLine(INDENT + " Here are the tasks in your list:");
        int taskNumber = 1;
        for (Task task : tasks) {
            printLine(INDENT + " " + taskNumber + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
            taskNumber++;
        }
        printDivider();
    }

    /**
     * Displays tasks that occur on the supplied date.
     *
     * @param date the date used to filter tasks
     * @param tasks the tasks that occur on the date
     */
    public void showTasksOnDate(LocalDate date, List<Task> tasks) {
        printDivider();
        printLine(INDENT + " Tasks occurring on " + date + ":");
        int displayedTaskNumber = 1;
        for (Task task : tasks) {
            printLine(INDENT + " " + displayedTaskNumber + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
            displayedTaskNumber++;
        }
        if (displayedTaskNumber == 1) {
            printLine(INDENT + " No deadlines or events found for that date.");
        }
        printDivider();
    }

    /**
     * Displays the tasks whose descriptions match a user-provided keyword.
     *
     * @param tasks the tasks that match the keyword
     */
    public void showMatchingTasks(List<Task> tasks) {
        printDivider();
        printLine(INDENT + " Here are the matching tasks in your list:");
        int displayedTaskNumber = 1;
        for (Task task : tasks) {
            printLine(INDENT + " " + displayedTaskNumber + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
            displayedTaskNumber++;
        }
        if (displayedTaskNumber == 1) {
            printLine(INDENT + " No matching tasks found.");
        }
        printDivider();
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after adding
     */
    public void showTaskAdded(Task task, int taskCount) {
        printDivider();
        printLine(INDENT + " Got it. I've added this task:");
        printLine(INDENT + "   " + task.getTypeIcon() + "["
                + task.getStatusIcon() + "] " + task.getDisplayDescription());
        printLine(INDENT + " Now you have " + taskCount + " tasks in the list.");
        printDivider();
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        printDivider();
        printLine(INDENT + " Nice! I've marked this task as done:");
        printLine(INDENT + "   [X] " + task.getDisplayDescription());
        printDivider();
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        printDivider();
        printLine(INDENT + " OK, I've marked this task as not done yet:");
        printLine(INDENT + "   [ ] " + task.getDisplayDescription());
        printDivider();
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task the task that was deleted
     * @param taskCount the number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        printDivider();
        printLine(INDENT + " Noted. I've removed this task:");
        printLine(INDENT + "   " + task.getTypeIcon() + "[" + task.getStatusIcon()
                + "] " + task.getDisplayDescription());
        printLine(INDENT + " Now you have " + taskCount + " tasks in the list.");
        printDivider();
    }

    /**
     * Displays a user-friendly error message.
     *
     * @param message the error explanation and correction
     */
    public void showError(String message) {
        printDivider();
        printLine(INDENT + " " + message);
        printDivider();
    }

    /** Displays Daddy's farewell message. */
    public void showExit() {
        printLine(INDENT + "Bye. See you soon :)");
        printDivider();
    }

    /** Prints a divider line with the standard indentation. */
    private void printDivider() {
        printLine(INDENT + DIVIDER);
    }

    /**
     * Prints every line in a banner with the standard indentation.
     *
     * @param banner the multi-line banner to print
     */
    private void printIndentedBanner(String banner) {
        for (String line : banner.split("\\n")) {
            printLine(INDENT + line);
        }
    }

    /**
     * Sends one complete output line through this interface's configured receiver.
     *
     * @param line the line to display
     */
    private void printLine(String line) {
        output.accept(line);
    }
}
