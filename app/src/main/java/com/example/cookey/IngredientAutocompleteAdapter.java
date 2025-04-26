    package com.example.cookey;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AutoCompleteTextView;
    import android.widget.CheckBox;
    import android.widget.ImageButton;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    public class IngredientAutocompleteAdapter extends RecyclerView.Adapter<IngredientAutocompleteAdapter.ViewHolder>{

        private List<ShoppingListItem> items;

        public boolean viewMode = true;
        public boolean editMode = false;
        public IngredientAutocompleteAdapter(List<ShoppingListItem> items) {
            this.items = items;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox ingredientCheckbox;
            AutoCompleteTextView autoCompleteIngredient;
            ImageButton deleteItem;
            public ViewHolder(View view) {
                super(view);
                ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
                autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);
                deleteItem = itemView.findViewById(R.id.deleteItem);

            }
        }

        @NonNull
        @Override
        public IngredientAutocompleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_layout, parent, false);
            return new IngredientAutocompleteAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShoppingListItem item = items.get(position);

            if (viewMode) {
                holder.autoCompleteIngredient.setText(item.getShoppingListItemName());
                holder.ingredientCheckbox.setText(item.getShoppingListItemName());
                holder.ingredientCheckbox.setText("");
                holder.deleteItem.setVisibility(View.GONE);

            }
            if (editMode) {
                holder.autoCompleteIngredient.setText("");
                holder.ingredientCheckbox.setText("");
                holder.deleteItem.setVisibility(View.VISIBLE);
                holder.autoCompleteIngredient.setText(item.getShoppingListItemName());
            }

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
    }
