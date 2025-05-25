package com.example.cookey;

import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedLanguage();


        super.onCreate(savedInstanceState);

        // Make Dark theme the default theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

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

        // database
        try (DBHandler dbHandler = new DBHandler(this, null, null, 1)) {
            SQLiteDatabase db = dbHandler.getWritableDatabase(); // this triggers onCreate if DB doesn't exist
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setAppLocale(String languageCode) {
        Log.d("test",languageCode);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void applySavedLanguage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLang = prefs.getString("app_lang", "en"); // default to English
        setAppLocale(savedLang);
    }

}