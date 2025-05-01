package com.example.meal.db;

import android.content.Context;
import android.os.Build;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.meal.Meal;

@Database(entities = {Meal.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase = null;

    public abstract MealDAO getMealDao();

    public static synchronized AppDataBase getInstance(Context context) {
        if (appDataBase == null) {
            appDataBase = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "MealDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDataBase;
    }
}
