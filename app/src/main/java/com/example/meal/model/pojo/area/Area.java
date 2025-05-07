package com.example.meal.model.pojo.area;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "area_table")
public class Area {

    @NonNull
    @PrimaryKey
    private String strArea;
    private int idAreaImg;

    public Area(@NonNull String strArea) {
        this.strArea = strArea;
    }

    @Ignore
    public Area(@NonNull String strArea, int idAreaImg) {
        this.strArea = strArea;
        this.idAreaImg = idAreaImg;
    }

    public void setIdAreaImg(int idAreaImg) {
        this.idAreaImg = idAreaImg;
    }

    public int getIdAreaImg() {
        return idAreaImg;
    }

    @NonNull
    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(@NonNull String strArea) {
        this.strArea = strArea;
    }
}
