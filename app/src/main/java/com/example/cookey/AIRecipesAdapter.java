package com.example.cookey;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AIRecipesAdapter extends RecyclerView.Adapter<AIRecipesAdapter.ViewHolder> {
    public List<AIRecipe> ai_recipes;

    public AIRecipesAdapter(List<AIRecipe> ai_recipes) {
        this.ai_recipes = ai_recipes;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView AIRecipeId;
        TextView AIRecipeDesc;

        public ViewHolder(View view) {
            super(view);
            AIRecipeId = view.findViewById(R.id.AIRecipeId);
            AIRecipeDesc = view.findViewById(R.id.AIRecipeDesc);

            view.setOnClickListener(new View.OnClickListener() {
                // Open AI Recipe View Activity
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AIRecipeViewActivity.class);
                    intent.putExtra("AIRecipeId", AIRecipeId.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    //Methods that must be implemented for a RecyclerView.Adapter
    @NonNull
    @Override
    public AIRecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ai_recipes_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AIRecipesAdapter.ViewHolder holder, int position) {
        AIRecipe ai_recipe = ai_recipes.get(position);

        holder.AIRecipeId.setText(String.format("Recipe%s", String.valueOf(ai_recipe.getAIRecipeId())));
        holder.AIRecipeDesc.setText(ai_recipe.getAIRecipeText());
    }

    @Override
    public int getItemCount() {
        return ai_recipes.size();
    }
}
