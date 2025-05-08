package com.example.cookey;

public class RecipeIngredient {

    private long recipeID;
    private long ingredientID;
    private double quantity;
    private String unit;

    public RecipeIngredient(long recipeID, long ingredientID, double quantity, String unit){
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
    }

    public long getRecipeId() { return recipeID; }
    public long getIngredientId() { return ingredientID; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }

}
