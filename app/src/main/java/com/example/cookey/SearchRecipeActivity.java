package com.example.cookey;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.ui.MyRecipes.MyRecipesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipeActivity extends AppCompatActivity {
    private EditText searchInput;
    private Button searchButton;
    private Spinner filterSpinner;
    private Button applyFiltersButton;
    private RecyclerView resultsRecyclerView;
    private MyRecipesAdapter adapter;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        // Initialize views
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        filterSpinner = findViewById(R.id.filterSpinner);
        applyFiltersButton = findViewById(R.id.applyFiltersButton);
        resultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        // Setup RecyclerView
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecipesAdapter(new ArrayList<>(),null); // Use your existing adapter
        resultsRecyclerView.setAdapter(adapter);

        // Initialize database handler
        dbHandler = new DBHandler(this);

        // Setup filter spinner
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_options,
                android.R.layout.simple_spinner_item
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        // Set click listeners
        searchButton.setOnClickListener(v -> performSearch());
        applyFiltersButton.setOnClickListener(v -> applyFilters());

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        String selectedFilter = filterSpinner.getSelectedItem().toString();
        List<MyRecipes> results;
        if (selectedFilter.equals("All")) {
            results = dbHandler.searchRecipes(query); // Απλή αναζήτηση
        } else {
            // Συνδυασμός αναζήτησης και φίλτρου
            results = dbHandler.searchRecipesWithFilter(query, selectedFilter);
        }
        adapter.updateData(results);
    }

    private void applyFilters() {
        String selectedFilter = filterSpinner.getSelectedItem().toString();
        List<MyRecipes> filteredResults = new ArrayList<>();
        try {
            switch (selectedFilter) {
                case "Favorites only":
                    filteredResults = dbHandler.getFavoriteRecipes();
                    Log.d("FILTER_DEBUG", "Favorites count: " + filteredResults.size());
                    break;
                case "Easy":
                    filteredResults = dbHandler.getRecipesByDifficulty("Easy");
                    break;
                case "Medium":
                    filteredResults = dbHandler.getRecipesByDifficulty("Medium");
                    break;
                case "Hard":
                    filteredResults = dbHandler.getRecipesByDifficulty("Hard");
                    break;
                default:
                    filteredResults = dbHandler.getAllRecipes();
            }
            if (filteredResults.isEmpty()) {
                Toast.makeText(this, "No recipes found", Toast.LENGTH_SHORT).show();
            }
            adapter.updateData(filteredResults);
        } catch (Exception e) {
            Log.e("FILTER_ERROR", "Filter crash: " + e.getMessage());
            Toast.makeText(this, "Filter error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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