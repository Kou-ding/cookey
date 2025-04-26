package com.example.cookey.ui.MyShoppingList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.IngredientAutocompleteAdapter;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;
import com.example.cookey.ui.MyIngredients.MyIngredientsAdapter;

import java.util.List;

public class MyShoppingListFragment extends Fragment {

    private IngredientAutocompleteAdapter adapter;

    private boolean foodMode = true;
    private boolean nonFoodMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shopping_list, container, false);

        // Initialize the layout components
        RecyclerView recyclerView = view.findViewById(R.id.listItemsRecyclerView);

        Button addItemButton = view.findViewById(R.id.addItem);
        Button deleteModeButton = view.findViewById(R.id.deleteMode);
        Button refillIngredientsButton = view.findViewById(R.id.refillIngredients);
        Button newListButton = view.findViewById(R.id.newList);
        ToggleButton toggleTypeButton = view.findViewById(R.id.toggleType);


        // Initialize the database handler
        List<ShoppingListItem> listItems;
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            // Get the list of ingredients from the database
            listItems = db.getAllFoodItems();
        }

        // Initialize the adapter
        adapter = new IngredientAutocompleteAdapter(listItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the recycler view
        recyclerView.setAdapter(adapter);

        // Item Type Toggle Listener
        toggleTypeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                foodMode = true;
                nonFoodMode = false;
                loadItems();

            } else {
                foodMode = false;
                nonFoodMode = true;
                loadItems();
            }
        });

        // New List Button Listener
        newListButton.setOnClickListener(v -> {
            try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                db.newShoppingList();
                loadItems();
            }
        });

        // Delete Mode Button Listener
        deleteModeButton.setOnClickListener(v -> {
            adapter.editMode = !adapter.editMode;
            adapter.notifyDataSetChanged();
        });

        // Add Item Button Listener
        addItemButton.setOnClickListener(v -> {
            addItem(foodMode);
            loadItems();
        });

        return view;
    }
    public void loadItems() {
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            List<ShoppingListItem> listItems = null;
            if (foodMode) {
                // Get the list of all food items from the database
                listItems = db.getAllFoodItems();
            }
            if (nonFoodMode) {
                // Get the list of all non-food items from the database
                listItems = db.getAllNonFoodItems();
            }
            if (listItems != null){
                // Update the adapter with the new data
                adapter.getItems().clear();
                adapter.getItems().addAll(listItems);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void addItem(boolean foodMode){
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            if(foodMode) {
                db.addFoodItem();
            } else {
                db.addNonFoodItem();
            }
        }
    }

}