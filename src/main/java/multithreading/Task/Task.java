package multithreading.Task;

import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private final Object lockFirst = new Object();
    private volatile T result;
    private volatile boolean already_yet;
    private volatile RuntimeException exception = null;
    private volatile boolean first_thread = true;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        if (!first_thread) return get_result();
        if (doFirst()) return compute();
        return get_result();
    }

    private boolean doFirst() {
        synchronized (lockFirst) {
            if (first_thread) {
                first_thread = false;
                return true;
            }
            return false;
        }
    }

    private T compute() {
        try {
            result = callable.call();
        } catch (Exception e) {
            exception = new TaskException("Exception during compute result");
        }
        already_yet = true;
        synchronized (this) {
            notifyAll();
        }
        return get_result();
    }

    private T get_result() {
        if (!already_yet) {
            synchronized (this) {
                while (!already_yet) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Thread is interrupt");
                    }
                }
            }
        }
        if (exception != null) throw exception;
        return result;
    }
}
