package com.ndungutse;

import java.util.PriorityQueue;

import com.ndungutse.model.Task;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Task> queue = new PriorityQueue<>();
        queue.add(new Task("Low Priority Queue", 1, "Payload A"));
        queue.add(new Task("Medium Priority Queue", 5, "Payload B"));
        queue.add(new Task("High Priority Queue", 10, "Payload C"));

        while (!queue.isEmpty()) {
            Task task = queue.poll();
            System.out.println("Processing: " + task.getName());
        }

    }
}