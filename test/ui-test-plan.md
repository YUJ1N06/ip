# UI Test Plan

The test runner compares stdout exactly, after normalizing CRLF line endings.
Compile the application first with Java 25:

```bash
javac -d /tmp/daddy-classes src/main/java/*.java
```

## Manual test: date and time parsing
Aim: Verify that deadlines and events parse ISO dates/times and display friendly values.

### Inputs

```text
deadline return book /by 02-12-2019 1800
event project meeting /from 02-12-2019 1400 /to 02-12-2019 1600
list
bye
```

Expected display includes `Dec 2 2019 18:00` for the deadline and
`Dec 2 2019 14:00` to `Dec 2 2019 16:00` for the event.

## Manual test: filter dated tasks
Aim: Verify that `list on dd-MM-yyyy` shows only deadlines and events occurring on that date.

Create a Todo, a Deadline, and an Event dated `02-12-2019`, then enter:

```text
list on 02-12-2019
```

The Todo should be excluded, while the matching Deadline and Event should be listed.

## Manual test: corrupted data recovery
Aim: Verify that malformed records are reported and skipped while valid records still load.

Before starting the app, place these lines in `data/duke.txt`:

```text
T|1|keep me
BROKEN
D|0|return book|2019-12-02T00:00
E|x|bad
```

Start the app and enter `list`. The console should report corrupted lines 2 and 4,
while listing `keep me` as done and `return book` as not done. The app should
remain usable and accept `bye` normally. It should also create
`data/duke.txt.backup` containing the original file and
`data/duke.txt.corrupt` containing the rejected records with line numbers.

## Manual test: saved tasks load after restart
Aim: Verify that a task saved by one app session is restored by the next session.

In one session, enter:

```text
deadline return book /by 02-12-2019 1800
bye
```

Start the app again and enter `list`. The list should contain
`[D][ ] return book (by: Dec 2 2019 18:00)`.

## Test case: delete removes and renumbers tasks
Aim: Verify that deleting a valid task removes it and that the remaining tasks are renumbered.

### Command
java -cp /tmp/daddy-classes Daddy

### Input
todo keep me
todo remove me
delete 2
list
bye

### Expected output
```text
    ____________________________________________________________
     ____                      _       _       
    |  _ \   __ _   __| |   __| |  _   _ 
    | | | | / _` | / _` |  / _` | | | | |
    | | | || (_| || (_| | | (_| | | |_| |
    | |_| | \__,_| \__,_|  \__,_|  \__, |
    |____/                          |___/ 
    Hello, little one.
    What can I assist you with today ;)?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] keep me
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] remove me
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [T][ ] remove me
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] keep me
    ____________________________________________________________
    Bye. See you soon :)
    ____________________________________________________________
```

## Test case: interleaved invalid commands preserve state
Aim: Verify that malformed commands report specific corrections and do not add invalid tasks or alter the valid task list.

### Command
java -cp /tmp/daddy-classes Daddy

### Input
todo buy milk
todo
mark
unmark
deadline
event
list
mark abc
deadline report
event meeting /from Mon
list
bye

### Expected output
```text
    ____________________________________________________________
     ____                      _       _       
    |  _ \   __ _   __| |   __| |  _   _ 
    | | | | / _` | / _` |  / _` | | | | |
    | | | || (_| || (_| | | (_| | | |_| |
    | |_| | \__,_| \__,_|  \__,_|  \__, |
    |____/                          |___/ 
    Hello, little one.
    What can I assist you with today ;)?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] buy milk
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Hmm, what are you thinking of doing today? You need a description for that. Try: todo borrow book.
    ____________________________________________________________
    ____________________________________________________________
     MARK?! Mark what... Try: mark 1 (after adding a task first).
    ____________________________________________________________
    ____________________________________________________________
     UNMARK?! Unmark what... Try: unmark 1 (after adding a task first).
    ____________________________________________________________
    ____________________________________________________________
     What deadlines are coming up? Better hurry, add them and get them done ASAP. Try: deadline return book /by Sunday.
    ____________________________________________________________
    ____________________________________________________________
     What exciting event are you planning? Give it a name so Daddy can keep track. Try: event meeting /from Mon 2pm /to 4pm.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] buy milk
    ____________________________________________________________
    ____________________________________________________________
     'abc' is not a task number. Try mark 1, for example.
    ____________________________________________________________
    ____________________________________________________________
     A deadline needs a /by date or time. Try: deadline return book /by Sunday
    ____________________________________________________________
    ____________________________________________________________
     An event needs both /from and /to times. Try: event meeting /from Mon 2pm /to 4pm
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] buy milk
    ____________________________________________________________
    Bye. See you soon :)
    ____________________________________________________________
```

## Test case: mark and unmark a todo
Aim: Verify that a todo can be created, marked done, reversed, and displayed with the correct status.

### Command
java -cp /tmp/daddy-classes Daddy

### Input
todo read book
mark 1
unmark 1
list
bye

### Expected output
```text
    ____________________________________________________________
     ____                      _       _       
    |  _ \   __ _   __| |   __| |  _   _ 
    | | | | / _` | / _` |  / _` | | | | |
    | | | || (_| || (_| | | (_| | | |_| |
    | |_| | \__,_| \__,_|  \__,_|  \__, |
    |____/                          |___/ 
    Hello, little one.
    What can I assist you with today ;)?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [X] read book
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    Bye. See you soon :)
    ____________________________________________________________
```
