package com.example.cookey.ui.MyRecipes;

import com.example.cookey.AIRecipeViewActivity;
import com.example.cookey.RecipeModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookey.AddOrEditActivity;
import com.example.cookey.DBHandler;
import com.example.cookey.R;
import com.example.cookey.RecipeDetailsActivity;
import com.example.cookey.SearchRecipeActivity;
import com.example.cookey.ViewRecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
public class MyRecipesFragment extends Fragment {
    private MyRecipesAdapter adapter;
    private FloatingActionButton fabAddRecipe, fabSearchRecipe, fabAIRecipe;
    private DBHandler dbHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (getContext() == null) {
            return null;
        }
        View root = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        // Initialize DB
        dbHandler = new DBHandler(getContext(),null,null,1);
        List<RecipeModel> recipes = dbHandler.getAllRecipes();
        adapter = new MyRecipesAdapter(recipes, new MyRecipesAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(RecipeModel recipe) {
                // Open recipe details
                openRecipeDetails(recipe);
            }
            @Override
            public void onFavoriteClick(RecipeModel recipe, boolean isFavorite) {
                try {
                    Log.d("FAVORITE", "Updating recipe ID: " + recipe.getId() + " to: " + isFavorite);
                    dbHandler.updateRecipeFavoriteStatus(recipe.getId(), isFavorite);
                } catch (Exception e) {
                    Log.e("FAVORITE", "Error updating favorite", e);
                    e.printStackTrace();
                }
            }
        });
        // Initialize RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.recipesRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize FABs
        fabAIRecipe = root.findViewById(R.id.fabAIRecipe);
        fabAddRecipe = root.findViewById(R.id.fabAddRecipe);
        fabSearchRecipe = root.findViewById(R.id.fabSearchRecipe);
        // Set click listeners for FABs
        fabAIRecipe.setOnClickListener(v -> openAIRecipeActivity());

        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOrEditActivity.class);
                startActivity(intent);
            }
        });

        fabSearchRecipe.setOnClickListener(v -> openSearchRecipeActivity());
        return root;
    }
    private void openAIRecipeActivity(){
        try{
            startActivity(new Intent(getActivity(), AIRecipeViewActivity.class));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void openAddRecipeActivity(){
        try{
            startActivity(new Intent(getActivity(), AddOrEditActivity.class));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void openSearchRecipeActivity() {
        try {
            Intent intent = new Intent(requireActivity(), SearchRecipeActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error opening search", Toast.LENGTH_SHORT).show();
        }
    }
    private void openRecipeDetails(RecipeModel recipe){
        if (recipe == null) return;

        Intent intent = new Intent(getActivity(), ViewRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        List<RecipeModel> recipes = dbHandler.getAllRecipes();
        if (recipes != null) {
            adapter.updateData(recipes);
        }
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
            adapter = null;
        }
    }
}