package com.ndungutse;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndungutse.consumer.TaskConsumer;
import com.ndungutse.producer.TaskProducer;
import com.ndungutse.queue.TaskQueue;
import com.ndungutse.tracker.MonitorThread;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        TaskQueue taskQueue = new TaskQueue();

        Thread producer1 = new Thread(new TaskProducer(taskQueue, "Producer-1", 1));
        Thread producer2 = new Thread(new TaskProducer(taskQueue, "Producer-2", 1));
        Thread producer3 = new Thread(new TaskProducer(taskQueue, "Producer-3", 1));

        producer1.start();
        producer2.start();
        producer3.start();

        // Consumers
        ThreadPoolExecutor workerPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 5; i++) {
            workerPool.submit(new TaskConsumer(taskQueue));
        }

        // Start the monitor thread
        Thread monitorThread = new Thread(new MonitorThread(taskQueue, workerPool), "Monitor-Thread");

        monitorThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown initiated. Attempting graceful shutdown...");

            workerPool.shutdown();
            try {
                if (!workerPool.awaitTermination(30, TimeUnit.MICROSECONDS)) {
                    logger.warn("Forcing shutdown...");

                    // Shutdown immediately if not terminated and drain tasks and put them in a text
                    // file
                    List<Runnable> remainingTasks = workerPool.shutdownNow();

                    logger.info("Remaining tasks in the pool: " + remainingTasks.size());

                }
            } catch (InterruptedException e) {
                // Shutdown immediately if not terminated and drain tasks and put them in a text
                // file
                List<Runnable> remainingTasks = workerPool.shutdownNow();
                logger.info("Remaining tasks in the pool****: " + remainingTasks.size());
                workerPool.shutdownNow();
            }
        }));

    }
}