package com.example.todolist;

import android.content.Context;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;

import com.example.todolist.dao.DBOperations;
import com.example.todolist.db_models.Tasks;
import com.example.todolist.room.DatabaseAccess;

import java.util.List;

public class DbFacade {
    private static final DbFacade ourInstance = new DbFacade();

    public static DbFacade getInstance() {
        return ourInstance;
    }

    private DbFacade() {
    }
    private DBOperations dbOperations;
    private DatabaseAccess access;
    public void init(Context context){
        access = Room.databaseBuilder(context, DatabaseAccess.class, "Tasks_DB").build();
        dbOperations=access.getDao();
    }

    public void insertTask(Tasks task){
        dbOperations.insertTask(task);
    }
    public void updateTask(Tasks task){
        dbOperations.updateTask(task);
    }
    public void deleteTask(Tasks task){
        dbOperations.deleteTask(task);
    }
    public List<Tasks> getAllTasks(){
        return dbOperations.getAllTasks();
    }
}
