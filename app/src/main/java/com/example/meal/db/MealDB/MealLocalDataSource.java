package com.example.meal.db.MealDB;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.model.pojo.meal.PlanMeal;

import java.util.List;

public interface MealLocalDataSource {

    // Favorite Meals
    void insertFavoriteMeal(FavMeal meal);
    void deleteFavoriteMeal(FavMeal meal);
    LiveData<List<FavMeal>> getStoredFavoriteMeals();

    // Planned Meals
    void insertPlannedMeal(PlanMeal meal);
    void deletePlannedMeal(PlanMeal meal);
    LiveData<List<PlanMeal>> getStoredPlannedMeals();
}
