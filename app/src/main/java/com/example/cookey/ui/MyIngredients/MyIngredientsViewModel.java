package com.example.cookey.ui.MyIngredients;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyIngredientsViewModel extends ViewModel {
    private final MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public void loadIngredients(Context context) {
        try(DBHandler db = new DBHandler(context)) {
            ingredients.setValue(new ArrayList<>(Arrays.asList(db.getAllIngredients())));
        }
    }

    public void updateIngredient(Context context, String ingredientName, float newQuantity) {
        try(DBHandler db = new DBHandler(context)){
            db.setNewQuantity(ingredientName,newQuantity);
        }
        loadIngredients(context); // Reload after update
    }

    public void deleteIngredient(Context context, int ingredientId) {
        try (DBHandler db = new DBHandler(context)) {
            db.deleteIngredient(ingredientId);
        }
        loadIngredients(context); // Reload after delete
    }
}
