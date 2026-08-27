# UI Test Plan

The test runner compares stdout exactly, after normalizing CRLF line endings.
Compile the application first with Java 25:

```bash
javac -d /tmp/daddy-classes src/main/java/*.java
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
