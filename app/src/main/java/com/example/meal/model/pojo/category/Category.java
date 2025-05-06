package com.example.meal.model.pojo.category;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_table")
public class Category {

    @NonNull
    @PrimaryKey
    private String idCategory;
    private String strCategory;
    private String strCategoryThumb;
    private String strCategoryDescription;

    public Category(@NonNull String strCategory) {
        this.strCategory = strCategory;
    }

    public void setIdCategory(@NonNull String idCategory) {
        this.idCategory = idCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setStrCategoryThumb(String strCategoryThumb) {
        this.strCategoryThumb = strCategoryThumb;
    }

    public void setStrCategoryDescription(String strCategoryDescription) {
        this.strCategoryDescription = strCategoryDescription;
    }

    @NonNull
    public String getIdCategory() {
        return idCategory;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrCategoryThumb() {
        return strCategoryThumb;
    }

    public String getStrCategoryDescription() {
        return strCategoryDescription;
    }
}
