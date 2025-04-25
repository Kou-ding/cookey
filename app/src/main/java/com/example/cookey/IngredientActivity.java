package com.example.cookey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class IngredientActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            // Enable the back button and set the title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ingredient Info");
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
                ingredientExpirationDatesTextView.setText("No expiration dates");
            } else {
                ingredientExpirationDatesTextView.setText(formattedDates.toString());
            }
        } catch (JSONException e) {
            // Handle parsing error
            Log.e("ExpirationDates", "Error parsing JSON: " + e.getMessage());
            ingredientExpirationDatesTextView.setText("Error displaying dates");
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
