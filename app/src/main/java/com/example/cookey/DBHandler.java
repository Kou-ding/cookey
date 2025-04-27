package com.example.cookey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cookeyDB.db";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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




    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Ingredient SET quantity = 0 WHERE ingredientId = " + ingredientId + ";";
        db.execSQL(query);//now instead of deleting we just make the quantity 0

        db.close();
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
        db.execSQL("DELETE FROM ShoppingList;");
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


    // Shopping List section


    public void deleteShoppingListItem(String shoppingListItemName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList WHERE shoppingListItemName = '" + shoppingListItemName + "';";
        db.execSQL(query);
        db.close();
    }





    public void newShoppingList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList;";
        db.execSQL(query);
        db.close();
    }

    public void addFoodItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO ShoppingList VALUES ('', 0, '', 1);";
        db.execSQL(query);
        db.close();
    }
    public void addNonFoodItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO ShoppingList VALUES ('', 0, '', 0);";
        db.execSQL(query);
        db.close();
    }

    public long addRecipe(String name, int timeToMake, int countryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = -1;

        try {
            String sql = "INSERT INTO Recipe (name, timeToMake, country) VALUES (?, ?, ?)";
            db.execSQL(sql, new Object[]{name, timeToMake, countryId});

            //Take the Last id
            Cursor cursor = db.rawQuery("SELECT last_insert_rowid()",null);
            if(cursor.moveToFirst()){
                id = cursor.getLong(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    //Add a step
    public boolean addStep(String description, long recipeId){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            String sql = "INSERT INTO Steps (description, idRecipe) VALUES (?,?)";
            db.execSQL(sql, new Object[]{description,recipeId});
            success = true;
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return success;
    }

    //Add Ingriedent
    public boolean addIngredientToRecipe(long recipeId, long ingredientId, double quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            String sql = "INSERT INTO Recipe_has_Ingredient (idRecipe, idIngredient, quantity) VALUES (?, ?, ?)";
            db.execSQL(sql, new Object[]{recipeId, ingredientId, quantity});
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }
}