    package com.example.cookey.ui.MyShoppingList;

    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
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
    import com.google.android.material.textfield.TextInputLayout;

    import java.util.ArrayList;
    import java.util.List;

    public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListAdapter.ViewHolder>{

        private List<ShoppingListItem> items;

        public boolean viewMode = true;
        public boolean editMode = false;
        public MyShoppingListAdapter(List<ShoppingListItem> items) {
            this.items = items;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox ingredientCheckbox;
            AutoCompleteTextView autoCompleteIngredient;
            ImageButton deleteItem;
            TextView itemUnitSystem;
            TextView itemQuantityText;
            EditText itemQuantityEdit;


            public ViewHolder(View view) {
                super(view);
                ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
                autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);
                deleteItem = itemView.findViewById(R.id.deleteItem);
                itemUnitSystem = itemView.findViewById(R.id.itemUnitSystem);
                itemQuantityText = itemView.findViewById(R.id.itemQuantityText);
                itemQuantityEdit = itemView.findViewById(R.id.itemQuantityEdit);
            }
        }

        @NonNull
        @Override
        public MyShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_layout, parent, false);
            return new MyShoppingListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShoppingListItem item = items.get(position);

            if (viewMode) {
                holder.autoCompleteIngredient.setVisibility(View.GONE);
                holder.ingredientCheckbox.setText(item.getShoppingListItemName());
                holder.deleteItem.setVisibility(View.GONE);
                String unitSystem;
                try (DBHandler db = new DBHandler(holder.itemView.getContext(), null, null, 1)) {
                    unitSystem = db.getUnitSystem(item.getShoppingListItemName());
                }
                holder.itemUnitSystem.setText(unitSystem);
                holder.itemQuantityText.setText(String.valueOf(item.getPurchasedQuantity()));
                holder.itemQuantityEdit.setVisibility(View.GONE);
                holder.itemQuantityText.setVisibility(View.VISIBLE);


            }
            if (editMode) {
                holder.autoCompleteIngredient.setVisibility(View.VISIBLE);
                holder.ingredientCheckbox.setText("");
                holder.deleteItem.setVisibility(View.VISIBLE);
                holder.autoCompleteIngredient.setText(item.getShoppingListItemName());
                holder.itemQuantityText.setVisibility(View.GONE);
                holder.itemQuantityEdit.setVisibility(View.VISIBLE);

                // Item Edit Text logic
                holder.autoCompleteIngredient.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            String newName = s.toString();
                            item.setShoppingListItemName(newName);
                        } catch (NumberFormatException e) {
                            Log.e("MyShoppingListAdapter", "Error parsing item quantity", e);
                        }
                    }
                });

                // Quantity Edit text logic
                holder.itemQuantityEdit.setText(String.valueOf(item.getPurchasedQuantity()));
                holder.itemQuantityEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            float newQuantity = Float.parseFloat(s.toString());
                            item.setPurchasedQuantity(newQuantity);
                        } catch (NumberFormatException e) {
                            Log.e("MyShoppingListAdapter", "Error parsing item quantity", e);
                        }
                    }
                });
            }

            // Create the suggestions list
            List<String> suggestions = new ArrayList<>();

            // Open DB and fill the suggestions
            try (DBHandler db = new DBHandler(holder.itemView.getContext(), null, null, 1)) {
                List<Ingredient> ingredients = db.getAllIngredients();
                for (Ingredient ingredient : ingredients) {
                    suggestions.add(ingredient.getIngredientName());
                }
            }

            // Now create the ArrayAdapter using the suggestions
            ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(
                    holder.itemView.getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    suggestions
            );

            // Set it to the AutoCompleteTextView
            holder.autoCompleteIngredient.setAdapter(autoCompleteAdapter);


            // Handle delete button click
            holder.deleteItem.setOnClickListener(v -> {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                // You may also want to delete it from DB here
                try (DBHandler db = new DBHandler(v.getContext(), null, null, 1)) {
                    db.deleteShoppingListItem(item.getShoppingListItemName());
                }
            });
        }
        @Override
        public int getItemCount() {
            return items.size();
        }

        public List<ShoppingListItem> getItems() {
            return items;
        }

        public List<ShoppingListItem> getCheckedItems() {

            return items;
        }

    }
