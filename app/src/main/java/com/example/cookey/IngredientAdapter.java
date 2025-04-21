package com.example.cookey;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    private boolean showCombinedQuantityUnit = true;
    private boolean editingEnabled = false;
    private OnDeleteClickListener onDeleteClickListener;
    private final List<Ingredient> ingredients;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Ingredient ingredient);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setShowCombinedQuantityUnit(boolean showCombined) {
        this.showCombinedQuantityUnit = showCombined;
        notifyDataSetChanged();
    }

    public void setEditingEnabled(boolean enabled) {
        this.editingEnabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Ingredient ingredient = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ingredient, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.ingredientName);
            holder.quantityTextView = convertView.findViewById(R.id.ingredientQuantity);
            holder.quantityEditText = convertView.findViewById(R.id.ingredientEditQuantity);
            holder.unitSystemTextView = convertView.findViewById(R.id.ingredientUnitSystem);
            holder.quantityUnitTextView = convertView.findViewById(R.id.ingredientQuantityUnit);
            holder.deleteButton = convertView.findViewById(R.id.deleteIngredient);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (ingredient != null) {
            holder.nameTextView.setText(ingredient.getIngredientName());

            if (editingEnabled) {
                holder.quantityEditText.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.quantityTextView.setVisibility(View.GONE);
                holder.quantityUnitTextView.setVisibility(View.GONE);

                // ✅ Keep unit system visible during edit
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
                            // Invalid number input, do nothing
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

        return convertView;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    static class ViewHolder {
        TextView nameTextView, quantityTextView, unitSystemTextView, quantityUnitTextView;
        EditText quantityEditText;
        ImageButton deleteButton;
    }
}
