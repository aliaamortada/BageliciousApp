package com.example.meal.db.IngrediantDB;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.ingrediant.Ingrediant;

import java.util.List;

public class IngrediantLocalDataSourceImpl implements IngrediantLocalDataSource {

    private final IngrediantDAO ingrediantDAO;
    private final LiveData<List<Ingrediant>> ingredientLiveData;

    public IngrediantLocalDataSourceImpl(Context context) {
        IngrediantDataBase ingrediantDataBase = IngrediantDataBase.getInstance(context.getApplicationContext());
        ingrediantDAO = ingrediantDataBase.getIngrediantDao();
        ingredientLiveData = ingrediantDAO.getAllIngredients();
    }

    @Override
    public LiveData<List<Ingrediant>> getIngredientLiveData() {
        return ingredientLiveData;
    }

    @Override
    public void insert(Ingrediant ingredient) {
        new Thread(() -> {
            if (ingrediantDAO.getIngredientById(ingredient.getIdIngredient()) == null) {
                ingrediantDAO.insertIngredient(ingredient);
            }
        }).start();
    }

    @Override
    public void delete(Ingrediant ingredient) {
        new Thread(() -> {
            if (ingrediantDAO.getIngredientById(ingredient.getIdIngredient()) != null) {
                ingrediantDAO.deleteIngredient(ingredient);
            }
        }).start();
    }
}
