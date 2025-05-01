package com.example.cookey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    private ImageView imageViewRecipe, imageViewFlag, imageViewClock;
    private ImageButton btnBack, btnConsume, btnFavorite;
    private TextView textViewRecipeName, textViewTags, textViewCountry, textViewTime;
    private TextView tabIngredients, tabSteps;
    private RecyclerView recyclerViewIngredients, recyclerViewSteps;
    private Button btnEditRecipe;


    ViewIngredientAdapter viewIngredientAdapter;
    private StepAdapter stepAdapter;

    private List<ViewIngredientModel> ingredientsList;

    private TextView textViewServing, textViewDifficulty;
    private List<StepModel> stepsList = new ArrayList<>();

    private DBHandler dbHandler;
    private int recipeID = 2; //ULTRA DUMMY

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_recipe_details);

        //Seeder - Used for testing
      //      RecipeSeeder.seed(this);

        // Find-Init Views
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        imageViewFlag = findViewById(R.id.imageViewFlag);
        imageViewClock = findViewById(R.id.imageViewClock);
        btnBack = findViewById(R.id.btnBack);
        btnConsume = findViewById(R.id.btnConsume);
        btnFavorite = findViewById(R.id.btnFavorite);
        textViewRecipeName = findViewById(R.id.textViewRecipeName);
        textViewTags = findViewById(R.id.textViewTags);
        textViewCountry = findViewById(R.id.textViewCountry);
        textViewTime = findViewById(R.id.textViewTime);
        tabIngredients = findViewById(R.id.tabIngredients);
        tabSteps = findViewById(R.id.tabSteps);
        textViewServing = findViewById(R.id.textViewServing);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        recyclerViewSteps = findViewById(R.id.recyclerViewSteps);
        btnEditRecipe = findViewById(R.id.btnEditRecipe);



        dbHandler = new DBHandler(this);

        //Get recipe from dB
        RecipeModel recipe = dbHandler.getRecipeId(recipeID);

        if (recipe != null) {
            // Image
            if (recipe.getPhoto() != null && recipe.getPhoto().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getPhoto(), 0, recipe.getPhoto().length);
                imageViewRecipe.setImageBitmap(bitmap);
            } else {
                imageViewRecipe.setImageResource(R.drawable.ic_placeholder);
            }

            // Name
            textViewRecipeName.setText(recipe.getName());

            // Tags
            if (recipe.getTags() != null && !recipe.getTags().isEmpty()) {
                textViewTags.setText(String.join(", ", recipe.getTags()));
            } else {
                textViewTags.setText("-");
            }

            // Country
            textViewCountry.setText(recipe.getCountryName());
            Log.d("BRO",recipe.getCountryName());

            // Time (minutes)
            textViewTime.setText(recipe.getTimeToMake() + "'");

            // Ingredients Recycler
            viewIngredientAdapter = new ViewIngredientAdapter(recipe.getIngredients());
            recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewIngredients.setAdapter(viewIngredientAdapter);

            // Steps Recycler
            stepAdapter = new StepAdapter(recipe.getSteps());
            recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewSteps.setAdapter(stepAdapter);

            // Servings (προσθήκη από το RecipeModel - αν δεν υπάρχει, αφαίρεσε αυτή τη γραμμή)
            // textViewServing.setText(recipe.getMealNumber() + " servings");

            // Difficulty
            textViewDifficulty.setText(recipe.getDifficulty());

            // Favorite button state
            btnFavorite.setImageResource(
                    recipe.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.favorite_24px
            );
        } else {
            Toast.makeText(this, "Recipe not found!", Toast.LENGTH_SHORT).show();
            finish(); // Κλείνει την activity αν η συνταγή δεν βρέθηκε
        }

        // Tabs Click
        tabIngredients.setOnClickListener(v -> {
            recyclerViewIngredients.setVisibility(View.VISIBLE);
            recyclerViewSteps.setVisibility(View.GONE);
            highlightSelectedTab(tabIngredients, tabSteps);
        });

        tabSteps.setOnClickListener(v -> {
            recyclerViewIngredients.setVisibility(View.GONE);
            recyclerViewSteps.setVisibility(View.VISIBLE);
            highlightSelectedTab(tabSteps, tabIngredients);
        });

        // Default tab
        tabIngredients.performClick();


        /*
        if(recipe != null){

            //Image First
            if(recipe.getPhoto() != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getPhoto(),0, recipe.getPhoto().length);
                imageViewRecipe.setImageBitmap(bitmap);
            }else{
                imageViewRecipe.setImageResource(R.drawable.ic_placeholder); // if image doesnt exist, fill the photo with a placeholder
            }

            //Name
            textViewRecipeName.setText(recipe.getName());

            //Tags
            if(recipe.getTags() != null && !recipe.getTags().isEmpty()){
                textViewTags.setText(String.join(", ", recipe.getTags()));
            }else{
                textViewTags.setText("-");
            }

            //Country
            textViewCountry.setText(recipe.getCountryName());

            //Time
            textViewTime.setText(recipe.getTimeToMake());

            //Ingredients Recycler
            viewIngredientAdapter = new ViewIngredientAdapter(recipe.getIngredients());
            recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewIngredients.setAdapter(viewIngredientAdapter);

            //Steps Recycler
            stepAdapter = new StepAdapter(recipe.getSteps());
            recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewSteps.setAdapter(stepAdapter);

            //Servings
            textViewServing.setText(recipe.getMealNumber() + " servings");

            //Difficulty
            textViewDifficulty.setText(recipe.getDifficulty());
        }


         */

        /*

        /// Dummy Data - OLD
        textViewRecipeName.setText("Kimchi");
        textViewTags.setText("Vegan, Spicy, Traditional");
        textViewCountry.setText("Korea");
        textViewTime.setText("60'");

        ingredientsList = new ArrayList<>();
        ingredientsList.add(new ViewIngredientModel("Cabbage", "kg", 1));
        ingredientsList.add(new ViewIngredientModel("Salt", "tbsp", 24));
        ingredientsList.add(new ViewIngredientModel("Chili Powder", "tbsp", 65));
        ingredientsList.add(new ViewIngredientModel("Sugar", "tsp", 19));
        ingredientsList.add(new ViewIngredientModel("Cabbage", "kg", 98.6f));
        ingredientsList.add(new ViewIngredientModel("Salt", "tbsp", 21));
        ingredientsList.add(new ViewIngredientModel("Chili Powder", "tbsp", 165));
        ingredientsList.add(new ViewIngredientModel("Sugar", "tsp", 69));

        stepsList.add(new StepModel("Chop the cabbage finely."));
        stepsList.add(new StepModel("Sprinkle salt and massage cabbage."));
        stepsList.add(new StepModel("Mix with chili powder and sugar."));
        stepsList.add(new StepModel("Ferment for 2-3 days at room temperature."));
        stepsList.add(new StepModel("Chop the cabbage finely."));
        stepsList.add(new StepModel("Sprinkle salt and massage cabbage."));
        stepsList.add(new StepModel("Mix with chili powder and sugar."));
        stepsList.add(new StepModel("Ferment for 2-3 days at room temperature."));

        // Set up RecyclerViews
        viewIngredientAdapter = new ViewIngredientAdapter(ingredientsList);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredients.setAdapter(viewIngredientAdapter);

        stepAdapter = new StepAdapter(stepsList);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSteps.setAdapter(stepAdapter);

        // Tabs Click
        tabIngredients.setOnClickListener(v -> {
            recyclerViewIngredients.setVisibility(View.VISIBLE);
            recyclerViewSteps.setVisibility(View.GONE);
            highlightSelectedTab(tabIngredients, tabSteps);
        });

        tabSteps.setOnClickListener(v -> {
            recyclerViewIngredients.setVisibility(View.GONE);
            recyclerViewSteps.setVisibility(View.VISIBLE);
            highlightSelectedTab(tabSteps, tabIngredients);
        });

        // Default tab
        tabIngredients.performClick();
        */

        btnConsume.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Consume?")
                    .setMessage("Are you sure that you want to cosnume this recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(this, "Recipe Consumed!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        final boolean[] isFav = {false}; //temp

        btnFavorite.setOnClickListener(view -> {
            isFav[0] = !isFav[0];

            if(isFav[0]){
                btnFavorite.setImageResource(R.drawable.ic_heart_filled);
            } else{
                btnFavorite.setImageResource(R.drawable.favorite_24px);
            }
        });


        // Back Button
       // btnBack.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewRecipeActivity.this, AddRecipeActivity.class));
            }
        });

        // TODO: btnConsume, btnFavorite, btnEditRecipe
    }

    /** @noinspection deprecation*/
    private void highlightSelectedTab(TextView selectedTab, TextView unselectedTab) {
        selectedTab.setBackgroundColor(getResources().getColor(R.color.teal_700));
        selectedTab.setTextColor(getResources().getColor(android.R.color.white));
        unselectedTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        unselectedTab.setTextColor(getResources().getColor(android.R.color.black));
    }
}
