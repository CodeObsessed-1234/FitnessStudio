package com.example.fitnessstudio.event;

public class Event {
    private long id;
    private final String task;
    private boolean completed;

    public Event(String task, boolean completed) {
        this.task = task;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
