package com.example.meal.db.MealDB;

import android.content.Context;

import androidx.lifecycle.LiveData;


import com.example.meal.model.pojo.meal.Meal;

import java.util.List;

public class MealLocalDataSourceImpl implements MealLocalDataSource {

    private final MealDAO mealDAO;
    private final LiveData<List<Meal>> mealLiveData;

    public MealLocalDataSourceImpl(Context context) {
        MealDataBase mealDataBase = MealDataBase.getInstance(context.getApplicationContext());
        mealDAO = mealDataBase.getMealDao();
        mealLiveData = mealDAO.getAllMeals();
    }

    @Override
    public LiveData<List<Meal>> getMealLiveData() {
        return mealLiveData;
    }

    @Override
    public void insert(Meal meal) {
        new Thread(() -> {
            if (mealDAO.getMealById(meal.getIdMeal()) == null) {
                mealDAO.insertMeal(meal);
            }
        }).start();
    }

    @Override
    public void delete(Meal meal) {
        new Thread(() -> {
            if (mealDAO.getMealById(meal.getIdMeal()) != null) {
                mealDAO.deleteMeal(meal);
            }
        }).start();
    }
}
