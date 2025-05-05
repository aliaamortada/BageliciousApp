package com.example.meal.network.category;

import androidx.annotation.NonNull;

import com.example.meal.model.pojo.category.CategoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryRemoteDataSourceImpl implements CategoryRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/";
    private static CategoryRemoteDataSourceImpl instance;
    private final CategoryService categoryService;

    private CategoryRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoryService = retrofit.create(CategoryService.class);
    }

    public static synchronized CategoryRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new CategoryRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public void getAllCategories(CategoryNetworkCallBack callBack) {
        categoryService.getAllCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callBack.onSuccessCategories(response.body().getCategories());
                } else {
                    callBack.onFailure("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                callBack.onFailure(t.getMessage());
            }
        });
    }
}
