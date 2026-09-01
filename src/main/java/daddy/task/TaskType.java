package daddy.task;

/** Identifies the kind of task and its display marker. */
public enum TaskType {
    TODO("[T]"),
    DEADLINE("[D]"),
    EVENT("[E]"),
    GENERAL("");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    /** Returns the marker used when displaying this task type. */
    public String getIcon() {
        return icon;
    }
}
