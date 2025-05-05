package com.example.meal.network.category;

import com.example.meal.model.pojo.category.Category;

import java.util.List;

public interface CategoryNetworkCallBack {
    void onSuccessCategories(List<Category> categories);
    void onFailure(String errorMessage);
}
