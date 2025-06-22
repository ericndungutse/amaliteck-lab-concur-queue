package com.ndungutse.consumer;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;
import com.ndungutse.queue.TaskQueue;
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
            try {
                Task task = taskQueue.takeTask();
                task.setStatus(TaskStatus.PROCESSING);

                // Add Task status to the task status tracker
                TaskStatusTracker.updateStatus(task.getId(), task.getStatus());

                logger.info(
                        "[{}] Processing Task: {} => {} at {}", Thread.currentThread().getName(), task.getName(),
                        task.getStatus(), LocalDateTime.now());

                // Simulate Processing
                Thread.sleep(new Random().nextInt(1000, 3000));

                task.setStatus(TaskStatus.COMPLETED);

                // Add Task status to the task status tracker
                TaskStatusTracker.updateStatus(task.getId(), task.getStatus());

                logger.info("[{}] Completed Task: {} => {} at {}",
                        Thread.currentThread().getName(), task.getName(), task.getStatus(), LocalDateTime.now());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("[{}] Interrupted, stopping consumer.", Thread.currentThread().getName());
                break;
            } catch (Exception e) {
                logger.error("[{}] Error processing task: {}", Thread.currentThread().getName(), e.getMessage(), e);
            }
        }
    }
}
