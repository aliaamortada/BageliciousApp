package com.example.meal.db.AreaDB;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.area.Area;

import java.util.List;

public interface AreaLocalDataSource {
    LiveData<List<Area>> getAreaLiveData();
    void insert(Area area);
    void delete(Area area);
}
