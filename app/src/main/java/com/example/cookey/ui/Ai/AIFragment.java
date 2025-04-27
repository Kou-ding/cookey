package com.example.cookey.ui.Ai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.cookey.AIRecipeEditActivity;
import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AIFragment extends Fragment {

    private AIAdapter adapter;

    private boolean useIngredientsMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout just once
        View view = inflater.inflate(R.layout.fragment_ai, container, false);


        Button cookButton = view.findViewById(R.id.generate_ai_recipe);
        MaterialButton expandButton = view.findViewById(R.id.expandButton);
        RecyclerView recyclerView = view.findViewById(R.id.includeIngredientsRecyclerView);
        Button addIngredientButton = view.findViewById(R.id.addIngredientAI);
        CheckBox useMyIngredientsCheckbox = view.findViewById(R.id.useMyIngredients);
        addIngredientButton.setVisibility(view.GONE);
        useMyIngredientsCheckbox.setVisibility(view.GONE);

        // Recycler View
        adapter = new AIAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Listener for the cook button
        cookButton.setOnClickListener(v -> {
            // Make a recipe based on my ingredients
            if(useMyIngredientsCheckbox.isChecked()){
                String AIResponse = generateAIRecipe(true, null);
                // Use intent to pass the AI response to the next activity
                Intent intent = new Intent(requireActivity(), AIRecipeEditActivity.class);
                intent.putExtra("AIResponse", AIResponse);
                startActivity(intent);

            // Make a recipe based on custom ingredients
            }else if(!adapter.getItems().isEmpty()){
                String AIResponse = generateAIRecipe(false, adapter.getItems());
                // Use intent to pass the AI response to the next activity
                Intent intent = new Intent(requireActivity(), AIRecipeEditActivity.class);
                intent.putExtra("AIResponse", AIResponse);
                startActivity(intent);
            } else {
                String AIResponse = "No extra Ingredients";
                // Use intent to pass the AI response to the next activity
                Intent intent = new Intent(requireActivity(), AIRecipeEditActivity.class);
                intent.putExtra("AIResponse", AIResponse);
                startActivity(intent);
            }
        });

        // Listener for the add ingredient button
        addIngredientButton.setOnClickListener(v -> {
            adapter.addItem();
        });

        // Listener for the expand button
        expandButton.setOnClickListener(v -> {
            // Toggle the useIngredientsMode flag
            useIngredientsMode = !useIngredientsMode;
            recyclerView.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);
            addIngredientButton.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);
            useMyIngredientsCheckbox.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);

            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    useIngredientsMode
                            ? R.drawable.ic_keyboard_arrow_up // Down arrow when collapsing
                            : R.drawable.ic_keyboard_arrow_down // Up arrow when expanding
            );
            expandButton.setIcon(newIcon);
        });

        // Listener for the checkbox check state
        useMyIngredientsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->{
            // Manage add ingredient recycler view visibility based on the checkbox state
            // Hide the recycler view and the add ingredient button
            recyclerView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            addIngredientButton.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });

        return view;
    }

    public String generateAIRecipe(boolean useMyIngredients, List<ShoppingListItem> customItems){
        if (useMyIngredients) {
            // Generate an AI recipe based on the ingredients
            List<Ingredient> ingredients;
            try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                ingredients = db.getAllIngredients();
            }
            // Contain only the ingredient names from ingredients into a string separated by commas
            StringBuilder ingredientsString = new StringBuilder();
            for (Ingredient ingredient : ingredients) {
                ingredientsString.append(ingredient.getIngredientName()).append(", ");
            }
            ingredientsString = new StringBuilder(ingredientsString.substring(0, ingredientsString.length() - 2));
            String ingredientsStringFinal = ingredientsString.toString();

            return "My Ingredients:" + ingredientsStringFinal;
        } else if(customItems!=null) {
            // Contain the ingredient names from the custom ingredients into a string separated by commas
            StringBuilder customItemsString = new StringBuilder();
            for (ShoppingListItem item : customItems) {
                if(!item.getShoppingListItemName().isEmpty()) {
                    customItemsString.append(item.getShoppingListItemName()).append(", ");
                }
            }
            customItemsString = new StringBuilder(customItemsString.substring(0, customItemsString.length() - 2));
            String customItemsStringFinal = customItemsString.toString();
            return "Custom Ingredients:" + customItemsStringFinal;
        }
        return "Nothing";
    }
}
