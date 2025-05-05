package com.example.meal.model.repository.meal;

import androidx.lifecycle.LiveData;

import com.example.meal.network.meal.MealNetworkCallBack;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.network.meal.MealNetworkCallBack;

import java.util.List;

public interface MealRepository {
     LiveData<List<Meal>> getStoredMeals();  // Get meals from local storage (Room)
     void searchMealByName(MealNetworkCallBack mealNetworkCallback, String mealName);  // Search meal by name (Remote)
     void searchMealByID(MealNetworkCallBack mealNetworkCallback, String mealID);  // Search meal by ID (Remote)
     void getSingleRandomMeal(MealNetworkCallBack mealNetworkCallback, Boolean isSingle);  // Get random meal (Remote)
     void getMealsByFirstLetter(MealNetworkCallBack mealNetworkCallback, String letter);  // Get meals by first letter (Remote)
     void filterByIngredient(MealNetworkCallBack mealNetworkCallback, String ingredient);  // Filter meals by ingredient (Remote)
     void filterByCategory(MealNetworkCallBack mealNetworkCallback, String category);  // Filter meals by category (Remote)
     void filterByArea(MealNetworkCallBack mealNetworkCallback, String area);  // Filter meals by area (Remote)
     void insertMeal(Meal meal);  // Insert meal into local storage (Room)
     void deleteMeal(Meal meal);  // Delete meal from local storage (Room)
}
