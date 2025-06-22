package com.ndungutse.tracker;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;

public class TaskStatusTracker {
    private final ConcurrentHashMap<UUID, TaskStatus> statusMap = new ConcurrentHashMap<>();

    public void record(Task task) {
        statusMap.put(task.getId(), task.getStatus());
    }

    public void updateStatus(UUID taskId, TaskStatus status) {
        statusMap.put(taskId, status);
    }

    public TaskStatus getStatus(UUID taskId) {
        return statusMap.get(taskId);
    }

    public ConcurrentHashMap<UUID, TaskStatus> getAllStatuses() {
        return statusMap;
    }
}
