package daddy.command;

import daddy.exception.DaddyException;
import daddy.storage.Storage;
import daddy.task.Task;
import daddy.task.TaskList;
import daddy.ui.Ui;

/**
 * Adds one task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the supplied task.
     *
     * @param task the task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /** Adds, saves, and displays the new task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DaddyException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}
