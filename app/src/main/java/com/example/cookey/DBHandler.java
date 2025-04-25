package com.example.cookey;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cookeyDB.db";
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_SCRIPT =
                "CREATE TABLE Recipe (\n" +
                "  idRecipe INTEGER NOT NULL,\n" +
                "  name VARCHAR,\n" +
                "  timeToMake INTEGER,\n" +
                "  country INTEGER,\n" +
                "  mealNumber INTEGER,\n" +
                "  difficulty ENUM,\n" +
                "  photo BLOB,\n" +
                "  favourites BOOL,\n" +
                "  PRIMARY KEY(idRecipe)\n" +
                ");\n\n" +

                "CREATE TABLE Steps (\n" +
                "  idSteps INTEGER NOT NULL,\n" +
                "  text VARCHAR,\n" +
                "  numberOfStep INTEGER,\n" +
                "  PRIMARY KEY(idSteps)\n" +
                ");\n\n" +

                "CREATE TABLE Tags (\n" +
                "  idTags INTEGER NOT NULL,\n" +
                "  name VARCHAR,\n" +
                "  PRIMARY KEY(idTags)\n" +
                ");\n\n" +

                "CREATE TABLE Recipe_has_Tags (\n" +
                "  Recipe_idRecipe INTEGER NOT NULL,\n" +
                "  Tags_idTags INTEGER NOT NULL,\n" +
                "  PRIMARY KEY(Recipe_idRecipe, Tags_idTags),\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Tags_idTags)\n" +
                "    REFERENCES Tags(idTags)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION\n" +
                ");\n\n" +

                "CREATE INDEX Recipe_has_Tags_FKIndex1 ON Recipe_has_Tags (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Tags_FKIndex2 ON Recipe_has_Tags (Tags_idTags);\n\n" +

                "CREATE TABLE Recipe_has_Ingredient (\n" +
                "  Recipe_idRecipe INTEGER NOT NULL,\n" +
                "  Ingredient_ingredientId INTEGER NOT NULL,\n" +
                "  PRIMARY KEY(Recipe_idRecipe, Ingredient_ingredientId),\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Ingredient_ingredientId)\n" +
                "    REFERENCES Ingredient(ingredientId)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION\n" +
                ");\n\n" +

                "CREATE INDEX Recipe_has_Ingredient_FKIndex1 ON Recipe_has_Ingredient (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Ingredient_FKIndex2 ON Recipe_has_Ingredient (Ingredient_ingredientId);\n\n" +

                "CREATE TABLE Recipe_has_Steps (\n" +
                "  Recipe_idRecipe INTEGER NOT NULL,\n" +
                "  Steps_idSteps INTEGER NOT NULL,\n" +
                "  PRIMARY KEY(Recipe_idRecipe, Steps_idSteps),\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Steps_idSteps)\n" +
                "    REFERENCES Steps(idSteps)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION\n" +
                ");\n\n" +

                "CREATE INDEX Recipe_has_Steps_FKIndex1 ON Recipe_has_Steps (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Steps_FKIndex2 ON Recipe_has_Steps (Steps_idSteps);\n\n" +

                "CREATE TABLE ShoppingList (\n" +
                "  shoppingListItemName VARCHAR,\n" +
                "  purchasedQuantity FLOAT,\n" +
                "  purchaseDate DATE,\n" +
                "  isFood BOOLEAN,\n" +
                "  PRIMARY KEY(shoppingListItemName)\n" +
                ");\n\n" +

                "CREATE TABLE AIRecipe (\n" +
                "  AIRecipeId INTEGER NOT NULL,\n" +
                "  AIRecipeText VARCHAR,\n" +
                "  PRIMARY KEY(AIRecipeId)\n" +
                ");\n\n" +

                "CREATE TABLE Ingredient (\n" +
                "  ingredientId INTEGER NOT NULL,\n" +
                "  ingredientName VARCHAR,\n" +
                "  quantity FLOAT,\n" +
                "  unitSystem VARCHAR,\n" +
                "  daysToSpoil INTEGER,\n" +
                "  checkIfSpoiledArray VARCHAR,\n" +
                "  PRIMARY KEY(ingredientId)\n" +
                ");";
        // Split and run each statement
        for (String statement : DATABASE_CREATE_SCRIPT.split(";")) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                db.execSQL(statement + ";");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades
        db.execSQL("DROP TABLE IF EXISTS Recipe");
        db.execSQL("DROP TABLE IF EXISTS Steps");
        db.execSQL("DROP TABLE IF EXISTS Tags");
        db.execSQL("DROP TABLE IF EXISTS Recipe_has_Steps");
        db.execSQL("DROP TABLE IF EXISTS Recipe_has_Ingredient");
        db.execSQL("DROP TABLE IF EXISTS Recipe_has_Tags");

        db.execSQL("DROP TABLE IF EXISTS ShoppingList");
        db.execSQL("DROP TABLE IF EXISTS AIRecipe");
        db.execSQL("DROP TABLE IF EXISTS Ingredient");

        // Recreate tables
        onCreate(db);
    }

    
    // Ingredients Section //
    // Return an array of all my Ingredients
    public List<Ingredient> getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Ingredient WHERE quantity > 0";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientId(cursor.getInt(0));
                    ingredient.setIngredientName(cursor.getString(1));
                    ingredient.setQuantity(cursor.getFloat(2));
                    ingredient.setUnitSystem(cursor.getString(3));
                    ingredient.setDaysToSpoil(cursor.getInt(4));
                    ingredient.setCheckIfSpoiledArray(cursor.getString(5));

                    ingredients.add(ingredient);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return ingredients;
    }
    public Ingredient getIngredient(String ingredientName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Ingredient ingredient = null;
        Cursor cursor = null;

        try {
            // Use rawQuery with parameter binding for safety
            String query = "SELECT * FROM Ingredient WHERE ingredientName = ?";
            cursor = db.rawQuery(query, new String[]{ingredientName});

            if (cursor != null && cursor.moveToFirst()) {
                ingredient = new Ingredient();
                ingredient.setIngredientId(cursor.getInt(0));
                ingredient.setIngredientName(cursor.getString(1));
                ingredient.setQuantity(cursor.getFloat(2));
                ingredient.setUnitSystem(cursor.getString(3));
                ingredient.setDaysToSpoil(cursor.getInt(4));
                ingredient.setCheckIfSpoiledArray(cursor.getString(5));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return ingredient;
    }
    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Ingredient SET quantity = 0 WHERE ingredientId = " + ingredientId + ";";
        db.execSQL(query);//now instead of deleting we just make the quantity 0

        db.close();
    }
    public String[] similarIngredients(String searchIngredient){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Ingredient " +
                "WHERE ingredientName LIKE '%" + searchIngredient + "%';";
        Cursor cursor = db.rawQuery(query, null);
        int ingredientCount = cursor.getCount();
        Ingredient[] ingredients = new Ingredient[ingredientCount];
        int i = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientId(Integer.parseInt(cursor.getString(0)));
                ingredient.setIngredientName(cursor.getString(1));
                ingredient.setQuantity(Float.parseFloat(cursor.getString(2)));
                ingredient.setUnitSystem(cursor.getString(3));
                ingredient.setDaysToSpoil(Integer.parseInt(cursor.getString(4)));
                ingredients[i] = ingredient;
                cursor.moveToNext();
                i++;
            }
        }
        cursor.close();
        db.close();

        String[] ingredientNames = new String[ingredients.length];
        for (int j = 0; j < ingredients.length; j++){
            ingredientNames[j] = ingredients[j].getIngredientName();
        }

        return ingredientNames;
    }
    public void setNewQuantity(String ingredientName, float newQuantity){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Ingredient SET quantity = " + newQuantity + " WHERE ingredientName = '" + ingredientName + "';";
        db.execSQL(query);
        db.close();
    }

    // For Database debugging //
    // Delete all ingredients from the database
    public void dropDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Remove all ingredients from the Ingredient table
        db.execSQL("DELETE FROM Ingredient;");
        db.close();
    }
    // Execute a command on the database
    public void executeCommand(String command){
        SQLiteDatabase db = this.getWritableDatabase();
        for (String statement : command.split(";")) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                db.execSQL(statement + ";");
            }
        }
        db.close();
    }

    // AI Recipe Section //
    // During AI Recipe creation make sure the AI Recipe ID is unique
    public int getNextUnusedAIRecipeId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // Default if table is empty

        try {
            Cursor cursor = db.rawQuery("SELECT MAX(AIRecipeId) FROM AIRecipe", null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                nextId = cursor.getInt(0) + 1;
            }
            cursor.close();
        } finally {
            db.close();
        }
        return nextId;
    }
    // Add an AI recipe to the database
    public void registerAIRecipe(AIRecipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        String registration_query = "INSERT INTO AIRecipe (AIRecipeId, AIRecipeText)" +
                "VALUES ('" + recipe.getAIRecipeId() + "', '" + recipe.getAIRecipeText() + "');";

        db.execSQL(registration_query);
        db.close();
    }
    // Get an AI recipe from the database
    public AIRecipe getAIRecipe(int AIRecipeId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM AIRecipe " +
                "WHERE AIRecipeId='" + AIRecipeId + "';";
        Cursor cursor = db.rawQuery(query, null);
        AIRecipe recipe = new AIRecipe();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipe.setAIRecipeId(Integer.parseInt(cursor.getString(0)));
            recipe.setAIRecipeText(cursor.getString(1));
            cursor.close();
        } else {
            recipe = null;
        }
        return recipe;
    }
}