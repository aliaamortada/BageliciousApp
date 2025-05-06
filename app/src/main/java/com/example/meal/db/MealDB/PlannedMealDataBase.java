package com.example.meal.db.MealDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.meal.model.pojo.meal.PlanMeal;

@Database(entities = {PlanMeal.class}, version = 2, exportSchema = false)
public abstract class PlannedMealDataBase extends RoomDatabase {

    private static volatile PlannedMealDataBase instance;

    public abstract PlannedMealDAO plannedMealDAO();

    public static PlannedMealDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (PlannedMealDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PlannedMealDataBase.class,
                            "planned_meals_db"
                    ).build();
                }
            }
        }
        return instance;
    }
}
