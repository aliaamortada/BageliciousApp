package com.example.meal.model.repository.meal;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.model.pojo.meal.PlanMeal;
import com.example.meal.network.meal.MealNetworkCallBack;

import java.util.List;

public interface MealRepository {

     // Favorite Meals (Local)
     LiveData<List<FavMeal>> getStoredFavoriteMeals();
     void insertFavoriteMeal(FavMeal meal);
     void deleteFavoriteMeal(FavMeal meal);

     // Planned Meals (Local)
     LiveData<List<PlanMeal>> getStoredPlannedMeals();
     void insertPlannedMeal(PlanMeal meal);
     void deletePlannedMeal(PlanMeal meal);

     // Remote Meals (Network)
     void searchMealByName(MealNetworkCallBack callback, String mealName);
     void searchMealByID(MealNetworkCallBack callback, String mealID);
     void getSingleRandomMeal(MealNetworkCallBack callback, Boolean isSingle);
     void getMealsByFirstLetter(MealNetworkCallBack callback, String letter);
     void filterByIngredient(MealNetworkCallBack callback, String ingredient);
     void filterByCategory(MealNetworkCallBack callback, String category);
     void filterByArea(MealNetworkCallBack callback, String area);
}
