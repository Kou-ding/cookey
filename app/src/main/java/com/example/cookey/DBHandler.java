package com.example.cookey;

import com.example.cookey.RecipeModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 26;
    private static final String DATABASE_NAME = "cookeyDB.db";
    private Context context;
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

                        "CREATE TABLE RecipeIngredients (\n" +
                        "  recipe_id INTEGER NOT NULL,\n" +
                        "  ingredient_id INTEGER NOT NULL,\n" +
                        "  quantity REAL,\n" +
                        "  unit TEXT,\n" +
                        "  PRIMARY KEY(recipe_id, ingredient_id),\n" +
                        "  FOREIGN KEY(recipe_id) REFERENCES Recipe(idRecipe),\n" +
                        "  FOREIGN KEY(ingredient_id) REFERENCES Ingredient(ingredientId)\n" +
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
        populateDefaultIngredients(db);
        populateDefaultTags(db);
    }


    /*
    private void initializeDefaultTags(SQLiteDatabase db) {
        String[] defaultTags = {"Gluten-Free", "Vegan", "Vegetarian", "Low-Carb", "Keto"};
        for (String tag : defaultTags) {
            ContentValues values = new ContentValues();
            values.put("name", tag);
            db.insert("Tags", null, values);
        }
    }

     */
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

        db.execSQL("DROP TABLE IF EXISTS RecipeIngredients");
        // Recreate tables
        onCreate(db);
    }

    public void populateDefaultIngredients(SQLiteDatabase db) {
        String[] defaultIngredients = new String[]{
                "Rice", "Salt", "Water", "Flour", "Sugar", "Butter", "Egg",
                "Garlic", "Olive Oil", "Potato", "Celery", "Carrot",
                "Mushroom", "Lemon", "Parmesan", "Cheddar"
        };
        String[] units = new String[]{
                "kg", "g", "ml", "kg", "g", "g", "piece",
                "g", "ml", "kg", "g", "g",
                "kg", "ml", "g", "g"
        };

        for (int i = 0; i < defaultIngredients.length; i++) {
            ContentValues values = new ContentValues();
            values.put("ingredientName", defaultIngredients[i]);
            values.put("quantity", 0); // default value
            values.put("unitSystem", units[i]);
            values.put("daysToSpoil", 10); // default value
            values.put("checkIfSpoiledArray", ""); // optional

            db.insert("Ingredient", null, values);
        }
    }

    public void populateDefaultTags(SQLiteDatabase db) {
        String[] defaultTags = new String[]{
                "Italian", "Quick", "Vegan", "Healthy", "Gluten-Free", "Breakfast", "Sweet", "Spicy", "Low Carb"
        };

        for (String tag : defaultTags) {
            ContentValues values = new ContentValues();
            values.put("name", tag);
            db.insert("Tags", null, values);
        }
        Log.d("DBHandler", "Adding default tags...");
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
          //  initializeDefaultTags(db);
         //   Log.d("DB_DEBUG", "Database recreated successfully!");
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


    // Μέθοδος για λήψη όλων των συνταγών
    public List<RecipeModel> getAllRecipes() {
        List<RecipeModel> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT idRecipe, name, timeToMake, difficulty, country, mealNumber, " +
                        "       photoPath, favourites " +
                        "FROM   Recipe " +
                        "ORDER  BY name COLLATE NOCASE";

        try (Cursor c = db.rawQuery(sql, null)) {
            while (c.moveToNext()) {
                recipes.add(cursorToRecipeModel(c));
            }
        }
        db.close();
        return recipes;
    }

    public List<RecipeModel> getRecipesByDifficulty(String difficulty) {
        List<RecipeModel> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT idRecipe, name, timeToMake, difficulty, country, mealNumber, " +
                        "       photoPath, favourites " +
                        "FROM   Recipe " +
                        "WHERE  difficulty = ?";
        String[] args = new String[]{difficulty};

        try (Cursor c = db.rawQuery(sql, args)) {
            while (c.moveToNext()) {
                recipes.add(cursorToRecipeModel(c));
            }
        }
        db.close();
        return recipes;
    }

    // Αναζήτηση συνταγών βάσει tag
    public List<RecipeModel> getRecipesByTag(String tagName) {
        List<RecipeModel> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT r.idRecipe, r.name, r.timeToMake, r.difficulty, " +
                        "       r.country, r.mealNumber, r.photoPath, r.favourites " +
                        "FROM   Recipe            r " +
                        "JOIN   Recipe_has_Tags rt ON r.idRecipe = rt.Recipe_idRecipe " +
                        "JOIN   Tags            t  ON rt.Tags_idTags = t.idTags " +
                        "WHERE  t.name = ?";
        String[] args = new String[]{tagName};

        try (Cursor c = db.rawQuery(sql, args)) {
            while (c.moveToNext()) {
                recipes.add(cursorToRecipeModel(c));
            }
        }
        db.close();
        return recipes;
    }


   //Μετατρέπει μια γραμμή cursor σε `RecipeModel`
    private RecipeModel cursorToRecipeModel(Cursor c) {
        long   id          = c.getLong   (c.getColumnIndexOrThrow("idRecipe"));
        String name        = c.getString (c.getColumnIndexOrThrow("name"));
        int    time        = c.getInt    (c.getColumnIndexOrThrow("timeToMake"));
        String diff        = c.getString (c.getColumnIndexOrThrow("difficulty"));
        String countryCode = c.getString (c.getColumnIndexOrThrow("country"));
        int    mealNo      = c.getInt    (c.getColumnIndexOrThrow("mealNumber"));
        String photoPath   = c.getString (c.getColumnIndexOrThrow("photoPath"));
        boolean fav        = c.getInt    (c.getColumnIndexOrThrow("favourites")) == 1;

        RecipeModel recipe = new RecipeModel();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setTimeToMake(time);
        recipe.setDifficulty(diff);
        recipe.setCountryCode(countryCode);
        recipe.setMealNumber(mealNo);
        recipe.setPhotoPath(photoPath);
        recipe.setFavorite(fav);


        recipe.setCountryName(getCountryNameByCode(countryCode));

        return recipe;
    }

    public List<RecipeModel> searchRecipes(String query) {
        List<RecipeModel> out = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT idRecipe, name, timeToMake, difficulty, country, mealNumber, " +
                        "       photoPath, favourites " +
                        "FROM   Recipe " +
                        "WHERE  name LIKE ? " +
                        "ORDER  BY name COLLATE NOCASE";
        String[] args = new String[]{"%" + query + "%"};

        try (Cursor c = db.rawQuery(sql, args)) {
            while (c.moveToNext()) {
                out.add(cursorToRecipeModel(c));
            }
        }
        db.close();
        return out;
    }



    public List<RecipeModel> getFavoriteRecipes() {
        List<RecipeModel> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql =
                "SELECT idRecipe, name, timeToMake, difficulty, country, mealNumber, " +
                        "       photoPath, favourites " +
                        "FROM   Recipe " +
                        "WHERE  favourites = 1";

        try (Cursor c = db.rawQuery(sql, null)) {
            while (c.moveToNext()) {
                results.add(cursorToRecipeModel(c));
            }
        }
        db.close();
        return results;
    }

    public List<RecipeModel> searchRecipesWithFilter(String query, String filter) {
        List<RecipeModel> out = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder sql = new StringBuilder(
                "SELECT idRecipe, name, timeToMake, difficulty, country, " +
                        "       mealNumber, photoPath, favourites " +
                        "FROM   Recipe " +
                        "WHERE  name LIKE ? "
        );
        List<String> args = new ArrayList<>();
        args.add('%' + query + '%');

        if (filter != null && !filter.isEmpty()) {
            switch (filter) {
                case "Favorites":
                    sql.append("AND favourites = 1 ");
                    break;
                case "Easy":
                case "Medium":
                case "Hard":
                    sql.append("AND difficulty = ? ");
                    args.add(filter);
                    break;
            }
        }
        sql.append("ORDER BY name COLLATE NOCASE");

        try (Cursor c = db.rawQuery(sql.toString(), args.toArray(new String[0]))) {
            while (c.moveToNext()) out.add(cursorToRecipeModel(c));
        }
        db.close();
        return out;
    }

    public void updateRecipeFavoriteStatus(long recipeId, boolean isFavorite) {
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

    // ////////////////////////////////////////////////////
                        //NIKOS TIME
    // ///////////////////////////////////////////////////
    public long addRecipe(String name, int timeToMake, String country, int mealNumber, String difficulty, String photoPath, boolean isFavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("timeToMake", timeToMake);
        values.put("country", country);
        values.put("mealNumber", mealNumber);
        values.put("difficulty", difficulty);
        values.put("photoPath", photoPath);
        values.put("favourites", isFavourite ? 1 : 0);
        long id = db.insert("Recipe", null, values);
        db.close();
        return id;
    }

    public void addStep(String description, long recipeId, int numberOfStep) {
        SQLiteDatabase db = this.getWritableDatabase();

        long stepId = -1;

        //Insert into steps
        String insertStepSQL = "INSERT INTO Steps (text, numberOfStep) VALUES (?,?)";
        SQLiteStatement stepStatement = db.compileStatement(insertStepSQL);
        stepStatement.bindString(1, description);
        stepStatement.bindLong(2, numberOfStep);

        try {
            stepId = stepStatement.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stepStatement.close();
        }

        // If it got inserted correctly-->
        if (stepId != -1) {
            // Insert into Recipe_has_Steps
            String insertRelationSQL = "INSERT INTO Recipe_has_Steps (Recipe_idRecipe, Steps_idSteps) VALUES (?, ?)";
            SQLiteStatement relationStatement = db.compileStatement(insertRelationSQL);
            relationStatement.bindLong(1, recipeId);
            relationStatement.bindLong(2, stepId);

            try {
                relationStatement.executeInsert();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                relationStatement.close();
            }
        }

        db.close();
    }

    public void addIngredientToRecipeIngredients(long recipeId, String ingredientName, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ingredientId = -1;

        // Get ingredientId from name
        Cursor cursor = db.rawQuery("SELECT ingredientId FROM Ingredient WHERE ingredientName = ?", new String[]{ingredientName});
        if (cursor.moveToFirst()) {
            ingredientId = cursor.getLong(0);
        } else {
            // If it doesnt exist, add it
            ContentValues values = new ContentValues();
            values.put("ingredientName", ingredientName);
            values.put("quantity", 0); // placeholder
            values.put("unitSystem", unit);
            values.put("daysToSpoil", 0);
            values.put("checkIfSpoiledArray", "");

            ingredientId = db.insert("Ingredient", null, values);
        }
        cursor.close();

        // Add it to RecipeIngredients
        if (ingredientId != -1) {
            ContentValues values = new ContentValues();
            values.put("recipe_id", recipeId);
            values.put("ingredient_id", ingredientId);
            values.put("quantity", quantity);
            values.put("unit", unit);

            db.insert("RecipeIngredients", null, values);
        }

        db.close();
    }

    public void addTagToRecipe(long recipeId, String tagName) {
        SQLiteDatabase db = this.getWritableDatabase();

        long tagId = getTagId(tagName, db);

        if (tagId == -1) {
            // If tag did not exist -> insert new tag
            String insertTagSQL = "INSERT INTO Tags (name) VALUES (?)";
            SQLiteStatement insertTagStmt = db.compileStatement(insertTagSQL);
            insertTagStmt.bindString(1, tagName);

            try {
                tagId = insertTagStmt.executeInsert();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                insertTagStmt.close();
            }
        }

        // We got tagId - Connect it with the recipe
        if (tagId != -1) {
            String insertRelationSQL = "INSERT INTO Recipe_has_Tags (Recipe_idRecipe, Tags_idTags) VALUES (?, ?)";
            SQLiteStatement relationStmt = db.compileStatement(insertRelationSQL);
            relationStmt.bindLong(1, recipeId);
            relationStmt.bindLong(2, tagId);

            try {
                relationStmt.executeInsert();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                relationStmt.close();
            }
        }

        db.close();
    }

    public RecipeModel getRecipeId(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        RecipeModel recipe = null;

        // Load all data except from the photo --> Fixing blob too big error
        Cursor cursor = db.rawQuery(
                "SELECT idRecipe, name, timeToMake, country, mealNumber, difficulty, photoPath, favourites " +
                        "FROM Recipe WHERE idRecipe = ?",
                new String[]{String.valueOf(recipeId)}
        );

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow("timeToMake"));
            String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country"));
            int mealNumber = cursor.getInt(cursor.getColumnIndexOrThrow("mealNumber"));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));
            String photoPath = cursor.getString(cursor.getColumnIndexOrThrow("photoPath"));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("favourites")) == 1;

            recipe = new RecipeModel();
            recipe.setId(recipeId);
            recipe.setName(name);
            recipe.setTimeToMake(time);
            recipe.setCountryCode(countryCode);
            recipe.setMealNumber(mealNumber);
            recipe.setDifficulty(difficulty);
            recipe.setPhotoPath(photoPath);
            recipe.setFavorite(isFavorite);
        }
        cursor.close();

        // 3. Tags
        List<String> tags = new ArrayList<>();
        Cursor tagCursor = db.rawQuery(
                "SELECT t.name FROM Tags t " +
                        "JOIN Recipe_has_Tags rt ON t.idTags = rt.Tags_idTags " +
                        "WHERE rt.Recipe_idRecipe = ?",
                new String[]{String.valueOf(recipeId)}
        );
        while (tagCursor.moveToNext()) {
            tags.add(tagCursor.getString(tagCursor.getColumnIndexOrThrow("name")));
        }
        tagCursor.close();
        if (recipe != null) recipe.setTags(tags);

        // 4. Country name from JSON
        if (recipe != null) {
            String countryName = getCountryNameByCode(recipe.getCountryCode());
            recipe.setCountryName(countryName);
        }

        // 5. Ingredients
        List<ViewIngredientModel> ingredients = new ArrayList<>();
        Cursor ingredientsCursor = db.rawQuery(
                "SELECT i.ingredientName, ri.unit, ri.quantity " +
                        "FROM RecipeIngredients ri " +
                        "JOIN Ingredient i ON ri.ingredient_id = i.ingredientId " +
                        "WHERE ri.recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );
        while (ingredientsCursor.moveToNext()) {
            String ingName = ingredientsCursor.getString(ingredientsCursor.getColumnIndexOrThrow("ingredientName"));
            String unit = ingredientsCursor.getString(ingredientsCursor.getColumnIndexOrThrow("unit"));
            float quantity = ingredientsCursor.getFloat(ingredientsCursor.getColumnIndexOrThrow("quantity"));
            ingredients.add(new ViewIngredientModel(ingName, unit, quantity));
        }
        ingredientsCursor.close();
        if (recipe != null) recipe.setIngredients(ingredients);

        // 6. Steps
        List<StepModel> steps = new ArrayList<>();
        Cursor stepCursor = db.rawQuery(
                "SELECT s.text FROM Steps s " +
                        "JOIN Recipe_has_Steps rhs ON s.idSteps = rhs.Steps_idSteps " +
                        "WHERE rhs.Recipe_idRecipe = ? ORDER BY s.numberOfStep ASC",
                new String[]{String.valueOf(recipeId)}
        );
        while (stepCursor.moveToNext()) {
            String description = stepCursor.getString(0);
            steps.add(new StepModel(description));
        }
        stepCursor.close();
        if (recipe != null) recipe.setSteps(steps);

        db.close();
        return recipe;

    }


    private long getTagId(String tagName, SQLiteDatabase db) {
        long id = -1;
        try (Cursor cursor = db.rawQuery("SELECT idTags FROM Tags WHERE name = ?", new String[]{tagName})) {
            if (cursor.moveToFirst()) {
                id = cursor.getLong(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public void setFavorite(long recipeId, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("favourites", isFavorite ? 1 : 0);
        db.update("Recipe", values, "idRecipe = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }


    private String getCountryNameByCode(String code) {
        try {
            InputStream is = context.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray countries = new JSONArray(json);
            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                if (country.getString("code").equalsIgnoreCase(code)) {
                    return country.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBHandler", "Error loading country name for code: " + code, e);
        }
        return "Unknown";
    }

    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Tags", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                tags.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            }
            cursor.close();
        }
        Log.d("DBHandler", "Tag count: " + tags.size());
        db.close();
        return tags;
    }

    public void updateRecipe(long recipeId, String name, int timeToMake, String country, int mealNumber, String difficulty, String photoPath, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("timeToMake", timeToMake);
        values.put("country", country);
        values.put("mealNumber", mealNumber);
        values.put("difficulty", difficulty);
        values.put("photoPath", photoPath);
        values.put("favourites", isFavorite ? 1 : 0);
        db.update("Recipe", values, "idRecipe = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }


    public List<SelectedIngredient> getIngredientsForRecipe(long recipeId) {
        List<SelectedIngredient> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT i.ingredientId, i.ingredientName, ri.quantity, ri.unit\n" +
                        "  FROM RecipeIngredients ri\n" +
                        "  JOIN Ingredient       i ON i.ingredientId = ri.ingredient_id\n" +
                        " WHERE ri.recipe_id = ?",
                new String[]{ String.valueOf(recipeId) }
        );
        while (c.moveToNext()) {
            long    id   = c.getLong(  c.getColumnIndexOrThrow("ingredientId"));
            String  name = c.getString(c.getColumnIndexOrThrow("ingredientName"));
            double  qty  = c.getDouble(c.getColumnIndexOrThrow("quantity"));
            String  unit = c.getString(c.getColumnIndexOrThrow("unit"));
            out.add(new SelectedIngredient(new IngredientModel(id, name, unit), qty));
        }
        c.close();
        db.close();
        return out;
    }


    public List<String> getTagsForRecipe(long recipeId) {
        List<String> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT t.name FROM Tags t " +
                        "JOIN Recipe_has_Tags rt ON t.idTags = rt.Tags_idTags " +
                        "WHERE rt.Recipe_idRecipe = ?",
                new String[]{String.valueOf(recipeId)}
        );

        while (cursor.moveToNext()) {
            tags.add(cursor.getString(0));
        }

        cursor.close();
        db.close();
        return tags;
    }

    public List<StepModel> getStepsForRecipe(long recipeId) {
        List<StepModel> steps = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT s.text FROM Steps s " +
                        "JOIN Recipe_has_Steps rhs ON s.idSteps = rhs.Steps_idSteps " +
                        "WHERE rhs.Recipe_idRecipe = ? ORDER BY s.numberOfStep ASC",
                new String[]{String.valueOf(recipeId)}
        );

        while (cursor.moveToNext()) {
            steps.add(new StepModel(cursor.getString(0)));
        }

        cursor.close();
        db.close();
        return steps;
    }

    public void clearIngredientsOfRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM RecipeIngredients WHERE recipe_id = " + recipeId);
        db.close();
    }

    public void clearStepsOfRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Recipe_has_Steps WHERE Recipe_idRecipe = " + recipeId);
        db.close();
    }

    public void clearTagsOfRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Recipe_has_Tags WHERE Recipe_idRecipe = " + recipeId);
        db.close();
    }

    public List<IngredientModel> getAllAddIngredients() {
        List<IngredientModel> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ingredientId, ingredientName, unitSystem FROM Ingredient", null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("ingredientId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("ingredientName"));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow("unitSystem"));
                ingredients.add(new IngredientModel(id, name, unit));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ingredients;
    }
}