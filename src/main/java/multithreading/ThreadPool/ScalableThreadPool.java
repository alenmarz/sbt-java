package multithreading.ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private final Queue<Runnable> tasksQueue = new ArrayDeque<>();
    private int currentWorkedThread = 0;
    private int currentPerformedThread = 0;
    private final Object queueLock = new Object();
    private final int min;
    private final int max;

    public ScalableThreadPool(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void start() {
        for (int i = 0; i < min; i++) {
            currentPerformedThread++;
            new ScalableThreadPool.ScalableThread().start();
        }
    }

    public void execute(Runnable runnable) {
        synchronized (queueLock) {
            tasksQueue.add(runnable);
            if (currentWorkedThread + tasksQueue.size() >= currentPerformedThread  && currentPerformedThread < max) {
                currentPerformedThread++;
                System.out.println("Performed: " + currentPerformedThread);
                new ScalableThreadPool.ScalableThread().start();
            } else queueLock.notify();
        }
    }

    public class ScalableThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (queueLock) {
                    if (tasksQueue.isEmpty() && currentPerformedThread > min) {
                        currentPerformedThread--;
                        System.out.println("Exit: " + Thread.currentThread());
                        System.out.println("Performed: " + currentPerformedThread);
                        break;
                    }
                }
                Runnable task;
                synchronized (queueLock) {
                    while (tasksQueue.isEmpty()) {
                        try {
                            queueLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    currentWorkedThread++;
                    task = tasksQueue.poll();
                }
                try {
                    task.run();
                    synchronized (queueLock) {
                        currentWorkedThread--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
