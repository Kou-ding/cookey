package com.example.cookey.ui.MyIngredients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.R;

import java.util.List;

public class MyIngredientsFragment extends Fragment {

    private MyIngredientsAdapter adapter;
    private boolean editMode = false;

    public void loadIngredients() {
        try(DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            List<Ingredient> ingredients = db.getAllIngredients();
            adapter.setIngredients(ingredients);
        } catch (Exception e) {
            Log.e("MyIngredientsViewModel", "Error loading ingredients", e);
        }
    }

    public void updateIngredient(String ingredientName, float newQuantity) {
        try(DBHandler db = new DBHandler(requireContext(),null,null,1)){
            db.setNewQuantity(ingredientName, newQuantity);
        } catch (Exception e) {
            Log.e("MyIngredientsViewModel", "Error updating ingredient", e);
        }
        loadIngredients(); // Reload after update
    }

    public void deleteIngredient(Ingredient ingredient) {
        int ingredientId = ingredient.getIngredientId();
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            db.deleteIngredient(ingredientId);
        } catch (Exception e) {
            Log.e("MyIngredientsViewModel", "Error deleting ingredient", e);
        }
        loadIngredients(); // Reload after delete
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);

        // Initialize the recycle view
        RecyclerView recyclerView = view.findViewById(R.id.myIngredientRecyclerView);

        // Initialize the edit button
        Button editButton = view.findViewById(R.id.editButton);

        // Initialize the adapter with callback methods that delegate to the ViewModel
        adapter = new MyIngredientsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Load the ingredients from the database
        loadIngredients();

        // Set the edit button click listener
        editButton.setOnClickListener(v -> {
            editMode = !editMode;
            if (!editMode) {
                for (Ingredient i : adapter.getIngredients()) {
                    updateIngredient(i.getIngredientName(), i.getQuantity());
                }
            }
            adapter.setMode(editMode);
            editButton.setText(editMode ? getString(R.string.done) : getString(R.string.edit));
        });
        // Set the delete button click listener
        adapter.setOnDeleteClickListener(ingredient -> {
            deleteIngredient(ingredient);
            loadIngredients();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh ingredients data when fragment resumes
        loadIngredients();
    }
}