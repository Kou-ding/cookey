package com.example.cookey.ui.MyRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.AddRecipeActivity;
import com.example.cookey.DBHandler;
import com.example.cookey.MyRecipes;
import com.example.cookey.R;
import com.example.cookey.RecipeDetailsActivity;
import com.example.cookey.SearchRecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class MyRecipesFragment extends Fragment {
    private MyRecipesAdapter adapter;
    private FloatingActionButton fabAddRecipe, fabSearchRecipe;
    private DBHandler dbHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (getContext() == null) {
            return null;
        }
        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        // Initialize DB
        dbHandler = new DBHandler(getContext());
        // Initialize RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.recipesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize FABs
        fabAddRecipe = root.findViewById(R.id.fabAddRecipe);
        fabSearchRecipe = root.findViewById(R.id.fabSearchRecipe);
        // Set click listeners for FABs
        fabAddRecipe.setOnClickListener(v -> openAddRecipeActivity());
        fabSearchRecipe.setOnClickListener(v -> openSearchRecipeActivity());
        // Load recipes
        List<MyRecipes> recipes = dbHandler.getAllRecipes();
        if (recipes == null){
            recipes = new ArrayList<>(); // Fallback σε άδεια λίστα αν η βάση επιστρέψει null
        }
        // Set adapter with click listeners
        adapter = new MyRecipesAdapter(recipes, new MyRecipesAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(MyRecipes recipe) {
                // Open recipe details
                openRecipeDetails(recipe);
            }
            @Override
            public void onFavoriteClick(MyRecipes recipe, boolean isFavorite) {
                try {
                    Log.d("FAVORITE", "Updating recipe ID: " + recipe.getRecipeId() + " to: " + isFavorite);
                    dbHandler.updateRecipeFavoriteStatus(recipe.getRecipeId(), isFavorite);
                } catch (Exception e) {
                    Log.e("FAVORITE", "Error updating favorite", e);
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        return root;
    }
    private void openAddRecipeActivity(){
        try{
            startActivity(new Intent(getActivity(), AddRecipeActivity.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void openSearchRecipeActivity(){
        startActivity(new Intent(getActivity(), SearchRecipeActivity.class));
    }
    private void openRecipeDetails(MyRecipes recipe){
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("recipe_id", recipe.getRecipeId());
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter = null; // Αποφυγή memory leaks
        }
    }
}