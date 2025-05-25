package com.example.cookey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AIRecipeEditActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_ai_recipe_edit);
        TextInputEditText recipeInput = findViewById(R.id.ai_recipe_text_input);
        recipeInput.setText(getIntent().getStringExtra("AIResponse"));

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit AI Recipe");
        }

        Button saveButton = findViewById(R.id.save_ai_recipe);
        saveButton.setOnClickListener(v -> {
            // Create a new AIRecipe object to store the data
            AIRecipe recipe = new AIRecipe();

            // Fetch the recipe from the EditText field
            String AiRecipeText = Objects.requireNonNull(recipeInput.getText()).toString().trim();

            // Save the recipe to the database
            int AiRecipeId;
            try (DBHandler db = new DBHandler(this, null, null, 1)) {
                AiRecipeId = db.getNextUnusedAIRecipeId();
                recipe.setAIRecipeId(AiRecipeId);
                recipe.setAIRecipeText(AiRecipeText);
                db.registerAIRecipe(recipe);
            }

            // Handle save button click here
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        Button discardButton = findViewById(R.id.discard_button);
        discardButton.setOnClickListener(v -> {
            // Handle discard button click here
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu here (if you have one, otherwise leave empty)
        // getMenuInflater().inflate(R.menu.your_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item selection here
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button press
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
