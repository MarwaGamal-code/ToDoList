package com.example.todolist.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.todolist.dao.DBOperations;
import com.example.todolist.db_models.Tasks;

@Database(entities = Tasks.class,version = 1,exportSchema = false)
public abstract class DatabaseAccess extends RoomDatabase {
    public abstract DBOperations getDao();

}
