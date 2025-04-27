package com.example.cookey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<IngredientModel> ingredientList;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(IngredientModel ingredient);
    }

    public IngredientAdapter(List<IngredientModel> ingredientList, OnIngredientClickListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientModel ingredient = ingredientList.get(position);
      //  holder.textViewName.setText(ingredient.getName());

        String displayText = ingredient.getName();
        if(ingredient.getUnit() != null && !ingredient.getUnit().isEmpty()){
            displayText += " (" + ingredient.getUnit() + ")";
        }
        holder.textViewName.setText(displayText);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIngredientClick(ingredient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewUnit;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewIngredientName);
        }
    }
}
