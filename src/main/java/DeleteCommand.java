/**
 * Removes one task from the list.
 */
public class DeleteCommand extends TaskIndexCommand {
    /**
     * Creates a command for the supplied zero-based task index.
     *
     * @param taskIndex the task index to remove
     */
    public DeleteCommand(int taskIndex) {
        super(taskIndex);
    }

    /** Removes, saves, and displays the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        ensureTaskExists(tasks);
        Task removedTask = tasks.removeTaskAt(getTaskIndex());
        storage.save(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}
