package com.example.meal.network.meal;
import java.util.List;
import com.example.meal.model.pojo.meal.Meal;

public interface MealNetworkCallBack {

     void onSuccessMeal(List<Meal> meals);
     void onFailureMeal(String errorMessage);

}
