package com.example.cookey.ui.MyIngredients;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.IngredientActivity;
import com.example.cookey.R;
import java.util.List;

public class MyIngredientsAdapter extends RecyclerView.Adapter<MyIngredientsAdapter.ViewHolder> {
    // Constructor
    public MyIngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    // List of ingredients
    private List<Ingredient> ingredients;
    // Forms the edit view
    private boolean editMode; // !editMode == viewMode

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, unitSystemTextView;
        EditText quantityEditText;
        ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.ingredientName);
            unitSystemTextView = view.findViewById(R.id.ingredientUnitSystem);
            quantityEditText = view.findViewById(R.id.ingredientEditQuantity);
            deleteButton = view.findViewById(R.id.deleteIngredient);

            // Delete button click listener
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                // Delete from db
                try (DBHandler db = new DBHandler(v.getContext(), null, null, 1)) {
                    db.deleteIngredient(ingredients.get(position).getIngredientId());
                } catch (Exception e) {
                    Log.e("MyIngredientsViewModel", "Error deleting ingredient", e);
                }
                // Delete from list
                ingredients.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, ingredients.size());
            });
        }
    }

    @NonNull
    @Override
    public MyIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getIngredientName());
        holder.unitSystemTextView.setText(ingredient.getUnitSystem());

        // Remove existing text watcher
        if (holder.quantityEditText.getTag() instanceof TextWatcher) {
            holder.quantityEditText.removeTextChangedListener((TextWatcher) holder.quantityEditText.getTag());
        }

        holder.quantityEditText.setText(String.valueOf(ingredient.getQuantity()));

        if (editMode) {
            holder.quantityEditText.setEnabled(true);
            holder.deleteButton.setVisibility(View.VISIBLE);

            holder.itemView.setOnClickListener(null); // Disable navigation in edit mode

            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
                    if (!text.isEmpty()) {
                        try {
                            float newQuantity = Float.parseFloat(text);
                            ingredient.setQuantity(newQuantity);
                        } catch (NumberFormatException e) {
                            Log.e("MyIngredientsAdapter", "Invalid number format", e);
                        }
                    }
                }
            };

            holder.quantityEditText.addTextChangedListener(watcher);
            holder.quantityEditText.setTag(watcher);
        } else {
            holder.quantityEditText.setEnabled(false);
            holder.deleteButton.setVisibility(View.GONE);

            // 🔓 Allow navigation when NOT in edit mode
            holder.itemView.setOnClickListener(v -> {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Ingredient ing = ingredients.get(pos);
                    Intent intent = new Intent(v.getContext(), IngredientActivity.class);
                    intent.putExtra("ingredientName", ing.getIngredientName());
                    v.getContext().startActivity(intent);
                    ((Activity) v.getContext()).overridePendingTransition(
                            android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return ingredients.size();
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}