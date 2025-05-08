package com.example.meal.db.MealDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.meal.model.pojo.meal.PlanMeal;

import java.util.List;

@Dao
public interface PlannedMealDAO {

    @Query("SELECT * FROM planned_meals_table WHERE date = :date")
    LiveData<List<PlanMeal>> getPlannedMealByDate(String date);

    @Query("SELECT * FROM planned_meals_table")
    LiveData<List<PlanMeal>> getPlannedMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlannedMeal(PlanMeal meal);

    @Query("DELETE FROM planned_meals_table WHERE date = :date AND plannedMealID = :mealId")
    void deletePlannedMeal(String date, String mealId);
    @Query("SELECT * FROM planned_meals_table WHERE plannedMealID = :id LIMIT 1")
    PlanMeal getPlannedMealById(String id);

}
