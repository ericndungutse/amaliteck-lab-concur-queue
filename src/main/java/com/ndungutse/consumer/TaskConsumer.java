package com.ndungutse.consumer;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;
import com.ndungutse.queue.TaskQueue;

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
                Thread.sleep(new Random().nextInt(1000, 3000));
                // Simulate Processing
                Task task = taskQueue.takeTask();

                task.setStatus(TaskStatus.PROCESSING);
                logger.info("[{}] Processing Task: {} => {} at {}",
                        Thread.currentThread().getName(), task.getName(), task.getStatus(), LocalDateTime.now());

                task.setStatus(TaskStatus.COMPLETED);
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
