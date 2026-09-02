package daddy.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the tasks managed by Daddy and provides list-level operations.
 */
public class TaskList implements Iterable<Task> {
    /** Stores tasks in the same order in which they are displayed to the user. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns whether a zero-based index identifies a task in this list.
     *
     * @param index the zero-based task index
     * @return whether the index is in range
     */
    public boolean hasTaskAt(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Marks the task at a zero-based index as done.
     *
     * @param index the zero-based task index
     * @return the task that was marked
     */
    public Task markTaskAsDone(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index the zero-based task index
     * @return the task that was unmarked
     */
    public Task markTaskAsNotDone(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the removed task
     */
    public Task removeTaskAt(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the tasks that occur on a supplied date.
     *
     * @param date the date to match
     * @return an unmodifiable list of matching tasks in their list order
     */
    public List<Task> getTasksOccurringOn(LocalDate date) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matchingTasks.add(task);
            }
        }
        return List.copyOf(matchingTasks);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an iterator for reading the tasks in their list order.
     *
     * @return an iterator over the tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
