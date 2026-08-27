import java.util.Scanner;

public class Daddy {
    private static final String INDENT = "    ";

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
            } else {
                System.out.println(INDENT + "____________________________________________________________");
                System.out.println(INDENT + input);
                System.out.println(INDENT + "____________________________________________________________");
            }
        }

        scanner.close();
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