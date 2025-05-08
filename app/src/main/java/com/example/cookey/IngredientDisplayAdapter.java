package com.example.cookey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class IngredientDisplayAdapter extends RecyclerView.Adapter<IngredientDisplayAdapter.ViewHolder> {

    private List<SelectedIngredient> ingredients;

    public IngredientDisplayAdapter(List<SelectedIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false); // το αρχικό view-only layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectedIngredient ing = ingredients.get(position);
        holder.name.setText(ing.getName());
        holder.details.setText(ing.getQuantity() + " " + ing.getUnit());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewIngredientName);
            details = itemView.findViewById(R.id.textViewUnit); // reuse του TextView για μονάδα
        }
    }

}
