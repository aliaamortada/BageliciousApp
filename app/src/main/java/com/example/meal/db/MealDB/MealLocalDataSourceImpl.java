package com.example.meal.db.MealDB;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.meal.FavMeal;
import com.example.meal.model.pojo.meal.PlanMeal;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
    private final MealDAO mealDAO;
    private final PlannedMealDAO plannedMealDAO;
    private static MealLocalDataSourceImpl localDataSourceImpl = null;
    private final LiveData<List<FavMeal>> storedFavoriteMeals;
    private final LiveData<List<PlanMeal>> storedPlannedMeals;

    // Private constructor to avoid instantiation from other classes
    private MealLocalDataSourceImpl(Context context) {
        MealDataBase mealDataBase = MealDataBase.getInstance(context.getApplicationContext());
        PlannedMealDataBase plannedMealDataBase = PlannedMealDataBase.getInstance(context.getApplicationContext());

        mealDAO = mealDataBase.getMealDao();
        plannedMealDAO = plannedMealDataBase.plannedMealDAO();

        storedFavoriteMeals = mealDAO.getStoredFavoriteMeals();
        storedPlannedMeals = plannedMealDAO.getPlannedMeals();
    }

    // Singleton pattern to get the instance of this class
    public static MealLocalDataSourceImpl getInstance(Context context) {
        if (localDataSourceImpl == null) {
            localDataSourceImpl = new MealLocalDataSourceImpl(context);
        }
        return localDataSourceImpl;
    }

    // For Favorite Meals
    @Override
    public void insertFavoriteMeal(FavMeal meal) {
        new Thread(() -> mealDAO.insertFavoriteMeal(meal)).start();
    }

    @Override
    public void deleteFavoriteMeal(FavMeal meal) {
        new Thread(() -> mealDAO.deleteFavoriteMeal(meal)).start();
    }

    @Override
    public LiveData<List<FavMeal>> getStoredFavoriteMeals() {
        return storedFavoriteMeals;
    }

    // For Planned Meals
    @Override
    public void insertPlannedMeal(PlanMeal meal) {
        new Thread(() -> plannedMealDAO.insertPlannedMeal(meal)).start();
    }

    @Override
    public void deletePlannedMeal(PlanMeal meal) {
        new Thread(() -> plannedMealDAO.deletePlannedMeal(meal.date, meal.plannedMealID)).start();
    }

    @Override
    public LiveData<List<PlanMeal>> getStoredPlannedMeals() {
        return storedPlannedMeals;
    }
}
