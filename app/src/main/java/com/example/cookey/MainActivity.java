package com.example.cookey;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.cookey.databinding.ActivityMainBinding;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before super.onCreate
        applyTheme();
        super.onCreate(savedInstanceState);

        // Set up the binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Bottom Navigation
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration= new AppBarConfiguration.Builder(
                R.id.navigation_MyRecipes,
                R.id.navigation_MyIngredients,
                R.id.navigation_AI,
                R.id.navigation_MyShoppingList,
                R.id.navigation_Settings).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        // Database
        try (DBHandler dbHandler = new DBHandler(this, null, null, 1)) {
            SQLiteDatabase db = dbHandler.getWritableDatabase(); // this triggers onCreate if DB doesn't exist
        }
    }

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
    public void onResume() {
        super.onResume();
    }
}