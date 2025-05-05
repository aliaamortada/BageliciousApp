package com.example.meal.db.IngrediantDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.meal.model.pojo.ingrediant.Ingrediant;

import java.util.List;

@Dao
public interface IngrediantDAO {
    @Query("SELECT * FROM ingredient_table")
    LiveData<List<Ingrediant>> getAllIngredients();

    @Insert
    void insertIngredient(Ingrediant ingredient);

    @Delete
    void deleteIngredient(Ingrediant ingredient);

    @Query("SELECT * FROM ingredient_table WHERE idIngredient = :id LIMIT 1")
    Ingrediant getIngredientById(String id);
}
