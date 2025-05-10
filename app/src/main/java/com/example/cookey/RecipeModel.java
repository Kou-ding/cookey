package com.example.cookey;

import java.util.List;

public class RecipeModel {

    private long id;
    private String name;
    private int timeToMake;
    private String countryCode;
    private String countryName;
    private int mealNumber;
    private String difficulty;
    private String photoPath; // Changed from byte[]
    private boolean isFavorite;
    private List<String> tags;
    private List<ViewIngredientModel> ingredients;
    private List<StepModel> steps;

    // Default constructor
    public RecipeModel() {
    }

    public RecipeModel(int id, String name, int timeToMake, String countryName, String difficulty, String photoPath, boolean isFavorite,
                       List<String> tags, List<ViewIngredientModel> ingredients, List<StepModel> steps) {
        this.id = id;
        this.name = name;
        this.timeToMake = timeToMake;
        this.countryName = countryName;
        this.difficulty = difficulty;
        this.photoPath = photoPath;
        this.isFavorite = isFavorite;
        this.tags = tags;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getMealNumber() {
        return mealNumber;
    }

    public void setMealNumber(int mealNumber) {
        this.mealNumber = mealNumber;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
