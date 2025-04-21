package com.example.cookey.ui.MyIngredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.IngredientAdapter;
import com.example.cookey.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyIngredientsFragment extends Fragment {

    private IngredientAdapter adapter;
    private DBHandler dbHandler;
    private boolean editing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);

        ListView ingredientListView = view.findViewById(R.id.myIngredientListView);
        Button editButton = view.findViewById(R.id.editButton);

        dbHandler = new DBHandler(requireContext());
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(dbHandler.getAllIngredients()));

        adapter = new IngredientAdapter(requireContext(), ingredients);
        ingredientListView.setAdapter(adapter);

        // Toggle edit mode on button click
        editButton.setOnClickListener(v -> {
            editing = !editing;
            adapter.setEditingEnabled(editing);
            editButton.setText(editing ? "Done" : "Edit");
        });

        adapter.setOnDeleteClickListener(ingredient -> {
            dbHandler.deleteIngredient(ingredient.getIngredientId()); // assuming getId() exists
            ingredients.remove(ingredient);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
