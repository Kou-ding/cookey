package com.example.cookey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class RecipeFull extends MyRecipes {
    private List<String> ingredients;
    private List<String> steps;
    private List<String> tags;

    public RecipeFull() {
        super();
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public RecipeFull(MyRecipes basicRecipe) {
        super(basicRecipe.getTitle(), basicRecipe.getCharacteristic(), basicRecipe.getImageResId());
        this.setRecipeId(basicRecipe.getRecipeId());
        this.setImageBytes(basicRecipe.getImageBytes());
        this.setFavorite(basicRecipe.isFavorite());
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
        tags = new ArrayList<>();
    }

    // Getters and Setters
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void addStep(String step) {
        this.steps.add(step);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    // Helper method to convert to basic recipe
    public MyRecipes toBasicRecipe() {
        MyRecipes basic = new MyRecipes();
        basic.setRecipeId(this.getRecipeId());
        basic.setTitle(this.getTitle());
        basic.setCharacteristic(this.getCharacteristic());
        basic.setImageResId(this.getImageResId());
        basic.setImageBytes(this.getImageBytes());
        basic.setFavorite(this.isFavorite());
        return basic;
    }

    @Override
    public String toString() {
        return "RecipeFull{" +
                "id=" + getRecipeId() +
                ", title='" + getTitle() + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                ", tags=" + tags +
                '}';
    }
}