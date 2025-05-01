package com.example.cookey;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountrySelectDialog extends Dialog {

    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private OnCountrySelectedListener listener;

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

        /*

        List<CountryModel> dummyCountries = new ArrayList<>();
        dummyCountries.add(new CountryModel(1, "Korea", R.drawable.kr));
        dummyCountries.add(new CountryModel(2, "Japan", R.drawable.jp));
        dummyCountries.add(new CountryModel(3, "Germany", R.drawable.de));
        dummyCountries.add(new CountryModel(4, "Italy", R.drawable.it));
        dummyCountries.add(new CountryModel(5, "Greece", R.drawable.gr));

         */


        List<CountryModel> countryList = loadCountriesFromJson(getContext());
        adapter = new CountryAdapter(countryList, country -> {
            if (listener != null) {
                listener.onCountrySelected(country);
                dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<CountryModel> loadCountriesFromJson(Context context){
        List<CountryModel> countries = new ArrayList<>();

        //Typical json loading
        try{
            InputStream is = context.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            int idCounter = 1;

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                String code = obj.getString("code");

                @SuppressLint("DiscouragedApi")
                int flagResID = context.getResources().getIdentifier(code.toLowerCase(), "drawable", context.getPackageName());

                //if there is not such flag, replace with placeholder
                if(flagResID == 0){
                    flagResID = R.drawable.ic_flag_placeholder;
                }

                countries.add(new CountryModel(idCounter++,name,code, flagResID));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return countries;
    }
}
