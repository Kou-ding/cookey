package com.example.cookey;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context context;
    private final List<Ingredient> ingredients;
    private boolean editingEnabled = false;
    private boolean showCombinedQuantityUnit = true;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Ingredient ingredient);
    }

    public IngredientAdapter(List<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void updateIngredients(List<Ingredient> newIngredients) {
        this.ingredients.clear();
        this.ingredients.addAll(newIngredients);
        notifyDataSetChanged();
    }

    public void setShowCombinedQuantityUnit(boolean showCombined) {
        this.showCombinedQuantityUnit = showCombined;
        notifyDataSetChanged();
    }

    public void setEditingEnabled(boolean enabled) {
        this.editingEnabled = enabled;
        notifyDataSetChanged();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.nameTextView.setText(ingredient.getIngredientName());

        if (editingEnabled) {
            holder.quantityEditText.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.quantityTextView.setVisibility(View.GONE);
            holder.quantityUnitTextView.setVisibility(View.GONE);
            holder.unitSystemTextView.setVisibility(View.VISIBLE);
            holder.unitSystemTextView.setText(ingredient.getUnitSystem());

            holder.quantityEditText.setText(String.valueOf(ingredient.getQuantity()));
            holder.quantityEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        float newQty = Float.parseFloat(s.toString());
                        ingredient.setQuantity(newQty);
                    } catch (NumberFormatException e) {
                        // Ignore invalid input
                    }
                }
            });

            holder.deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(ingredient);
                }
            });

        } else {
            holder.quantityEditText.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

            if (showCombinedQuantityUnit) {
                holder.quantityUnitTextView.setVisibility(View.VISIBLE);
                holder.quantityTextView.setVisibility(View.GONE);
                holder.unitSystemTextView.setVisibility(View.GONE);

                String formatted = String.format("%.2f %s", ingredient.getQuantity(), ingredient.getUnitSystem());
                holder.quantityUnitTextView.setText(formatted);
            } else {
                holder.quantityUnitTextView.setVisibility(View.GONE);
                holder.quantityTextView.setVisibility(View.VISIBLE);
                holder.unitSystemTextView.setVisibility(View.VISIBLE);

                holder.quantityTextView.setText(String.format("%.2f", ingredient.getQuantity()));
                holder.unitSystemTextView.setText(ingredient.getUnitSystem());
            }
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView, unitSystemTextView, quantityUnitTextView;
        EditText quantityEditText;
        ImageButton deleteButton;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.ingredientName);
            quantityTextView = view.findViewById(R.id.ingredientQuantity);
            quantityUnitTextView = view.findViewById(R.id.ingredientQuantityUnit);
            unitSystemTextView = view.findViewById(R.id.ingredientUnitSystem);
            quantityEditText = view.findViewById(R.id.ingredientEditQuantity);
            deleteButton = view.findViewById(R.id.deleteIngredient);
        }
    }
}
