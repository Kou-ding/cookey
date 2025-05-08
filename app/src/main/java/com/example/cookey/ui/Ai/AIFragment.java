package com.example.cookey.ui.Ai;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.example.cookey.AIRecipeEditActivity;
import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.R;
import com.example.cookey.ShoppingListItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIFragment extends Fragment {

    private static final String API_URL = "https://api-inference.huggingface.co/models/google/flan-t5-large";
    private static final String API_TOKEN = "your_api_here"; // Replace with your token
    private AIAdapter adapter;
    private boolean useIngredientsMode = false;

    private ProgressBar progressBar; // Class-level variable

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout just once
        View view = inflater.inflate(R.layout.fragment_ai, container, false);

        TextInputEditText promptEditText = view.findViewById(R.id.textInputEditText);
        Button cookButton = view.findViewById(R.id.generate_ai_recipe);
        MaterialButton expandButton = view.findViewById(R.id.expandButton);
        RecyclerView recyclerView = view.findViewById(R.id.includeIngredientsRecyclerView);
        Button addIngredientButton = view.findViewById(R.id.addIngredientAI);
        CheckBox useMyIngredientsCheckbox = view.findViewById(R.id.useMyIngredients);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        addIngredientButton.setVisibility(view.GONE);
        useMyIngredientsCheckbox.setVisibility(view.GONE);

        // Recycler View
        adapter = new AIAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Listener for the cook button
        cookButton.setOnClickListener(v -> {
            // Make a recipe based on the user's prompt and choices

            if(useMyIngredientsCheckbox.isChecked()){
                // Pass My Ingredients
                generateAIRecipeAsync(promptEditText.getText().toString(),true, null);
            }else if(!adapter.getItems().isEmpty()){
                // Pass custom ingredients
                generateAIRecipeAsync(promptEditText.getText().toString(),false, adapter.getItems());
            } else {
                // Pass no ingredients
                generateAIRecipeAsync(promptEditText.getText().toString(),false, null);
            }
        });

        // Listener for the add ingredient button
        addIngredientButton.setOnClickListener(v -> {
            adapter.addItem();
            // Scroll to the new item
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });

        // Listener for the expand button
        expandButton.setOnClickListener(v -> {
            // Toggle the useIngredientsMode flag
            useIngredientsMode = !useIngredientsMode;
            recyclerView.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);
            addIngredientButton.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);
            useMyIngredientsCheckbox.setVisibility(useIngredientsMode ? View.VISIBLE : View.GONE);

            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    useIngredientsMode
                            ? R.drawable.ic_keyboard_arrow_up // Down arrow when collapsing
                            : R.drawable.ic_keyboard_arrow_down // Up arrow when expanding
            );
            expandButton.setIcon(newIcon);
        });

        // Listener for the checkbox check state
        useMyIngredientsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->{
            // Manage add ingredient recycler view visibility based on the checkbox state
            // Hide the recycler view and the add ingredient button
            recyclerView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            addIngredientButton.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });

        return view;
    }

    public String generateAPIResponse(String prompt) {
        try {
            // Prepare JSON request
            JSONObject requestBody = new JSONObject();
            requestBody.put("inputs", prompt);

            JSONObject parameters = new JSONObject();
            parameters.put("max_new_tokens", 250);
            parameters.put("temperature", 0.9);
            parameters.put("top_p", 0.95);
            parameters.put("do_sample", true);
            requestBody.put("parameters", parameters);

            // Create connection
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            int status = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream()
                    )
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            reader.close();

            // Parse JSON response
            String responseBody = response.toString();
            if (responseBody.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(responseBody);
                return jsonArray.getJSONObject(0).getString("generated_text");
            } else if (responseBody.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.optString("generated_text", "No generated text found.");
            }

            return "Unexpected response format.";

        } catch (Exception e) {
            Log.e("API Error", e.toString());
            return "API Error: " + e.toString(); // Gives more helpful error info than e.getMessage()
        }

    }
    public void generateAIRecipeAsync(String userPrompt, boolean useMyIngredients, List<ShoppingListItem> customItems) {
        Context context = getContext();
        if (context == null) return;

        // Show spinning wheel
        progressBar.setVisibility(View.VISIBLE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String prompt;

            if (useMyIngredients) {
                List<Ingredient> ingredients;
                try (DBHandler db = new DBHandler(context, null, null, 1)) {
                    ingredients = db.getAllIngredients();
                }

                StringBuilder ingredientsString = new StringBuilder();
                for (Ingredient ingredient : ingredients) {
                    ingredientsString.append(ingredient.getIngredientName()).append(", ");
                }

                if (ingredientsString.length() > 2) {
                    ingredientsString.setLength(ingredientsString.length() - 2);
                }

                prompt = userPrompt + "Use the following ingredients: " + ingredientsString;
            } else if (customItems != null && !customItems.isEmpty()) {
                StringBuilder customItemsString = new StringBuilder();
                for (ShoppingListItem item : customItems) {
                    if (!item.getShoppingListItemName().isEmpty()) {
                        customItemsString.append(item.getShoppingListItemName()).append(", ");
                    }
                }

                if (customItemsString.length() > 2) {
                    customItemsString.setLength(customItemsString.length() - 2);
                }

                prompt = userPrompt + "Use the following ingredients: " + customItemsString;
            } else {
                prompt = userPrompt;
            }

            String result = generateAPIResponse(prompt);

            // Post back to main thread
            handler.post(() -> {
                progressBar.setVisibility(View.GONE); // Hide
                Intent intent = new Intent(context, AIRecipeEditActivity.class);
                intent.putExtra("AIResponse", result);
                startActivity(intent);
            });
        });
    }
}
