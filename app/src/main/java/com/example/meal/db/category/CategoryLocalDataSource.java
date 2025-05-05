package com.example.meal.db.category;

import androidx.lifecycle.LiveData;
import com.example.meal.model.pojo.category.Category;

import java.util.List;

public interface CategoryLocalDataSource {
    LiveData<List<Category>> getCategoryLiveData();
    void insert(Category category);
    void delete(Category category);
}
