package com.example.cookey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AIRecipeViewActivity extends AppCompatActivity {
    private void applyTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = prefs.getString("app_theme", "light");
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
        setContentView(R.layout.activity_ai_recipe_view);

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("View AI Recipe");
        }
        // Get the ai recipe id from the intent
        String AIRecipeIdString = getIntent().getStringExtra("AIRecipeId");
        int AIRecipeId = -1; // Default or error value
        if (AIRecipeIdString != null && !AIRecipeIdString.isEmpty()) {
            AIRecipeId = Integer.parseInt(AIRecipeIdString);
        }
        final int finalAIRecipeId = AIRecipeId;
        try(DBHandler db = new DBHandler(this,null,null,1)){
            AIRecipe recipe = db.getAIRecipe(AIRecipeId);
            TextView recipeTextView = findViewById(R.id.recipe_text);
            if (recipe != null) {
                // Display the recipe text
                recipeTextView.setText(recipe.getAIRecipeText());
            } else {
                // Handle the case where recipe is null
                recipeTextView.setText("No recipe found.");
            }
        }
        // Set the AI Recipe Id in the title
        TextView AiRecipeIdTitle = findViewById(R.id.AiRecipeIdTitle);
        AiRecipeIdTitle.setText(AIRecipeIdString);

        ImageButton deleteAIRecipe = findViewById(R.id.deleteAIRecipe);
        deleteAIRecipe.setOnClickListener(v -> {
            // Delete the AI recipe
            try(DBHandler db = new DBHandler(this,null,null,1)){
                db.deleteAIRecipe(finalAIRecipeId);
            }
            finish();
        });

        // TODO: Editable AI recipes. Not a priority since it is more like an index for ai recipes
        ImageButton editAIRecipe = findViewById(R.id.editAIRecipe);
    }
}
