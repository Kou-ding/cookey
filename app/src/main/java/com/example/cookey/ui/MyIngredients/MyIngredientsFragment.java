package com.example.cookey.ui.MyIngredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.IngredientAdapter;
import com.example.cookey.R;
import java.util.ArrayList;

public class MyIngredientsFragment extends Fragment {

    private IngredientAdapter adapter;
    private MyIngredientsViewModel viewModel;
    private boolean editing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);

        // Initialize the recycle view
        RecyclerView recyclerView = view.findViewById(R.id.myIngredientRecyclerView);

        // Initialize the edit button
        Button editButton = view.findViewById(R.id.editButton);

        // Initialize the view model
        viewModel = new ViewModelProvider(this).get(MyIngredientsViewModel.class);

        // Load the ingredients from the database
        viewModel.loadIngredients(requireContext());

        // Initialize the adapter for the recycle view
        adapter = new IngredientAdapter(new ArrayList<>(), requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Observe the ingredients from the view model and update the adapter when they change
        viewModel.getIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            adapter.updateIngredients(ingredients);
            editButton.setEnabled(!ingredients.isEmpty());
        });

        // Listen for clicks on the edit button
        editButton.setOnClickListener(v -> {
            editing = !editing;
            adapter.setEditingEnabled(editing);
            //Change the text of the button based on the editing state
            editButton.setText(editing ? getString(R.string.done) : getString(R.string.edit));
        });

        // Listen for clicks on the delete button
        adapter.setOnDeleteClickListener(ingredient ->
            viewModel.deleteIngredient(requireContext(), ingredient.getIngredientId())
        );

        return view;
    }
}
