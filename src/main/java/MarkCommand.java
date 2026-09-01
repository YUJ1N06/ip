/**
 * Marks one task in the list as done.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for the supplied zero-based task index.
     *
     * @param taskIndex the task index to mark
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** Marks, saves, and displays the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        ensureTaskExists(tasks);
        Task task = tasks.markTaskAsDone(taskIndex);
        storage.save(tasks);
        ui.showTaskMarked(task);
    }

    /** Ensures the selected task index exists in the current list. */
    private void ensureTaskExists(TaskList tasks) throws DaddyException {
        if (!tasks.hasTaskAt(taskIndex)) {
            throw new DaddyException(
                    "That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }
    }
}
