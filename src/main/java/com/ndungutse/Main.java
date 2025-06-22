package com.ndungutse;

import com.ndungutse.producer.TaskProducer;
import com.ndungutse.queue.TaskQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskQueue taskQueue = new TaskQueue();
        Thread producer1 = new Thread(new TaskProducer(taskQueue, "Producer-1", 4));
        Thread producer2 = new Thread(new TaskProducer(taskQueue, "Producer-2", 3));
        Thread producer3 = new Thread(new TaskProducer(taskQueue, "Producer-3", 8));

        producer1.start();
        producer2.start();
        producer3.start();

        producer1.join();
        producer2.join();
        producer3.join();

        System.out.println("QUEUE SIZE: " + taskQueue.getQueueSize());

    }
}