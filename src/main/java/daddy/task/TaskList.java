package daddy.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
     * Adds one or more tasks to the end of the list in the supplied order.
     *
     * @param tasksToAdd the tasks to add
     * @throws AssertionError if the task array or any task is null
     */
    public void add(Task... tasksToAdd) {
        assert tasksToAdd != null : "tasks to add must not be null";
        for (Task task : tasksToAdd) {
            assert task != null : "task to add must not be null";
        }
        Collections.addAll(tasks, tasksToAdd);
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
     * @throws AssertionError if the task index is out of range
     */
    public Task markTaskAsDone(int index) {
        assert hasTaskAt(index) : "task index must be in range";
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at a zero-based index as not done.
     *
     * @param index the zero-based task index
     * @return the task that was unmarked
     * @throws AssertionError if the task index is out of range
     */
    public Task markTaskAsNotDone(int index) {
        assert hasTaskAt(index) : "task index must be in range";
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the removed task
     * @throws AssertionError if the task index is out of range
     */
    public Task removeTaskAt(int index) {
        assert hasTaskAt(index) : "task index must be in range";
        return tasks.remove(index);
    }

    /**
     * Returns the tasks that occur on a supplied date.
     *
     * @param date the date to match
     * @return an unmodifiable list of matching tasks in their list order
     * @throws AssertionError if the date is null
     */
    public List<Task> getTasksOccurringOn(LocalDate date) {
        assert date != null : "date must not be null";
        return tasks.stream()
                .filter(task -> task.occursOn(date))
                .toList();
    }

    /**
     * Returns tasks whose descriptions contain a keyword, ignoring letter case.
     *
     * @param keyword the text to find in task descriptions
     * @return an unmodifiable list of matching tasks in their list order
     * @throws AssertionError if the keyword is null
     */
    public List<Task> getTasksMatching(String keyword) {
        assert keyword != null : "keyword must not be null";
        String lowerCaseKeyword = keyword.toLowerCase(Locale.ROOT);
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase(Locale.ROOT).contains(lowerCaseKeyword))
                .toList();
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
