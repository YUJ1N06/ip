/**
 * Ends the Daddy chat session.
 */
public class ExitCommand extends Command {
    /** Displays the farewell message. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showExit();
    }

    /** This command ends the application. */
    @Override
    public boolean isExit() {
        return true;
    }
}
