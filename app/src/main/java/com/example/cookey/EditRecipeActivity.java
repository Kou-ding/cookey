package com.example.cookey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditRecipeActivity extends AppCompatActivity {

    private TextView editTextRecipeName;
    private TextView editTextMealNumber;
    private AutoCompleteTextView autoCompleteDifficulty;
    private ImageView imageViewRecipe;
    private Button btnSelectTime, btnSelectCountry;
    private LinearLayout linearLayoutIngredients, linearLayoutSteps;
    private RecyclerView recyclerViewTags;
    private Button buttonSave, buttonCancel;
    private ImageButton btnChangeImage;

    private RecipeModel recipe;
    private DBHandler dbHandler;
    private int selectedTimeMinutes = 0;
    private long selectedCountryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Initialize views
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextMealNumber = findViewById(R.id.editTextMealNumber);
        autoCompleteDifficulty = findViewById(R.id.autoCompleteDifficulty);
        imageViewRecipe = findViewById(R.id.imageRecipe);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSelectCountry = findViewById(R.id.btnSelectCountry);
        linearLayoutIngredients = findViewById(R.id.linearLayoutIngredients);
        linearLayoutSteps = findViewById(R.id.linearLayoutSteps);
        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        buttonSave = findViewById(R.id.btnConfirm);
        buttonCancel = findViewById(R.id.buttonCancel);


        dbHandler = new DBHandler(this);


        // Set difficulty dropdown
        String[] difficulties = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, difficulties);
        autoCompleteDifficulty.setAdapter(adapter);

        // Get recipeId from Intent
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            recipe = dbHandler.getRecipeId(recipeId);
            if (recipe == null) {
                Toast.makeText(this, "Recipe not found!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Fill UI
            editTextRecipeName.setText(recipe.getName());
            editTextMealNumber.setText(String.valueOf(recipe.getMealNumber()));
            autoCompleteDifficulty.setText(recipe.getDifficulty());
            btnSelectTime.setText("Time: " + recipe.getTimeToMake() + "'");
            selectedTimeMinutes = recipe.getTimeToMake();
            btnSelectCountry.setText(recipe.getCountryName());

            if (recipe.getPhoto() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getPhoto(), 0, recipe.getPhoto().length);
                imageViewRecipe.setImageBitmap(bitmap);
            }

            loadIngredients();
            loadSteps();
            loadTags();
        }

        // Save button click
        buttonSave.setOnClickListener(v -> {
            String name = editTextRecipeName.getText().toString().trim();
            String diff = autoCompleteDifficulty.getText().toString().trim();
            int time = selectedTimeMinutes;
            int servings = Integer.parseInt(editTextMealNumber.getText().toString().trim());

            dbHandler.updateRecipe(recipe.getId(), name, time, recipe.getCountryCode(), servings, diff, null, recipe.isFavorite());

            Toast.makeText(this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        buttonCancel.setOnClickListener(v -> finish());
    }

    private void loadIngredients() {
        List<SelectedIngredient> ingredients = dbHandler.getIngredientsForRecipe(recipe.getId());
        for (SelectedIngredient ing : ingredients) {
            TextView tv = new TextView(this);
            tv.setText(ing.getQuantity() + " " + ing.getUnit() + " " + ing.getName());
            tv.setTextSize(16f);
            linearLayoutIngredients.addView(tv);
        }
    }

    private void loadSteps() {
        List<StepModel> steps = dbHandler.getStepsForRecipe(recipe.getId());
        int i = 1;
        for (StepModel s : steps) {
            TextView tv = new TextView(this);
            tv.setText(String.format("%02d. %s", i++, s.getDescription()));
            tv.setTextSize(16f);
            linearLayoutSteps.addView(tv);
        }
    }

    private void loadTags() {
        List<String> tags = dbHandler.getTagsForRecipe(recipe.getId());
        TagAdapter adapter = new TagAdapter(tags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTags.setAdapter(adapter);
    }
}
