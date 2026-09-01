/**
 * Recognizes Daddy commands and extracts their argument text.
 */
public class Parser {
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
}
