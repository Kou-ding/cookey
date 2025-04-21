package com.example.cookey;

public class Ingredient {
    private int ingredientId; // id of the ingredient
    private String ingredientName; // name of the ingredient
    private float quantity; // total quantity of the ingredient
    private String unitSystem;
    // Arrays that hold different purchases of the same ingredient
    private String timeToSpoil; // the time it take for the ingredient to spoil (days)
    private String checkIfSpoiledArray; // the date you should check if something has spoiled

    public Ingredient(){
        // empty constructor
    }
    public Ingredient(int ingredientId, String ingredientName, float quantity, String unitSystem, String timeToSpoil, String purchaseDate){
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unitSystem = unitSystem;
        this.timeToSpoil = timeToSpoil;
    }

    public int getIngredientId(){
        return ingredientId;
    }
    public void setIngredientId(int ingredientId){
        this.ingredientId = ingredientId;
    }

    public String getIngredientName(){
        return ingredientName;
    }
    public void setIngredientName(String ingredientName){
        this.ingredientName = ingredientName;
    }

    public float getQuantity(){
        return quantity;
    }
    public void setQuantity(float quantity){
        this.quantity = quantity;
    }

    public String getUnitSystem(){
        return unitSystem;
    }
    public void setUnitSystem(String unitSystem){
        this.unitSystem = unitSystem;
    }

    public String getTimeToSpoil(){
        return timeToSpoil;
    }
    public void setTimeToSpoil(String timeToSpoil){
        this.timeToSpoil = timeToSpoil;
    }

    public String getCheckIfSpoiledArray(){
        return checkIfSpoiledArray;
    }
    public void setCheckIfSpoiledArray(String checkIfSpoiledArray){
        this.checkIfSpoiledArray = checkIfSpoiledArray;
    }

}
