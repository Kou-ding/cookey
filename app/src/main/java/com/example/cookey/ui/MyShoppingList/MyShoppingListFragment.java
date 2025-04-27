package com.example.cookey.ui.MyShoppingList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.cookey.DBHandler;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;

import java.util.List;

public class MyShoppingListFragment extends Fragment {

    private MyShoppingListAdapter adapter;

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
        TextView foodType = view.findViewById(R.id.foodType);
        Button addItemButton = view.findViewById(R.id.addItem);
        Button editModeButton = view.findViewById(R.id.editMode);
        Button refillIngredientsButton = view.findViewById(R.id.refillIngredients);
        Button newListButton = view.findViewById(R.id.newList);
        ToggleButton toggleTypeButton = view.findViewById(R.id.toggleType);

        // Set the initial text for the food type
        foodType.setText("Food");

        // Initialize the database handler
        List<ShoppingListItem> listItems;
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            // Get the list of ingredients from the database
            listItems = db.getAllFoodItems();
        }

        // Initialize the adapter
        adapter = new MyShoppingListAdapter(listItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the recycler view
        recyclerView.setAdapter(adapter);

        // Item Type Toggle Listener
        toggleTypeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                foodType.setText("Food");
                foodMode = true;
                nonFoodMode = false;
                loadItems();

            } else {
                foodType.setText("Non-Food");
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
        editModeButton.setOnClickListener(v -> {
            // Toggle the edit mode inside the adapter
            adapter.editMode = !adapter.editMode;
            adapter.notifyDataSetChanged();

            // Change the edit text into done
            if (adapter.editMode) {
                editModeButton.setText(R.string.done);
            } else {
                editModeButton.setText(R.string.edit);
            }
            if (!adapter.editMode) {
                try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                    for (ShoppingListItem item : adapter.getItems()) {
                        db.setNewItemNameAndQuantity(item.getShoppingListItemId(), item.getShoppingListItemName(), item.getPurchasedQuantity());
                    }
                    loadItems();
                }
            }
        });

        // Add Item Button Listener
        addItemButton.setOnClickListener(v -> {
            addItem(foodMode);
            loadItems();
        });

        // Refill Ingredients Button Listener
        refillIngredientsButton.setOnClickListener(v -> {
            try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                db.refillIngredients(adapter.getCheckedItems());
            }
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
                db.addFoodItem(db.getNextUnusedShoppingListItemId());
            } else {
                db.addNonFoodItem(db.getNextUnusedShoppingListItemId());
            }
        }
    }

}