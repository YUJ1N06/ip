/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private final String deadline;

    /** Creates a deadline task. */
    public Deadline(String description, String deadline) {
        super(description, TaskType.DEADLINE);
        this.deadline = deadline;
    }

    /** Returns the description together with the deadline. */
    @Override
    public String getDisplayDescription() {
        return getDescription() + " (by: " + deadline + ")";
    }

    /** Returns the deadline value for persistence. */
    public String getDeadline() {
        return deadline;
    }
}
