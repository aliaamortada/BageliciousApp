package com.example.meal.model.repository.category;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.category.Category;
import com.example.meal.network.category.CategoryNetworkCallBack;

import java.util.List;

public interface CategoryRepository {
    void getAllCategories(CategoryNetworkCallBack callBack);

    LiveData<List<Category>> getStoredCategories();

    void insertCategory(Category category);

    void deleteCategory(Category category);
}
