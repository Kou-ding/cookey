package com.example.cookey;

public class ShoppingListItem {
    private String shoppingListItemName;
    private float purchasedQuantity;
    private String purchaseDate;
    private boolean isFood;

    public ShoppingListItem() {
        // empty constructor
    }
    public ShoppingListItem(String shoppingListItemName, float purchasedQuantity, String purchaseDate, boolean isFood) {
        this.shoppingListItemName = shoppingListItemName;
        this.purchasedQuantity = purchasedQuantity;
        this.purchaseDate = purchaseDate;
        this.isFood = isFood;
    }
    // Ingredient Name
    public void setShoppingListItemName(String shoppingListItemName) {
        this.shoppingListItemName = shoppingListItemName;
    }
    public String getShoppingListItemName(){
        return shoppingListItemName;
    }

    // Purchased Quantity
    public void setPurchasedQuantity(float purchasedQuantity) {
        this.purchasedQuantity = purchasedQuantity;
    }
    public float getPurchasedQuantity(){
        return purchasedQuantity;
    }

    // Purchase Date
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public String getPurchaseDate(){
        return purchaseDate;
    }

    // Is Food
    public void setIsFood(boolean isFood) {
        this.isFood = isFood;
    }
    public boolean getIsFood() {
        return isFood;
    }
}
