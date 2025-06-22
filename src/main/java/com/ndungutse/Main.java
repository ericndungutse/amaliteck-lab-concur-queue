package com.ndungutse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ndungutse.consumer.TaskConsumer;
import com.ndungutse.producer.TaskProducer;
import com.ndungutse.queue.TaskQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskQueue taskQueue = new TaskQueue();
        Thread producer1 = new Thread(new TaskProducer(taskQueue, "Producer-1", 2));
        Thread producer2 = new Thread(new TaskProducer(taskQueue, "Producer-2", 2));
        Thread producer3 = new Thread(new TaskProducer(taskQueue, "Producer-3", 2));

        producer1.start();
        producer2.start();
        producer3.start();

        // Consumers
        ExecutorService workerPool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            workerPool.submit(new TaskConsumer(taskQueue));
        }
        producer1.join();
        producer2.join();
        producer3.join();

        workerPool.shutdown();

    }
}