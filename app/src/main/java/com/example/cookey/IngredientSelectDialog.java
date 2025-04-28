package com.example.cookey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IngredientSelectDialog extends Dialog {

    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private OnIngredientSelectedListener listener;

    public interface OnIngredientSelectedListener {
        void onIngredientSelected(IngredientModel ingredient, double quantity);
    }

    public IngredientSelectDialog(@NonNull Context context, OnIngredientSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_ingredient);

        recyclerView = findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<IngredientModel> dummyIngredients = new ArrayList<>();
        dummyIngredients.add(new IngredientModel(1, "Rice", "kg"));
        dummyIngredients.add(new IngredientModel(2, "Salt", "g"));
        dummyIngredients.add(new IngredientModel(3, "Water", "ml"));
        dummyIngredients.add(new IngredientModel(4, "Flour", "kg"));
        dummyIngredients.add(new IngredientModel(5, "Sugar", "g"));
        dummyIngredients.add(new IngredientModel(5, "Butter", "g"));
        dummyIngredients.add(new IngredientModel(5, "Egg", "kg"));
        dummyIngredients.add(new IngredientModel(5, "Garlic", "g"));
        dummyIngredients.add(new IngredientModel(5, "Cat", "kg"));
        dummyIngredients.add(new IngredientModel(5, "Olive Oil", "ml"));
        dummyIngredients.add(new IngredientModel(5, "Potato", "kg"));
        dummyIngredients.add(new IngredientModel(5, "Celery", "g"));
        dummyIngredients.add(new IngredientModel(5, "Carrot", "g"));
        dummyIngredients.add(new IngredientModel(5, "Mushroom", "kg"));
        dummyIngredients.add(new IngredientModel(5, "Lemon", "ml"));
        dummyIngredients.add(new IngredientModel(5, "Parmesan", "g"));
        dummyIngredients.add(new IngredientModel(5, "Cheddar", "g"));

        adapter = new IngredientAdapter(dummyIngredients, ingredient -> showQuantityDialog(ingredient));
        recyclerView.setAdapter(adapter);
    }

    private void showQuantityDialog(IngredientModel ingredient) {
        Dialog quantityDialog = new Dialog(getContext());
        quantityDialog.setContentView(R.layout.dialog_enter_quantity);
        EditText editTextQuantity = quantityDialog.findViewById(R.id.editTextQuantity);
        quantityDialog.findViewById(R.id.buttonConfirmQuantity).setOnClickListener(v -> {
            String quantityText = editTextQuantity.getText().toString().trim();
            if (!quantityText.isEmpty()) {
                double quantity = Double.parseDouble(quantityText);
                listener.onIngredientSelected(ingredient, quantity);
                quantityDialog.dismiss();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });
        quantityDialog.show();
    }
}
