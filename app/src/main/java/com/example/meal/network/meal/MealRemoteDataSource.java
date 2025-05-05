package com.example.meal.network.meal;

public interface MealRemoteDataSource {

    void getMealByID(String id, MealNetworkCallBack callback);

    void getMealByName(String name, MealNetworkCallBack callback);

    void getMealsByFirstLetter(String letter, MealNetworkCallBack callback);

    void getRandomMeal(MealNetworkCallBack callback);

    void filterByIngredient(String ingredient, MealNetworkCallBack callback);

    void filterByCategory(String category, MealNetworkCallBack callback);

    void filterByArea(String area, MealNetworkCallBack callback);
}
