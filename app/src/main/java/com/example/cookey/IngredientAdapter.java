package com.example.cookey;

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

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<IngredientModel> allIngredients;
    private List<SelectedIngredient> selectedIngredients;

    public IngredientAdapter(List<IngredientModel> allIngredients) {
        this.allIngredients = new ArrayList<>(allIngredients);
        this.selectedIngredients = new ArrayList<>();
        for (IngredientModel model : allIngredients) {
            selectedIngredients.add(new SelectedIngredient(model, 0));
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selectable_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        SelectedIngredient selected = selectedIngredients.get(position);
        IngredientModel ingredient = selected.getIngredient();

        holder.nameText.setText(ingredient.getName());
        holder.unitText.setText(ingredient.getUnit());

        // avoid multiple listeners
        holder.quantityEdit.setText("");
        holder.quantityEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                try {
                    double qty = Double.parseDouble(s.toString());
                    selected.setQuantity(qty);
                } catch (NumberFormatException e) {
                    selected.setQuantity(0);
                }
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                selectedIngredients.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedIngredients.size();
    }

    public List<SelectedIngredient> getSelectedIngredients() {
        List<SelectedIngredient> result = new ArrayList<>();
        for (SelectedIngredient ing : selectedIngredients) {
            if (ing.getQuantity() > 0) {
                result.add(ing);
            }
        }
        return result;
    }

    public void filter(String query) {
        selectedIngredients.clear();
        for (IngredientModel ing : allIngredients) {
            if (ing.getName().toLowerCase().contains(query.toLowerCase())) {
                selectedIngredients.add(new SelectedIngredient(ing, 0));
            }
        }
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, unitText;
        EditText quantityEdit;
        ImageButton deleteButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textViewIngredientName);
            unitText = itemView.findViewById(R.id.textViewUnit);
            quantityEdit = itemView.findViewById(R.id.editTextQuantity);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
