package com.ndungutse.model;

import java.time.Instant;
import java.util.UUID;

public class Task implements Comparable<Task> {
    private final UUID id;
    private final String name;
    private final int priority;
    private final Instant createdTimestamp;
    private final String payload;
    private TaskStatus status;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;

    public Task(String name, int priority, String payload) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.priority = priority;
        this.createdTimestamp = Instant.now();
        this.payload = payload;
        this.status = TaskStatus.SUBMITTED;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void incrementRetry() {
        retryCount++;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public boolean shouldRetry() {
        return retryCount <= MAX_RETRIES;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", createdTimestamp=" + createdTimestamp +
                ", payload='" + payload + '\'' +
                ", status=" + status +
                '}';
    }

}
