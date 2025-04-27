package com.example.cookey;

import java.util.List;
public class RecipeModel {

    //This class models a recipe. "FULL Recipe" in other words
    // Will be used to show everything in the edit-recipe function of the project

    private int id;
    private String name;
    private int timeToMake;
    private String countryName; // INT?
    private String difficulty;
    private byte[] photo;
    private boolean isFavorite;
    private List<String> tags;
    private List<ViewIngredientModel> ingredients;
    private List<StepModel> steps;

    public RecipeModel(int id, String name, int timeToMake, String countryName, String difficulty, byte[] photo, boolean isFavorite,
                       List<String> tags, List<ViewIngredientModel> ingredients, List<StepModel> steps){
        this.id = id;
        this.name = name;
        this.timeToMake = timeToMake;
        this.countryName = countryName;
        this.difficulty = difficulty;
        this.photo = photo;
        this.isFavorite = isFavorite;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeToMake() {
        return timeToMake;
    }

    public void setTimeToMake(int timeToMake) {
        this.timeToMake = timeToMake;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<ViewIngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ViewIngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<StepModel> steps) {
        this.steps = steps;
    }

}
