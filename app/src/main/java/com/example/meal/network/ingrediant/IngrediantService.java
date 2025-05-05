package com.example.meal.network.ingrediant;

import com.example.meal.model.pojo.ingrediant.IngrediantResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IngrediantService {
    @GET("/api/json/v1/1/list.php?i=list")
    Call<IngrediantResponse> getAllIngredients();
}
