package com.ndungutse;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.ndungutse.consumer.TaskConsumer;
import com.ndungutse.producer.TaskProducer;
import com.ndungutse.queue.TaskQueue;
import com.ndungutse.tracker.MonitorThread;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskQueue taskQueue = new TaskQueue();

        Thread producer1 = new Thread(new TaskProducer(taskQueue, "Producer-1", 100));
        Thread producer2 = new Thread(new TaskProducer(taskQueue, "Producer-2", 100));
        Thread producer3 = new Thread(new TaskProducer(taskQueue, "Producer-3", 100));

        producer1.start();
        producer2.start();
        producer3.start();

        // Consumers
        ThreadPoolExecutor workerPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            workerPool.submit(new TaskConsumer(taskQueue));
        }

        // Start the monitor thread
        Thread monitorThread = new Thread(new MonitorThread(taskQueue, workerPool), "Monitor-Thread");

        monitorThread.start();
        workerPool.shutdown();

    }
}