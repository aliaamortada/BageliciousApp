package com.example.meal.db.category;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.meal.model.pojo.category.Category;

import java.util.List;

@Dao
public interface CategoeyDAO {
    @Query("SELECT * FROM category_table")
    LiveData<List<Category>> getAllCategories();

    @Insert
    void insertCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM category_table WHERE strCategory = :id LIMIT 1")
    Category getCategoryById(String id);
}
