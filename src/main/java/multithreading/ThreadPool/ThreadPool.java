package multithreading.ThreadPool;

public interface ThreadPool {
    void start();
    void execute(Runnable runnable);
}
