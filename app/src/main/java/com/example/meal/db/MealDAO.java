package com.example.meal.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.meal.model.pojo.meal.Meal;

import java.util.List;
@Dao
public interface MealDAO {
    @Query("SELECT * FROM meal_table")
    LiveData<List<Meal>> getAllMeals();
    @Insert
    void insertMeal (Meal meal);
    @Delete
    void deleteMeal(Meal meal);

    @Query("SELECT * FROM meal_table WHERE idMeal = :id LIMIT 1")
    Meal getMealById(String id);

}
