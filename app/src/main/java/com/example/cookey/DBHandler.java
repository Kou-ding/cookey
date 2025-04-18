package com.example.cookey;

import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cookeyDB.db";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE Recipe (\n" +
                "  idRecipe INTEGER  NOT NULL  ,\n" +
                "  name VARCHAR    ,\n" +
                "  timeToMake INTEGER    ,\n" +
                "  country INTEGER    ,\n" +
                "  mealNumber INTEGER    ,\n" +
                "  difficulty ENUM    ,\n" +
                "  photo BLOB    ,\n" +
                "  favourites BOOL      ,\n" +
                "PRIMARY KEY(idRecipe));\n" +
                "\n" +
                "CREATE TABLE Steps (\n" +
                "  idSteps INTEGER  NOT NULL  ,\n" +
                "  text VARCHAR    ,\n" +
                "  numberOfStep INTEGER      ,\n" +
                "PRIMARY KEY(idSteps));\n" +
                "\n" +
                "CREATE TABLE ShoppingList (\n" +
                "  idShoppingList INTEGER  NOT NULL    ,\n" +
                "PRIMARY KEY(idShoppingList));\n" +
                "\n" +
                "CREATE TABLE AIRecipe (\n" +
                "  idAIRecipe INTEGER  NOT NULL  ,\n" +
                "  AIRecipeText VARCHAR      ,\n" +
                "PRIMARY KEY(idAIRecipe));\n" +
                "\n" +
                "\n" +
                "CREATE TABLE Tags (\n" +
                "  idTags INTEGER  NOT NULL  ,\n" +
                "  name VARCHAR      ,\n" +
                "PRIMARY KEY(idTags));\n" +
                "\n" +
                "\n" +
                "CREATE TABLE MyIngredients (\n" +
                "  idMyIngredients INTEGER  NOT NULL    ,\n" +
                "PRIMARY KEY(idMyIngredients));\n" +
                "\n" +
                "CREATE TABLE Recipe_has_Tags (\n" +
                "  Recipe_idRecipe INTEGER  NOT NULL  ,\n" +
                "  Tags_idTags INTEGER  NOT NULL    ,\n" +
                "PRIMARY KEY(Recipe_idRecipe, Tags_idTags)    ,\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Tags_idTags)\n" +
                "    REFERENCES Tags(idTags)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION);\n" +
                "\n" +
                "CREATE INDEX Recipe_has_Tags_FKIndex1 ON Recipe_has_Tags (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Tags_FKIndex2 ON Recipe_has_Tags (Tags_idTags);\n" +
                "\n" +
                "CREATE TABLE Ingredient (\n" +
                "  idIngredient INTEGER  NOT NULL  ,\n" +
                "  MyIngredients_idMyIngredients INTEGER  NOT NULL  ,\n" +
                "  ShoppingList_idShoppingList INTEGER  NOT NULL  ,\n" +
                "  name VARCHAR    ,\n" +
                "  quantity FLOAT    ,\n" +
                "  measurement VARCHAR    ,\n" +
                "  expirationTime DATE    ,\n" +
                "  boughtTime DATE      ,\n" +
                "PRIMARY KEY(idIngredient)    ,\n" +
                "  FOREIGN KEY(ShoppingList_idShoppingList)\n" +
                "    REFERENCES ShoppingList(idShoppingList)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(MyIngredients_idMyIngredients)\n" +
                "    REFERENCES MyIngredients(idMyIngredients)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION);\n" +
                "\n" +
                "CREATE INDEX Ingredient_FKIndex1 ON Ingredient (ShoppingList_idShoppingList);\n" +
                "CREATE INDEX Ingredient_FKIndex2 ON Ingredient (MyIngredients_idMyIngredients);\n" +
                "\n" +
                "CREATE TABLE Recipe_has_Ingredient (\n" +
                "  Recipe_idRecipe INTEGER  NOT NULL  ,\n" +
                "  Ingredient_idIngredient INTEGER  NOT NULL    ,\n" +
                "PRIMARY KEY(Recipe_idRecipe, Ingredient_idIngredient)    ,\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Ingredient_idIngredient)\n" +
                "    REFERENCES Ingredient(idIngredient)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION);\n" +
                "\n" +
                "\n" +
                "CREATE INDEX Recipe_has_Ingredient_FKIndex1 ON Recipe_has_Ingredient (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Ingredient_FKIndex2 ON Recipe_has_Ingredient (Ingredient_idIngredient);\n" +
                "\n" +
                "CREATE TABLE Recipe_has_Steps (\n" +
                "  Recipe_idRecipe INTEGER  NOT NULL  ,\n" +
                "  Steps_idSteps INTEGER  NOT NULL    ,\n" +
                "PRIMARY KEY(Recipe_idRecipe, Steps_idSteps)    ,\n" +
                "  FOREIGN KEY(Recipe_idRecipe)\n" +
                "    REFERENCES Recipe(idRecipe)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION,\n" +
                "  FOREIGN KEY(Steps_idSteps)\n" +
                "    REFERENCES Steps(idSteps)\n" +
                "      ON DELETE NO ACTION\n" +
                "      ON UPDATE NO ACTION);\n" +
                "\n" +
                "CREATE INDEX Recipe_has_Steps_FKIndex1 ON Recipe_has_Steps (Recipe_idRecipe);\n" +
                "CREATE INDEX Recipe_has_Steps_FKIndex2 ON Recipe_has_Steps (Steps_idSteps);\n" +
                "\n" +
                "\n" +
                "\n";
        // Split and run each statement
        for (String statement : query.split(";")) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                db.execSQL(statement + ";");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades
    }
}