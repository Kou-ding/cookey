package com.example.cookey;

public class SelectedIngredient {

    private IngredientModel ingredient;
    private double quantity;

    public SelectedIngredient(IngredientModel ingredient, double quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public IngredientModel getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientModel ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


    public String getName() {
        return ingredient.getName();
    }

    public String getUnit() {
        return ingredient.getUnit();
    }

}
