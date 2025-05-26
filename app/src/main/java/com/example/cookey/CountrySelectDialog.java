package com.example.cookey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CountrySelectDialog extends Dialog {

    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private final OnCountrySelectedListener listener;

    public interface OnCountrySelectedListener {
        void onCountrySelected(CountryModel country);
    }

    public CountrySelectDialog(@NonNull Context context, OnCountrySelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_country);

        recyclerView = findViewById(R.id.recyclerViewCountries);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        List<CountryModel> countryModels = loadCountriesFromJson();

        adapter = new CountryAdapter(getContext(), countryModels, country -> {
            if (listener != null) listener.onCountrySelected(country);
            dismiss();
        });


        recyclerView.setAdapter(adapter);
    }

    private List<CountryModel> loadCountriesFromJson(){
        List<CountryModel> countries = new ArrayList<>();

        //Json loading
        try (InputStream is = getContext().getAssets().open("countries.json")) {
            byte[] buf = new byte[is.available()];
            is.read(buf);
            JSONArray arr = new JSONArray(new String(buf, StandardCharsets.UTF_8));

            long idCounter = 1;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o      = arr.getJSONObject(i);
                String     name   = o.getString("name");
                String     code   = o.getString("code").toLowerCase();
                String path     = "Flags/" + code + ".png";              // assets/flags/us.png

            /* Verify that the PNG actually exists in assets.
               If it doesn't, we'll fall back to the placeholder later. */
                try (InputStream test = getContext().getAssets().open(path)) {
                    /* OK – file exists. Nothing to do. */
                } catch (IOException e) {                  // file missing then...
                    path = null;                           // --> adapter shows placeholder
                }

                countries.add(new CountryModel(idCounter++, name, code, path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }
}