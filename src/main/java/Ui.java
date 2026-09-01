import java.time.LocalDate;
import java.util.List;

/**
 * Handles all console output shown to a Daddy user.
 */
public class Ui {
    private static final String INDENT = "    ";
    private static final String DIVIDER = "____________________________________________________________";

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
        System.out.println(INDENT + "Hello, little one.");
        System.out.println(INDENT + "What can I assist you with today ;)?");
        printDivider();
    }

    /**
     * Displays every task in its numbered list order.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(TaskList tasks) {
        printDivider();
        System.out.println(INDENT + " Here are the tasks in your list:");
        int taskNumber = 1;
        for (Task task : tasks) {
            System.out.println(INDENT + " " + taskNumber + "." + task.getTypeIcon()
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
        System.out.println(INDENT + " Tasks occurring on " + date + ":");
        int displayedTaskNumber = 1;
        for (Task task : tasks) {
            System.out.println(INDENT + " " + displayedTaskNumber + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDisplayDescription());
            displayedTaskNumber++;
        }
        if (displayedTaskNumber == 1) {
            System.out.println(INDENT + " No deadlines or events found for that date.");
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
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + task.getTypeIcon() + "["
                + task.getStatusIcon() + "] " + task.getDisplayDescription());
        System.out.println(INDENT + " Now you have " + taskCount + " tasks in the list.");
        printDivider();
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        printDivider();
        System.out.println(INDENT + " Nice! I've marked this task as done:");
        System.out.println(INDENT + "   [X] " + task.getDisplayDescription());
        printDivider();
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        printDivider();
        System.out.println(INDENT + " OK, I've marked this task as not done yet:");
        System.out.println(INDENT + "   [ ] " + task.getDisplayDescription());
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
        System.out.println(INDENT + " Noted. I've removed this task:");
        System.out.println(INDENT + "   " + task.getTypeIcon() + "[" + task.getStatusIcon()
                + "] " + task.getDisplayDescription());
        System.out.println(INDENT + " Now you have " + taskCount + " tasks in the list.");
        printDivider();
    }

    /**
     * Displays a user-friendly error message.
     *
     * @param message the error explanation and correction
     */
    public void showError(String message) {
        printDivider();
        System.out.println(INDENT + " " + message);
        printDivider();
    }

    /** Displays Daddy's farewell message. */
    public void showExit() {
        System.out.println(INDENT + "Bye. See you soon :)");
        printDivider();
    }

    /** Prints a divider line with the standard indentation. */
    private void printDivider() {
        System.out.println(INDENT + DIVIDER);
    }

    /** Prints every banner line with the standard indentation. */
    private void printIndentedBanner(String banner) {
        for (String line : banner.split("\\n")) {
            System.out.println(INDENT + line);
        }
    }
}
