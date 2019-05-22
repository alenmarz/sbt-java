package multithreading.ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {
    private final Queue<Runnable> tasksQueue = new ArrayDeque<Runnable>();
    private final Object queueLock = new Object();
    private final int threadCount;

    public FixedThreadPool(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < threadCount; i++) {
            new FixedThread("Thread: " + i).start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (queueLock) {
            tasksQueue.add(runnable);
            queueLock.notify();
        }

    }

    private class FixedThread extends Thread {
        public FixedThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (queueLock) {
                    while (tasksQueue.isEmpty()) {
                        try {
                            queueLock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("thread was interrupted");
                        }
                    }
                    task = tasksQueue.poll();
                }
                try {
                    task.run();
                } catch (Exception e) {
                    System.out.println("Run error");
                }
            }
        }
    }
}
