package com.example.meal.model.repository.meal;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.db.MealDB.MealDAO;
import com.example.meal.db.MealDB.MealDataBase;
import com.example.meal.network.meal.MealNetworkCallBack;
import com.example.meal.network.meal.MealRemoteDataSource;
import com.example.meal.model.pojo.meal.Meal;

import java.util.List;

public class MealRepositoryImpl implements MealRepository {
    private static MealRepositoryImpl instance;
    private final MealDAO mealDAO;
    private final MealRemoteDataSource mealRemoteDataSource;
    private final LiveData<List<Meal>> mealLiveData;

    // Private constructor for Singleton pattern
    private MealRepositoryImpl(Context context, MealRemoteDataSource mealRemoteDataSource) {
        MealDataBase mealDataBase = MealDataBase.getInstance(context.getApplicationContext());
        mealDAO = mealDataBase.getMealDao();
        this.mealRemoteDataSource = mealRemoteDataSource;
        mealLiveData = mealDAO.getAllMeals();
    }

    // Singleton method to get the instance of the repository
    public static MealRepositoryImpl getInstance(Context context, MealRemoteDataSource mealRemoteDataSource) {
        if (instance == null) {
            instance = new MealRepositoryImpl(context, mealRemoteDataSource);
        }
        return instance;
    }

    @Override
    public LiveData<List<Meal>> getStoredMeals() {
        return mealLiveData;
    }

    @Override
    public void searchMealByName(MealNetworkCallBack mealNetworkCallback, String mealName) {
        mealRemoteDataSource.getMealByName(mealName, mealNetworkCallback);
    }

    @Override
    public void searchMealByID(MealNetworkCallBack mealNetworkCallback, String mealID) {
        mealRemoteDataSource.getMealByID(mealID, mealNetworkCallback);
    }

    @Override
    public void getSingleRandomMeal(MealNetworkCallBack mealNetworkCallback, Boolean isSingle) {
        mealRemoteDataSource.getRandomMeal(mealNetworkCallback);
    }

    @Override
    public void getMealsByFirstLetter(MealNetworkCallBack mealNetworkCallback, String letter) {
        mealRemoteDataSource.getMealsByFirstLetter(letter, mealNetworkCallback);
    }

    @Override
    public void filterByIngredient(MealNetworkCallBack mealNetworkCallback, String ingredient) {
        mealRemoteDataSource.filterByIngredient(ingredient, mealNetworkCallback);
    }

    @Override
    public void filterByCategory(MealNetworkCallBack mealNetworkCallback, String category) {
        mealRemoteDataSource.filterByCategory(category, mealNetworkCallback);
    }

    @Override
    public void filterByArea(MealNetworkCallBack mealNetworkCallback, String area) {
        mealRemoteDataSource.filterByArea(area, mealNetworkCallback);
    }

    @Override
    public void insertMeal(Meal meal) {
        new Thread(() -> mealDAO.insertMeal(meal)).start();
    }

    @Override
    public void deleteMeal(Meal meal) {
        new Thread(() -> mealDAO.deleteMeal(meal)).start();
    }
}
