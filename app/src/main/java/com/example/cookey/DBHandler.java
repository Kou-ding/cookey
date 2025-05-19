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
    private static final int DATABASE_VERSION = 25;
    private static final String DATABASE_NAME = "cookeyDB.db";
    private Context context;
    public DBHandler(Context context) {
        super(context, "cookeyDB.db", null, DATABASE_VERSION);
        initializeDefaultTags();
        Log.d("DB_VERSION", "Using DB version: " + DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_SCRIPT =
                "CREATE TABLE Recipe (\n" +
                        "  idRecipe INTEGER NOT NULL,\n" +
                        "  name VARCHAR,\n" +
                        "  timeToMake INTEGER,\n" +
                        "  country VARCHAR,\n" +
                        "  mealNumber INTEGER,\n" +
                        "  difficulty ENUM,\n" +
                        "  photoPath TEXT,\n" +
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

                        "CREATE TABLE Ingredient (\n" +
                        "  ingredientId INTEGER NOT NULL,\n" +
                        "  ingredientName VARCHAR,\n" +
                        "  quantity FLOAT,\n" +
                        "  unitSystem VARCHAR,\n" +
                        "  daysToSpoil INTEGER,\n" +
                        "  checkIfSpoiledArray VARCHAR,\n" +
                        "  PRIMARY KEY(ingredientId)\n" +
                        ");"+

                        "CREATE INDEX Recipe_has_Steps_FKIndex1 ON Recipe_has_Steps (Recipe_idRecipe);\n" +
                        "CREATE INDEX Recipe_has_Steps_FKIndex2 ON Recipe_has_Steps (Steps_idSteps);\n\n"+
                        "CREATE INDEX Recipe_favourites_index ON Recipe(favourites);"+
                        "CREATE INDEX Recipe_name_index ON Recipe(name)";
        // Split and run each statement
        for (String statement : DATABASE_CREATE_SCRIPT.split(";")) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                db.execSQL(statement + ";");
            }
        }
    }


    private void initializeDefaultTags(SQLiteDatabase db) {
        String[] defaultTags = {"Gluten-Free", "Vegan", "Vegetarian", "Low-Carb", "Keto"};
        for (String tag : defaultTags) {
            ContentValues values = new ContentValues();
            values.put("name", tag);
            db.insert("Tags", null, values);
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

    /*
    public DBHandler(Context context) {
        super(context, "cookeyDB.db", null, DATABASE_VERSION);
        initializeDefaultTags();
    }

     */

    public void dropDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Διαγραφή όλων των πινάκων
            db.execSQL("DROP TABLE IF EXISTS Recipe_has_Tags;");
            db.execSQL("DROP TABLE IF EXISTS Recipe_has_Ingredient;");
            db.execSQL("DROP TABLE IF EXISTS Recipe_has_Steps;");
            db.execSQL("DROP TABLE IF EXISTS Tags;");
            db.execSQL("DROP TABLE IF EXISTS Steps;");
            db.execSQL("DROP TABLE IF EXISTS Ingredient;");
            db.execSQL("DROP TABLE IF EXISTS Recipe;");
            db.execSQL("DROP TABLE IF EXISTS ShoppingList;");

            onCreate(db);
            initializeDefaultTags(db);
            Log.d("DB_DEBUG", "Database recreated successfully!");
        } catch (Exception e) {
            Log.e("DB_DEBUG", "Error recreating database", e);
        } finally {
            db.close();
        }
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
            // Προσθήκη του favourites στο query
            String query = "SELECT idRecipe, name, timeToMake, difficulty, favourites FROM Recipe";
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
        Log.d("DB_DEBUG", "Getting all recipes...");
        List<MyRecipes> recipes = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String query = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites FROM Recipe";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MyRecipes recipe = new MyRecipes();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(1));
                    recipe.setCharacteristic(cursor.getInt(2) + " mins | " + cursor.getString(3));

                    if (!cursor.isNull(4)) {
                        recipe.setImageBytes(cursor.getBlob(4));
                    } else {
                        recipe.setImageResId(R.drawable.lunch_dining_24px);
                    }

                    recipe.setFavorite(cursor.getInt(5) == 1);
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error getting recipes", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        Log.d("DB_DEBUG", "Found " + recipes.size() + " recipes");
        return recipes;
    }
    public RecipeFull getFullRecipe(int recipeId) {
        RecipeFull recipeFull = new RecipeFull(getRecipe(recipeId));

        // Get ingredients
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT i.name FROM Ingredient i " +
                        "JOIN Recipe_has_Ingredient ri ON i.ingredientId = ri.Ingredient_ingredientId " +
                        "WHERE ri.Recipe_idRecipe = ?",
                new String[]{String.valueOf(recipeId)});

        while (cursor.moveToNext()) {
            recipeFull.addIngredient(cursor.getString(0));
        }
        cursor.close();

        // Get steps
        cursor = db.rawQuery(
                "SELECT s.text FROM Steps s " +
                        "JOIN Recipe_has_Steps rs ON s.idSteps = rs.Steps_idSteps " +
                        "WHERE rs.Recipe_idRecipe = ? ORDER BY s.numberOfStep",
                new String[]{String.valueOf(recipeId)});

        while (cursor.moveToNext()) {
            recipeFull.addStep(cursor.getString(0));
        }
        cursor.close();

        // Get tags
        cursor = db.rawQuery(
                "SELECT t.name FROM Tags t " +
                        "JOIN Recipe_has_Tags rt ON t.idTags = rt.Tags_idTags " +
                        "WHERE rt.Recipe_idRecipe = ?",
                new String[]{String.valueOf(recipeId)});

        while (cursor.moveToNext()) {
            recipeFull.addTag(cursor.getString(0));
        }
        cursor.close();

        db.close();
        return recipeFull;
    }

    public long addFullRecipe(RecipeFull recipe, Bitmap image) {
        long recipeId = addRecipe(recipe.toBasicRecipe(), image);

        if (recipeId != -1) {
            // Add ingredients
            for (String ingredient : recipe.getIngredients()) {
                executeCommand(
                        "INSERT INTO Ingredient (ingredientId, ingredientName) VALUES (NULL, '" + ingredient + "'); " +
                                "INSERT INTO Recipe_has_Ingredient (Recipe_idRecipe, Ingredient_ingredientId) " +
                                "VALUES (" + recipeId + ", (SELECT MAX(ingredientId) FROM Ingredient));"
                );
            }

            // Add steps
            int stepNumber = 1;
            for (String step : recipe.getSteps()) {
                executeCommand(
                        "INSERT INTO Steps (idSteps, text, numberOfStep) VALUES (NULL, '" + step + "', " + stepNumber + "); " +
                                "INSERT INTO Recipe_has_Steps (Recipe_idRecipe, Steps_idSteps) " +
                                "VALUES (" + recipeId + ", (SELECT MAX(idSteps) FROM Steps));"
                );
                stepNumber++;
            }

            // Add tags
            for (String tag : recipe.getTags()) {
                executeCommand(
                        "INSERT OR IGNORE INTO Tags (idTags, name) VALUES (NULL, '" + tag + "'); " +
                                "INSERT INTO Recipe_has_Tags (Recipe_idRecipe, Tags_idTags) " +
                                "VALUES (" + recipeId + ", (SELECT idTags FROM Tags WHERE name = '" + tag + "'));"
                );
            }
        }

        return recipeId;
    }

    // Μέθοδος για λήψη συγκεκριμένης συνταγής
    public MyRecipes getRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        MyRecipes recipe = null;
        Cursor cursor = null;
        try {
            String query = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites FROM Recipe WHERE idRecipe = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(recipeId)});
            if (cursor != null && cursor.moveToFirst()) {
                recipe = new MyRecipes();
                recipe.setRecipeId(cursor.getInt(0));
                recipe.setTitle(cursor.getString(1));
                recipe.setFavorite(cursor.getInt(5)==1);
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

    public List<MyRecipes> getRecipesByDifficulty(String difficulty) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Recipe WHERE favourites = 1 AND difficulty = ?", new String[]{difficulty}
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

    // Επιστρέφει όλα τα διαθέσιμα tags από τη βάση
    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Tags", null);
        if (cursor.moveToFirst()) {
            do {
                tags.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tags;
    }

    public void initializeDefaultTags() {
        String[] defaultTags = {"Gluten-Free", "Vegan", "Vegetarian", "Low-Carb", "Keto"};
        SQLiteDatabase db = this.getWritableDatabase();
        for (String tag : defaultTags) {
            // Ελέγχουμε πρώτα αν υπάρχει ήδη το tag
            Cursor cursor = db.rawQuery("SELECT name FROM Tags WHERE name = ?", new String[]{tag});
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("name", tag);
                db.insert("Tags", null, values);
            }
            cursor.close();
        }
    }


    // Αναζήτηση συνταγών βάσει tag
    public List<MyRecipes> getRecipesByTag(String tagName) {
        String sql = "SELECT r.idRecipe, r.name, r.timeToMake, r.difficulty, r.photo, r.favourites " +
                "FROM Recipe r " +
                "JOIN Recipe_has_Tags rt ON r.idRecipe = rt.Recipe_idRecipe " +
                "JOIN Tags t ON rt.Tags_idTags = t.idTags " +
                "WHERE t.name = ?";
        return executeSearch(sql, new String[]{tagName});
    }

    public List<MyRecipes> searchRecipes(String query) {
        String sql = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites " +
                "FROM Recipe WHERE LOWER(name) LIKE LOWER(?)";
        return executeSearch(sql, new String[]{"%" + query + "%"});
    }

    private List<MyRecipes> executeSearch(String sql, String[] params) {
        List<MyRecipes> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(sql, params);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MyRecipes recipe = new MyRecipes();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(1));

                    // Χαρακτηριστικό (χρόνος + δυσκολία)
                    int time = cursor.getInt(2);
                    String difficulty = cursor.getString(3);
                    recipe.setCharacteristic(time + " mins | " + difficulty);

                    // Εικόνα
                    if (!cursor.isNull(4)) {
                        byte[] imageBytes = cursor.getBlob(4);
                        recipe.setImageBytes(imageBytes);
                    }

                    // Favourites status
                    recipe.setFavorite(cursor.getInt(5) == 1);

                    results.add(recipe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_SEARCH", "Search failed", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return results;
    }

    public List<MyRecipes> getFavoriteRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyRecipes> results = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites FROM Recipe WHERE favourites = 1",
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MyRecipes recipe = new MyRecipes();
                    recipe.setRecipeId(cursor.getInt(0));
                    recipe.setTitle(cursor.getString(1));
                    recipe.setCharacteristic(cursor.getInt(2) + " mins | " + cursor.getString(3));
                    if (!cursor.isNull(4)) {
                        byte[] imageBytes = cursor.getBlob(4);
                        recipe.setImageBytes(imageBytes);
                    }
                    recipe.setFavorite(cursor.getInt(5) == 1);
                    results.add(recipe);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Failed to get favourites", e);
            return new ArrayList<>(); // Fallback σε άδεια λίστα
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return results;
    }

    public List<MyRecipes> searchRecipesWithFilter(String query, String filter) {
        String sql;
        String[] params;

        switch (filter) {
            case "Favorites only":
                sql = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites " +
                        "FROM Recipe WHERE LOWER(name) LIKE LOWER(?) AND favourites = 1";
                params = new String[]{"%" + query + "%"};
                break;
            case "Easy":
            case "Medium":
            case "Hard":
                sql = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites " +
                        "FROM Recipe WHERE LOWER(name) LIKE LOWER(?) AND difficulty = ?";
                params = new String[]{"%" + query + "%", filter};
                break;
            default:
                sql = "SELECT idRecipe, name, timeToMake, difficulty, photoPath, favourites " +
                        "FROM Recipe WHERE LOWER(name) LIKE LOWER(?)";
                params = new String[]{"%" + query + "%"};
        }

        return executeSearch(sql, params);
    }



    public void updateRecipeFavoriteStatus(int recipeId, boolean isFavorite) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("favourites", isFavorite ? 1 : 0);
            Log.d("DB_UPDATE", "Updating recipe " + recipeId + " favourites to " + isFavorite);
            int rowsAffected = db.update(
                    "Recipe",
                    values,
                    "idRecipe = ?",
                    new String[]{String.valueOf(recipeId)}
            );
            Log.d("DB_UPDATE", "Rows affected: " + rowsAffected);
            if (rowsAffected == 0) {
                Log.w("DB_UPDATE", "No rows affected - recipe ID might not exist");
            }
        } catch (Exception e) {
            Log.e("DB_UPDATE", "Error updating favourite status", e);
            throw new RuntimeException("Failed to update favourite status", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}