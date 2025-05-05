package com.example.meal.network.area;

import androidx.annotation.NonNull;

import com.example.meal.model.pojo.area.AreaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AreaRemoteDataSourceImpl implements AreaRemoteDataSource {

    private static final String BASE_URL = "https://www.themealdb.com/";
    private final AreaService areaService;
    private static AreaRemoteDataSourceImpl instance;

    private AreaRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        areaService = retrofit.create(AreaService.class);
    }

    public static AreaRemoteDataSourceImpl getInstance() {
        if (instance == null) {
            instance = new AreaRemoteDataSourceImpl();
        }
        return instance;
    }

    @Override
    public void getAllAreas(AreaNetworkCallBack callback) {
        areaService.getAllAreas().enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(@NonNull Call<AreaResponse> call, @NonNull Response<AreaResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    callback.onSuccessAreas(response.body().getAreas());
                } else {
                    callback.onFailure("Failed to fetch areas");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AreaResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
