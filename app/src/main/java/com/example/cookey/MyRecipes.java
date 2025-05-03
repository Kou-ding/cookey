package com.example.cookey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MyRecipes {
    private int recipeId;
    private String title;
    private String characteristic;
    private int imageResId;
    private byte[] imageBytes;
    private boolean favorite;

    // Κατασκευαστές
    public MyRecipes() {}

    public MyRecipes(String title, String characteristic, int imageResId) {
        this.title = title;
        this.characteristic = characteristic;
        this.imageResId = imageResId;
        this.favorite = false; // Προεπιλογή: όχι αγαπημένο
    }

    // Getters & Setters
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    // Getters & Setters
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCharacteristic() { return characteristic; }
    public void setCharacteristic(String characteristic) { this.characteristic = characteristic; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public byte[] getImageBytes() { return imageBytes; }
    public void setImageBytes(byte[] imageBytes) { this.imageBytes = imageBytes; }

    public Bitmap getImageAsBitmap() {
        if (imageBytes != null && imageBytes.length > 0) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }
}