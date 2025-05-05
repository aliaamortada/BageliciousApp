package com.example.meal.network.ingrediant;

import androidx.annotation.NonNull;

import com.example.meal.model.pojo.ingrediant.IngrediantResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngrediantRemoteDataSourceImpl implements IngrediantRemoteDatasorce {

    private static final String BASE_URL = "https://www.themealdb.com/";
    private IngrediantService ingrediantService;
    private static IngrediantRemoteDataSourceImpl instance;

    private IngrediantRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ingrediantService = retrofit.create(IngrediantService.class);
    }

    public static IngrediantRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new IngrediantRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public void getAllIngredients(IngrediantNetworkCallBack callBack) {
        ingrediantService.getAllIngredients().enqueue(new Callback<IngrediantResponse>() {
            @Override
            public void onResponse(@NonNull Call<IngrediantResponse> call, @NonNull Response<IngrediantResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    callBack.onSuccessIngrediants(response.body().getIngredients());
                } else {
                    callBack.onFailure("Failed to load ingredients");
                }
            }

            @Override
            public void onFailure(@NonNull Call<IngrediantResponse> call, @NonNull Throwable t) {
                callBack.onFailure(t.getMessage());
            }
        });
    }
}
