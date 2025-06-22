package com.ndungutse.producer;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndungutse.model.Task;
import com.ndungutse.queue.TaskQueue;
import com.ndungutse.tracker.TaskStatusTracker;

public class TaskProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TaskProducer.class);

    private final TaskQueue taskQueue;
    private final String producerName;
    private final int numberOfTasks;
    private final Random random = new Random();

    public TaskProducer(TaskQueue taskQueue, String producerName, int numberOfTasks) {
        this.taskQueue = taskQueue;
        this.producerName = producerName;
        this.numberOfTasks = numberOfTasks;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= numberOfTasks; i++) {
                Thread.sleep(random.nextInt(500, 1000));
                int priority = (i % 3 == 0) ? 10 : random.nextInt(5) + 1;

                Task task = new Task(
                        producerName + "-Task-" + i,
                        priority,
                        "Payload for " + producerName + "-Task-" + i);

                taskQueue.submitTask(task);

                // Add task status in status tracker
                TaskStatusTracker.record(task);

                logger.info("[{}] Submitted Task: {} => {} at {}",
                        Thread.currentThread().getName(), task.getName(), task.getStatus(), LocalDateTime.now());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("[{}] Interrupted during task generation", producerName);
        }
    }
}
