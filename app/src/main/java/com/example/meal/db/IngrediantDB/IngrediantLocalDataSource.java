package com.example.meal.db.IngrediantDB;

import androidx.lifecycle.LiveData;
import com.example.meal.model.pojo.ingrediant.Ingrediant;
import java.util.List;

public interface IngrediantLocalDataSource {
    LiveData<List<Ingrediant>> getIngredientLiveData();
    void insert(Ingrediant ingredient);
    void delete(Ingrediant ingredient);
}
