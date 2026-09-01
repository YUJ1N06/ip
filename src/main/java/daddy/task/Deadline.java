package daddy.task;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    private final LocalDateTime deadline;

    /** Creates a deadline task. */
    public Deadline(String description, LocalDateTime deadline) {
        super(description, TaskType.DEADLINE);
        this.deadline = deadline;
    }

    /** Returns the description together with the deadline. */
    @Override
    public String getDisplayDescription() {
        return getDescription() + " (by: " + deadline.format(DISPLAY_FORMAT) + ")";
    }

    /** Returns the deadline value for persistence. */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /** Returns whether this deadline falls on the supplied date. */
    @Override
    public boolean occursOn(LocalDate date) {
        return deadline.toLocalDate().equals(date);
    }
}
