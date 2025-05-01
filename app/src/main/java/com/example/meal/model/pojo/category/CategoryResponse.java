package com.example.meal.model.pojo.category;

import java.util.List;

public class CategoryResponse {
    private List<Category> meals;

    public List<Category> getMeals() {
        return meals;
    }

    public void setMeals(List<Category> meals) {
        this.meals = meals;
    }
}

