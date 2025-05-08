package com.example.cookey;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "cookeyDB.db";
    private Context context;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

                        "CREATE TABLE RecipeIngredients (\n" +
                        "  recipe_id INTEGER NOT NULL,\n" +
                        "  ingredient_id INTEGER NOT NULL,\n" +
                        "  quantity REAL,\n" +
                        "  unit TEXT,\n" +
                        "  PRIMARY KEY(recipe_id, ingredient_id),\n" +
                        "  FOREIGN KEY(recipe_id) REFERENCES Recipe(idRecipe),\n" +
                        "  FOREIGN KEY(ingredient_id) REFERENCES Ingredient(ingredientId)\n" +
                        ");\n\n"+

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




    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Ingredient SET quantity = 0 WHERE ingredientId = " + ingredientId + ";";
        db.execSQL(query);//now instead of deleting we just make the quantity 0

        db.close();
    }

    public void setNewQuantity(String ingredientName, float newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Ingredient SET quantity = " + newQuantity + " WHERE ingredientName = '" + ingredientName + "';";
        db.execSQL(query);
        db.close();
    }

    // For Database debugging //
    // Delete all ingredients from the database
    public void dropDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Remove all ingredients from the Ingredient table
        db.execSQL("DELETE FROM Ingredient;");
        db.execSQL("DELETE FROM ShoppingList;");
        db.close();
    }

    // Execute a command on the database
    public void executeCommand(String command) {
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


    public void deleteShoppingListItem(String shoppingListItemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList WHERE shoppingListItemName = '" + shoppingListItemName + "';";
        db.execSQL(query);
        db.close();
    }


    public void newShoppingList() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList;";
        db.execSQL(query);
        db.close();
    }

    public void addFoodItem() {
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

    public long addRecipe(String name, int timeToMake, String country, int mealNumber, String difficulty, byte[] photoBytes, boolean isFavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRecipeID = -1;

        String sql = "INSERT INTO Recipe (name, timeToMake, country, mealNumber, difficulty, photo, favourites) VALUES (?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindLong(2, timeToMake);
        statement.bindString(3, country);
        statement.bindLong(4, mealNumber);
        statement.bindString(5, difficulty);

        if (photoBytes != null) {
            statement.bindBlob(6, photoBytes);
        } else {
            statement.bindNull(6);
        }

        statement.bindLong(7, isFavourite ? 1 : 0);

        try {
            newRecipeID = statement.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            statement.close();
            db.close();
        }
        return newRecipeID;
    }

    //Add a step
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

    //Add Ingredient
    // ITS BROKEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void addIngredientToRecipe(long recipeId, String ingredientName, double quantity, String unit, int daysToSpoil) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ingredientId = -1;

        // Get ingredientId from the name
        Cursor cursor = db.rawQuery("SELECT ingredientId FROM Ingredient WHERE ingredientName = ?", new String[]{ingredientName});
        if (cursor.moveToFirst()) {
            ingredientId = cursor.getLong(0);
        } else {
            // If it doesnt exist, add it
            ContentValues values = new ContentValues();
            values.put("ingredientName", ingredientName);
            values.put("quantity", 0); // placeholder
            values.put("unitSystem", unit);
            values.put("daysToSpoil", 0); // default
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

    //Check if there is tagID
    private long getTagId(String tagName, SQLiteDatabase db) {
        long id = -1;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT idTags FROM Tags WHERE name = ?", new String[]{tagName});
            if (cursor.moveToFirst()) {
                id = cursor.getLong(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return id;
    }


    public RecipeModel getRecipeId(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        RecipeModel recipe = null;

        // Load all data except from the photo --> Fixing blob too big error
        Cursor cursor = db.rawQuery(
                "SELECT idRecipe, name, timeToMake, country, mealNumber, difficulty, favourites " +
                        "FROM Recipe WHERE idRecipe = ?",
                new String[]{String.valueOf(recipeId)}
        );

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow("timeToMake"));
            String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("country"));
            int mealNumber = cursor.getInt(cursor.getColumnIndexOrThrow("mealNumber"));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow("difficulty"));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("favourites")) == 1;

            recipe = new RecipeModel();
            recipe.setId(recipeId);
            recipe.setName(name);
            recipe.setTimeToMake(time);
            recipe.setCountryCode(countryCode);
            recipe.setMealNumber(mealNumber);
            recipe.setDifficulty(difficulty);
            recipe.setFavorite(isFavorite);
        }
        cursor.close();

        //Load image WITH DIFFERENT query
        if (recipe != null) {
            Cursor photoCursor = db.rawQuery(
                    "SELECT photo FROM Recipe WHERE idRecipe = ?",
                    new String[]{String.valueOf(recipeId)}
            );
            if (photoCursor.moveToFirst()) {
                byte[] photo = photoCursor.getBlob(0);
                recipe.setPhoto(photo);
            }
            photoCursor.close();
        }

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


    public void setFavorite(int recipeId, boolean isFavorite) {
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
            String json = new String(buffer, "UTF-8");

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

    public List<String> getAllTags(){
        List<String> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Tags", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                tags.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            }
            cursor.close();
        }
        db.close();
        return tags;
    }



}
