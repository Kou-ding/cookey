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

import com.example.cookey.Ingredient;
import com.example.cookey.IngredientActivity;
import com.example.cookey.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MyIngredientsAdapter extends RecyclerView.Adapter<MyIngredientsAdapter.ViewHolder> {
    // List of ingredients
    private List<Ingredient> ingredients = new ArrayList<>();
    // Forms the edit view
    // !editMode == viewMode
    private boolean editMode = false;
    private TextWatcher textWatcher;
    public void setMode(boolean mode) {
        this.editMode = mode;
        notifyItemRangeChanged(0, getItemCount());
    }

    // Delete button
    /**
     * Interface for the delete button
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(Ingredient ingredient);

    }
    // Instance of the interface
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Sets the listener for the delete button
     * @param listener The listener to set
     */
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged(); // change to something smarter
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     *  Class that holds the items to be displayed in the RecyclerView
     */
     class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, unitSystemTextView;
        EditText quantityEditText;
        ImageButton deleteButton;

        /** Constructor for the ViewHolder:
         * Connects the ui elements from the layout xml to their view variables.
         * @param view The view in which the ViewHolder is contained
         */
        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.ingredientName);
            unitSystemTextView = view.findViewById(R.id.ingredientUnitSystem);
            quantityEditText = view.findViewById(R.id.ingredientEditQuantity);
            deleteButton = view.findViewById(R.id.deleteIngredient);

            // Quantity text watcher
            textWatcher = new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        float newQuantity = Float.parseFloat(s.toString());
                        ingredients.get(position).setQuantity(newQuantity);
                    }
                }
            };

            if (!editMode) {
                view.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Ingredient ingredient = ingredients.get(position);

                        // Launch IngredientActivity with the ingredient name
                        Intent intent = new Intent(v.getContext(), IngredientActivity.class);
                        intent.putExtra("ingredientName", ingredient.getIngredientName());
                        v.getContext().startActivity(intent);

                        // Optional transition animation
                        ((Activity) v.getContext()).overridePendingTransition(
                                android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                });
            }
        }
    }

    /**
     *
     */
    @NonNull
    @Override
    public MyIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Populate Views in the ViewHolder with data
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Visibility regardless of mode
        Ingredient ingredient = ingredients.get(position);
        // Test regardless of mode
        holder.nameTextView.setText(ingredient.getIngredientName());

        // Editing mode Recycle View
        if (editMode) {
            // Visibility
            holder.quantityEditText.setEnabled(true);
            holder.deleteButton.setVisibility(View.VISIBLE);

            holder.deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(ingredient);
                }
            });

            // Text
            holder.unitSystemTextView.setText(ingredient.getUnitSystem());
            holder.quantityEditText.setText(String.valueOf(ingredient.getQuantity()));
            holder.quantityEditText.addTextChangedListener(textWatcher);
        }
        if (!editMode) {
            // Visibility
            holder.quantityEditText.setEnabled(false);
            holder.deleteButton.setVisibility(View.GONE);

            // Text
            holder.unitSystemTextView.setText(ingredient.getUnitSystem());
            holder.quantityEditText.setText(String.valueOf(ingredient.getQuantity()));
        }
    }

    /** The number of Ingredients in the list
     * @return The total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}