package com.example.cookey.ui.MyRecipes;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.R;
import com.example.cookey.RecipeModel;

import java.io.File;
import java.util.List;
public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeModel recipe);

        void onFavoriteClick(RecipeModel recipe, boolean isFavorite);

    }

    private List<RecipeModel> recipes;
    private OnRecipeClickListener listener;

    public MyRecipesAdapter(List<RecipeModel> recipes, OnRecipeClickListener listener) {
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
        RecipeModel recipe = recipes.get(position);
        if (recipe == null) return;

        //Title
        holder.titleTv.setText(recipe.getName());

        //Time + Difficulty
        holder.characteristicTv.setText(recipe.getTimeToMake() + " mins | " + recipe.getDifficulty());

        // Φόρτωση εικόνας
        String path = recipe.getPhotoPath();
        if (path != null && !path.isEmpty()) {
            File f = new File(path);
            if (f.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(path);
                holder.imageIv.setImageBitmap(bmp);
            } else {
                holder.imageIv.setImageResource(R.drawable.cookie_40px);
            }
        } else {
            holder.imageIv.setImageResource(R.drawable.cookie_40px);
        }

        // Χειρισμός favorites
        setFavIcon(holder.favBtn, recipe.isFavorite());

        /* ----- LISTENERS ----- */
        holder.favBtn.setOnClickListener(v -> {
            boolean newFav = !recipe.isFavorite();
            recipe.setFavorite(newFav);           // ενημέρωσε το αντικείμενο
            setFavIcon(holder.favBtn, newFav);
            if (listener != null) listener.onFavoriteClick(recipe, newFav);  // => Fragment → DB
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });
    }


    @Override
    public int getItemCount() {
        return (recipes == null) ? 0 : recipes.size();
    }

    //Δημόσια μέθοδος ανανέωσης λίστας
    public void updateData(List<RecipeModel> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }

    private void setFavIcon(ImageButton btn, boolean fav) {
        btn.setImageResource(fav ? R.drawable.heart_check_24px     // γεμάτη καρδιά
                : R.drawable.favorite_24px); // κενή καρδιά
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIv;
        TextView  titleTv, characteristicTv;
        ImageButton favBtn;

        RecipeViewHolder(@NonNull View v) {
            super(v);
            imageIv          = v.findViewById(R.id.recipeImage);
            titleTv          = v.findViewById(R.id.recipeTitle);
            characteristicTv = v.findViewById(R.id.recipeCharacteristic);
            favBtn           = v.findViewById(R.id.favoriteButton);
        }
    }
}