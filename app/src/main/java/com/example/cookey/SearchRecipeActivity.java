package com.example.cookey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.ui.MyRecipes.MyRecipesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchRecipeActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button searchButton;
    private Spinner filterSpinner;
    private Button applyFiltersButton;
    private RecyclerView resultsRecyclerView;
    private MyRecipesAdapter adapter;
    private DBHandler dbHandler;
    private void applyTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("app_theme", "dark");
        if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_Cookey_Dark);
            Log.d("Theme!", "Dark theme applied");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.Theme_Cookey_Light);
            Log.d("Theme!", "Light theme applied");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_search_recipe);
        setContentView(R.layout.activity_search_recipe);
        // Initialize database handler first
        dbHandler = new DBHandler(this,null ,null,1);
        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        //searchButton = findViewById(R.id.searchButton);
        filterSpinner = findViewById(R.id.filterSpinner);
        applyFiltersButton = findViewById(R.id.applyFiltersButton);
        resultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        // Setup RecyclerView
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<RecipeModel> initialRecipes=dbHandler.getAllRecipes();

        //Accessibility
        NarratorManager.speakIfEnabled(this, getString(R.string.title_search_recipe));



        filterSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                NarratorManager.speakIfEnabled(v.getContext(), getString(R.string.all_filter));
            }
            return false;
        });



        adapter = new MyRecipesAdapter(initialRecipes, new MyRecipesAdapter.OnRecipeClickListener() {
            @Override
            public void onRecipeClick(RecipeModel recipe) {
                if (recipe != null) {
                    openRecipeDetails(recipe);
                }
            }
            @Override
            public void onFavoriteClick(RecipeModel recipe, boolean isFavorite) {
                try {
                    if (recipe != null && dbHandler != null) {
                        dbHandler.updateRecipeFavoriteStatus(recipe.getId(), isFavorite);
                    }
                } catch (Exception e) {
                    Log.e("SEARCH_ACTIVITY", "Error updating favorite", e);
                    Toast.makeText(SearchRecipeActivity.this,
                            "Error updating favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resultsRecyclerView.setAdapter(adapter);
        // Setup filter spinner
        setupCombinedSpinner();
        // Set click listeners
        applyFiltersButton.setOnClickListener(v -> applyFilters());
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                performSearch();
            }
        });
    }

    private void openRecipeDetails(RecipeModel recipe) {
        if (recipe == null) return;

        try {
            Intent intent = new Intent(this, ViewRecipeActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        } catch (Exception e) {
            Log.e("SEARCH_ACTIVITY", "Error opening recipe details", e);
            Toast.makeText(this, "Error opening recipe", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCombinedSpinner() {
        if (filterSpinner == null || dbHandler == null) return;
        List<String> filters = new ArrayList<>();
        // Προσθήκη βασικών κατηγοριών
        filters.add(getString(R.string.all_filter));
        // Προσθήκη επιλογών για αγαπημένα και δυσκολία
        filters.add(getString(R.string.favorites_only));
        filters.add(getString(R.string.filter_difficulty_easy));
        filters.add(getString(R.string.filter_difficulty_medium));
        filters.add(getString(R.string.filter_difficulty_hard));
        // Προσθήκη separator για tags
        filters.add("--- Tags ---");
        // Προσθήκη tags από τη βάση
        List<String> tags = dbHandler.getAllTags();
        if (tags != null && !tags.isEmpty()) {
            filters.addAll(tags);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                filters
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
    }

    private void performSearch() {
        if (searchInput == null || filterSpinner == null || dbHandler == null || adapter == null) return;
        String query = searchInput.getText().toString().trim();
        String selectedOption = filterSpinner.getSelectedItem().toString();
        List<RecipeModel> results = new ArrayList<>();
        try {
            if (selectedOption.equals(getString(R.string.all_filter))) {
                results = dbHandler.searchRecipes(query);
            }
            // Ελέγχουμε αν είναι tag
            else if (dbHandler.getAllTags().contains(selectedOption)) {
                results = dbHandler.getRecipesByTag(selectedOption);
                // Εφαρμογή επιπλέον φίλτρου αναζήτησης αν υπάρχει query
                if (!query.isEmpty() && results != null) {
                    results = results.stream()
                            .filter(recipe -> recipe != null && recipe.getName() != null &&
                                    recipe.getName().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());
                }
            } else {
                // Αλλιώς είναι difficulty/favorites
                results = dbHandler.searchRecipesWithFilter(query, selectedOption);
            }

            if (results != null) {
                adapter.updateData(results);
            } else {
                adapter.updateData(new ArrayList<>());
                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("SEARCH_ERROR", "Search failed", e);
            Toast.makeText(this, "Search failed", Toast.LENGTH_SHORT).show();
            adapter.updateData(new ArrayList<>());
        }
    }

    private void applyFilters() {
        String selectedFilter = filterSpinner.getSelectedItem().toString();
        String query = searchInput.getText().toString().trim();
        List<RecipeModel> filteredResults = new ArrayList<>();
        try {
            if (selectedFilter.equals(getString(R.string.favorites_only))) {
                filteredResults = dbHandler.getFavoriteRecipes();
            } else if (selectedFilter.equals(getString(R.string.filter_difficulty_easy))) {
                filteredResults = dbHandler.getRecipesByDifficulty("Easy");
            } else if (selectedFilter.equals(getString(R.string.filter_difficulty_medium))) {
                filteredResults = dbHandler.getRecipesByDifficulty("Medium");
            } else if (selectedFilter.equals(getString(R.string.filter_difficulty_hard))) {
                filteredResults = dbHandler.getRecipesByDifficulty("Hard");
            } else if (dbHandler.getAllTags().contains(selectedFilter)) {
                filteredResults = dbHandler.getRecipesByTag(selectedFilter);
            } else {
                filteredResults = dbHandler.getAllRecipes();
            }
            // Εφαρμογή αναζήτησης αν υπάρχει query
            if (!query.isEmpty()) {
                filteredResults = filteredResults.stream()
                        .filter(recipe -> recipe != null && recipe.getName() != null &&
                                recipe.getName().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
            }
            adapter.updateData(filteredResults);
        } catch (Exception e) {
            Log.e("FILTER_ERROR", "Error applying filters", e);
            Toast.makeText(this, "Error applying filters", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}