/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private final String deadline;

    /** Creates a deadline task. */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /** Returns the marker for this task type. */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /** Returns the description together with the deadline. */
    @Override
    public String getDisplayDescription() {
        return getDescription() + " (by: " + deadline + ")";
    }
}
