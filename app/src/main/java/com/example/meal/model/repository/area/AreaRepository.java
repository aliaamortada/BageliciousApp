package com.example.meal.model.repository.area;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.area.Area;

import java.util.List;

public interface AreaRepository {
    LiveData<List<Area>> getAllAreas();  // Fetches both local and remote areas
    void insertArea(Area area);  // Insert into local
    void deleteArea(Area area);  // Delete from local
}
