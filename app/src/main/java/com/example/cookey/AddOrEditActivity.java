package com.example.cookey;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddOrEditActivity extends AppCompatActivity {

    private boolean isEditMode = false;
    private long editRecipeId = -1;
    private RecipeModel existingRecipe;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private ImageView imageRecipe;

    private Button btnConfirm;
    private TextInputEditText editTextRecipeName;
    private LinearLayout ingredientsLayout, stepsLayout;
    private DBHandler dbHandler;

    private List<SelectedIngredient> selectedIngredients = new ArrayList<>();
    private List<StepModel> selectedSteps = new ArrayList<>();
    private List<String> selectedTags = new ArrayList<>();
    private long selectedCountryId = -1;
    private int selectedTimeMinutes = 0;

    private Button btnSelectTime;
    private Button btnSelectCountry;
    private TextInputEditText editTextMealNumber;
    private AutoCompleteTextView autoCompleteDifficulty;
    private RecyclerView recyclerViewTags;
    private TagAdapter tagsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Toast.makeText(this, "AddOrEditActivity opened", Toast.LENGTH_SHORT).show();

        //Check if editing
        if (getIntent().hasExtra("RECIPE_ID")) {
            isEditMode = true;
            editRecipeId = getIntent().getLongExtra("RECIPE_ID", -1); //
            Toast.makeText(this, "RECIPE_ID received: " + editRecipeId, Toast.LENGTH_SHORT).show();

        }

        initViews();
        setupDifficultyDropdown();
        dbHandler = new DBHandler(this);

        setupTagAdapter();

        if(isEditMode && editRecipeId != -1){
            existingRecipe = dbHandler.getRecipeId(editRecipeId);

            if (existingRecipe == null) {
                Toast.makeText(this, "Recipe not found in database", Toast.LENGTH_SHORT).show();
            //    finish();
                return;
            }


            populateFieldsWithExistingData();
        }

        setupImagePicker();
        setupButtons();
    }

    private void initViews() {
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        btnConfirm = findViewById(R.id.btnConfirm);
        ingredientsLayout = findViewById(R.id.linearLayoutIngredients);
        stepsLayout = findViewById(R.id.linearLayoutSteps);
        btnSelectCountry = findViewById(R.id.btnSelectCountry);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        imageRecipe = findViewById(R.id.imageRecipe);
        ImageButton btnChangeImage = findViewById(R.id.btnChangeImage);
        editTextMealNumber = findViewById(R.id.editTextMealNumber);
        autoCompleteDifficulty = findViewById(R.id.autoCompleteDifficulty);
        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDifficultyDropdown() {
        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, difficultyLevels);
        autoCompleteDifficulty.setAdapter(adapterDifficulty);
        autoCompleteDifficulty.setInputType(0);
        autoCompleteDifficulty.setOnTouchListener((v, event) -> {
            autoCompleteDifficulty.showDropDown();
            return false;
        });
    }

    private void setupTagAdapter() {
        List<String> allTags = dbHandler.getAllTags();
        tagsAdapter = new TagAdapter(allTags);
        recyclerViewTags.setAdapter(tagsAdapter);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imageRecipe.setImageURI(selectedImageUri);
                    }
                }
        );

        findViewById(R.id.btnChangeImage).setOnClickListener(v -> openImageChooser());
    }

    private void setupButtons() {
        btnConfirm.setOnClickListener(v -> {
            if (validateFields()) {
                if (isEditMode) updateRecipe();
                else saveNewRecipe();
            }
        });

        findViewById(R.id.btnAddIngredient).setOnClickListener(v -> {
            IngredientSelectDialog dialog = new IngredientSelectDialog(this, (ingredient, quantity) -> {
                addSelectedIngredient(ingredient, quantity);
            });
            dialog.show();
        });

        findViewById(R.id.btnAddStep).setOnClickListener(v -> showAddStepDialog());

        btnSelectCountry.setOnClickListener(v -> {
            CountrySelectDialog dialog = new CountrySelectDialog(this, country -> {
                btnSelectCountry.setText(country.getName());
                btnSelectCountry.setTag(country);
                selectedCountryId = country.getId();
            });
            dialog.show();
        });

        btnSelectTime.setOnClickListener(v -> showTimeDialog());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private boolean validateFields() {
        String recipeName = editTextRecipeName.getText().toString().trim();
        String servingsText = editTextMealNumber.getText().toString().trim();
        String difficulty = autoCompleteDifficulty.getText().toString().trim();

        if (recipeName.isEmpty()) {
            Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (servingsText.isEmpty()) {
            Toast.makeText(this, "Please enter the number of servings", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (difficulty.isEmpty()) {
            Toast.makeText(this, "Please select difficulty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTimeMinutes <= 0) {
            Toast.makeText(this, "Please enter the preparation time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedIngredients.isEmpty()) {
            Toast.makeText(this, "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedSteps.isEmpty()) {
            Toast.makeText(this, "Please add at least one step", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveNewRecipe() {
        String recipeName = editTextRecipeName.getText().toString().trim();
        int timeToMake = selectedTimeMinutes;
        String difficulty = autoCompleteDifficulty.getText().toString().trim();
        int mealNumber = 0;
        byte[] photoBytes = null;

        String servingsText = editTextMealNumber.getText().toString().trim();
        if (!servingsText.isEmpty()) {
            mealNumber = Integer.parseInt(servingsText);
        }

        CountryModel country = (CountryModel) btnSelectCountry.getTag();
        String countryCode = (country != null) ? country.getCode() : "XX";

        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                photoBytes = new byte[inputStream.available()];
                inputStream.read(photoBytes);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long recipeId = dbHandler.addRecipe(recipeName, timeToMake, countryCode, mealNumber, difficulty, photoBytes, false);

        for (SelectedIngredient sel : selectedIngredients) {
            dbHandler.addIngredientToRecipeIngredients(
                    recipeId,
                    sel.getName(),
                    sel.getQuantity(),
                    sel.getUnit()
            );
        }

        int stepCounter = 1;
        for (StepModel step : selectedSteps) {
            dbHandler.addStep(step.getDescription(), recipeId, stepCounter++);
        }

        for (String tag : tagsAdapter.getSelectedTags()) {
            dbHandler.addTagToRecipe(recipeId, tag);
        }

        Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateRecipe() {
        String recipeName = editTextRecipeName.getText().toString().trim();
        int timeToMake = selectedTimeMinutes;
        String difficulty = autoCompleteDifficulty.getText().toString().trim();
        int mealNumber = 0;
        byte[] photoBytes = null;

        // Servings
        String servingsText = editTextMealNumber.getText().toString().trim();
        if (!servingsText.isEmpty()) {
            mealNumber = Integer.parseInt(servingsText);
        }

        // Country code
        CountryModel country = (CountryModel) btnSelectCountry.getTag();
        String countryCode = (country != null) ? country.getCode() : "XX";

        // Image
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                photoBytes = new byte[inputStream.available()];
                inputStream.read(photoBytes);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // if there is no image, keep old img
            photoBytes = existingRecipe.getPhoto();
        }

        // Update Recipe
        dbHandler.updateRecipe(existingRecipe.getId(), recipeName, timeToMake, countryCode, mealNumber, difficulty, photoBytes, existingRecipe.isFavorite());

        // Delete previous data
        dbHandler.clearIngredientsOfRecipe(existingRecipe.getId());
        dbHandler.clearStepsOfRecipe(existingRecipe.getId());
        dbHandler.clearTagsOfRecipe(existingRecipe.getId());

        // Insert new ingredients
        for (SelectedIngredient sel : selectedIngredients) {
            dbHandler.addIngredientToRecipeIngredients(
                    existingRecipe.getId(),
                    sel.getName(),
                    sel.getQuantity(),
                    sel.getUnit()
            );
        }

        // Insert new steps
        int stepCounter = 1;
        for (StepModel step : selectedSteps) {
            dbHandler.addStep(step.getDescription(), existingRecipe.getId(), stepCounter++);
        }

        // Insert new Tags
        for (String tag : tagsAdapter.getSelectedTags()) {
            dbHandler.addTagToRecipe(existingRecipe.getId(), tag);
        }

        Toast.makeText(this, "Recipe updated successfully!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void populateFieldsWithExistingData() {
        if (existingRecipe == null) return;

        editTextRecipeName.setText(existingRecipe.getName());
        editTextMealNumber.setText(String.valueOf(existingRecipe.getMealNumber()));
        autoCompleteDifficulty.setText(existingRecipe.getDifficulty(),false);

        btnSelectTime.setText("Time: " + existingRecipe.getTimeToMake() + "'");
        selectedTimeMinutes = existingRecipe.getTimeToMake();

        CountryModel country = new CountryModel(
                existingRecipe.getId(),
                existingRecipe.getCountryName(),
                existingRecipe.getCountryCode(),
                0
        );

        btnSelectCountry.setText(country.getName());
        btnSelectCountry.setTag(country);
        selectedCountryId = country.getId();

        if (existingRecipe.getPhoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(existingRecipe.getPhoto(), 0, existingRecipe.getPhoto().length);
            imageRecipe.setImageBitmap(bitmap);
        }

        for (SelectedIngredient ing : dbHandler.getIngredientsForRecipe(existingRecipe.getId())) {
            addSelectedIngredient(ing.getIngredient(), ing.getQuantity());
        }

        for (StepModel step : dbHandler.getStepsForRecipe(existingRecipe.getId())) {
            addSelectedStep(step.getDescription());
        }

        List<String> tags = dbHandler.getTagsForRecipe(existingRecipe.getId());
        tagsAdapter.setSelectedTags(tags);


    }

    private void addSelectedStep(String description) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setPadding(8, 8, 8, 8);
        container.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView textViewNumber = new TextView(this);
        textViewNumber.setText(String.format("%02d.", selectedSteps.size() + 1));
        textViewNumber.setTextSize(16f);
        textViewNumber.setPadding(0, 0, 8, 0);

        TextView textViewStep = new TextView(this);
        textViewStep.setText(description);
        textViewStep.setTextSize(16f);
        textViewStep.setTypeface(null, android.graphics.Typeface.BOLD);
        textViewStep.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        textViewStep.setMaxLines(Integer.MAX_VALUE);

        ImageButton btnRemove = new ImageButton(this);
        btnRemove.setImageResource(R.drawable.delete_24px);
        btnRemove.setBackground(null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
        params.setMargins(16, 0, 0, 0);
        btnRemove.setLayoutParams(params);

        StepModel step = new StepModel(description);
        selectedSteps.add(step);

        btnRemove.setOnClickListener(v -> {
            stepsLayout.removeView(container);
            selectedSteps.remove(step);
            refreshStepNumbers();
        });

        container.addView(textViewNumber);
        container.addView(textViewStep);
        container.addView(btnRemove);

        stepsLayout.addView(container);

    }

    private void refreshStepNumbers() {
        int count = 1;
        for (int i = 0; i < stepsLayout.getChildCount(); i++) {
            View view = stepsLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout container = (LinearLayout) view;
                if (container.getChildCount() >= 2 && container.getChildAt(0) instanceof TextView) {
                    TextView numberView = (TextView) container.getChildAt(0);
                    numberView.setText(String.format("%02d.", count));
                    count++;
                }
            }
        }
    }

    private void addSelectedIngredient(IngredientModel ingredient, double quantity) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setPadding(8, 8, 8, 8);
        container.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView textView = new TextView(this);
        textView.setText(quantity + " " + ingredient.getUnit() + " " + ingredient.getName());
        textView.setTextSize(16f);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        ImageButton btnRemove = new ImageButton(this);
        btnRemove.setImageResource(R.drawable.delete_24px);
        btnRemove.setBackground(null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
        params.setMargins(16, 0, 0, 0);
        btnRemove.setLayoutParams(params);

        btnRemove.setOnClickListener(v -> {
            ingredientsLayout.removeView(container);
            selectedIngredients.removeIf(i -> i.getName().equals(ingredient.getName()));
        });

        container.addView(textView);
        container.addView(btnRemove);
        ingredientsLayout.addView(container);

        selectedIngredients.add(new SelectedIngredient(ingredient, quantity));
    }

    private void showAddStepDialog() {
        Dialog stepDialog = new Dialog(this);
        stepDialog.setContentView(R.layout.dialog_enter_step);
        EditText editTextStep = stepDialog.findViewById(R.id.editTextStep);
        Button buttonConfirmStep = stepDialog.findViewById(R.id.buttonConfirmStep);

        buttonConfirmStep.setOnClickListener(v -> {
            String stepText = editTextStep.getText().toString().trim();
            if (!stepText.isEmpty()) {
                addSelectedStep(stepText);
                stepDialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter the step description", Toast.LENGTH_SHORT).show();
            }
        });

        stepDialog.show();
    }

    private void showTimeDialog() {
        Dialog timeDialog = new Dialog(this);
        timeDialog.setContentView(R.layout.dialog_select_time);

        EditText editTextTime = timeDialog.findViewById(R.id.editTextTime);
        Button buttonConfirmTime = timeDialog.findViewById(R.id.buttonConfirmTime);

        buttonConfirmTime.setOnClickListener(v -> {
            String timeText = editTextTime.getText().toString().trim();
            if (!timeText.isEmpty()) {
                int minutes = Integer.parseInt(timeText);
                btnSelectTime.setText("Time: " + minutes + "'");
                selectedTimeMinutes = minutes;
                timeDialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter the time in minutes", Toast.LENGTH_SHORT).show();
            }
        });

        timeDialog.show();
    }

}
