package com.example.meal.network.area;

import com.example.meal.model.pojo.area.AreaResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AreaService {
    @GET("api/json/v1/1/list.php?a=list")
    Call<AreaResponse> getAllAreas();
}
