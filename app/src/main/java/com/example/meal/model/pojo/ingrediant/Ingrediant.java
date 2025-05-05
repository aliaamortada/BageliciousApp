package com.example.meal.model.pojo.ingrediant;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "ingredient_table")

public class Ingrediant {
    @PrimaryKey
    @NonNull
    private String idIngredient;
    private String strIngredient;
    private String strDescription;
    private String strType;
    private String strMesure;

    public void setStrMesure(String strMesure) {
        this.strMesure = strMesure;
    }

    public String getStrMesure() {
        return strMesure;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(String idIngredient) {
        this.idIngredient = idIngredient;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public void setStrIngredient(String strIngredient) {
        this.strIngredient = strIngredient;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

}
