package com.ndungutse.tracker;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ndungutse.model.Task;
import com.ndungutse.model.TaskStatus;

public class TaskStatusTracker {
    private final ConcurrentHashMap<UUID, TaskStatus> statusMap = new ConcurrentHashMap<>();

    private TaskStatusTracker() {
    };

    private static class Holder {
        private static final TaskStatusTracker INSTANCE = new TaskStatusTracker();
    }

    private static TaskStatusTracker getInstance() {
        return Holder.INSTANCE;
    }

    public static void record(Task task) {
        getInstance().statusMap.put(task.getId(), task.getStatus());
    }

    public static void updateStatus(UUID taskId, TaskStatus status) {
        getInstance().statusMap.put(taskId, status);
    }

    public static TaskStatus getStatus(UUID taskId) {
        return getInstance().statusMap.get(taskId);
    }

    public static ConcurrentHashMap<UUID, TaskStatus> getAllStatuses() {
        return getInstance().statusMap;
    }
}
