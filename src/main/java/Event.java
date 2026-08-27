/** Represents a task with a specified start and end date or time. */
public class Event extends Task {
    private final String from;
    private final String to;

    /** Creates an event task. */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /** Returns the description together with the event's time range. */
    @Override
    public String getDisplayDescription() {
        return getDescription() + " (from: " + from + " to: " + to + ")";
    }
}
