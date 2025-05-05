package com.example.meal.db.AreaDB;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.area.Area;

import java.util.List;

public class AreaLocalDataSourceImpl implements AreaLocalDataSource {

    private final AreaDAO areaDAO;
    private final LiveData<List<Area>> areaLiveData;

    public AreaLocalDataSourceImpl(Context context) {
        AreaDataBase areaDataBase = AreaDataBase.getInstance(context.getApplicationContext());
        areaDAO = areaDataBase.getAreaDao();
        areaLiveData = areaDAO.getAllAreas();
    }

    @Override
    public LiveData<List<Area>> getAreaLiveData() {
        return areaLiveData;
    }

    @Override
    public void insert(Area area) {
        new Thread(() -> {
            if (areaDAO.getAreaById(area.getStrArea()) == null) {
                areaDAO.insertArea(area);
            }
        }).start();
    }

    @Override
    public void delete(Area area) {
        new Thread(() -> {
            if (areaDAO.getAreaById(area.getStrArea()) != null) {
                areaDAO.deleteArea(area);
            }
        }).start();
    }
}
