package com.example.meal.db.AreaDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.meal.model.pojo.area.Area;

import java.util.List;

@Dao
public interface AreaDAO {

    @Query("SELECT * FROM area_table")
    LiveData<List<Area>> getAllAreas();

    @Insert
    void insertArea(Area area);

    @Delete
    void deleteArea(Area area);

    @Query("SELECT * FROM area_table WHERE strArea = :id LIMIT 1")
    Area getAreaById(String id);
}
