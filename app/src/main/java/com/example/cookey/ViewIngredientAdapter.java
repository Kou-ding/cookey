package com.example.cookey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewIngredientAdapter extends RecyclerView.Adapter<ViewIngredientAdapter.ViewIngredientViewHolder> {

    private final List<ViewIngredientModel> ingredients;

    public ViewIngredientAdapter(List<ViewIngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewIngredientViewHolder holder, int position) {
        ViewIngredientModel ingredient = ingredients.get(position);
        holder.textViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.textViewUnit.setText(ingredient.getUnit());
        holder.textViewName.setText(ingredient.getName());

        // Accessibility - Narrator must read the full Ingredient description
        String fullDescription = String.format("%.0f %s %s",
                ingredient.getQuantity(),
                ingredient.getUnit(),
                ingredient.getName());

        holder.itemView.setOnClickListener(v ->
                NarratorManager.speakIfEnabled(v.getContext(), fullDescription));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class ViewIngredientViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity, textViewUnit, textViewName;

        public ViewIngredientViewHolder(View itemView) {
            super(itemView);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewUnit = itemView.findViewById(R.id.textViewUnit);
            textViewName = itemView.findViewById(R.id.textViewIngredientName);
        }
    }
}
