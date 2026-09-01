/**
 * Removes one task from the list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for the supplied zero-based task index.
     *
     * @param taskIndex the task index to remove
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /** Removes, saves, and displays the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        ensureTaskExists(tasks);
        Task removedTask = tasks.removeTaskAt(taskIndex);
        storage.save(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /** Ensures the selected task index exists in the current list. */
    private void ensureTaskExists(TaskList tasks) throws DaddyException {
        if (!tasks.hasTaskAt(taskIndex)) {
            throw new DaddyException(
                    "That task number is out of range. Pick a number from 1 to " + tasks.size() + ".");
        }
    }
}
