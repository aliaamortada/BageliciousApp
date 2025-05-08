package com.example.meal.db.MealDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.Meal;

import java.util.List;

@Dao
public interface MealDAO {

    @Query("SELECT * FROM favorite_meals_table")
    LiveData<List<FavMeal>> getStoredFavoriteMeals();

    @Insert
    void insertFavoriteMeal(FavMeal meal);

    @Delete
    void deleteFavoriteMeal(FavMeal meal); // CHANGED from deleteMeal

    @Query("SELECT * FROM favorite_meals_table WHERE favoriteMealID = :id LIMIT 1")
    FavMeal getMealById(String id);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals_table WHERE favoriteMealID = :id)")
    boolean isFavorite(String id);

}
