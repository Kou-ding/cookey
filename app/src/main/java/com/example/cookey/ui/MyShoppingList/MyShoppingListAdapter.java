    package com.example.cookey.ui.MyShoppingList;

    import android.content.Context;
    import android.os.Handler;
    import android.os.Looper;
    import android.text.Editable;
    import android.text.InputType;
    import android.text.TextWatcher;
    import android.view.KeyEvent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.inputmethod.EditorInfo;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import com.example.cookey.DBHandler;
    import com.example.cookey.Ingredient;
    import com.example.cookey.R;
    import com.example.cookey.ShoppingListItem;
    import java.util.ArrayList;
    import java.util.List;

    public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.ViewHolder>{
        private List<ShoppingListItem> items;
        private ArrayAdapter<String> autoCompleteAdapter;

        private boolean foodMode = true;

        public MyShoppingListAdapter(List<ShoppingListItem> items, Context context) {
            this.items = items;
            this.autoCompleteAdapter = createAutoCompleteAdapter(context);
        }
        private ArrayAdapter<String> createAutoCompleteAdapter(Context context) {
            List<String> suggestions = new ArrayList<>();
            try (DBHandler db = new DBHandler(context, null, null, 1)) {
                for (Ingredient ingredient : db.getAllIngredients()) {
                    suggestions.add(ingredient.getIngredientName());
                }
            }
            return new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    suggestions
            );
        }
        private TextWatcher nameTextWatcher;
        private TextWatcher quantityTextWatcher;
        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox ingredientCheckbox;
            AutoCompleteTextView autoCompleteIngredient;
            ImageButton deleteItem;
            ImageButton editItem;
            TextView itemUnitSystem;
            EditText itemQuantityEdit;

            public ViewHolder(View view) {
                super(view);
                ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
                autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);
                deleteItem = itemView.findViewById(R.id.deleteItem);
                editItem = itemView.findViewById(R.id.editItem);
                itemUnitSystem = itemView.findViewById(R.id.itemUnitSystem);
                itemQuantityEdit = itemView.findViewById(R.id.itemQuantityEdit);

                // Set it to the AutoCompleteTextView
                autoCompleteIngredient.setThreshold(1);

                // Listen for when an item is selected from dropdown
                autoCompleteIngredient.setOnItemClickListener((parent, dropdownView, position, id) -> {
                    // When an item is selected from dropdown:
                    autoCompleteIngredient.setEnabled(false);  // Make text uneditable
                    itemQuantityEdit.setEnabled(true);         // Enable quantity field
                    itemQuantityEdit.setText("");              // Clear quantity field
                    itemQuantityEdit.requestFocus();           // Move focus to quantity
                    // Show the keyboard
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(itemQuantityEdit, InputMethodManager.SHOW_IMPLICIT);
                    }

                    autoCompleteIngredient.setVisibility(View.GONE);  // Hide the auto complete text
                    ingredientCheckbox.setVisibility(View.VISIBLE);  // Show the prettier checkbox text
                    ingredientCheckbox.setText((String) parent.getItemAtPosition(position));  // Set the checkbox text to the selected item

                    // Save the item name immediately on the list
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    int adapterPos = getAdapterPosition();
                    if (adapterPos != RecyclerView.NO_POSITION) {
                        items.get(adapterPos).setShoppingListItemName(selectedItem);
                    }
                    // Save the item name to the database
                    try (DBHandler db = new DBHandler(itemView.getContext(), null, null, 1)) {
                        db.setShoppingListItemName(items.get(adapterPos).getShoppingListItemId(), selectedItem);
                    }

                    // Fetch the correct unit system
                    String unitSystem;
                    try (DBHandler db = new DBHandler(itemView.getContext(), null, null, 1)) {
                        unitSystem = db.getUnitSystem((String) parent.getItemAtPosition(position));
                    }
                    itemUnitSystem.setText(unitSystem);
                });

                // Make "enter" viable for non food entries
                autoCompleteIngredient.setOnEditorActionListener((v, actionId, event) -> {
                    // Handle both soft keyboard "Done" action and physical Enter key
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                    event.getAction() == KeyEvent.ACTION_DOWN)) {

                        String selectedText = autoCompleteIngredient.getText().toString().trim();
                        if (!selectedText.isEmpty()) {
                            // 1. First clear focus from current view
                            autoCompleteIngredient.clearFocus();

                            // 2. Prevent new line from being created
                            autoCompleteIngredient.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                            // 3. Execute the focus change after a small delay
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                // Hide the AutoCompleteTextView
                                autoCompleteIngredient.setVisibility(View.GONE);

                                // Show the checkbox with selected text
                                ingredientCheckbox.setVisibility(View.VISIBLE);
                                ingredientCheckbox.setText(selectedText);

                                // Prepare quantity field
                                itemQuantityEdit.setText("");
                                itemQuantityEdit.setEnabled(true);

                                // Force focus to quantity field
                                itemQuantityEdit.requestFocusFromTouch();

                                // Show keyboard for quantity field
                                InputMethodManager imm = (InputMethodManager) itemView.getContext()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.showSoftInput(itemQuantityEdit, InputMethodManager.SHOW_IMPLICIT);
                                }

                                // Save to list and database
                                int adapterPos = getAdapterPosition();
                                if (adapterPos != RecyclerView.NO_POSITION) {
                                    items.get(adapterPos).setShoppingListItemName(selectedText);
                                    try (DBHandler db = new DBHandler(itemView.getContext(), null, null, 1)) {
                                        db.setShoppingListItemName(items.get(adapterPos).getShoppingListItemId(), selectedText);

                                        // Fetch unit system
                                        String unitSystem = db.getUnitSystem(selectedText);
                                        itemUnitSystem.setText(unitSystem);
                                    }
                                }
                            }, 50); // Small delay to ensure smooth transition

                            return true; // Consume the event
                        }
                    }
                    return false;
                });

                // Also add this to your AutoCompleteTextView setup to prevent multi-line input
                autoCompleteIngredient.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS |
                        InputType.TYPE_TEXT_VARIATION_FILTER);
                autoCompleteIngredient.setSingleLine(true);

                // Handle "enter" during editing the quantity
                itemQuantityEdit.setOnEditorActionListener((v, actionId, event) -> {
                    // Handle "Done" or "Enter" key press
                    if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                        int adapterPos = getAdapterPosition();
                        if (adapterPos != RecyclerView.NO_POSITION) {
                            // Get the entered quantity (default to 0 if empty)
                            String quantityStr = itemQuantityEdit.getText().toString();
                            float quantity = quantityStr.isEmpty() ? 0 : Float.parseFloat(quantityStr);

                            // If quantity is 0, clear the field to allow re-entry
                            if (quantity == 0) {
                                itemQuantityEdit.setText(""); // Clear the field
                                itemQuantityEdit.requestFocus(); // Keep focus for new input
                                return true; // Do not proceed with saving
                            }

                            // Save the quantity to the list
                            items.get(adapterPos).setPurchasedQuantity(quantity);

                            // Save to database
                            try (DBHandler db = new DBHandler(itemView.getContext(), null, null, 1)) {
                                db.setShoppingListItemQuantity(
                                        items.get(adapterPos).getShoppingListItemId(),
                                        quantity
                                );
                            }

                            // Disable editing and switch UI states
                            itemQuantityEdit.setEnabled(false);
                            deleteItem.setVisibility(View.GONE);
                            editItem.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                    return false;
                });

                // Handle edit button click
                editItem.setOnClickListener(v -> {
                    deleteItem.setVisibility(View.VISIBLE);
                    editItem.setVisibility(View.GONE);
                    itemQuantityEdit.setEnabled(true);
                    // Move focus to quantity
                    itemQuantityEdit.requestFocus();
                    // Move cursor to end
                    itemQuantityEdit.setSelection(itemQuantityEdit.getText().length());
                    // Show the keyboard
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(itemQuantityEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                // Handle delete button click
                deleteItem.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Store the name before removing
                        int itemId = items.get(position).getShoppingListItemId();

                        // Remove from list and notify adapter
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, items.size());

                        // Delete from DB
                        try (DBHandler db = new DBHandler(v.getContext(), null, null, 1)) {
                            db.deleteShoppingListItem(itemId);
                        }
                    }
                });

                // Handle checkbox state change
                ingredientCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Update the checked status of the item
                    items.get(getAdapterPosition()).setIsChecked(isChecked);

                    // Update the database
                    try (DBHandler db = new DBHandler(buttonView.getContext(), null, null, 1)) {
                        db.setShoppingListItemChecked(items.get(getAdapterPosition()).getShoppingListItemId(), isChecked);
                    }
                });

                // Initialize TextWatchers once
                nameTextWatcher = new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            items.get(pos).setShoppingListItemName(s.toString());
                        }
                    }
                };
                quantityTextWatcher = new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override public void afterTextChanged(Editable s) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            float quantity = s.toString().isEmpty() ? 0 : Float.parseFloat(s.toString());
                            items.get(pos).setPurchasedQuantity(quantity);
                        }
                    }
                };
            }
        }

        @NonNull
        @Override
        public MyShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_layout, parent, false);
            return new MyShoppingListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyShoppingListAdapter.ViewHolder holder, int position) {
            ShoppingListItem item = items.get(position);

            // Set the checkbox state based on the item 'checked' property
            holder.ingredientCheckbox.setChecked(item.getIsChecked());

            // Set different app behaviour for new items based on the fact that they start with null text
            if (item.getShoppingListItemName() == null || item.getShoppingListItemName().isEmpty()) {
                holder.autoCompleteIngredient.setVisibility(View.VISIBLE);
                holder.ingredientCheckbox.setText("");
                holder.autoCompleteIngredient.requestFocus(); // optionally focus it
            } else {
                holder.autoCompleteIngredient.setVisibility(View.GONE);
                holder.ingredientCheckbox.setVisibility(View.VISIBLE);
                holder.ingredientCheckbox.setText(item.getShoppingListItemName());
            }

            // Quantity edit text
            holder.itemQuantityEdit.setText(String.valueOf(item.getPurchasedQuantity()));
            holder.itemQuantityEdit.setEnabled(false);

            // Edit and Delete Item buttons
            holder.editItem.setVisibility(View.VISIBLE);
            holder.deleteItem.setVisibility(View.GONE);

            // Unit system
            String unitSystem;
            try (DBHandler db = new DBHandler(holder.itemView.getContext(), null, null, 1)) {
                unitSystem = db.getUnitSystem(item.getShoppingListItemName());
            }
            holder.itemUnitSystem.setText(unitSystem);

            // Text watchers for quantity and ingredient name
            holder.autoCompleteIngredient.addTextChangedListener(nameTextWatcher);
            holder.itemQuantityEdit.addTextChangedListener(quantityTextWatcher);

            // Adapter for auto complete
            if (foodMode) {
                holder.autoCompleteIngredient.setAdapter(autoCompleteAdapter);
            } else {
                holder.autoCompleteIngredient.setAdapter(null);
                holder.autoCompleteIngredient.setText(""); // Clear the previous food name
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
        public void setFoodMode(boolean foodMode) {
            this.foodMode = foodMode;
        }
        public List<ShoppingListItem> getItems() {
            return items;
        }

        // Implementing getCheckedItems function
        public List<ShoppingListItem> getCheckedItems() {
            List<ShoppingListItem> checkedItems = new ArrayList<>();
            // Iterate through the items and add the checked ones to the list
            for (ShoppingListItem item : items) {
                // Check if the checkbox is ticked
                if (item.getIsChecked()) {
                    checkedItems.add(item);
                }
            }
            // Return the list of checked items
            return checkedItems;
        }
    }
