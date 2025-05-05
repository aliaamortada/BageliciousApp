package com.example.meal.model.repository.ingrediant;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.ingrediant.Ingrediant;
import com.example.meal.network.ingrediant.IngrediantNetworkCallBack;

import java.util.List;

public interface IngrediantRepository {
    void getAllIngredients(IngrediantNetworkCallBack callBack);

    LiveData<List<Ingrediant>> getStoredIngredients();

    void insertIngredient(Ingrediant ingrediant);

    void deleteIngredient(Ingrediant ingrediant);
}
