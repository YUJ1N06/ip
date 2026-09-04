package daddy.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import daddy.exception.DaddyException;
import daddy.task.Deadline;
import daddy.task.Event;
import daddy.task.Task;
import daddy.task.TaskList;
import daddy.task.Todo;

/**
 * Loads tasks from and saves tasks to Daddy's data files.
 */
public class Storage {
    /** Identifies the file that stores Daddy's current saved tasks. */
    private final Path dataFile;
    /** Identifies the old Duke-named data file that can be migrated once. */
    private final Path legacyDataFile;
    /** Identifies the copy of the original data file made during corruption recovery. */
    private final Path backupFile;
    /** Identifies the file that records malformed data-file lines. */
    private final Path corruptedFile;

    /**
     * Creates storage that uses a new data file and migrates an older one when necessary.
     *
     * @param dataFile the file used to persist tasks
     * @param legacyDataFile the previous task data file name
     */
    public Storage(Path dataFile, Path legacyDataFile) {
        this.dataFile = dataFile;
        this.legacyDataFile = legacyDataFile;
        this.backupFile = dataFile.resolveSibling(dataFile.getFileName() + ".backup");
        this.corruptedFile = dataFile.resolveSibling(dataFile.getFileName() + ".corrupt");
    }

    /**
     * Loads valid task records into the supplied list and returns messages for records that could not be read.
     *
     * @param tasks the task list to populate
     * @return user-friendly messages describing any loading problems
     */
    public List<String> loadInto(TaskList tasks) {
        List<String> messages = new ArrayList<>();
        migrateLegacyDataFileIfNeeded(messages);
        if (!Files.exists(dataFile)) {
            return messages;
        }

        try {
            List<String> lines = Files.readAllLines(dataFile);
            List<String> corruptedLines = new ArrayList<>();
            for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
                String line = lines.get(lineNumber);
                if (line.isBlank()) {
                    continue;
                }
                try {
                    String[] fields = line.split("\\|", -1);
                    Task task = createTask(fields);
                    if ("1".equals(fields[1])) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException exception) {
                    corruptedLines.add("line " + (lineNumber + 1) + ": " + line);
                    messages.add("Daddy skipped corrupted data on line " + (lineNumber + 1)
                            + ". Expected T|0|description, D|0|description|date, or "
                            + "E|0|description|from|to.");
                }
            }
            if (!corruptedLines.isEmpty()) {
                Files.copy(dataFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
                Files.write(corruptedFile, corruptedLines);
                messages.add("Daddy saved the original file as " + backupFile + " and "
                        + "the bad records as " + corruptedFile + " so you can fix them.");
            }
        } catch (IOException | RuntimeException exception) {
            messages.add("Daddy couldn't load the task list. Check " + dataFile + " and try again.");
        }
        return messages;
    }

    /**
     * Moves the legacy data file to the current file name when no current data file exists.
     *
     * @param messages messages to show the user about migration problems or completion
     */
    private void migrateLegacyDataFileIfNeeded(List<String> messages) {
        if (Files.exists(dataFile) || !Files.exists(legacyDataFile)) {
            return;
        }
        try {
            Files.move(legacyDataFile, dataFile);
            messages.add("Daddy moved your saved tasks from " + legacyDataFile + " to " + dataFile + ".");
        } catch (IOException exception) {
            messages.add("Daddy couldn't migrate saved tasks from " + legacyDataFile + " to " + dataFile
                    + ". Your old tasks are still in " + legacyDataFile + ".");
        }
    }

    /**
     * Saves the supplied tasks in Daddy's persistent file format.
     *
     * @param tasks the task list to save
     * @throws DaddyException if the data file cannot be written
     */
    public void save(TaskList tasks) throws DaddyException {
        try {
            Path parent = dataFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(serialize(task));
            }
            Files.write(dataFile, lines);
        } catch (IOException exception) {
            throw new DaddyException("Daddy couldn't save your tasks. Check that the data folder is writable.");
        }
    }

    /**
     * Creates a task from one validated data-file record.
     *
     * @param fields the pipe-separated fields in one saved task record
     * @return the task represented by the record
     * @throws IllegalArgumentException if the record has an invalid task type or field count
     */
    private Task createTask(String[] fields) {
        if (fields.length < 3 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("invalid task record");
        }
        return switch (fields[0]) {
            case "T" -> {
                if (fields.length != 3) {
                    throw new IllegalArgumentException("invalid todo record");
                }
                yield new Todo(fields[2]);
            }
            case "D" -> {
                if (fields.length != 4) {
                    throw new IllegalArgumentException("invalid deadline record");
                }
                yield new Deadline(fields[2], LocalDateTime.parse(fields[3]));
            }
            case "E" -> {
                if (fields.length != 5) {
                    throw new IllegalArgumentException("invalid event record");
                }
                yield new Event(fields[2], LocalDateTime.parse(fields[3]), LocalDateTime.parse(fields[4]));
            }
            default -> throw new IllegalArgumentException("unknown task type");
        };
    }

    /**
     * Converts a task into one line in Daddy's persistent file format.
     *
     * @param task the task to save
     * @return the pipe-separated record representing the task
     */
    private String serialize(Task task) {
        String description = task.getDescription().replace("|", " ");
        String status = task.getStatusIcon().equals("X") ? "1" : "0";
        return switch (task.getType()) {
            case TODO, GENERAL -> "T|" + status + "|" + description;
            case DEADLINE -> "D|" + status + "|" + description + "|" + ((Deadline) task).getDeadline();
            case EVENT -> "E|" + status + "|" + description + "|" + ((Event) task).getFrom()
                    + "|" + ((Event) task).getTo();
        };
    }
}
