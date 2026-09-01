/**
 * Represents one task in the task list.
 */
public class Task {
    private final String description;
    private final TaskType type;
    private boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description the task description
     */
    public Task(String description) {
        this(description, TaskType.GENERAL);
    }

    /** Creates a task with the specified type. */
    protected Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /** Returns the description used when displaying this task. */
    public String getDisplayDescription() {
        return description;
    }

    /**
     * Returns the icon used to display this task's completion state.
     *
     * @return {@code X} for a done task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the marker used to identify this task type.
     *
     * @return an empty marker for a general task
     */
    public String getTypeIcon() {
        return type.getIcon();
    }

    /** Returns this task's type for persistence. */
    public TaskType getType() {
        return type;
    }
}
