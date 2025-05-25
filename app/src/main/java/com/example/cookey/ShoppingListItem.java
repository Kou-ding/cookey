package com.example.cookey;

public class ShoppingListItem {
    private int shoppingListItemId;
    private String shoppingListItemName;
    private float purchasedQuantity;
    private boolean isFood;
    private boolean isChecked;

    public ShoppingListItem() {
        // empty constructor
    }
    public ShoppingListItem(int shoppingListItemId, String shoppingListItemName, float purchasedQuantity, boolean isFood, boolean isChecked) {
        this.shoppingListItemId = shoppingListItemId;
        this.shoppingListItemName = shoppingListItemName;
        this.purchasedQuantity = purchasedQuantity;
        this.isFood = isFood;
        this.isChecked = isChecked;
    }

    // Shopping List Item Id
    public void setShoppingListItemId(int shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }
    public int getShoppingListItemId(){
        return shoppingListItemId;
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

    // Is Food
    public void setIsFood(boolean isFood) {
        this.isFood = isFood;
    }
    public boolean getIsFood() {
        return isFood;
    }

    // Is Checked
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public boolean getIsChecked() {
        return isChecked;
    }
}
