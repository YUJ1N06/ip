package daddy.command;

import daddy.exception.DaddyException;
import daddy.storage.Storage;
import daddy.task.Task;
import daddy.task.TaskList;
import daddy.ui.Ui;

/**
 * Marks one task in the list as not done.
 */
public class UnmarkCommand extends TaskIndexCommand {
    /**
     * Creates a command for the supplied zero-based task index.
     *
     * @param taskIndex the task index to unmark
     */
    public UnmarkCommand(int taskIndex) {
        super(taskIndex);
    }

    /** Unmarks, saves, and displays the selected task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        ensureTaskExists(tasks);
        Task task = tasks.markTaskAsNotDone(getTaskIndex());
        storage.save(tasks);
        ui.showTaskUnmarked(task);
    }
}
