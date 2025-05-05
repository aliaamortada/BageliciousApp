package com.example.meal.network.area;

import com.example.meal.model.pojo.area.Area;

import java.util.List;

public interface AreaNetworkCallBack {
    void onSuccessAreas(List<Area> areas);
    void onFailure(String errorMessage);
}
