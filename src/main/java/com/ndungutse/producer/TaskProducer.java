package com.ndungutse.producer;

import com.ndungutse.model.Task;
import com.ndungutse.queue.TaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

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
                int priority = (i % 3 == 0) ? 10 : random.nextInt(5) + 1;

                Task task = new Task(
                        producerName + "-Task-" + i,
                        priority,
                        "Payload for " + producerName + "-Task-" + i);

                taskQueue.submitTask(task);
                logger.info("[{}] Submitted: {}", producerName, task);

                Thread.sleep(random.nextInt(3000) + 1000); // Simulated delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("[{}] Interrupted during task generation", producerName);
        }
    }
}
