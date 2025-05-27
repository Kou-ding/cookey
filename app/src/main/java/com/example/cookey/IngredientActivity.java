package com.example.cookey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONArray;
import org.json.JSONException;

public class IngredientActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            // Enable the back button and set the title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ingredient_info);
        }
        // Get the ingredient name from the intent
        String ingredientName = getIntent().getStringExtra("ingredientName");

        // Initialize the ingredient
        Ingredient ingredient;
        try(DBHandler db = new DBHandler(this,null,null,1)){
            ingredient = db.getIngredient(ingredientName);
        }

        // Display the ingredient id in the UI
        TextView ingredientIdTextView = findViewById(R.id.input_id);
        ingredientIdTextView.setText(String.valueOf(ingredient.getIngredientId()));
        // Display the ingredient name in the UI
        TextView ingredientNameTextView = findViewById(R.id.input_name);
        ingredientNameTextView.setText(ingredient.getIngredientName());
        // Display the ingredient quantity in the UI
        TextView ingredientQuantityTextView = findViewById(R.id.input_quantity);
        ingredientQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        // Display the ingredient unit system in the UI
        TextView ingredientUnitSystemTextView = findViewById(R.id.input_unitSystem);
        ingredientUnitSystemTextView.setText(ingredient.getUnitSystem());
        // Display the ingredient expiration dates in the UI
        TextView ingredientExpirationDatesTextView = findViewById(R.id.input_exp);
        String expirationDates = ingredient.getCheckIfSpoiledArray();
        try {
            // Parse the JSON array
            JSONArray datesArray = new JSONArray(expirationDates);

            // Format the dates for display with bullet points
            StringBuilder formattedDates = new StringBuilder();
            for (int i = 0; i < datesArray.length(); i++) {
                String date = datesArray.getString(i);
                formattedDates.append("• ").append(date); // Add bullet point before each date
                if (i < datesArray.length() - 1) {
                    formattedDates.append("\n"); // Add newline between dates
                }
            }

            // If no dates, show a message
            if (datesArray.length() == 0) {
                ingredientExpirationDatesTextView.setText(R.string.no_expiration_dates);
            } else {
                ingredientExpirationDatesTextView.setText(formattedDates.toString());
            }
        } catch (JSONException e) {
            // Handle parsing error
            Log.e("ExpirationDates", "Error parsing JSON: " + e.getMessage());
            ingredientExpirationDatesTextView.setText(R.string.error_displaying_dates);
        }

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
