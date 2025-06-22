package com.ndungutse.consumer;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;
import com.ndungutse.queue.TaskQueue;
import com.ndungutse.tracker.TaskStats;
import com.ndungutse.tracker.TaskStatusTracker;

public class TaskConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private final TaskQueue taskQueue;

    public TaskConsumer(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Task task = null;
            try {
                task = taskQueue.takeTask();
                task.setStatus(TaskStatus.PROCESSING);

                // Add Task status to the task status tracker
                TaskStatusTracker.updateStatus(task.getId(), task.getStatus());

                logger.info(
                        "[{}] Processing Task: {} => {} at {}", Thread.currentThread().getName(), task.getName(),
                        task.getStatus(), LocalDateTime.now());

                // Simulate Processing
                Thread.sleep(new Random().nextInt(1000, 3000));

                task.setStatus(TaskStatus.COMPLETED);

                TaskStats.incrementProcessedCount();

                // Add Task status to the task status tracker
                TaskStatusTracker.updateStatus(task.getId(), task.getStatus());

                logger.info("[{}] Completed Task: {} => {} at {}",
                        Thread.currentThread().getName(), task.getName(), task.getStatus(), LocalDateTime.now());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("[{}] Interrupted, stopping consumer.", Thread.currentThread().getName());
                // break;
            } catch (Exception e) {
                logger.error("[{}] Error processing task: {}", Thread.currentThread().getName(), e.getMessage(), e);

                if (task != null) {
                    task.incrementRetry();
                    if (task.shouldRetry()) {
                        logger.info("[{}] Retrying Task: {} => Retry Count: {}",
                                Thread.currentThread().getName(), task.getName(), task.getRetryCount());
                        taskQueue.submitTask(task);
                        // Update status to SUBMITTED for retry
                        task.setStatus(TaskStatus.SUBMITTED);
                        TaskStatusTracker.updateStatus(task.getId(), task.getStatus());
                    } else {
                        task.setStatus(TaskStatus.FAILED);
                        TaskStatusTracker.updateStatus(task.getId(), TaskStatus.FAILED);
                        logger.error("[{}] Task permanently failed: {}", Thread.currentThread().getName(),
                                task.getName());
                    }
                }

            }
        }
    }
}
