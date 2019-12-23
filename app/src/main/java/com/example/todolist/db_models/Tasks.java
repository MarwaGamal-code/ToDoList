package com.example.todolist.db_models;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tasks_table")
public class Tasks implements Serializable {
    public Tasks() {
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String details;
    private String dueDate;
    private String priority;

    public Tasks(String details, String dueDate, String priority) {
        this.details = details;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
