package com.example.cookey.ui.MyRecipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cookey.DBHandler;
import com.example.cookey.R;

public class MyRecipesFragment extends Fragment {

    public MyRecipesFragment() {
        // Required empty public constructor
    }
    private DBHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        Button dbdropper = view.findViewById(R.id.dropDB);
        Button dbexecutor = view.findViewById(R.id.executeCMD);

        db = new DBHandler(getContext(),null,null,1);
        dbdropper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.dropDatabase();
            }
        });
        dbexecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command =
                    "INSERT INTO Ingredient VALUES (1, 'Whole Milk', 1.0, 'liters', 7, '[\"2024-04-05\",\"2024-04-07\"]');\n" +
                    "INSERT INTO Ingredient VALUES (2, 'Large Eggs', 12, 'count', 28, '[\"2024-04-10\",\"2024-04-15\"]');\n" +
                    "INSERT INTO Ingredient VALUES (3, 'Greek Yogurt', 500, 'grams', 14, '[\"2024-04-08\"]');\n" +
                    "INSERT INTO Ingredient VALUES (4, 'Spaghetti', 500, 'grams', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (5, 'Yellow Onions', 3, 'count', 30, '[\"2024-04-01\"]');\n" +
                    "INSERT INTO Ingredient VALUES (6, 'Feta Cheese', 200, 'grams', 21, '[\"2024-04-12\"]');\n" +
                    "INSERT INTO Ingredient VALUES (7, 'Cheddar Cheese', 250, 'grams', 30, '[\"2024-04-05\",\"2024-04-10\"]');\n" +
                    "INSERT INTO Ingredient VALUES (8, 'IPA Beer', 6, 'bottles', 180, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (9, 'Chicken Breast', 600, 'grams', 3, '[\"2024-04-03\"]');\n" +
                    "INSERT INTO Ingredient VALUES (10, 'Fresh Basil', 1, 'bunch', 5, '[\"2024-04-04\"]');\n" +
                    "INSERT INTO Ingredient VALUES (11, 'Romaine Lettuce', 1, 'head', 7, '[\"2024-04-05\",\"2024-04-08\"]');\n" +
                    "INSERT INTO Ingredient VALUES (12, 'Strawberries', 500, 'grams', 5, '[\"2024-04-06\"]');\n" +
                    "INSERT INTO Ingredient VALUES (13, 'Baking Powder', 1, 'can', 180, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (14, 'Vanilla Extract', 100, 'ml', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (15, 'Salmon Fillet', 300, 'grams', 2, '[\"2024-04-04\"]');\n" +
                    "INSERT INTO Ingredient VALUES (16, 'Ground Beef', 450, 'grams', 3, '[\"2024-04-05\"]');\n" +
                    "INSERT INTO Ingredient VALUES (17, 'Tomato Paste', 200, 'grams', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (18, 'Black Beans', 400, 'grams', 1095, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (19, 'Soy Sauce', 250, 'ml', 730, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (20, 'Mixed Vegetables', 750, 'grams', 180, '[\"2024-04-01\"]');" +

                    "INSERT INTO ShoppingList VALUES (1, 'Whole Milk', 1.0, '2023-06-15', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (2, 'Bread', 2.0, '2023-06-16', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (3, 'Eggs', 12.0, '2023-06-17', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (4, 'Chicken', 1.5, '2023-06-18', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (5, 'Rice', 5.0, '2023-06-19', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (6, 'Pasta', 2.0, '2023-06-20', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (7 ,'Tomatoes', 6.0, '2023-06-21', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (8, 'Potatoes', 10.0, '2023-06-22', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (9, 'Onions', 4.0, '2023-06-23', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (10, 'Apples', 8.0, '2023-06-24', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (11, 'Bananas', 5.0, '2023-06-25', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (12, 'Toilet Paper', 4.0, '2023-06-26', 0);\n" +
                    "INSERT INTO ShoppingList VALUES (13, 'Soap', 2.0, '2023-06-27', 0);\n" +
                    "INSERT INTO ShoppingList VALUES (14, 'Shampoo', 1.0, '2023-06-28', 0);\n" +
                    "INSERT INTO ShoppingList VALUES (15, 'Toothpaste', 1.0, '2023-06-29', 0);\n" +
                    "INSERT INTO ShoppingList VALUES (16, 'Coffee', 1.0, '2023-06-30', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (17, 'Tea', 1.0, '2023-07-01', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (18, 'Sugar', 2.0, '2023-07-02', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (19, 'Salt', 1.0, '2023-07-03', 1);\n" +
                    "INSERT INTO ShoppingList VALUES (20, 'Olive Oil', 1.0, '2023-07-04', 1);\n";
                db.executeCommand(command);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}