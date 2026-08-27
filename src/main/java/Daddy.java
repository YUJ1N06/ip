import java.util.Scanner;

public class Daddy {
    private static final String INDENT = "    ";
    private static final Task[] tasks = new Task[100];
    private static int taskCount = 0;

    public static void main(String[] args) {
        printGreeting();
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
                } else if (input.toLowerCase().startsWith("deadline ")) {
                    addDeadline(input.substring(9).trim());
                } else if (input.toLowerCase().startsWith("event ")) {
                    addEvent(input.substring(6).trim());
                } else if (input.toLowerCase().startsWith("unmark ")) {
                    unmarkTask(input.substring(7).trim());
                } else if (input.toLowerCase().startsWith("mark ")) {
                    markTask(input.substring(5).trim());
                } else {
                    throw new DaddyException("Daddy has no clue what '" + input
                            + "' means. Try todo, deadline, event, list, mark, unmark, or bye.");
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
        for (int i = 0; i < taskCount; i++) {
            System.out.println(INDENT + " " + (i + 1) + "." + tasks[i].getTypeIcon()
                    + "[" + tasks[i].getStatusIcon() + "] "
                    + tasks[i].getDisplayDescription());
        }
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void addTodo(String description) throws DaddyException {
        if (description.isEmpty()) {
            throw new DaddyException("Hmm, what are you thinking of doing today? You need a description "
                    + "for that. Try: todo borrow book.");
        }
        tasks[taskCount] = new Todo(description);
        taskCount++;

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks[taskCount - 1].getTypeIcon() + "["
                + tasks[taskCount - 1].getStatusIcon() + "] " + description);
        System.out.println(INDENT + " Now you have " + taskCount + " tasks in the list.");
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
        tasks[taskCount] = new Deadline(description, deadline);
        taskCount++;

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks[taskCount - 1].getTypeIcon() + "["
                + tasks[taskCount - 1].getStatusIcon() + "] "
                + tasks[taskCount - 1].getDisplayDescription());
        System.out.println(INDENT + " Now you have " + taskCount + " tasks in the list.");
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
        tasks[taskCount] = new Event(description, from, to);
        taskCount++;

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Got it. I've added this task:");
        System.out.println(INDENT + "   " + tasks[taskCount - 1].getTypeIcon() + "["
                + tasks[taskCount - 1].getStatusIcon() + "] "
                + tasks[taskCount - 1].getDisplayDescription());
        System.out.println(INDENT + " Now you have " + taskCount + " tasks in the list.");
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void markTask(String taskNumber) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + taskCount + ".");
            }

            tasks[taskIndex].markAsDone();
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " Nice! I've marked this task as done:");
            System.out.println(INDENT + "   [X] " + tasks[taskIndex].getDisplayDescription());
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try mark 1, for example.");
        }
    }

    private static void unmarkTask(String taskNumber) throws DaddyException {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new DaddyException("That task number is out of range. Pick a number from 1 to " + taskCount + ".");
            }

            tasks[taskIndex].markAsNotDone();
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " OK, I've marked this task as not done yet:");
            System.out.println(INDENT + "   [ ] " + tasks[taskIndex].getDisplayDescription());
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            throw new DaddyException("'" + taskNumber + "' is not a task number. Try unmark 1, for example.");
        }
    }

    private static void printError(String message) {
        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " " + message);
        System.out.println(INDENT + "____________________________________________________________");
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
