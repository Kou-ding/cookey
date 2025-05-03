package com.example.cookey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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
                        "  favorites INTEGER DEFAULT 0,\n" +
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
                        "CREATE INDEX Recipe_has_Steps_FKIndex2 ON Recipe_has_Steps (Steps_idSteps);\n\n";
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

    public List<MyRecipes> getRecipes() {
        List<MyRecipes> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT idRecipe, name, timeToMake, difficulty FROM Recipe";
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int time = cursor.getInt(2);
                    String difficulty = cursor.getString(3);
                    // Δημιουργία χαρακτηριστικού (π.χ. "20 mins | Medium")
                    String characteristic = time + " mins | " + difficulty;
                    // Προσαρμογή: Θα χρησιμοποιήσουμε μια default εικόνα μέχρι να υλοποιηθεί η φόρτωση από τη βάση
                    int imageResId = R.drawable.lunch_dining_24px;
                    recipes.add(new MyRecipes(name, characteristic, imageResId));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return recipes;
    }

    public long addRecipe(MyRecipes recipe, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", recipe.getTitle());
        values.put("timeToMake", extractTimeFromCharacteristic(recipe.getCharacteristic()));
        values.put("difficulty", extractDifficultyFromCharacteristic(recipe.getCharacteristic()));
        // Μετατροπή Bitmap σε byte array
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            values.put("photo", byteArray);
        }
        long id = db.insert("Recipe", null, values);
        db.close();
        return id;
    }

    // Μέθοδος για λήψη όλων των συνταγών
    public List<MyRecipes> getAllRecipes() {
        List<MyRecipes> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT idRecipe, name, timeToMake, difficulty, photo FROM Recipe";
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MyRecipes recipe = new MyRecipes();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(1));
                    int time = cursor.getInt(2);
                    String difficulty = cursor.getString(3);
                    recipe.setCharacteristic(time + " mins | " + difficulty);
                    // Ανάγνωση εικόνας από BLOB
                    byte[] imageBytes = cursor.getBlob(4);
                    if (imageBytes != null && imageBytes.length > 0) {
                        // Αποθήκευση του byte array για μελλοντική χρήση
                        recipe.setImageBytes(imageBytes);
                    } else {
                        recipe.setImageResId(R.drawable.lunch_dining_24px); // Default εικόνα
                    }
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHandler", "Error getting recipes", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return recipes;
    }

    // Μέθοδος για λήψη συγκεκριμένης συνταγής
    public MyRecipes getRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        MyRecipes recipe = null;
        Cursor cursor = null;
        try {
            String query = "SELECT idRecipe, name, timeToMake, difficulty, photo FROM Recipe WHERE idRecipe = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(recipeId)});
            if (cursor != null && cursor.moveToFirst()) {
                recipe = new MyRecipes();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setTitle(cursor.getString(1));
                int time = cursor.getInt(2);
                String difficulty = cursor.getString(3);
                recipe.setCharacteristic(time + " mins | " + difficulty);
                byte[] imageBytes = cursor.getBlob(4);
                if (imageBytes != null && imageBytes.length > 0) {
                    recipe.setImageBytes(imageBytes);
                } else {
                    recipe.setImageResId(R.drawable.lunch_dining_24px);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return recipe;
    }

    // Βοηθητικές μέθοδοι
    private int extractTimeFromCharacteristic(String characteristic) {
        try{
            String[] parts = characteristic.split("\\|");
            if (parts.length > 0) {
                String timePart = parts[0].trim().replace("mins", "").trim();
                return Integer.parseInt(timePart);
            }
        } catch (Exception e) {
            Log.e("DBHandler", "Error parsing time", e);
        }
        return 0;
    }

    private String extractDifficultyFromCharacteristic(String characteristic) {
        try {
            String[] parts = characteristic.split("\\|");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        } catch (Exception e) {
            Log.e("DBHandler", "Error parsing difficulty", e);
        }
        return "Medium";
    }

    public DBHandler(Context context) {
        super(context, "cookeyDB.db", null, 1);
    }
    public List<MyRecipes> getRecipesByDifficulty(String difficulty) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Recipe WHERE favorites = 1",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                MyRecipes recipe = new MyRecipes();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setTitle(cursor.getString(1));
                int time = cursor.getInt(2);
                String diff = cursor.getString(3);
                recipe.setCharacteristic(time + " mins | " + diff);
                // Επεξεργασία εικόνας
                byte[] imageBytes = cursor.getBlob(4);
                if (imageBytes != null && imageBytes.length > 0) {
                    recipe.setImageBytes(imageBytes);
                } else {
                    recipe.setImageResId(R.drawable.lunch_dining_24px);
                }
                recipe.setFavorite(cursor.getInt(5) == 1);
                results.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }

    public List<MyRecipes> searchRecipes(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Recipe WHERE name LIKE ?",
                new String[]{"%" + query + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                MyRecipes recipe = new MyRecipes();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setTitle(cursor.getString(1));
                recipe.setCharacteristic(cursor.getInt(2) + " mins | " + cursor.getString(3));
                recipe.setFavorite(cursor.getInt(7) == 1);
                results.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }

    public List<MyRecipes> getFavoriteRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT idRecipe, name, timeToMake, difficulty, photo FROM Recipe WHERE favorites = 1",
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MyRecipes recipe = new MyRecipes();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(1));
                    recipe.setCharacteristic(cursor.getInt(2) + " mins | " + cursor.getString(3));
                    // Ανάκτηση εικόνας
                    byte[] imageBytes = cursor.getBlob(4);
                    recipe.setImageBytes(imageBytes);

                    results.add(recipe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Failed to get favorites", e);
            return new ArrayList<>(); // Fallback σε άδεια λίστα
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return results;
    }

    public List<MyRecipes> searchRecipesWithFilter(String query, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        String sqlQuery;
        String[] selectionArgs;
        switch (filter) {
            case "Favorites only":
                sqlQuery = "SELECT * FROM Recipe WHERE name LIKE ? AND favorites = 1";
                selectionArgs = new String[]{"%" + query + "%"};
                break;
            case "Easy":
            case "Medium":
            case "Hard":
                sqlQuery = "SELECT * FROM Recipe WHERE name LIKE ? AND difficulty = ?";
                selectionArgs = new String[]{"%" + query + "%", filter};
                break;
            default:
                sqlQuery = "SELECT * FROM Recipe WHERE name LIKE ?";
                selectionArgs = new String[]{"%" + query + "%"};
        }
        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                MyRecipes recipe = new MyRecipes();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setTitle(cursor.getString(1));
                recipe.setCharacteristic(cursor.getInt(2) + " mins | " + cursor.getString(3));
                // Ανάκτηση εικόνας
                byte[] imageBytes = cursor.getBlob(4);
                recipe.setImageBytes(imageBytes);
                results.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }



    public void updateRecipeFavoriteStatus(int recipeId, boolean isFavorite) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("favorites", isFavorite ? 1 : 0);
            // Debug log
            Log.d("DB_UPDATE", "Updating recipe " + recipeId + " favorites to " + isFavorite);
            int rowsAffected = db.update("Recipe", values, "idRecipe = ?",
                    new String[]{String.valueOf(recipeId)});
            Log.d("DB_UPDATE", "Rows affected: " + rowsAffected);
        } catch (Exception e) {
            Log.e("DB_UPDATE", "Error updating favorite", e);
            throw e; // Προσθήκη αυτής της γραμμής για να δεις το stack trace
        } finally {
            if (db != null) db.close();
        }
    }
}