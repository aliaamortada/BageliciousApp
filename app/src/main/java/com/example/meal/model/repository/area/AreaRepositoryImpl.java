package com.example.meal.model.repository.area;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meal.model.pojo.area.Area;
import com.example.meal.network.area.AreaNetworkCallBack;
import com.example.meal.network.area.AreaRemoteDataSource;
import com.example.meal.db.AreaDB.AreaLocalDataSource;

import java.util.List;

public class AreaRepositoryImpl implements AreaRepository {

    private static AreaRepositoryImpl instance;
    private final AreaLocalDataSource localDataSource;
    private final AreaRemoteDataSource remoteDataSource;

    // Mutable LiveData to hold areas locally
    private final MutableLiveData<List<Area>> areaLiveData = new MutableLiveData<>();

    // Private constructor for Singleton pattern
    private AreaRepositoryImpl(Context context, AreaLocalDataSource localDataSource, AreaRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    // Singleton method to get the instance of the repository
    public static AreaRepositoryImpl getInstance(Context context, AreaLocalDataSource localDataSource, AreaRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new AreaRepositoryImpl(context, localDataSource, remoteDataSource);
        }
        return instance;
    }

    @Override
    public LiveData<List<Area>> getAllAreas() {
        // Try to fetch from local
        localDataSource.getAreaLiveData().observeForever(localAreas -> {
            if (localAreas != null && !localAreas.isEmpty()) {
                areaLiveData.setValue(localAreas);  // Set local data if available
            } else {
                // Fetch from remote if local is empty or null
                remoteDataSource.getAllAreas(new AreaNetworkCallBack() {
                    @Override
                    public void onSuccessAreas(List<Area> areas) {
                        // Update local data source
                        for (Area area : areas) {
                            localDataSource.insert(area);  // Save to local database
                        }
                        areaLiveData.setValue(areas);  // Set remote data
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // Handle failure (e.g., show an error message or log it)
                    }
                });
            }
        });
        return areaLiveData;
    }


    @Override
    public void insertArea(Area area) {
        localDataSource.insert(area);  // Insert into local data source
    }

    @Override
    public void deleteArea(Area area) {
        localDataSource.delete(area);  // Delete from local data source
    }
}
