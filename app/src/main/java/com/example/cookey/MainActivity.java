package com.example.cookey;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.cookey.databinding.ActivityMainBinding;
import android.database.sqlite.SQLiteDatabase;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        AppBarConfiguration appBarConfiguration= new AppBarConfiguration.Builder(R.id.navigation_MyRecipes,R.id.navigation_MyIngredients,R.id.navigation_AI,R.id.navigation_MyShoppingList,R.id.navigation_Settings).build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        // database
        DBHandler dbHandler = new DBHandler(this); // 'this' is the Context
        SQLiteDatabase db = dbHandler.getWritableDatabase(); // this triggers onCreate if DB doesn't exist

    }
}