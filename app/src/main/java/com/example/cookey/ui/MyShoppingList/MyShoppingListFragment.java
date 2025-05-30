package com.example.cookey.ui.MyShoppingList;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.cookey.DBHandler;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class MyShoppingListFragment extends Fragment {

    private MyShoppingListAdapter adapter;

    private boolean foodMode = true;
    private boolean nonFoodMode = false;
    private boolean checkAll = true;

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
        MaterialButton selectAllButton = view.findViewById(R.id.selectAll);
        Button refillIngredientsButton = view.findViewById(R.id.refillIngredients);
        Button newListButton = view.findViewById(R.id.newList);
        ToggleButton toggleTypeButton = view.findViewById(R.id.toggleType);

        // Set the initial text for the food type
        foodType.setText(R.string.food);

        // Initialize the database handler
        List<ShoppingListItem> listItems;
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            // Get the list of ingredients from the database
            listItems = db.getAllFoodItems();
        }

        // Initialize the adapter
        adapter = new MyShoppingListAdapter(listItems, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the recycler view
        recyclerView.setAdapter(adapter);

        // Item Type Toggle Listener
        toggleTypeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                foodType.setText(R.string.food);
                foodMode = true;
                nonFoodMode = false;
                refillIngredientsButton.setVisibility(View.VISIBLE);
                adapter.setFoodMode(true);
                loadItemsFromDB();
            } else {
                foodType.setText(R.string.non_food);
                foodMode = false;
                nonFoodMode = true;
                refillIngredientsButton.setVisibility(View.INVISIBLE);
                adapter.setFoodMode(false);
                loadItemsFromDB();
            }
        });

        // New List Button Listener
        newListButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("New List?")
                    .setMessage("Are you sure that you want create a new Shopping List? (All the items in your current Shopping List will be deleted!)")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(requireContext(), "New List created!", Toast.LENGTH_SHORT).show();
                        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                            db.newShoppingList();
                            loadItemsFromDB();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        // Check if all are checked and use the correct initial icon
        boolean areAllChecked = true;
        for (ShoppingListItem item : adapter.getItems()){
            if (!item.getIsChecked()){
                areAllChecked = false;
                break;
            }
        }
        if (areAllChecked){
            checkAll = false;
        }
        Drawable initialIcon = ContextCompat.getDrawable(
                requireContext(),
                areAllChecked
                        ? R.drawable.ic_check_box_outline_blank // All checked → show "uncheck all" icon
                        : R.drawable.ic_select_check_box        // Not all checked → show "check all" icon
        );
        selectAllButton.setIcon(initialIcon);

        // Select All Button Listener
        selectAllButton.setOnClickListener(v -> {
            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    checkAll
                            ? R.drawable.ic_check_box_outline_blank
                            : R.drawable.ic_select_check_box
            );
            selectAllButton.setIcon(newIcon);

            // Implement the checking/unchecking logic
            if (checkAll) {
                checkAllItems();
                loadItemsFromDB();
            }
            if (!checkAll) {
                uncheckAllItems();
                loadItemsFromDB();
            }

            // Change the state of the button
            checkAll = !checkAll;
        });

        // Add Item Button Listener
        addItemButton.setOnClickListener(v -> {
            addItem(foodMode);
            recyclerView.smoothScrollToPosition(adapter.getItemCount());
            loadItemsFromDB();
        });

        // Refill Ingredients Button Listener
        refillIngredientsButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Refill Ingredients?")
                    .setMessage("Are you sure that you want to add all the checked items in your Ingredients?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(requireContext(), "Ingredients Refilled!", Toast.LENGTH_SHORT).show();
                        // Perform the refilling in the database
                        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                            // Refill food items
                            db.refillIngredients(adapter.getCheckedItems());
                            // Reload the items from the database to reflect the changes
                            loadItemsFromDB();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });
        return view;
    }

    public void loadItemsFromDB() {
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
    public void addItem(boolean foodMode) {
        // First save any pending text in unfinished items
        discardPendingItems();

        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            if(foodMode) {
                db.addFoodItem(db.getNextUnusedShoppingListItemId());
            } else {
                db.addNonFoodItem(db.getNextUnusedShoppingListItemId());
            }
        }
        loadItemsFromDB();
    }

    private void discardPendingItems() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ShoppingListItem item = adapter.getItems().get(i);
            if (item.getShoppingListItemName() == null || item.getShoppingListItemName().isEmpty()) {
                // This is an unfinished item - remove it
                try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                    db.deleteShoppingListItem(item.getShoppingListItemId());
                }
            }
        }
    }
    public void checkAllItems(){
        // Save on the database
        try (DBHandler db = new DBHandler(getContext(), null, null, 1)) {
            db.massCheck();
        }
    }
    public void uncheckAllItems(){
        // Save on the database
        try (DBHandler db = new DBHandler(getContext(), null, null, 1)) {
            db.massUncheck();
        }
    }

}