package com.example.cookey.ui.MyRecipes;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookey.MyRecipes;
import com.example.cookey.R;

import java.util.List;
public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.RecipeViewHolder> {
    private List<MyRecipes> recipes;
    private OnRecipeClickListener listener;
    public interface OnRecipeClickListener {
        void onRecipeClick(MyRecipes recipe);
        void onFavoriteClick(MyRecipes recipe, boolean isFavorite);
    }
    public MyRecipesAdapter(List<MyRecipes> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        MyRecipes recipe = recipes.get(position);
        if (recipe == null) return;
        holder.recipeTitle.setText(recipe.getTitle());
        holder.recipeCharacteristic.setText(recipe.getCharacteristic());
        // Φόρτωση εικόνας
        if (recipe.getImageBytes() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    recipe.getImageBytes(),
                    0,
                    recipe.getImageBytes().length
            );
            holder.recipeImage.setImageBitmap(bitmap);
        } else {
            holder.recipeImage.setImageResource(recipe.getImageResId());
        }

        // Χειρισμός favorites
        int iconRes = recipe.isFavorite() ?
                R.drawable.favorite_24px : R.drawable.heart_check_24px;
        holder.favoriteButton.setImageResource(iconRes);

        holder.favoriteButton.setOnClickListener(v -> {
            try {
                boolean newState = !recipe.isFavorite();
                recipe.setFavorite(newState);
                holder.favoriteButton.setImageResource(
                        newState ? R.drawable.favorite_24px : R.drawable.heart_check_24px
                );
                if (listener != null) {
                    listener.onFavoriteClick(recipe, newState);
                }
            } catch (Exception e) {
                Log.e("ADAPTER", "Favorite click error", e);
            }
        });

        // Χειρισμός κλικ στη συνταγή
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }


    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        button.setImageResource(
                isFavorite ? R.drawable.favorite_24px : R.drawable.heart_check_24px
        );
    }
    @Override
    public int getItemCount() { return recipes.size(); }
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle, recipeCharacteristic;
        ImageButton favoriteButton;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCharacteristic = itemView.findViewById(R.id.recipeCharacteristic);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
    public void updateData(List<MyRecipes> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged(); // Ενημέρωση RecyclerView
    }
}