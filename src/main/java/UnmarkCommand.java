/**
 * Marks one task in the list as not done.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for the supplied zero-based task index.
     *
     * @param taskIndex the task index to unmark
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** Unmarks, saves, and displays the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        ensureTaskExists(tasks);
        Task task = tasks.markTaskAsNotDone(taskIndex);
        storage.save(tasks);
        ui.showTaskUnmarked(task);
    }

    /** Ensures the selected task index exists in the current list. */
    private void ensureTaskExists(TaskList tasks) throws DaddyException {
        if (!tasks.hasTaskAt(taskIndex)) {
            throw new DaddyException(
                    "That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }
    }
}
