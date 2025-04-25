package com.example.cookey;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
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
//        Ingredient ingredient = new Ingredient(12,"Mushrooms",12,"kg",12,"11/3/2021");
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
        ingredientExpirationDatesTextView.setText(ingredientName);
    }
}
