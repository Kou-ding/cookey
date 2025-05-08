package com.example.cookey; // άλλαξε το αν χρειάζεται

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

public class AddRecipeActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextRecipeName = findViewById(R.id.editTextRecipeName);

        btnConfirm = findViewById(R.id.btnConfirm);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);


        ingredientsLayout = findViewById(R.id.linearLayoutIngredients);
        stepsLayout = findViewById(R.id.linearLayoutSteps);

        /*
        CheckBox cbSpicy = findViewById(R.id.checkboxSpicy);
        CheckBox cbVegetarian = findViewById(R.id.checkboxVegetarian);
        CheckBox cbQuick = findViewById(R.id.checkboxQuick);
        */

        recyclerViewTags = findViewById(R.id.recyclerViewTags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        btnSelectCountry = findViewById(R.id.btnSelectCountry);

        ScrollView scrollView = findViewById(R.id.scrollView);

        //Prevent scrollview from resetting and also in a magical way, it fixed the <<not removing focus>> while user is NOT in editTextFields?"
        scrollView.setOnTouchListener((v, event) -> {
            hideKeyboard();
            return false;
        });

        btnSelectTime = findViewById(R.id.btnSelectTime);

        imageRecipe = findViewById(R.id.imageRecipe);
        ImageButton btnChangeImage = findViewById(R.id.btnChangeImage);

        editTextMealNumber = findViewById(R.id.editTextMealNumber);
        autoCompleteDifficulty = findViewById(R.id.autoCompleteDifficulty);

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, difficultyLevels);
        autoCompleteDifficulty.setAdapter(adapterDifficulty);

        // Disable  Keyboard
        autoCompleteDifficulty.setInputType(0);

        // Open dropdown Menu
        autoCompleteDifficulty.setOnTouchListener((v, event) -> {
            autoCompleteDifficulty.showDropDown();
            return false;
        });


        dbHandler = new DBHandler(this); // Connect with DB

        //Tag connect
        List<String> allTags = dbHandler.getAllTags(); //
        tagsAdapter = new TagAdapter(allTags);
        recyclerViewTags.setAdapter(tagsAdapter);



        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imageRecipe.setImageURI(selectedImageUri);
                    }
                }
        );

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        /*
        cbSpicy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTags.add("Spicy");
            } else {
                selectedTags.remove("Spicy");
            }
        });

        cbVegetarian.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTags.add("Vegetarian");
            } else {
                selectedTags.remove("Vegetarian");
            }
        });

        cbQuick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTags.add("Quick");
            } else {
                selectedTags.remove("Quick");
            }
        });
                 */

        btnSelectCountry.setOnClickListener(v -> {
            CountrySelectDialog dialog = new CountrySelectDialog(AddRecipeActivity.this, country -> {
                btnSelectCountry.setText(country.getName());
                btnSelectCountry.setTag(country);  // save country object
                selectedCountryId = country.getId();
            });
            dialog.show();
        });

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });


        btnConfirm.setOnClickListener(v -> {
            if (validateFields()) {
                saveRecipe();
            }
        });

        btnAddIngredient.setOnClickListener(v -> {
            IngredientSelectDialog dialog = new IngredientSelectDialog(AddRecipeActivity.this, (ingredient, quantity) -> {
                addSelectedIngredient(ingredient, quantity);
            });
            dialog.show();
        });

        Button btnAddStep = findViewById(R.id.btnAddStep);
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStepDialog();
            }
        });

    }

    private String getCountryNameByCode(String code) {
        try {
            InputStream is = getAssets().open("countries.json");
            String json = new Scanner(is).useDelimiter("\\A").next();
            JSONArray countries = new JSONArray(json);

            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                if (country.getString("code").equalsIgnoreCase(code)) {
                    return country.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }


    private void saveRecipe() {

        //Take the input from all fields
        String recipeName = editTextRecipeName.getText().toString().trim();
        int timeToMake = selectedTimeMinutes;
        String countryCode = ((CountryModel) btnSelectCountry.getTag()).getCode();
        int mealNumber = 0;
        String difficulty = autoCompleteDifficulty.getText().toString().trim();
        boolean isFavorite = false;
        byte[] photoBytes = null;

        //Servings
        String servingsText = editTextMealNumber.getText().toString().trim();
        if(!servingsText.isEmpty()){
            mealNumber =Integer.parseInt(servingsText);
        }

        //image handling
        if(selectedImageUri != null ){
            try{
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                photoBytes = new byte[inputStream.available()];
                inputStream.read(photoBytes);
                inputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        DBHandler dbHandler = new DBHandler(this);
        long recipeId = dbHandler.addRecipe(
                recipeName,
                timeToMake,
                countryCode,
                mealNumber,
                difficulty,
                photoBytes,
                isFavorite
        );


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
            dbHandler.addStep(step.getDescription(), recipeId, stepCounter);
            stepCounter++;
        }

        for (String tag : tagsAdapter.getSelectedTags()) {
            dbHandler.addTagToRecipe(recipeId, tag);
        }

        Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
   //     finish(); // Close AddRecipe Activity
        Log.d("Got added OK","recipe added");
    }



    private void addSelectedStep(String description) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setPadding(8, 8, 8, 8);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView textViewNumber = new TextView(this);
        textViewNumber.setText(String.format("%02d.", selectedSteps.size() + 1));
        textViewNumber.setTextSize(16f);
        textViewNumber.setPadding(0, 0, 8, 0);
        textViewNumber.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView textViewStep = new TextView(this);
        textViewStep.setText(description);
        textViewStep.setTextSize(16f);
        textViewStep.setTypeface(null, Typeface.BOLD);
        textViewStep.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));
        textViewStep.setSingleLine(false);
        textViewStep.setMaxLines(Integer.MAX_VALUE);

        ImageButton btnRemove = new ImageButton(this);
        btnRemove.setId(View.generateViewId());
        btnRemove.setImageResource(R.drawable.delete_24px); // Delete icon
        btnRemove.setBackground(null);
        btnRemove.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        btnRemove.setPadding(8, 8, 8, 8);

        ConstraintLayout.LayoutParams paramsBtn = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        btnRemove.setLayoutParams(paramsBtn);


        container.addView(textViewNumber);
        container.addView(textViewStep);
        container.addView(btnRemove);

        stepsLayout.addView(container);

        StepModel step = new StepModel(description);
        selectedSteps.add(step);

        btnRemove.setOnClickListener(v -> {
            stepsLayout.removeView(container);
            selectedSteps.remove(step);
            refreshStepNumbers();
        });
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
                selectedTimeMinutes = minutes; //Save time
                timeDialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter the time in minutes", Toast.LENGTH_SHORT).show();
            }
        });

        timeDialog.show();
    }


    private void addSelectedIngredient(IngredientModel ingredient, double quantity) {
        // Create a TextView thats shows quantity + name + unity
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setPadding(8, 8, 8, 8);

        TextView textView = new TextView(this);
        textView.setText(quantity + " " + ingredient.getUnit() + " " + ingredient.getName());
        textView.setTextSize(16f);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnRemove = new Button(this);
        btnRemove.setText("X");
        btnRemove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        container.addView(textView);
        container.addView(btnRemove);

        ingredientsLayout.addView(container);

        // Add to list
        SelectedIngredient selected = new SelectedIngredient(ingredient, quantity);
        selectedIngredients.add(selected);

        // Delete ingredient
        btnRemove.setOnClickListener(v -> {
            ingredientsLayout.removeView(container);
            selectedIngredients.remove(selected);
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    //Remove focus when not in editText
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    private boolean validateFields() {
        String recipeName = editTextRecipeName.getText().toString().trim();

        //Recipe Name check
        if (recipeName.isEmpty()) {
            Toast.makeText(this, "Please enter the recipe name", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Ingredients check
        if (selectedIngredients.isEmpty()) {
            Toast.makeText(this, "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Steps check
        if (selectedSteps.isEmpty()) {
            Toast.makeText(this, "Please add at least one step", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Country check
        if (selectedCountryId == -1) {
            Toast.makeText(this, "Please select a country", Toast.LENGTH_SHORT).show();
            return false;
        }

        //TimetoMake check
        if (selectedTimeMinutes <= 0) {
            Toast.makeText(this, "Please enter cooking time", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Meal Number check
        String servingsText = editTextMealNumber.getText().toString().trim();
        if (servingsText.isEmpty()) {
            Toast.makeText(this, "Please enter the number of servings", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Difficulty check
        String difficultyText = autoCompleteDifficulty.getText().toString().trim();
        if (difficultyText.isEmpty()) {
            Toast.makeText(this, "Please select a difficulty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

