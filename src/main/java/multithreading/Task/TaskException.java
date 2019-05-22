package multithreading.Task;

public class TaskException extends RuntimeException {
    public TaskException(String s) {
        super("Exception during running Callable");
    }
}
