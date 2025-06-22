package com.ndungutse.tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;
import com.ndungutse.queue.TaskQueue;

public class MonitorThread implements Runnable {
    private final TaskQueue taskQueue;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(MonitorThread.class);
    private final ThreadPoolExecutor threadPool;

    public MonitorThread(TaskQueue taskQueue, ThreadPoolExecutor threadPool) {
        this.taskQueue = taskQueue;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            try {
                Thread.sleep(500);

                int queueSize = taskQueue.getQueueSize();
                int activeThreads = threadPool.getActiveCount();
                int poolSize = threadPool.getPoolSize();
                int queuedTasks = threadPool.getQueue().size();

                Map<TaskStatus, Integer> statusCounts = countTaskStatuses();

                logger.info(
                        "[{}] Queue size: {}, ThreadPool active: {}, pool size: {}, queued tasks: {}, Task status counts: {}",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        queueSize,
                        activeThreads,
                        poolSize,
                        queuedTasks,
                        statusCounts);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("[{}] Monitor thread interrupted, stopping monitoring.", Thread.currentThread().getName());
                break;
            }

        }
    }

    private Map<TaskStatus, Integer> countTaskStatuses() {
        EnumMap<TaskStatus, Integer> counts = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            counts.put(status, 0);
        }

        ConcurrentHashMap<UUID, TaskStatus> map = TaskStatusTracker.getAllStatuses();

        for (TaskStatus status : map.values()) {
            counts.compute(status, (k, v) -> v == null ? 1 : v + 1);
        }

        return counts;
    }

}
