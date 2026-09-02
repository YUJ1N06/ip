package daddy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task collection operations that change or query Daddy's task list.
 */
class TaskListTest {
    /**
     * Verifies that tasks are retained in their insertion order.
     */
    @Test
    void addTasks_multipleTasks_preservesOrderAndSize() {
        TaskList tasks = new TaskList();
        Task firstTask = new Todo("read book");
        Task secondTask = new Todo("return book");

        tasks.add(firstTask);
        tasks.add(secondTask);

        assertEquals(2, tasks.size());
        assertEquals(List.of(firstTask, secondTask), asList(tasks));
    }

    /**
     * Verifies that only valid zero-based task indexes are accepted.
     */
    @Test
    void hasTaskAt_negativeValidAndPastEndIndexes_returnsExpectedResult() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertFalse(tasks.hasTaskAt(-1));
        assertTrue(tasks.hasTaskAt(0));
        assertFalse(tasks.hasTaskAt(1));
    }

    /**
     * Verifies that marking and unmarking change the selected task's status.
     */
    @Test
    void markAndUnmarkTask_existingTask_updatesAndReturnsSameTask() {
        TaskList tasks = new TaskList();
        Task task = new Todo("read book");
        tasks.add(task);

        assertSame(task, tasks.markTaskAsDone(0));
        assertEquals("X", task.getStatusIcon());
        assertSame(task, tasks.markTaskAsNotDone(0));
        assertEquals(" ", task.getStatusIcon());
    }

    /**
     * Verifies that deletion returns the removed task and closes the remaining index gap.
     */
    @Test
    void removeTaskAt_middleTask_returnsTaskAndRenumbersRemainingTasks() {
        TaskList tasks = new TaskList();
        Task firstTask = new Todo("read book");
        Task removedTask = new Todo("return book");
        Task lastTask = new Todo("buy bread");
        tasks.add(firstTask);
        tasks.add(removedTask);
        tasks.add(lastTask);

        assertSame(removedTask, tasks.removeTaskAt(1));
        assertEquals(2, tasks.size());
        assertEquals(List.of(firstTask, lastTask), asList(tasks));
    }

    /**
     * Verifies that date filtering includes matching deadlines and either boundary of matching events.
     */
    @Test
    void getTasksOccurringOn_matchingDate_returnsDatedTasksAndImmutableResult() {
        TaskList tasks = new TaskList();
        LocalDate selectedDate = LocalDate.of(2026, 12, 2);
        Task todo = new Todo("read book");
        Task deadline = new Deadline("return book", selectedDate.atTime(18, 0));
        Task eventEndingOnDate = new Event("trip",
                LocalDateTime.of(2026, 12, 1, 9, 0), selectedDate.atTime(17, 0));
        tasks.add(todo);
        tasks.add(deadline);
        tasks.add(eventEndingOnDate);

        List<Task> matchingTasks = tasks.getTasksOccurringOn(selectedDate);

        assertEquals(List.of(deadline, eventEndingOnDate), matchingTasks);
        assertThrows(UnsupportedOperationException.class, () -> matchingTasks.add(todo));
    }

    /**
     * Verifies that description matching ignores case, preserves task order, and returns an immutable result.
     */
    @Test
    void getTasksMatching_matchingDescriptions_returnsTasksInOrderAndImmutableResult() {
        TaskList tasks = new TaskList();
        Task matchingTodo = new Todo("read Book");
        Task nonMatchingTask = new Todo("buy bread");
        Task matchingDeadline = new Deadline("return book", LocalDateTime.of(2026, 12, 2, 18, 0));
        tasks.add(matchingTodo);
        tasks.add(nonMatchingTask);
        tasks.add(matchingDeadline);

        List<Task> matchingTasks = tasks.getTasksMatching("BOOK");

        assertEquals(List.of(matchingTodo, matchingDeadline), matchingTasks);
        assertThrows(UnsupportedOperationException.class, () -> matchingTasks.add(nonMatchingTask));
    }

    /**
     * Converts a task list into a list to allow order assertions.
     *
     * @param tasks the task list to read
     * @return tasks in their iteration order
     */
    private List<Task> asList(TaskList tasks) {
        List<Task> taskValues = new ArrayList<>();
        for (Task task : tasks) {
            taskValues.add(task);
        }
        return taskValues;
    }
}
