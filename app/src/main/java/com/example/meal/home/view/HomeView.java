package com.example.meal.home.view;
import com.example.meal.model.pojo.meal.Meal;

import java.util.List;

public interface HomeView {
    void updateRandomMealsAdapter(List<Meal> meals);
    void updateStoredMealsAdapter(List<Meal> meals);
    void showError(String message);
}

