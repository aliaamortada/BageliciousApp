package com.example.meal.network.category;

import com.example.meal.model.pojo.category.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("api/json/v1/1/categories.php")
    Call<CategoryResponse> getAllCategories();
}
