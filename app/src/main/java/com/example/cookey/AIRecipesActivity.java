package com.example.cookey;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AIRecipesActivity extends AppCompatActivity {
    List<AIRecipe> ai_recipes;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter<AIRecipesAdapter.ViewHolder> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_recipes);
        recyclerView = findViewById(R.id.ai_recipe_recycler);

        //Set the layout of the items in the RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try (DBHandler db = new DBHandler(this, null, null, 1)) {
            ai_recipes = db.getAllAIRecipes();
        }
        //Set my Adapter for the RecyclerView
        adapter = new AIRecipesAdapter(ai_recipes);
        recyclerView.setAdapter(adapter);
    }

}
