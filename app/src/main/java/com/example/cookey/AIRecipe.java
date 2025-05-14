package com.example.cookey;

public class AIRecipe {
    private int AIRecipeId;
    private String AIRecipeText;

    public AIRecipe(){
        // empty constructor
    }
    public AIRecipe(int AIRecipeId, String AIRecipeText){
        this.AIRecipeId = AIRecipeId;
        this.AIRecipeText = AIRecipeText;
    }
    public int getAIRecipeId(){
        return AIRecipeId;
    }
    public void setAIRecipeId(int AIRecipeId){
        this.AIRecipeId = AIRecipeId;
    }
    public String getAIRecipeText(){
        return AIRecipeText;
    }
    public void setAIRecipeText(String AIRecipeText){
        this.AIRecipeText = AIRecipeText;
    }
}
