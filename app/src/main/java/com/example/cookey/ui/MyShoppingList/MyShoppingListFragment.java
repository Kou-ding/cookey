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
import com.example.cookey.NarratorManager;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MyShoppingListFragment extends Fragment {

    private MyShoppingListAdapter adapter;
    private boolean foodMode = true;
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
            listItems = db.getAllShoppingListItems();
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
                refillIngredientsButton.setVisibility(View.VISIBLE);
                adapter.setFoodMode(true);

                //ACCESSIBILITY
                NarratorManager.speakIfEnabled(this.getContext(), getString(R.string.food));
            } else {
                foodType.setText(R.string.non_food);
                foodMode = false;
                refillIngredientsButton.setVisibility(View.INVISIBLE);
                adapter.setFoodMode(false);

                //ACCESSIBILITY
                NarratorManager.speakIfEnabled(this.getContext(), getString(R.string.non_food));
            }
        });

        // New List Button Listener
        newListButton.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(getString(R.string.new_list_dialog_title))
                    .setMessage(getString(R.string.new_list_dialog_message))
                    .setPositiveButton(getString(R.string.new_list_confirm), (dialog, which) -> {
                        Toast.makeText(requireContext(), getString(R.string.new_list_created_toast), Toast.LENGTH_SHORT).show();
                        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                            // Delete all items from the permanent database
                            db.newShoppingList();
                            // Delete all items from the temporary array
                            adapter.getItems().clear();
                            adapter.setDisplayedItems();
                        }
                    })
                    .setNegativeButton(getString(R.string.new_list_cancel), (dialog, which) -> {
                        Toast.makeText(requireContext(), getString(R.string.new_list_no_changes_toast), Toast.LENGTH_SHORT).show();
                    })
                    .show();
            //ACCESSIBILITY
            NarratorManager.speakIfEnabled(v.getContext(), getString(R.string.new_list_dialog_message));
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
            }
            if (!checkAll) {
                uncheckAllItems();
            }

            // Change the state of the button
            checkAll = !checkAll;

            //ACCESSIBILITY
            NarratorManager.speakIfEnabled(v.getContext(), getString(R.string.all));
        });

        // Add Item Button Listener
        addItemButton.setOnClickListener(v -> {
            addItem(foodMode);
            recyclerView.smoothScrollToPosition(adapter.getItemCount());

            //ACCESSIBILITY
            NarratorManager.speakIfEnabled(v.getContext(), getString(R.string.add));
        });

        // Refill Ingredients Button Listener
        refillIngredientsButton.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(getString(R.string.refill_dialog_title))
                    .setMessage(getString(R.string.refill_dialog_message))
                    .setPositiveButton(getString(R.string.new_list_confirm), (dialog, which) -> {
                        Toast.makeText(requireContext(), getString(R.string.refill_success_toast), Toast.LENGTH_SHORT).show();
                        // Perform the refilling in the database
                        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
                            // Save items to the database
                            db.storeItemsWhenExiting(adapter.getItems());
                            // Refill food items
                            db.refillIngredients(adapter.getCheckedItems());
                            // Clear the checked items for the main list
                            adapter.getItems().removeAll(adapter.getCheckedItems()); // Remove from main list
                            adapter.setDisplayedItems();
                        }
                    })
                    .setNegativeButton(getString(R.string.new_list_cancel), (dialog, which) -> {
                        Toast.makeText(requireContext(), getString(R.string.new_list_no_changes_toast), Toast.LENGTH_SHORT).show();
                    })
                    .show();

            //ACCESSIBILITY
            NarratorManager.speakIfEnabled(v.getContext(), getString(R.string.refill_dialog_message));
        });
        return view;
    }

    public void loadItemsFromDB() {
        try (DBHandler db = new DBHandler(requireContext(), null, null, 1)) {
            List<ShoppingListItem> listItems = null;
            listItems = db.getAllShoppingListItems();
            if (listItems != null){
                // Update the adapter with the new data
                adapter.getItems().clear();
                adapter.getItems().addAll(listItems);
                adapter.notifyDataSetChanged();
            }
        }
    }
    public void addItem(boolean foodMode) {
        if(foodMode) {
            adapter.addFoodItem();
        } else {
            adapter.addNonFoodItem();
        }
    }

    public void checkAllItems(){
        adapter.massCheck();
    }
    public void uncheckAllItems(){
        adapter.massUncheck();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) {
            // Save synchronously to ensure completion before pause finishes
            new Thread(() -> {
                adapter.saveAllItemsToDB();
            }).run();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load items on UI thread
        requireActivity().runOnUiThread(this::loadItemsFromDB);
        adapter.setDisplayedItems();
    }

}