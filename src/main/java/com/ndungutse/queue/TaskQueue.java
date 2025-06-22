package com.ndungutse.queue;

import java.util.concurrent.PriorityBlockingQueue;
import com.ndungutse.model.Task;

public class TaskQueue {
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

    public void submitTask(Task task) {
        queue.put(task);
    }

    public Task takeTask() throws InterruptedException {
        return queue.take();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
