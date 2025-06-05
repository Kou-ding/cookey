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
        private List<ShoppingListItem> displayedItems;
        private ArrayAdapter<String> foodAutoCompleteAdapter;
        private ArrayAdapter<String> nonFoodAutoCompleteAdapter;
        private boolean foodMode = true;
        Context context;

        public MyShoppingListAdapter(List<ShoppingListItem> items, Context context) {
            this.context = context;
            this.items = new ArrayList<>(items);
            this.displayedItems = new ArrayList<>();
            this.foodAutoCompleteAdapter = createFoodAutoCompleteAdapter(context);
            this.nonFoodAutoCompleteAdapter = createNonFoodAutoCompleteAdapter(context);
            setDisplayedItems();
        }
        public void setDisplayedItems() {
            displayedItems.clear();
            for (ShoppingListItem item : items) {
                if (foodMode && item.getIsFood() || !foodMode && !item.getIsFood()) {
                    displayedItems.add(item);
                }
            }
            notifyDataSetChanged();
        }
        private ArrayAdapter<String> createFoodAutoCompleteAdapter(Context context) {
            List<String> foodSuggestions = new ArrayList<>();
            try (DBHandler db = new DBHandler(context, null, null, 1)) {
                for (Ingredient ingredient : db.getAllIngredients()) {
                    foodSuggestions.add(ingredient.getIngredientName());
                }
            }
            return new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    foodSuggestions
            );
        }

        private ArrayAdapter<String> createNonFoodAutoCompleteAdapter(Context context) {
            List<String> nonFoodSuggestions = new ArrayList<>();

            // Add common non-food shopping items
            nonFoodSuggestions.add("Paper towels");
            nonFoodSuggestions.add("Toilet paper");
            nonFoodSuggestions.add("Laundry detergent");
            nonFoodSuggestions.add("Dish soap");
            nonFoodSuggestions.add("Trash bags");
            nonFoodSuggestions.add("Batteries");
            nonFoodSuggestions.add("Light bulbs");
            nonFoodSuggestions.add("Aluminum foil");
            nonFoodSuggestions.add("Plastic wrap");
            nonFoodSuggestions.add("Ziploc bags");
            nonFoodSuggestions.add("Sponges");
            nonFoodSuggestions.add("Cleaning supplies");
            nonFoodSuggestions.add("Shampoo");
            nonFoodSuggestions.add("Conditioner");
            nonFoodSuggestions.add("Body wash");
            nonFoodSuggestions.add("Toothpaste");
            nonFoodSuggestions.add("Deodorant");
            nonFoodSuggestions.add("Razors");
            nonFoodSuggestions.add("Shaving cream");
            nonFoodSuggestions.add("Band-aids");
            nonFoodSuggestions.add("Vitamins");
            nonFoodSuggestions.add("Cotton swabs");
            nonFoodSuggestions.add("Tissues");
            nonFoodSuggestions.add("Napkins");
            nonFoodSuggestions.add("Parchment paper");
            nonFoodSuggestions.add("Plastic containers");
            nonFoodSuggestions.add("Aluminum pans");
            nonFoodSuggestions.add("Matches");
            nonFoodSuggestions.add("Candles");
            nonFoodSuggestions.add("Air freshener");

            return new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    nonFoodSuggestions
            );
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox ingredientCheckbox;
            AutoCompleteTextView autoCompleteIngredient;
            ImageButton deleteItem;
            TextView itemUnitSystem;
            EditText itemQuantityEdit;


            public ViewHolder(View view) {
                super(view);
                ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
                autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);
                deleteItem = itemView.findViewById(R.id.deleteItem);
                itemUnitSystem = itemView.findViewById(R.id.itemUnitSystem);
                itemQuantityEdit = itemView.findViewById(R.id.itemQuantityEdit);

                // Set it to the AutoCompleteTextView
                autoCompleteIngredient.setThreshold(1);
                // Single line input and no suggestions
                autoCompleteIngredient.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_FILTER);
                autoCompleteIngredient.setSingleLine(true);

                // Listen for when an item is selected from dropdown
                autoCompleteIngredient.setOnItemClickListener((parent, dropdownView, position, id) -> {
                    // When an item is selected from dropdown:
                    autoCompleteIngredient.setVisibility(View.GONE);  // Hide the auto complete text
                    ingredientCheckbox.setText((String) parent.getItemAtPosition(position));  // Set the checkbox text to the selected item

                    // Get the current displayed item
                    int displayPos = getAdapterPosition();
                    if (displayPos != RecyclerView.NO_POSITION) {
                        ShoppingListItem currentItem = displayedItems.get(displayPos);
                        currentItem.setShoppingListItemName((String) parent.getItemAtPosition(position));

                        // Update the corresponding item in the main list
                        for (ShoppingListItem item : items) {
                            if (item.getShoppingListItemId() == currentItem.getShoppingListItemId()) {
                                item.setShoppingListItemName(currentItem.getShoppingListItemName());
                                break;
                            }
                        }

                        // Update unit system
                        String unitSystem;
                        try (DBHandler db = new DBHandler(itemView.getContext(), null, null, 1)) {
                            unitSystem = db.getUnitSystem(currentItem.getShoppingListItemName());
                        }
                        itemUnitSystem.setText(unitSystem);
                    }
                });

                // Handle delete button click
                deleteItem.setOnClickListener(v -> {
                    int displayPosition = getAdapterPosition();
                    if (displayPosition != RecyclerView.NO_POSITION) {
                        ShoppingListItem itemToRemove = displayedItems.get(displayPosition);

                        // Remove from both lists by ID
                        int idToRemove = itemToRemove.getShoppingListItemId();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getShoppingListItemId() == idToRemove) {
                                items.remove(i);
                                break;
                            }
                        }
                        displayedItems.remove(displayPosition);

                        notifyItemRemoved(displayPosition);
                        notifyItemRangeChanged(displayPosition, displayedItems.size());
                    }
                });

                // Handle checkbox state change
                ingredientCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Update the checked status of the item
                    int displayPosition = getAdapterPosition();
                    if (displayPosition != RecyclerView.NO_POSITION) {
                        displayedItems.get(displayPosition).setIsChecked(isChecked);
                    }
                });

                // TextWatcher for the edit text
                itemQuantityEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            try {
                                float quantity = s.toString().isEmpty() ? 0 : Float.parseFloat(s.toString());
                                ShoppingListItem currentItem = displayedItems.get(position);
                                currentItem.setPurchasedQuantity(quantity);

                                // Update main list
                                for (ShoppingListItem item : items) {
                                    if (item.getShoppingListItemId() == currentItem.getShoppingListItemId()) {
                                        item.setPurchasedQuantity(quantity);
                                        break;
                                    }
                                }
                            } catch (NumberFormatException e) {
                                // Handle invalid number input
                                itemQuantityEdit.setError("Invalid number");
                            }
                        }
                    }
                });
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

            // Get the position of the item in the displayed list
            ShoppingListItem displayedItem = displayedItems.get(position);

            // Set the checkbox state based on the item 'checked' property
            holder.ingredientCheckbox.setChecked(displayedItem.getIsChecked());

            // Set different app behaviour for new items based on the fact that they start with null text
            if (displayedItem.getShoppingListItemName() == null || displayedItem.getShoppingListItemName().isEmpty()) {
                holder.autoCompleteIngredient.setVisibility(View.VISIBLE);
                holder.ingredientCheckbox.setText("");
                holder.autoCompleteIngredient.setText("");
            } else {
                holder.autoCompleteIngredient.setVisibility(View.GONE);
                holder.ingredientCheckbox.setVisibility(View.VISIBLE);
                holder.ingredientCheckbox.setText(displayedItem.getShoppingListItemName());
            }

            // Quantity edit text
            float quantity = displayedItem.getPurchasedQuantity();
            holder.itemQuantityEdit.setText(quantity == 0 ? "" : String.valueOf(quantity));

            // Delete Item button
            holder.deleteItem.setVisibility(View.VISIBLE);

            // Unit system
            String unitSystem;
            try (DBHandler db = new DBHandler(holder.itemView.getContext(), null, null, 1)) {
                unitSystem = db.getUnitSystem(displayedItem.getShoppingListItemName());
            }
            holder.itemUnitSystem.setText(unitSystem);

            // Enable autocomplete only when on food mode
            if (foodMode) {
                holder.autoCompleteIngredient.setAdapter(foodAutoCompleteAdapter);
            } else {
                holder.autoCompleteIngredient.setAdapter(nonFoodAutoCompleteAdapter);
            }
        }

        @Override
        public int getItemCount() {
            return displayedItems.size();
        }
        public void setFoodMode(boolean foodMode) {
            this.foodMode = foodMode;
            setDisplayedItems();
        }
        public List<ShoppingListItem> getDisplayedItems() {
            return displayedItems;
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
        public void massCheck(){
            for (ShoppingListItem item : items) {
                item.setIsChecked(true);
            }
            notifyItemRangeChanged(0, items.size());
        }
        public void massUncheck(){
            for (ShoppingListItem item : items) {
                item.setIsChecked(false);
            }
            notifyItemRangeChanged(0, items.size());
        }
        public void addFoodItem() {
            ShoppingListItem newItem = new ShoppingListItem();
            newItem.setShoppingListItemId(getNextId()); // Use a proper ID generator
            newItem.setShoppingListItemName("");
            newItem.setIsFood(true);
            newItem.setIsChecked(false);

            items.add(newItem);
            if (foodMode) {
                int position = displayedItems.size();
                displayedItems.add(newItem);
                notifyItemInserted(position);
            }
        }
        public void addNonFoodItem() {
            ShoppingListItem newItem = new ShoppingListItem();
            newItem.setShoppingListItemId(getNextId()); // Use a proper ID generator
            newItem.setShoppingListItemName("");
            newItem.setIsFood(false);
            newItem.setIsChecked(false);

            items.add(newItem);
            if (!foodMode) {
                int position = displayedItems.size();
                displayedItems.add(newItem);
                notifyItemInserted(position);
            }
        }

        private int getNextId() {
            int maxId = 0;
            for (ShoppingListItem item : items) {
                if (item.getShoppingListItemId() > maxId) {
                    maxId = item.getShoppingListItemId();
                }
            }
            return maxId + 1;
        }
        public void saveAllItemsToDB() {
            try (DBHandler db = new DBHandler(context, null, null, 1)) {
                db.storeItemsWhenExiting(items);
            }
        }

    }
