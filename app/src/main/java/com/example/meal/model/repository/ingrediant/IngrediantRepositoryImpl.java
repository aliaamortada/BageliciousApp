package com.example.meal.model.repository.ingrediant;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.db.IngrediantDB.IngrediantDAO;
import com.example.meal.db.IngrediantDB.IngrediantDataBase;
import com.example.meal.model.pojo.ingrediant.Ingrediant;
import com.example.meal.network.ingrediant.IngrediantNetworkCallBack;
import com.example.meal.network.ingrediant.IngrediantRemoteDatasorce;

import java.util.List;

public class IngrediantRepositoryImpl implements IngrediantRepository {

    private static IngrediantRepositoryImpl instance;
    private final IngrediantRemoteDatasorce remoteDataSource;
    private final IngrediantDAO ingrediantDAO;
    private final LiveData<List<Ingrediant>> localIngredients;

    private IngrediantRepositoryImpl(Context context, IngrediantRemoteDatasorce remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
        IngrediantDataBase db = IngrediantDataBase.getInstance(context.getApplicationContext());
        this.ingrediantDAO = db.getIngrediantDao();
        this.localIngredients = ingrediantDAO.getAllIngredients();
    }

    public static IngrediantRepositoryImpl getInstance(Context context, IngrediantRemoteDatasorce remoteDataSource) {
        if (instance == null) {
            instance = new IngrediantRepositoryImpl(context, remoteDataSource);
        }
        return instance;
    }

    @Override
    public void getAllIngredients(IngrediantNetworkCallBack callBack) {
        remoteDataSource.getAllIngredients(callBack);
    }

    @Override
    public LiveData<List<Ingrediant>> getStoredIngredients() {
        return localIngredients;
    }

    @Override
    public void insertIngredient(Ingrediant ingrediant) {
        new Thread(() -> ingrediantDAO.insertIngredient(ingrediant)).start();
    }

    @Override
    public void deleteIngredient(Ingrediant ingrediant) {
        new Thread(() -> ingrediantDAO.deleteIngredient(ingrediant)).start();
    }
}
