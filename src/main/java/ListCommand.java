/**
 * Displays every task in the current task list.
 */
public class ListCommand extends Command {
    /** Displays the task list. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
