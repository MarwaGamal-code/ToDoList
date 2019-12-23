package com.example.todolist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.db_models.Tasks;

import java.util.List;
@Dao
public interface DBOperations {
    @Insert
    void insertTask(Tasks task);
    @Update
    void updateTask(Tasks task);
    @Delete
    void deleteTask(Tasks task);
    @Query("select * from tasks_table")
    List<Tasks> getAllTasks();
}
