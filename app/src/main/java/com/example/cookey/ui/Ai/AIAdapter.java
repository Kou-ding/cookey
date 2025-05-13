package com.example.cookey.ui.Ai;

import android.content.Context;
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
import com.example.cookey.ui.MyShoppingList.MyShoppingListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AIAdapter extends RecyclerView.Adapter<AIAdapter.ViewHolder>{
    private List<ShoppingListItem> items;
    private ArrayAdapter<String> autoCompleteAdapter;

    public AIAdapter(Context context) {

        items = new ArrayList<>();
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
    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox ingredientCheckbox;
        EditText itemQuantityEdit;
        TextView itemUnitSystem;
        ImageButton deleteItem, editItem;
        AutoCompleteTextView autoCompleteIngredient;
        private TextWatcher textWatcher;

        public ViewHolder(View view) {
            super(view);
            ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
            itemQuantityEdit = itemView.findViewById(R.id.itemQuantityEdit);
            itemUnitSystem = itemView.findViewById(R.id.itemUnitSystem);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            editItem = itemView.findViewById(R.id.editItem);
            autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);

            // Initialize TextWatcher once in ViewHolder
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        items.get(position).setShoppingListItemName(s.toString());
                    }
                }
            };
            autoCompleteIngredient.addTextChangedListener(textWatcher);
            // Set the auto complete threshold to 1
            autoCompleteIngredient.setThreshold(1);
        }
    }
    @NonNull
    @Override
    public AIAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_layout, parent, false);
        return new AIAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        ShoppingListItem item = items.get(position);

        // Set the checkbox state based on the item 'checked' property
        holder.ingredientCheckbox.setChecked(item.getIsChecked());

        holder.ingredientCheckbox.setText("");
        holder.editItem.setVisibility(View.GONE);
        holder.itemQuantityEdit.setVisibility(View.GONE);
        holder.itemUnitSystem.setVisibility(View.GONE);
        holder.autoCompleteIngredient.setText(item.getShoppingListItemName());
        holder.autoCompleteIngredient.addTextChangedListener(holder.textWatcher);

        // Handle delete button click
        holder.deleteItem.setOnClickListener(v -> {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        });

        // Auto complete adapter
        holder.autoCompleteIngredient.setAdapter(autoCompleteAdapter);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(){
        // Add a blank item to the local list
        int newId = items.isEmpty() ? 1 : items.get(items.size() - 1).getShoppingListItemId() + 1;
        items.add(new ShoppingListItem(newId, "", 0, false, false)); // Notify the adapter that an item has been added
        notifyItemInserted(items.size() - 1);
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

}
