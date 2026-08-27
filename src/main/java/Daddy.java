import java.util.Scanner;

public class Daddy {
    private static final String INDENT = "    ";
    private static final String[] tasks = new String[100];
    private static final boolean[] completedTasks = new boolean[100];
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

            if (input.equalsIgnoreCase("bye")) {
                printExit();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                printList();
            } else if (input.toLowerCase().startsWith("unmark ")) {
                unmarkTask(input.substring(7).trim());
            } else if (input.toLowerCase().startsWith("mark ")) {
                markTask(input.substring(5).trim());
            } else {
                addTask(input);
            }
        }

        scanner.close();
    }

    private static void addTask(String task) {
        tasks[taskCount] = task;
        taskCount++;

        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " added: " + task);
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void printList() {
        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            String status = completedTasks[i] ? "[X]" : "[ ]";
            System.out.println(INDENT + " " + (i + 1) + "." + status + " " + tasks[i]);
        }
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void markTask(String taskNumber) {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                printInvalidTaskNumber();
                return;
            }

            completedTasks[taskIndex] = true;
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " Nice! I've marked this task as done:");
            System.out.println(INDENT + "   [X] " + tasks[taskIndex]);
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            printInvalidTaskNumber();
        }
    }

    private static void printInvalidTaskNumber() {
        System.out.println(INDENT + "____________________________________________________________");
        System.out.println(INDENT + " Please provide a valid task number.");
        System.out.println(INDENT + "____________________________________________________________");
    }

    private static void unmarkTask(String taskNumber) {
        try {
            int taskIndex = Integer.parseInt(taskNumber) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                printInvalidTaskNumber();
                return;
            }

            completedTasks[taskIndex] = false;
            System.out.println(INDENT + "____________________________________________________________");
            System.out.println(INDENT + " OK, I've marked this task as not done yet:");
            System.out.println(INDENT + "   [ ] " + tasks[taskIndex]);
            System.out.println(INDENT + "____________________________________________________________");
        } catch (NumberFormatException exception) {
            printInvalidTaskNumber();
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
