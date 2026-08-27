/**
 * A task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates a todo task that is initially not done.
     *
     * @param description the todo description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the marker used to identify this task type.
     *
     * @return the todo marker
     */
    @Override
    public String getTypeIcon() {
        return "[T]";
    }
}
