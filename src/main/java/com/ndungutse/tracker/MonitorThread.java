package com.ndungutse.tracker;

import org.slf4j.Logger;

import com.ndungutse.queue.TaskQueue;

public class MonitorThread implements Runnable {
    private final TaskQueue taskQueue;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MonitorThread.class);

    public MonitorThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(500);
                logger.info("Current task queue size: {}", taskQueue.getQueueSize());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
