import java.util.Scanner;

public class Daddy {
    private static final String INDENT = "    ";
    private static final String[] tasks = new String[100];
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
        for (int i = 0; i < taskCount; i++) {
            System.out.println(INDENT + " " + (i + 1) + ". " + tasks[i]);
        }
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