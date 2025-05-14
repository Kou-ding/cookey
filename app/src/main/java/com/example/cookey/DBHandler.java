package com.example.cookey;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                "  shoppingListItemId INTEGER NOT NULL,\n" +
                "  shoppingListItemName VARCHAR,\n" +
                "  purchasedQuantity FLOAT,\n" +
                "  isFood BOOLEAN,\n" +
                "  isChecked BOOLEAN,\n" +
                "  PRIMARY KEY(shoppingListItemId)\n" +
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
                ");" +
                // ingredientId, ingredientName, ingredientQuantity, ingredientUnitSystem, daysToRot, checkIfRottenArray
                // Basic
                "INSERT INTO Ingredient VALUES (1, 'Baking Soda', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (2, 'Baking Powder', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (3, 'Yeast', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (4, 'Gelatin', 0, 'gr', 730, '[]');\n" +

                // Sweets
                "INSERT INTO Ingredient VALUES (5, 'Sugar', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (6, 'Honey', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (7, 'Maple Syrup', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (8, 'Vanilla Extract', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (9, 'Powdered Sugar', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (10, 'Sprinkles', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (11, 'Cocoa Powder', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (12, 'Chocolate', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (13, 'Chocolate Chips', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (14, 'Marshmallows', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (15, 'Ice Cream', 0, 'gr', 180, '[]');\n" +

                // Dairy
                "INSERT INTO Ingredient VALUES (16, 'Milk', 0, 'lt', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (17, 'Eggs', 0, 'count', 28, '[]');\n" +
                "INSERT INTO Ingredient VALUES (18, 'Yogurt', 0, 'gr', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (19, 'Butter', 0, 'gr', 60, '[]');\n" +
                "INSERT INTO Ingredient VALUES (20, 'Heavy Cream', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (21, 'Sour Cream', 0, 'gr', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (22, 'Evaporated Milk', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (23, 'Condensed Milk', 0, 'ml', 365, '[]');\n" +

                // Cheeses
                "INSERT INTO Ingredient VALUES (24, 'Feta Cheese', 0, 'gr', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (25, 'Cheddar Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (26, 'Parmesan Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (27, 'Mozzarella Cheese', 0, 'gr', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (28, 'Graviera Cheese', 0, 'gr', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (29, 'Manouri Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (30, 'Halloumi Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (31, 'Cream Cheese', 0, 'gr', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (32, 'Gouda Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (33, 'Edam Cheese', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (34, 'Roquefort Cheese', 0, 'gr', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (35, 'Swiss Cheese', 0, 'gr', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (36, 'Cottage Cheese', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (37, 'Ricotta Cheese', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (38, 'Pecorino Romano', 0, 'gr', 60, '[]');\n" +

                // Spices
                "INSERT INTO Ingredient VALUES (39, 'Cinnamon', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (40, 'Thyme', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (41, 'Oregano', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (42, 'Rosemary', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (43, 'Paprika', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (44, 'Salt', 0, 'gr', 1000, '[]');\n" +
                "INSERT INTO Ingredient VALUES (45, 'Pepper', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (46, 'Saffron', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (47, 'Cayenne Pepper', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (48, 'Nutmeg', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (49, 'Ginger', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (50, 'Basil', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (51, 'Cilantro', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (52, 'Parsley', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (53, 'Dill', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (54, 'Cumin', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (55, 'Curry', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (56, 'Turmeric', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (57, 'Cardamom', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (58, 'Cloves', 0, 'gr', 730, '[]');\n" +

                // Sauces
                "INSERT INTO Ingredient VALUES (59, 'Ketchup', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (60, 'Mayonnaise', 0, 'ml', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (61, 'Mustard', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (62, 'Soy Sauce', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (63, 'Pesto', 0, 'gr', 90, '[]');\n" +
                "INSERT INTO Ingredient VALUES (64, 'Hummus', 0, 'gr', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (65, 'Tahini', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (66, 'Taramosalata', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (67, 'Tzatziki', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (68, 'Barbecue Sauce', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (69, 'Olive Paste', 0, 'gr', 365, '[]');\n" +

                // Vegetables
                "INSERT INTO Ingredient VALUES (70, 'Vinegar', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (71, 'Olive Oil', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (72, 'Onions', 0, 'count', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (73, 'Green Onions', 0, 'stalks', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (74, 'Basil', 0, 'bunch', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (75, 'Garlic', 0, 'cloves', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (76, 'Carrots', 0, 'count', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (77, 'Peppers', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (78, 'Potatoes', 0, 'count', 45, '[]');\n" +
                "INSERT INTO Ingredient VALUES (79, 'Eggplants', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (80, 'Lettuce', 0, 'heads', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (81, 'Tomatoes', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (82, 'Cucumbers', 0, 'count', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (83, 'Celery', 0, 'stalks', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (84, 'Cabbage', 0, 'heads', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (85, 'Broccoli', 0, 'heads', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (86, 'Zucchini', 0, 'count', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (87, 'Sweet Potatoes', 0, 'count', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (88, 'Spinach', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (89, 'Olives', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (90, 'Peas', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (91, 'Corn', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (92, 'Cauliflower', 0, 'heads', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (93, 'Asparagus', 0, 'spears', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (94, 'Leeks', 0, 'stalks', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (95, 'Okra', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (96, 'Artichokes', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (97, 'Radishes', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (98, 'Pumpkin', 0, 'count', 90, '[]');\n" +
                "INSERT INTO Ingredient VALUES (99, 'Turnips', 0, 'count', 21, '[]');\n" +

                // Canned
                "INSERT INTO Ingredient VALUES (100, 'Pickles', 0, 'count', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (101, 'Canned Beans', 0, 'gr', 540, '[]');\n" +
                "INSERT INTO Ingredient VALUES (102, 'Canned Tuna', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (103, 'Canned Salmon', 0, 'gr', 365, '[]');\n" +

                // Fruit
                "INSERT INTO Ingredient VALUES (104, 'Bananas', 0, 'count', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (105, 'Apples', 0, 'count', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (106, 'Oranges', 0, 'count', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (107, 'Lemons', 0, 'count', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (108, 'Limes', 0, 'count', 21, '[]');\n" +
                "INSERT INTO Ingredient VALUES (109, 'Strawberries', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (110, 'Blueberries', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (111, 'Raspberries', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (112, 'Blackberries', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (113, 'Grapes', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (114, 'Pineapple', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (115, 'Mango', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (116, 'Peaches', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (117, 'Plums', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (118, 'Cherries', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (119, 'Pears', 0, 'count', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (120, 'Kiwis', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (121, 'Avocados', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (122, 'Watermelon', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (123, 'Melon', 0, 'count', 7, '[]');\n" +

                // Meat
                "INSERT INTO Ingredient VALUES (124, 'Chicken Breast', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (125, 'Chicken Thighs', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (126, 'Chicken Wings', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (127, 'Bacon', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (128, 'Pork Chops', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (129, 'Sausages', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (130, 'Ham', 0, 'gr', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (131, 'Salami', 0, 'gr', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (132, 'Mortadella', 0, 'gr', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (133, 'Turkey Breast', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (134, 'Roast Beef', 0, 'gr', 5, '[]');\n" +
                "INSERT INTO Ingredient VALUES (135, 'Ground Beef', 0, 'gr', 2, '[]');\n" +
                "INSERT INTO Ingredient VALUES (136, 'Beef Steak', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (137, 'Lamb Chops', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (138, 'Prosciutto', 0, 'gr', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (139, 'Pastrami', 0, 'gr', 7, '[]');\n" +

                // Fish
                "INSERT INTO Ingredient VALUES (140, 'Anchovies', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (141, 'Sardines', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (142, 'Tuna', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (143, 'Salmon', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (144, 'Squid', 0, 'gr', 3, '[]');\n" +
                "INSERT INTO Ingredient VALUES (145, 'Lobster', 0, 'gr', 3, '[]');\n" +

                // Cereals
                "INSERT INTO Ingredient VALUES (146, 'Flour', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (147, 'Corn Flour', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (148, 'Spaghetti', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (149, 'Macaroni', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (150, 'Penne', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (151, 'Lasagna', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (152, 'Rigatoni', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (153, 'Fusilli', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (154, 'Tagliatelle', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (155, 'Linguine', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (156, 'Pappardelle', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (157, 'Capellini', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (158, 'Noodles', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (159, 'Rice', 0, 'gr', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (160, 'Trahana', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (161, 'Bread', 0, 'loaves', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (162, 'Baguette', 0, 'count', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (163, 'Tortillas', 0, 'count', 30, '[]');\n" +
                "INSERT INTO Ingredient VALUES (164, 'Cereal', 0, 'boxes', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (165, 'Quinoa', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (166, 'Couscous', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (167, 'Oats', 0, 'gr', 365, '[]');\n" +

                // Legumes
                "INSERT INTO Ingredient VALUES (168, 'Lentils', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (169, 'Chickpeas', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (170, 'Peas', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (171, 'Beans', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (172, 'Green Beans', 0, 'gr', 7, '[]');\n" +

                // Alcohol
                "INSERT INTO Ingredient VALUES (173, 'Red Wine', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (174, 'White Wine', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (175, 'Rose Wine', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (176, 'Beer', 0, 'ml', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (177, 'Champagne', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (178, 'Whiskey', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (179, 'Vodka', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (180, 'Rum', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (181, 'Gin', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (182, 'Tonic Water', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (183, 'Tequila', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (184, 'Ouzo', 0, 'ml', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (185, 'Sake', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (186, 'Tsikoudia', 0, 'ml', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (187, 'Cognac', 0, 'ml', 1825, '[]');\n" +
                "INSERT INTO Ingredient VALUES (188, 'Ginger Ale', 0, 'ml', 365, '[]');\n" +

                // Nuts
                "INSERT INTO Ingredient VALUES (189, 'Almonds', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (190, 'Walnuts', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (191, 'Pecans', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (192, 'Cashews', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (193, 'Pistachios', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (194, 'Hazelnuts', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (195, 'Pine Nuts', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (196, 'Sunflower Seeds', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (197, 'Pumpkin Seeds', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (198, 'Chia Seeds', 0, 'gr', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (199, 'Raisins', 0, 'gr', 180, '[]');\n" +

                // Drinks
                "INSERT INTO Ingredient VALUES (200, 'Tea Bags', 0, 'count', 730, '[]');\n" +
                "INSERT INTO Ingredient VALUES (201, 'Sparkling Water', 0, 'bottles', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (202, 'Lemonade', 0, 'ml', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (203, 'Cola', 0, 'ml', 365, '[]');\n" +
                "INSERT INTO Ingredient VALUES (204, 'Ice Tea', 0, 'ml', 7, '[]');\n" +
                "INSERT INTO Ingredient VALUES (205, 'Almond Milk', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (206, 'Oat Milk', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (207, 'Coconut Milk', 0, 'ml', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (208, 'Soy Milk', 0, 'ml', 180, '[]');\n" +

                // Juices
                "INSERT INTO Ingredient VALUES (209, 'Mixed Juice', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (210, 'Banana Juice', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (211, 'Orange Juice', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (212, 'Apple Juice', 0, 'ml', 10, '[]');\n" +
                "INSERT INTO Ingredient VALUES (213, 'Cranberry Juice', 0, 'ml', 14, '[]');\n" +
                "INSERT INTO Ingredient VALUES (214, 'Pineapple Juice', 0, 'ml', 14, '[]');\n" +

                // Frozen
                "INSERT INTO Ingredient VALUES (215, 'Pizza', 0, 'gr', 90, '[]');\n" +
                "INSERT INTO Ingredient VALUES (216, 'Frozen Shrimp', 0, 'gr', 180, '[]');\n" +
                "INSERT INTO Ingredient VALUES (217, 'Frozen Vegetables', 0, 'gr', 180, '[]');\n";
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
    public List<Ingredient> getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Ingredient";
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
    // Return an array of all my Ingredients
    public List<Ingredient> getAllMyIngredients() {
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

    // Shopping List section
    public List<ShoppingListItem> getAllShoppingListItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShoppingListItem> items = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ShoppingList";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ShoppingListItem item = new ShoppingListItem();
                    item.setShoppingListItemId(cursor.getInt(0));
                    item.setShoppingListItemName(cursor.getString(1));
                    item.setPurchasedQuantity(cursor.getFloat(2));
                    item.setIsFood(cursor.getInt(3) != 0);
                    item.setIsChecked(cursor.getInt(4) != 0);

                    items.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("MyShoppingListViewModel", "Error loading the shopping list items", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return items;
    }

    public void deleteShoppingListItem(int shoppingListItemId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList WHERE shoppingListItemId = '" + shoppingListItemId + "';";
        db.execSQL(query);
        db.close();
    }

    public List<ShoppingListItem> getAllFoodItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShoppingListItem> items = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ShoppingList WHERE isFood = 1";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ShoppingListItem item = new ShoppingListItem();
                    item.setShoppingListItemId(cursor.getInt(0));
                    item.setShoppingListItemName(cursor.getString(1));
                    item.setPurchasedQuantity(cursor.getFloat(2));
                    item.setIsFood(cursor.getInt(3) != 0);
                    item.setIsChecked(cursor.getInt(4) != 0);

                    items.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("MyIngredientsViewModel", "Error loading ingredients", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return items;
    }

    public List<ShoppingListItem> getAllNonFoodItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<ShoppingListItem> items = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM ShoppingList WHERE isFood = 0";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ShoppingListItem item = new ShoppingListItem();
                    item.setShoppingListItemId(cursor.getInt(0));
                    item.setShoppingListItemName(cursor.getString(1));
                    item.setPurchasedQuantity(cursor.getFloat(2));
                    item.setIsFood(cursor.getInt(3) != 0);
                    item.setIsChecked(cursor.getInt(4) != 0);

                    items.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("MyIngredientsViewModel", "Error loading ingredients", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return items;
    }

    public void newShoppingList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM ShoppingList;";
        db.execSQL(query);
        db.close();
    }

    public void addFoodItem(int Id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO ShoppingList VALUES ("+Id+",'', NULL, 1, 0);";
        db.execSQL(query);
        db.close();
    }
    public void addNonFoodItem(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO ShoppingList VALUES ("+Id+",'', NULL, 0, 0);";
        db.execSQL(query);
        db.close();
    }
    public int getNextUnusedShoppingListItemId(){
        SQLiteDatabase db = this.getReadableDatabase();
        int nextId = 1; // Default if table is empty

        try {
            Cursor cursor = db.rawQuery("SELECT MAX(shoppingListItemId) FROM ShoppingList", null);
            if (cursor.moveToFirst() && !cursor.isNull(0)) {
                nextId = cursor.getInt(0) + 1;
            }
            cursor.close();
        } finally {
            db.close();
        }
        return nextId;
    }

    public String getUnitSystem(String ingredientName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT unitSystem FROM Ingredient WHERE ingredientName = '" + ingredientName + "';";
        Cursor cursor = db.rawQuery(query, null);
        String unitSystem = "units";
        if (cursor.moveToFirst()) {
            unitSystem = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return unitSystem;
    }
    public void refillIngredients(List<ShoppingListItem> items) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Format to match DB format

        for (ShoppingListItem item : items) {
            String name = item.getShoppingListItemName();
            double quantity = item.getPurchasedQuantity();

            // Update quantity
            String updateQuantityQuery = "UPDATE Ingredient SET quantity = quantity + " + quantity +
                    " WHERE ingredientName = ?";
            db.execSQL(updateQuantityQuery, new Object[]{name});

            // Fetch daysToSpoil and checkIfSpoiledArray
            Cursor cursor = db.rawQuery("SELECT daysToSpoil, checkIfSpoiledArray FROM Ingredient WHERE ingredientName = ?", new String[]{name});
            if (cursor.moveToFirst()) {
                int daysToSpoil = cursor.getInt(0);
                String currentArray = cursor.getString(1);

                // Compute new spoil date
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, daysToSpoil);
                String newSpoilDate = sdf.format(cal.getTime());

                // Append to JSON array
                JSONArray spoilArray;
                try {
                    spoilArray = new JSONArray(currentArray);
                } catch (Exception e) {
                    spoilArray = new JSONArray();  // Start fresh if bad format
                }
                spoilArray.put(newSpoilDate);

                // Update the array in the DB
                ContentValues values = new ContentValues();
                values.put("checkIfSpoiledArray", spoilArray.toString());
                db.update("Ingredient", values, "ingredientName = ?", new String[]{name});
            }
            cursor.close();
            // Delete from shopping list
            db.execSQL("DELETE FROM ShoppingList WHERE shoppingListItemId = " + item.getShoppingListItemId() + ";");
        }

        db.close();
    }

    public void setNewItemNameAndQuantity(int id, String name, float quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET shoppingListItemName = '" + name + "', purchasedQuantity = " + quantity + " WHERE shoppingListItemId = " + id + ";";
        db.execSQL(query);
        db.close();
    }

    public void setShoppingListItemChecked(int Id, boolean isChecked){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET isChecked = " + isChecked + " WHERE shoppingListItemId = " + Id + ";";
        db.execSQL(query);
        db.close();
    }

    public void setShoppingListItemName(int Id,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET shoppingListItemName = '" + name + "' WHERE shoppingListItemId = '" + Id + "';";
        db.execSQL(query);
        db.close();
    }

    public void setShoppingListItemQuantity(int Id, float quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET purchasedQuantity = " + quantity + " WHERE shoppingListItemId = " + Id + ";";
        db.execSQL(query);
        db.close();
    }

    public void massCheck(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET isChecked = 1;";
        db.execSQL(query);
        db.close();
    }
    public void massUncheck() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE ShoppingList SET isChecked = 0;";
        db.execSQL(query);
        db.close();
    }
}