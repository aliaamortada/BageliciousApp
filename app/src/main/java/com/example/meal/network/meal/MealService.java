package com.example.meal.network.meal;

import com.example.meal.model.pojo.meal.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("api/json/v1/1/search.php")
    Call<MealResponse> getMealByName(@Query("s") String name);
    @GET("api/json/v1/1/lookup.php")
    Call<MealResponse> getMealByID(@Query("i") String name);
    @GET("api/json/v1/1/search.php")
    Call<MealResponse> listMealsByFirstLetter(@Query("f") String letter);
    @GET("api/json/v1/1/random.php")
    Call<MealResponse> lookupSingleRandomMeal();
    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);
    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);
    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> getMealsByArea(@Query("a") String area);

}
