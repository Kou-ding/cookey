package com.example.cookey.ui.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.cookey.MainActivity;
import com.example.cookey.NarratorHelper;
import com.example.cookey.NarratorManager;
import com.example.cookey.R;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Button changeLanguageBtn = root.findViewById(R.id.button);
        Button changeThemeBtn = root.findViewById(R.id.button2);

        MaterialSwitch narratorSwitch = root.findViewById(R.id.switchNarrator);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

       // NarratorHelper.enableNarrationForAllTextViews(root);

        //Init the switch according to saved key
        narratorSwitch.setChecked(prefs.getBoolean("narrator_enabled", false));

        // Saves the switch state
        narratorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("narrator_enabled", isChecked).apply();
        });

        narratorSwitch.setOnClickListener(v -> {
            NarratorManager.speakIfEnabled(this.getContext(), getString(R.string.narrator_btn));
        });


        changeLanguageBtn.setOnClickListener(v -> {
            NarratorManager.speakIfEnabled(this.getContext(), getString(R.string.change_language));
            changeLanguage();
        });

        changeThemeBtn.setOnClickListener(v ->{
                changeTheme();
                NarratorManager.speakIfEnabled(this.getContext(), getString(R.string.change_theme));
        });
        return root;
    }

    private void changeLanguage() {
        String[] languages = {"English", "Ελληνικά", "한국어"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Language");
        builder.setItems(languages, (dialog, which) -> {
            switch (which) {
                case 0:

                    setLocale("en");
                    break;
                case 1:

                    setLocale("el");
                    break;
                case 2:

                    setLocale("ko");
                    break;
            }
            NarratorManager.speakIfEnabled(requireContext(), languages[which]);
        });
        builder.show();
    }
    private void setLocale(String languageCode) {
        // Save for the next launches
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefs.edit().putString("app_lang", languageCode).apply();

        // Apply it right now
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        requireContext()
                .getResources()
                .updateConfiguration(config,
                        requireContext().getResources().getDisplayMetrics());

        // Restart Activity
        requireActivity().recreate();

    }
    private void changeTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        String currentTheme = prefs.getString("app_theme", "light");

        if ("dark".equals(currentTheme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putString("app_theme", "light");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putString("app_theme", "dark");
        }

        editor.apply();
        // Recreate the activity to apply the theme
        requireActivity().recreate();
    }

}