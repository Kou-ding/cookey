package com.example.cookey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
    private List<StepModel> stepsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_recipe_details);

        // Find Views
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
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        recyclerViewSteps = findViewById(R.id.recyclerViewSteps);
        btnEditRecipe = findViewById(R.id.btnEditRecipe);

        // Dummy Data
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

        // Back Button
       // btnBack.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewRecipeActivity.this, AddRecipeActivity.class));
            }
        });

        // TODO: Consume and Favorite button clicks
    }

    private void highlightSelectedTab(TextView selectedTab, TextView unselectedTab) {
        selectedTab.setBackgroundColor(getResources().getColor(R.color.teal_700));
        selectedTab.setTextColor(getResources().getColor(android.R.color.white));
        unselectedTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        unselectedTab.setTextColor(getResources().getColor(android.R.color.black));
    }
}
