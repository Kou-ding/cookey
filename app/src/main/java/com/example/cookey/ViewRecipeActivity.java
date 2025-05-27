package com.example.cookey;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

public class ViewRecipeActivity extends AppCompatActivity {

    private ImageView imageViewRecipe;
    private ImageButton btnBack, btnConsume, btnFavorite;
    private TextView textViewRecipeName, textViewTags, textViewCountry, textViewTime;
    private TextView tabIngredients, tabSteps;
    private RecyclerView recyclerViewIngredients, recyclerViewSteps;
    private Button btnEditRecipe;


    ViewIngredientAdapter viewIngredientAdapter;
    private StepAdapter stepAdapter;


    private TextView textViewServing, textViewDifficulty;

    private DBHandler dbHandler;
    private long recipeID = -1;

    private ActivityResultLauncher<Intent> editRecipeLauncher;

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
    protected void onCreate(Bundle savedInstance){
        applyTheme();
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_recipe_details);

        recipeID = getIntent().getLongExtra("RECIPE_ID", -1);

        if (recipeID == -1) {
            Toast.makeText(this, R.string.recipe_not_found_text, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Find-Init Views
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
      //  btnBack = findViewById(R.id.btnBack);
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


        ImageButton btnDelete = findViewById(R.id.btnDelete);


        dbHandler = new DBHandler(this, null, null, 1);


        editRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        recreate(); // Reload ViewRecipeActivity for real time update
                    }
                });

        //Get recipe from dB
        RecipeModel recipe = dbHandler.getRecipeId(recipeID);

        if (recipe != null) {
            // Image
            String photoPath = recipe.getPhotoPath();
            if (photoPath != null && !photoPath.isEmpty()) {
                File imgFile = new File(photoPath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    imageViewRecipe.setImageBitmap(bitmap);
                }
            } else {
                imageViewRecipe.setImageResource(R.drawable.placeholder_view_recipe);
            }

            // Name. If it cannot get a name, write unnamed. It was a bug that i cannot reproduce
            textViewRecipeName.setText((recipe.getName() != null && !recipe.getName().isEmpty()) ? recipe.getName() : getString(R.string.unnamed_recipe_text));


            // Tags
            if (recipe.getTags() != null && !recipe.getTags().isEmpty()) {
                textViewTags.setText(String.join(", ", recipe.getTags()));
            } else {
                textViewTags.setText("-");
            }

            // Country
            textViewCountry.setText(recipe.getCountryName());

            // Added OnClick to display full country name if it is too big
            textViewCountry.setOnClickListener(v -> Toast.makeText(this, recipe.getCountryName(), Toast.LENGTH_SHORT).show());
            Log.d("CountryCheck!",recipe.getCountryName());

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

            // Servings
             textViewServing.setText(recipe.getMealNumber() + "");

            // Difficulty
            textViewDifficulty.setText(recipe.getDifficulty());

            // Favorite button state
            btnFavorite.setImageResource(
                    recipe.isFavorite() ? R.drawable.favorite_red_filled_24px : R.drawable.favorite_24px
            );
        } else {
            Toast.makeText(this, R.string.recipe_not_found_text, Toast.LENGTH_SHORT).show();
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


        btnConsume.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.consume_question_mark)
                    .setMessage(R.string.consume_recipe_warning_msg)
                    .setPositiveButton(R.string.consume_recipe_yes_msg, (dialog, which) -> {
                        dbHandler.consumeIngredients(recipeID);
                        Toast.makeText(this, R.string.recipe_consumed_msg,
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.consume_recipe_no_msg, null)
                    .show();
        });

        btnFavorite.setOnClickListener(view -> {
            assert recipe != null;
            boolean newFavStatus = !recipe.isFavorite(); // toggle
            dbHandler.setFavorite(recipeID, newFavStatus); // update dB

            recipe.setFavorite(newFavStatus); // update obj
            btnFavorite.setImageResource(
                    newFavStatus ? R.drawable.favorite_red_filled_24px : R.drawable.favorite_24px
            );
        });

        btnDelete.setOnClickListener(view ->{
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.delete_recipe_title)        // π.χ.  «Διαγραφή»
                    .setMessage(R.string.delete_recipe_message)    // «Σίγουρα;»
                    .setPositiveButton(R.string.consume_recipe_yes_msg, (d, w) -> deleteRecipe())
                    .setNegativeButton(R.string.consume_recipe_no_msg, null)
                    .show();
        });


        // Back Button
       // btnBack.setOnClickListener(v -> finish());

        /*
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewRecipeActivity.this, MainActivity.class));
            }
        });

         */


        // TODO: btnConsume

        btnEditRecipe.setOnClickListener(v -> {
            // Get recipeId and send it to AddOrEditActivity
            Intent intent = new Intent(ViewRecipeActivity.this, AddOrEditActivity.class);
            intent.putExtra("RECIPE_ID", recipeID); // Add recipeId to Intent
            editRecipeLauncher.launch(intent); // Start AddOrEditActivity
            //instead of start(intent) because we want real time update after editing a recipe
        });
    }

    /** @noinspection deprecation*/
    private void highlightSelectedTab(TextView selected, TextView unselected) {
        // Ask the running theme for its MD3 container / on-container colours
        @ColorInt int bgSelected  = MaterialColors.getColor(
                selected,           // any view whose context has the theme
                com.google.android.material.R.attr.colorPrimaryContainer);
        String selectedColor = "#FFFFFF";
        @ColorInt int txtSelected = Color.parseColor(selectedColor);

        @ColorInt int txtUnselect = MaterialColors.getColor(
                unselected,
                com.google.android.material.R.attr.colorOnSurface);

        selected.setBackgroundColor(bgSelected);
        selected.setTextColor(txtSelected);

        unselected.setBackgroundColor(Color.TRANSPARENT);
        unselected.setTextColor(txtUnselect);
    }

    private void deleteRecipe() {
        dbHandler.deleteRecipe(recipeID);
        Toast.makeText(this, R.string.recipe_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }
}
