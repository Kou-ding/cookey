package com.example.cookey;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AIRecipeViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recipe_view);

        // Enable the back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("View AI Recipe");
        }

        AIRecipe recipe = new AIRecipe(1, "\n" +
                "\n" +
                "    3 tablespoons olive oil, or as needed\n" +
                "\n" +
                "    3 large garlic cloves, minced\n" +
                "\n" +
                "    1 pound mild Italian sausage\n" +
                "\n" +
                "    5 fluid ounces white wine, such as Sauvignon Blanc or Pinot Grigio\n" +
                "\n" +
                "    5 ounces heavy whipping cream\n" +
                "\n" +
                "    8 ounces penne or rigatoni pasta\n" +
                "\n" +
                "    salt and freshly ground black pepper to taste\n" +
                "\n" +
                "    1/4 cup grated Parmesan cheese, divided\n" +
                "\n" +
                "    fresh parsley, to taste\n" +
                "\n" +
                "Directions\n" +
                "\n" +
                "    Bring a large pot salted water to a boil and cook pasta until tender with a bite, 9 to 11 minutes. Reserve 1 cup pasta water, then drain pasta.\n" +
                "\n" +
                "    Meanwhile, in a skillet over medium heat, add olive oil and then sausage. Cook sausage, breaking it up with a spatula until it starts to brown, about 7 minutes.\n" +
                "\n" +
                "    Add garlic and a little more olive oil if skillet seems dry. Pour in wine, and use a spoon to scrape up any browned bits on the bottom of the skillet.\n" +
                "\n" +
                "    Simmer until wine has reduced by about half; add pasta water and half the Parmesan. Stir, then add cream. Stir again, season with salt and pepper. Let it cook for a minute, then stir in pasta. Check for seasoning, then add in remaining Parmesan. Stir well and serve sprinkled with parsley. ");

        TextView recipeTextView = findViewById(R.id.recipe_text);
        if (recipe != null) {
            // Display the recipe text
            recipeTextView.setText(recipe.getAIRecipeText());
        } else {
            // Handle the case where recipe is null
            recipeTextView.setText("No recipe found.");
        }
    }
}
