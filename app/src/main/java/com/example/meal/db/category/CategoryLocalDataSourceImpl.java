package com.example.meal.db.category;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.model.pojo.category.Category;

import java.util.List;

public class CategoryLocalDataSourceImpl implements CategoryLocalDataSource {

    private final CategoeyDAO categoryDAO;
    private final LiveData<List<Category>> categoryLiveData;

    public CategoryLocalDataSourceImpl(Context context) {
        CategoryDataBase db = CategoryDataBase.getInstance(context.getApplicationContext());
        categoryDAO = db.getCategoryDao();
        categoryLiveData = categoryDAO.getAllCategories();
    }

    @Override
    public LiveData<List<Category>> getCategoryLiveData() {
        return categoryLiveData;
    }

    @Override
    public void insert(Category category) {
        new Thread(() -> {
            if (categoryDAO.getCategoryById(category.getStrCategory()) == null) { // Fix: Use strCategory instead of idCategory
                categoryDAO.insertCategory(category);
            }
        }).start();
    }

    @Override
    public void delete(Category category) {
        new Thread(() -> {
            if (categoryDAO.getCategoryById(category.getStrCategory()) != null) { // Fix: Use strCategory instead of idCategory
                categoryDAO.deleteCategory(category);
            }
        }).start();
    }
}
