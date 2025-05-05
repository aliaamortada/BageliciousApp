package com.example.meal.model.pojo.ingrediant;

import com.example.meal.model.pojo.ingrediant.Ingrediant;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IngrediantResponse {
    @SerializedName("meals")
    private List<Ingrediant> ingredients;

    public List<Ingrediant> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingrediant> ingredients) {
        this.ingredients = ingredients;
    }
}
