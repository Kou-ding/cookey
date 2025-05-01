package com.example.cookey;

import android.content.Context;
import android.util.Log;

public class RecipeSeeder {

    public static void seed(Context context){

        DBHandler db = new DBHandler(context);

        //Recipe 1
        long id1 = db.addRecipe("Spaghetti Carbonara",30, "it", 2,"Medium",null,false);

        if(id1 != -1){
            db.addIngredientToRecipe(id1, "Spaghetti", 200, "grams", 180);
            db.addIngredientToRecipe(id1, "Eggs", 2, "pcs", 10);
            db.addIngredientToRecipe(id1, "Pecorino Romano", 50, "grams", 20);
            db.addIngredientToRecipe(id1, "Guanciale", 100, "grams", 7);
            db.addIngredientToRecipe(id1, "Black Pepper", 1, "tsp", 365);

            db.addStep("Boil the pasta in salted water.", id1, 1);
            db.addStep("Cook the guanciale until crispy.", id1, 2);
            db.addStep("Mix eggs and cheese in a bowl.", id1, 3);
            db.addStep("Combine all together off the heat.", id1, 4);

            db.addTagToRecipe(id1, "Italian");
            db.addTagToRecipe(id1, "Quick");
        }

        //Recipe 2
        long id2 = db.addRecipe("Vegan Buddha Bowl",20,"ch",1,"Easy",null,true);

        if (id2 != -1) {
            db.addIngredientToRecipe(id2, "Quinoa", 100, "g", 120);
            db.addIngredientToRecipe(id2, "Chickpeas", 150, "g", 180);
            db.addIngredientToRecipe(id2, "Avocado", 1, "pcs", 3);
            db.addIngredientToRecipe(id2, "Spinach", 50, "g", 4);
            db.addIngredientToRecipe(id2, "Tahini", 2, "tbsp", 60);

            db.addStep("Cook quinoa and let cool.", id2, 1);
            db.addStep("Drain and season chickpeas.", id2, 2);
            db.addStep("Assemble all ingredients in a bowl.", id2, 3);
            db.addStep("Drizzle tahini on top and serve.", id2, 4);

            db.addTagToRecipe(id2, "Vegan");
            db.addTagToRecipe(id2, "Healthy");
            db.addTagToRecipe(id2, "Gluten-Free");
        }

        //Recipe 3
        long id3 = db.addRecipe("Classic Pankcakes",25,"us",4,"Easy",null,false);
        if(id3 != -1){
            db.addIngredientToRecipe(id3, "Flour", 200, "grams", 365);
            db.addIngredientToRecipe(id3, "Milk", 250, "ml", 7);
            db.addIngredientToRecipe(id3, "Eggs", 2, "pcs", 10);
            db.addIngredientToRecipe(id3, "Baking Powder", 1, "tsp", 365);
            db.addIngredientToRecipe(id3, "Butter", 30, "grams", 15);

            db.addStep("Mix dry ingredients.", id3, 1);
            db.addStep("Add wet ingredients and whisk.", id3, 2);
            db.addStep("Heat pan and cook pancakes.", id3, 3);

            db.addTagToRecipe(id3, "Breakfast");
            db.addTagToRecipe(id3, "Sweet");
        }

        Log.d("Seeder", "3 recipes added successfully.");
    }
}
