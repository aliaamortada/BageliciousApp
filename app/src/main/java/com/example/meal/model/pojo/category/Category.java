package com.example.meal.model.pojo.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {

    @NonNull
    @PrimaryKey
    private String strCategory;

    public Category(@NonNull String strCategory) {
        this.strCategory = strCategory;
    }

    @NonNull
    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(@NonNull String strCategory) {
        this.strCategory = strCategory;
    }
}
