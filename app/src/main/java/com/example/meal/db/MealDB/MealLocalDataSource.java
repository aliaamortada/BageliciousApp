package com.example.meal.db.MealDB;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.meal.Meal;

import java.util.List;

public interface MealLocalDataSource {
    LiveData<List<Meal>> getMealLiveData();
    void insert(Meal meal);
    void delete(Meal meal);
}
