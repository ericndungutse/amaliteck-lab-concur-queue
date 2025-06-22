package com.ndungutse.tracker;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskStats {
    private AtomicInteger taskProcessedCount = new AtomicInteger(0);

    private static class Holder {
        private static final TaskStats INSTANCE = new TaskStats();
    }

    public static TaskStats getInstance() {
        return Holder.INSTANCE;
    }

    public static void incrementProcessedCount() {
        getInstance().taskProcessedCount.incrementAndGet();
    }

    public static int getProcessedCount() {
        return getInstance().taskProcessedCount.get();
    }
}
