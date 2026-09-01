import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Represents a task with a specified start and end date or time. */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /** Creates an event task. */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /** Returns the description together with the event's time range. */
    @Override
    public String getDisplayDescription() {
        return getDescription() + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }

    /** Returns the event start value for persistence. */
    public LocalDateTime getFrom() {
        return from;
    }

    /** Returns the event end value for persistence. */
    public LocalDateTime getTo() {
        return to;
    }

    /** Returns whether this event starts or ends on the supplied date. */
    @Override
    public boolean occursOn(LocalDate date) {
        return from.toLocalDate().equals(date) || to.toLocalDate().equals(date);
    }
}
