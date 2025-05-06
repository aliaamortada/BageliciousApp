package com.example.meal.model.repository.meal;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.db.MealDB.MealLocalDataSource;
import com.example.meal.db.MealDB.MealLocalDataSourceImpl;
import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.PlanMeal;
import com.example.meal.model.pojo.meal.Meal;
import com.example.meal.network.meal.MealNetworkCallBack;
import com.example.meal.network.meal.MealRemoteDataSource;

import java.util.List;

public class MealRepositoryImpl implements MealRepository {

    private static MealRepositoryImpl instance;

    private final MealRemoteDataSource mealRemoteDataSource;
    private final MealLocalDataSource mealLocalDataSource;

    private MealRepositoryImpl(Context context, MealRemoteDataSource mealRemoteDataSource) {
        this.mealRemoteDataSource = mealRemoteDataSource;
        this.mealLocalDataSource = MealLocalDataSourceImpl.getInstance(context);
    }

    public static MealRepositoryImpl getInstance(Context context, MealRemoteDataSource mealRemoteDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(context, mealRemoteDataSource);
        }
        return instance;
    }

    // ===== Favorite Meals =====
    @Override
    public LiveData<List<FavMeal>> getStoredFavoriteMeals() {
        return mealLocalDataSource.getStoredFavoriteMeals();
    }

    @Override
    public void insertFavoriteMeal(FavMeal meal) {
        mealLocalDataSource.insertFavoriteMeal(meal);
    }

    @Override
    public void deleteFavoriteMeal(FavMeal meal) {
        mealLocalDataSource.deleteFavoriteMeal(meal);
    }

    // ===== Planned Meals =====
    @Override
    public LiveData<List<PlanMeal>> getStoredPlannedMeals() {
        return mealLocalDataSource.getStoredPlannedMeals();
    }

    @Override
    public void insertPlannedMeal(PlanMeal meal) {
        mealLocalDataSource.insertPlannedMeal(meal);
    }

    @Override
    public void deletePlannedMeal(PlanMeal meal) {
        mealLocalDataSource.deletePlannedMeal(meal);
    }

    // ===== Remote Meal Operations =====
    @Override
    public void searchMealByName(MealNetworkCallBack callback, String mealName) {
        mealRemoteDataSource.getMealByName(mealName, callback);
    }

    @Override
    public void searchMealByID(MealNetworkCallBack callback, String mealID) {
        mealRemoteDataSource.getMealByID(mealID, callback);
    }

    @Override
    public void getSingleRandomMeal(MealNetworkCallBack callback, Boolean isSingle) {
        mealRemoteDataSource.getRandomMeal(callback);
    }

    @Override
    public void getMealsByFirstLetter(MealNetworkCallBack callback, String letter) {
        mealRemoteDataSource.getMealsByFirstLetter(letter, callback);
    }

    @Override
    public void filterByIngredient(MealNetworkCallBack callback, String ingredient) {
        mealRemoteDataSource.filterByIngredient(ingredient, callback);
    }

    @Override
    public void filterByCategory(MealNetworkCallBack callback, String category) {
        mealRemoteDataSource.filterByCategory(category, callback);
    }

    @Override
    public void filterByArea(MealNetworkCallBack callback, String area) {
        mealRemoteDataSource.filterByArea(area, callback);
    }
}
