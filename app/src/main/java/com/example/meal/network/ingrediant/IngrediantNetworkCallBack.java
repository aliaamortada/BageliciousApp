package com.example.meal.network.ingrediant;

import com.example.meal.model.pojo.ingrediant.Ingrediant;

import java.util.List;

public interface IngrediantNetworkCallBack {
    void onSuccessIngrediants(List<Ingrediant> ingrediants);
    void onFailure(String errorMessage);
}
