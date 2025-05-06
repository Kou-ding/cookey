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
                        // 10 Debugging Ingredients
                        "INSERT INTO Ingredient VALUES (1, 'Whole Milk', 1.0, 'liters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (2, 'Large Eggs', 12, 'count', 28, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (3, 'Greek Yogurt', 500, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (4, 'Spaghetti', 500, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (5, 'Yellow Onions', 3, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (6, 'Feta Cheese', 200, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (7, 'Cheddar Cheese', 250, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (8, 'IPA Beer', 6, 'bottles', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (9, 'Chicken Breast', 600, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (10, 'Fresh Basil', 1, 'bunch', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (11, 'Salt', NULL, 'grams', 1000, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (12, 'Black Pepper', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (13, 'White Rice', NULL, 'grams', 540, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (14, 'Garlic', NULL, 'cloves', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (15, 'Olive Oil', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (16, 'All-Purpose Flour', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (17, 'Granulated Sugar', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (18, 'Brown Sugar', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (19, 'Baking Soda', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (20, 'Baking Powder', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (21, 'White Vinegar', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (22, 'Soy Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (23, 'Tomato Paste', NULL, 'grams', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (24, 'Canned Corn', NULL, 'grams', 540, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (25, 'Carrots', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (26, 'Bell Peppers', NULL, 'count', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (27, 'Potatoes', NULL, 'count', 45, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (28, 'Ketchup', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (29, 'Mayonnaise', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (30, 'Mustard', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (31, 'Cinnamon', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (32, 'Nutmeg', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (33, 'Canned Beans', NULL, 'grams', 540, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (34, 'Apple Cider Vinegar', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (35, 'Ground Beef', NULL, 'grams', 2, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (36, 'Frozen Peas', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (37, 'Frozen Spinach', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (38, 'Parmesan Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (39, 'Mozzarella Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (40, 'Butter', NULL, 'grams', 60, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (41, 'Heavy Cream', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (42, 'Tofu', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (43, 'Coconut Milk', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (44, 'Curry Powder', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (45, 'Chili Powder', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (46, 'Cumin', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (47, 'Chickpeas', NULL, 'grams', 540, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (48, 'Noodles', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (49, 'Green Beans', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (50, 'Zucchini', NULL, 'count', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (51, 'Sour Cream', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (52, 'Yogurt', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (53, 'Frozen Berries', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (54, 'Bacon', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (55, 'Bananas', NULL, 'count', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (56, 'Apples', NULL, 'count', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (57, 'Oranges', NULL, 'count', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (58, 'Lemons', NULL, 'count', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (59, 'Limes', NULL, 'count', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (60, 'Strawberries', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (61, 'Blueberries', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (62, 'Raisins', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (63, 'Honey', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (64, 'Maple Syrup', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (65, 'Vanilla Extract', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (66, 'Yeast', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (67, 'Cornstarch', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (68, 'Cocoa Powder', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (69, 'Tea Bags', NULL, 'count', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (70, 'Coffee Grounds', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (71, 'Mixed Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (72, 'Banana Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (73, 'Sparkling Water', NULL, 'bottles', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (74, 'Lemonade', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (75, 'Lettuce', NULL, 'heads', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (76, 'Tomatoes', NULL, 'count', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (77, 'Cucumbers', NULL, 'count', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (78, 'Celery', NULL, 'stalks', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (79, 'Cabbage', NULL, 'heads', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (80, 'Broccoli', NULL, 'heads', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (81, 'Cauliflower', NULL, 'heads', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (82, 'Green Onions', NULL, 'stalks', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (83, 'Mushrooms', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (84, 'Sweet Potatoes', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (85, 'Radishes', NULL, 'count', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (86, 'Beets', NULL, 'count', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (87, 'Spinach', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (88, 'Arugula', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (89, 'Asparagus', NULL, 'spears', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (90, 'Eggplant', NULL, 'count', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (91, 'Chili Peppers', NULL, 'count', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (92, 'Pumpkin', NULL, 'count', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (93, 'Turnips', NULL, 'count', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (94, 'Leeks', NULL, 'stalks', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (95, 'Kalamata Olives', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (96, 'Graviera Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (97, 'Yogurt desert', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (98, 'Halloumi Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (99, 'Orzo Pasta', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (100, 'Phyllo Dough', NULL, 'sheets', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (101, 'Ouzo', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (102, 'Greek Honey', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (103, 'Capers', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (104, 'Ladotyri Cheese', NULL, 'grams', 60, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (105, 'Saganaki Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (106, 'Tzatziki Sauce', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (107, 'Dolmades', NULL, 'count', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (108, 'Taramosalata', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (109, 'Baklava', NULL, 'pieces', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (110, 'Pastitsio Pasta', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (111, 'Manouri Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (112, 'Trahana', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (113, 'Greek Oregano', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (114, 'Loukaniko Sausage', NULL, 'grams', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (115, 'Red Wine', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (116, 'White Wine', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (117, 'Rosé Wine', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (118, 'Sparkling Wine', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (119, 'Champagne', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (120, 'Whiskey', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (121, 'Vodka', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (122, 'Rum', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (123, 'Gin', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (124, 'Tequila', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (125, 'Brandy', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (126, 'Port Wine', NULL, 'milliliters', 1095, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (127, 'Sherry', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (128, 'Baileys Irish Cream', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (129, 'Cognac', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (130, 'Absinthe', NULL, 'milliliters', 1825, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (131, 'Beer', NULL, 'bottles', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (132, 'Tsikoudia', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (133, 'Cider', NULL, 'bottles', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (134, 'Sake', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (135, 'Cola', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (136, 'Lemon-Lime Soda', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (137, 'Tonic Water', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (138, 'Club Soda', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (139, 'Ginger Ale', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (140, 'Root Beer', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (141, 'Orange Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (142, 'Apple Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (143, 'Cranberry Juice', NULL, 'milliliters', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (144, 'Pineapple Juice', NULL, 'milliliters', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (145, 'Tomato Juice', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (146, 'Lemon Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (147, 'Lime Juice', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (148, 'Iced Tea', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (149, 'Sweet Tea', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (150, 'Cold Brew Coffee', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (151, 'Almond Milk', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (152, 'Oat Milk', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (153, 'Soy Milk', NULL, 'milliliters', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (154, 'Coconut Water', NULL, 'milliliters', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (155, 'Mozzarella Cheese', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (156, 'Parmesan Cheese', NULL, 'grams', 60, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (157, 'Ricotta Cheese', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (158, 'Blue Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (159, 'Gorgonzola Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (160, 'Brie Cheese', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (161, 'Camembert Cheese', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (162, 'Swiss Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (163, 'Provolone Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (164, 'Gruyère Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (165, 'Pecorino Romano', NULL, 'grams', 60, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (166, 'Cream Cheese', NULL, 'grams', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (167, 'Cottage Cheese', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (168, 'Colby Jack Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (169, 'Monterey Jack Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (170, 'Queso Fresco', NULL, 'grams', 10, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (171, 'Paneer', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (172, 'Smoked Gouda', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (173, 'Edam Cheese', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (174, 'Roquefort Cheese', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (175, 'Ground Beef', NULL, 'grams', 2, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (176, 'Pork Chops', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (177, 'Bacon', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (178, 'Sausages', NULL, 'count', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (179, 'Ham', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (180, 'Salami', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (181, 'Turkey Breast', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (182, 'Roast Beef', NULL, 'grams', 5, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (183, 'Chicken Thighs', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (184, 'Chicken Wings', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (185, 'Beef Steak', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (186, 'Lamb Chops', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (187, 'Chorizo', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (188, 'Prosciutto', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (189, 'Duck Breast', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (190, 'Veal Cutlets', NULL, 'grams', 3, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (191, 'Pastrami', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (192, 'Corned Beef', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (193, 'Bresaola', NULL, 'grams', 21, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (194, 'Mortadella', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (195, 'White Rice', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (196, 'Brown Rice', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (197, 'Quinoa', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (198, 'Couscous', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (199, 'Bulgur', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (200, 'Barley', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (201, 'Farro', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (202, 'Lentils', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (203, 'Chickpeas', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (204, 'Black Beans', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (205, 'Kidney Beans', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (206, 'Pinto Beans', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (207, 'Navy Beans', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (208, 'Cannellini Beans', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (209, 'Split Peas', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (210, 'Green Peas (Dried)', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (211, 'Millet', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (212, 'Buckwheat', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (213, 'Oats', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (214, 'Polenta (Cornmeal)', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (215, 'Fresh Parsley', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (216, 'Fresh Cilantro', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (217, 'Fresh Dill', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (218, 'Fresh Mint', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (219, 'Fresh Rosemary', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (220, 'Fresh Thyme', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (221, 'Fresh Sage', NULL, 'grams', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (222, 'Fresh Chives', NULL, 'grams', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (223, 'Lemongrass', NULL, 'stalks', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (224, 'Ginger (Fresh)', NULL, 'grams', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (225, 'Turmeric (Fresh)', NULL, 'grams', 30, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (226, 'Powdered Sugar', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (227, 'Molasses', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (228, 'Puff Pastry', NULL, 'sheets', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (229, 'Pie Crust (Pre-made)', NULL, 'sheets', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (230, 'Shortening', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (231, 'Cream of Tartar', NULL, 'grams', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (232, 'Almond Extract', NULL, 'milliliters', 730, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (233, 'Food Coloring', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (234, 'Sprinkles', NULL, 'grams', 365, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (235, 'Almonds', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (236, 'Walnuts', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (237, 'Pecans', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (238, 'Cashews', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (239, 'Pistachios', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (240, 'Hazelnuts', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (241, 'Pine Nuts', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (242, 'Sunflower Seeds', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (243, 'Pumpkin Seeds', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (244, 'Chia Seeds', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (245, 'Flaxseeds', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (246, 'Sesame Seeds', NULL, 'grams', 365, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (247, 'Canned Tuna', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (248, 'Canned Salmon', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (249, 'Anchovies', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (250, 'Sardines', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (251, 'Artichoke Hearts', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (252, 'Roasted Red Peppers', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (253, 'Sun-Dried Tomatoes', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (254, 'Pickles', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (255, 'Jalapeños (Pickled)', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (256, 'Tahini', NULL, 'grams', 365, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (257, 'Miso Paste', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (258, 'Fish Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (259, 'Oyster Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (260, 'Hoisin Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (261, 'Gochujang', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (262, 'Sriracha', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (263, 'Harissa', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (264, 'Tamarind Paste', NULL, 'grams', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (265, 'Panko Breadcrumbs', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (266, 'Rice Vinegar', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (267, 'Mirin', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (268, 'Sesame Oil', NULL, 'milliliters', 180, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (269, 'Frozen Pizza Dough', NULL, 'grams', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (270, 'Frozen Dumplings', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (271, 'Frozen Shrimp', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (272, 'Frozen Mixed Vegetables', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (273, 'Frozen Mango', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (274, 'Frozen Pineapple', NULL, 'grams', 180, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (275, 'Breadcrumbs', NULL, 'grams', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (276, 'Corn Tortillas', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (277, 'Flour Tortillas', NULL, 'count', 30, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (278, 'Pita Bread', NULL, 'count', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (279, 'Naan Bread', NULL, 'count', 14, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (280, 'Baguette', NULL, 'count', 7, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (281, 'Chicken Broth', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (282, 'Beef Broth', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (283, 'Vegetable Broth', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (284, 'Bouillon Cubes', NULL, 'count', 365, '[]');\n" +

                        "INSERT INTO Ingredient VALUES (285, 'Worcestershire Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (286, 'Hot Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (287, 'Barbecue Sauce', NULL, 'milliliters', 365, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (288, 'Marinara Sauce', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (289, 'Alfredo Sauce', NULL, 'milliliters', 180, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (290, 'Pesto', NULL, 'grams', 90, '[]');\n" +
                        "INSERT INTO Ingredient VALUES (291, 'Hummus', NULL, 'grams', 14, '[]');\n" +

                        // Their 10 Corresponding Shopping List Items
                        "INSERT INTO ShoppingList VALUES (1, 'Whole Milk', 1.0, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (2, 'Large Eggs', 12, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (3, 'Greek Yogurt', 500, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (4, 'Spaghetti', 500, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (5, 'Yellow Onions', 3, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (6, 'Feta Cheese', 200, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (7, 'Cheddar Cheese', 250, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (8, 'IPA Beer', 6, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (9, 'Chicken Breast', 600, 1, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (10, 'Fresh Basil', 1, 1, 0);\n" +

                        // 5 Non Food Shopping List Items
                        "INSERT INTO ShoppingList VALUES (11, 'Toilet Paper', 4.0, 0, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (12, 'Soap', 2.0, 0, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (13, 'Shampoo', 1.0, 0, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (14, 'Toothpaste', 1.0, 0, 0);\n" +
                        "INSERT INTO ShoppingList VALUES (15, 'Coffee', 1.0, 1, 0);\n";
                db.executeCommand(command);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}