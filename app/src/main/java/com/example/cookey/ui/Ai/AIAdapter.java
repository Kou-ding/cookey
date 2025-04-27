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
    public AIAdapter(Context context) {
        items = new ArrayList<>();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox ingredientCheckbox;
        TextView itemQuantityText;
        EditText itemQuantityEdit;
        TextView itemUnitSystem;
        ImageButton deleteItem;
        AutoCompleteTextView autoCompleteIngredient;

        public ViewHolder(View view) {
            super(view);
            ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
            itemQuantityText = itemView.findViewById(R.id.itemQuantityText);
            itemQuantityEdit = itemView.findViewById(R.id.itemQuantityEdit);
            itemUnitSystem = itemView.findViewById(R.id.itemUnitSystem);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            autoCompleteIngredient = itemView.findViewById(R.id.autoCompleteIngredient);
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
        holder.itemQuantityText.setVisibility(View.GONE);
        holder.itemQuantityEdit.setVisibility(View.GONE);
        holder.itemUnitSystem.setVisibility(View.GONE);
        holder.autoCompleteIngredient.setText(item.getShoppingListItemName());

        // Handle delete button click
        holder.deleteItem.setOnClickListener(v -> {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        });

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
                    item.setShoppingListItemName(s.toString());
                } catch (NumberFormatException e) {
                    Log.e("MyShoppingListAdapter", "Error parsing item quantity", e);
                }
            }
        });

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
        holder.autoCompleteIngredient.setThreshold(1);
        holder.autoCompleteIngredient.setAdapter(autoCompleteAdapter);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(){
        // Add a blank item to the local list
        items.add(new ShoppingListItem(items.size()+1, "", 0, "", false, false));
        // Notify the adapter that an item has been added
        notifyDataSetChanged();
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

}
