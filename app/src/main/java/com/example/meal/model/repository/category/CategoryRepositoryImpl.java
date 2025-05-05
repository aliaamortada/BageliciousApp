package com.example.meal.model.repository.category;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.meal.db.category.CategoeyDAO;
import com.example.meal.db.category.CategoryDataBase;
import com.example.meal.model.pojo.category.Category;
import com.example.meal.network.category.CategoryNetworkCallBack;
import com.example.meal.network.category.CategoryRemoteDataSource;

import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private static CategoryRepositoryImpl instance;
    private final CategoryRemoteDataSource remoteDataSource;
    private final CategoeyDAO categoryDAO;
    private final LiveData<List<Category>> localCategories;

    private CategoryRepositoryImpl(Context context, CategoryRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
        CategoryDataBase db = CategoryDataBase.getInstance(context.getApplicationContext());
        this.categoryDAO = db.getCategoryDao();
        this.localCategories = categoryDAO.getAllCategories();
    }

    public static CategoryRepositoryImpl getInstance(Context context, CategoryRemoteDataSource remoteDataSource) {
        if (instance == null) {
            instance = new CategoryRepositoryImpl(context, remoteDataSource);
        }
        return instance;
    }

    @Override
    public void getAllCategories(CategoryNetworkCallBack callBack) {
        remoteDataSource.getAllCategories(callBack);
    }

    @Override
    public LiveData<List<Category>> getStoredCategories() {
        return localCategories;
    }

    @Override
    public void insertCategory(Category category) {
        new Thread(() -> categoryDAO.insertCategory(category)).start();
    }

    @Override
    public void deleteCategory(Category category) {
        new Thread(() -> categoryDAO.deleteCategory(category)).start();
    }
}
