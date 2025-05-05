package com.example.meal.model.pojo.area;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "area_table")
public class Area {

    @NonNull
    @PrimaryKey
    private String strArea;

    public Area(@NonNull String strArea) {
        this.strArea = strArea;
    }

    @NonNull
    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(@NonNull String strArea) {
        this.strArea = strArea;
    }
}
