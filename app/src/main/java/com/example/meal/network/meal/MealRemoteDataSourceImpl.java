package com.example.meal.network.meal;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.meal.model.pojo.meal.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealRemoteDataSourceImpl implements MealRemoteDataSource {
    private static final String TAG = "MealRemoteDataSourceImpl";
    private static final String BASE_URL = "https://www.themealdb.com/";

    private final MealService mealService;

    public MealRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mealService = retrofit.create(MealService.class);
    }

    @Override
    public void getMealByID(String id, MealNetworkCallBack callback) {
        mealService.getMealByID(id).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error fetching meal by ID", t);
            }
        });
    }

    @Override
    public void getMealByName(String name, MealNetworkCallBack callback) {
        mealService.getMealByName(name).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error fetching meal by name", t);
            }
        });
    }

    @Override
    public void getMealsByFirstLetter(String letter, MealNetworkCallBack callback) {
        mealService.listMealsByFirstLetter(letter).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error fetching meals by first letter", t);
            }
        });
    }

    @Override
    public void getRandomMeal(MealNetworkCallBack callback) {
        mealService.lookupSingleRandomMeal().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error fetching random meal", t);
            }
        });
    }

    @Override
    public void filterByIngredient(String ingredient, MealNetworkCallBack callback) {
        mealService.getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error filtering meals by ingredient", t);
            }
        });
    }

    @Override
    public void filterByCategory(String category, MealNetworkCallBack callback) {
        mealService.getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error filtering meals by category", t);
            }
        });
    }

    @Override
    public void filterByArea(String area, MealNetworkCallBack callback) {
        mealService.getMealsByArea(area).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccessMeal(response.body().getMeals());
                } else {
                    callback.onFailureMeal("Empty response or failed request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                callback.onFailureMeal(t.getMessage());
                Log.e(TAG, "Error filtering meals by area", t);
            }
        });
    }
}
