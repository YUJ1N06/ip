public class Daddy {
    public static void main(String[] args) {
        printGreeting();
        printExit();
    }

    private static void printGreeting() {
        String banner = " ____                      _       _       \n"
                + "|  _ \\   __ _   __| |   __| |  _   _ \n"
                + "| | | | / _` | / _` |  / _` | | | | |\n"
                + "| | | || (_| || (_| | | (_| | | |_| |\n"
                + "| |_| | \\__,_| \\__,_|  \\__,_|  \\__, |\n"
                + "|____/                          |___/ \n";
        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Hello, little one.");
        System.out.println("What can I assist you with today ;)?");
        System.out.println("____________________________________________________________");
    }

    private static void printExit() {
        System.out.println("Bye. See you soon :)");
        System.out.println("____________________________________________________________");
    }
}