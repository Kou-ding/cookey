package com.example.cookey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.cookey.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHandler = new DBHandler(this);
        checkAndInsertSampleData();
        setupBottomNavigation();
    }
    private void checkAndInsertSampleData() {
        // Έλεγχος αν υπάρχουν ήδη συνταγές
        dbHandler.dropDatabase();
        if (dbHandler.getAllRecipes().isEmpty()) {
            addSampleRecipes();
        }
    }
    // Στο MainActivity.java, στη μέθοδο addSampleRecipes()

    private void addSampleRecipes() {
        try {
            Log.d("SAMPLE_DATA", "Adding sample recipes...");
            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lunch_dining_24px);

            // 1. Spaghetti Carbonara
            RecipeFull carbonara = new RecipeFull();
            carbonara.setTitle("Spaghetti Carbonara");
            carbonara.setCharacteristic("20 mins | Medium");
            carbonara.addIngredient("Spaghetti");
            carbonara.addIngredient("Eggs");
            carbonara.addIngredient("Pancetta");
            carbonara.addStep("Boil spaghetti");
            carbonara.addStep("Fry pancetta");
            carbonara.addTag("Italian");
            carbonara.addTag("Pasta");
            long id1 = dbHandler.addFullRecipe(carbonara, defaultBitmap);
            dbHandler.updateRecipeFavoriteStatus((int)id1, true);

            // 2. Greek Salad
            RecipeFull salad = new RecipeFull();
            salad.setTitle("Greek Salad");
            salad.setCharacteristic("15 mins | Easy");
            salad.addIngredient("Tomatoes");
            salad.addIngredient("Cucumber");
            salad.addIngredient("Feta Cheese");
            salad.addStep("Chop vegetables");
            salad.addStep("Combine ingredients");
            salad.addTag("Greek");
            salad.addTag("Vegetarian");
            long id2 = dbHandler.addFullRecipe(salad, defaultBitmap);
            dbHandler.updateRecipeFavoriteStatus((int)id2, false);

            Log.d("SAMPLE_DATA", "Added Carbonara with ID: " + id1);
            Log.d("SAMPLE_DATA", "Added Salad with ID: " + id2);

            // Επιβεβαίωση ότι οι συνταγές προστέθηκαν
            List<MyRecipes> allRecipes = dbHandler.getAllRecipes();
            Log.d("SAMPLE_DATA", "Total recipes in DB: " + allRecipes.size());
        } catch (Exception e) {
            Log.e("MainActivity", "Error adding sample recipes", e);
        }
    }
    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_MyRecipes,
                R.id.navigation_MyList,
                R.id.navigation_AI,
                R.id.navigation_MyShoppingList,
                R.id.navigation_Settings
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }
}