package com.example.meal.db.MealDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.Meal;

@Database(entities = {FavMeal.class}, version = 2, exportSchema = false)

public abstract class MealDataBase extends RoomDatabase {
    private static MealDataBase mealDataBase = null;

    public abstract MealDAO getMealDao();

    public static synchronized MealDataBase getInstance(Context context) {
        if (mealDataBase == null) {
            mealDataBase = Room.databaseBuilder(context.getApplicationContext(),
                            MealDataBase.class, "MealDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mealDataBase;
    }
}
