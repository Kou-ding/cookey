package com.example.cookey;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cookey.R;

public class RecipeDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Λήψη του recipe_id από το Intent
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        // Κώδικας για εμφάνιση λεπτομερειών της συνταγής
    }
}