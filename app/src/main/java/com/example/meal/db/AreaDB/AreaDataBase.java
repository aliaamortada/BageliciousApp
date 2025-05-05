package com.example.meal.db.AreaDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.area.Area;

@Database(entities = {Area.class}, version = 1, exportSchema = false)
public abstract class AreaDataBase extends RoomDatabase {

    private static AreaDataBase instance;

    public abstract AreaDAO getAreaDao();

    public static synchronized AreaDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AreaDataBase.class, "AreaDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
