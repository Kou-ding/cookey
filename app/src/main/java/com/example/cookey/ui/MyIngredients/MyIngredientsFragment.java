package com.example.cookey.ui.MyIngredients;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MyIngredientsFragment extends Fragment {
    private MyIngredientsAdapter adapter;
    private boolean editMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);

        // Initialize the recycle view
        RecyclerView recyclerView = view.findViewById(R.id.myIngredientRecyclerView);

        // Initialize the edit button
        MaterialButton editButton = view.findViewById(R.id.editButton);

        // Initialize the adapter with callback methods that delegate to the ViewModel
        List<Ingredient> ingredients;
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            ingredients = db.getAllMyIngredients();
        }
        adapter = new MyIngredientsAdapter(ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Set the edit button click listener
        editButton.setOnClickListener(v -> {
            editMode = !editMode;
            adapter.setEditMode(editMode);
            if (!editMode) {
                try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                    // Update the quantities of the Ingredients in the database
                    List<Ingredient> ings = adapter.getIngredients();
                    for (Ingredient ing : ings) {
                        db.setNewQuantity(ing.getIngredientName(), ing.getQuantity());
                    }
                }
            }
            // Update the button text
            editButton.setText(editMode ? getString(R.string.done) : getString(R.string.edit));

            // Update the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    editMode
                            ? R.drawable.ic_check
                            : R.drawable.ic_edit
            );
            editButton.setIcon(newIcon);
        });

        return view;
    }
    public void loadIngredients(){
        List<Ingredient> ingredients;
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            ingredients = db.getAllMyIngredients();
        }
        adapter.setIngredients(ingredients);
        adapter.notifyDataSetChanged();
    }
}