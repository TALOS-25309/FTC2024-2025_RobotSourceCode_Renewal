package org.firstinspires.ftc.teamcode.features;

import androidx.annotation.NonNull;

import java.util.PriorityQueue;

/**
 * Subclass responsible for managing individual tasks.
 * Understanding its implementation is not required.
 */
class Task implements Comparable<Task> {
    private final Runnable task;
    private final long delay;
    private final boolean async;

    public Task(Runnable task, long delay, boolean async) {
        this.task = task;
        this.delay = delay;
        this.async = async;
    }

    public void run() {
        if (async) {
            new Thread(task).start();
        } else {
            task.run();
        }
    }

    public boolean isReady(long currentTime) {
        return currentTime >= delay;
    }

    @Override
    public int compareTo(Task task) {
        if (this.delay > task.delay)
            return 1;
        else if (this.delay < task.delay)
            return -1;
        return 0;
    }
}

/**
 * Scheduler class for executing tasks at a specific time or asynchronously.
 * <p>
 * You need to call the `update` method in your OpMode's loop to make it work.
 * Tasks can be added to the scheduler using the {@link Schedule#addTask(Runnable, double)} or {@link Schedule#addTaskAsync(Runnable, double)} methods.
 * The `addTask` method executes the task immediately after the specified delay (in seconds).
 * The `addTaskAsync` method executes the task in a separate thread after the specified delay (in seconds).
 */
public class Schedule {
    private Schedule() {}
    private static final PriorityQueue<Task> tasks = new PriorityQueue<>();
    public static final double RUN_INSTANTLY = 0.0;

    public static void init() {
        tasks.clear();
    }

    public static void addTask(@NonNull Runnable task, double delay) {
        if (delay <= RUN_INSTANTLY) {
            task.run();
        } else {
            tasks.add(new Task(task, System.nanoTime() + (long) (delay * 1e9), false));
        }
    }

    public static void addTaskAsync(@NonNull Runnable task, double delay) {
        if (delay <= RUN_INSTANTLY) {
            new Thread(task).start();
        } else {
            tasks.add(new Task(task, System.nanoTime() + (long)(delay * 1e9), true));
        }
    }

    public static void update() {
        long currentTime = System.nanoTime();
        while (!tasks.isEmpty()) {
            if (!tasks.peek().isReady(currentTime)) break;
            tasks.poll().run();
        }
    }

    public static void stop() {
        tasks.clear();
    }
}