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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.cookey.MainActivity;
import com.example.cookey.R;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Button changeLanguageBtn = root.findViewById(R.id.button);
        Button changeThemeBtn = root.findViewById(R.id.button2);
        changeLanguageBtn.setOnClickListener(v -> changeLanguage());
        changeThemeBtn.setOnClickListener(v -> changeTheme());
        return root;
    }

    private void changeLanguage() {
        String[] languages = {"English", "Ελληνικά", "Korean"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Language");
        builder.setItems(languages, (dialog, which) -> {
            switch (which) {
                case 0: setLocale("en"); break;
                case 1: setLocale("el"); break;
                case 2: setLocale("kr"); break;
            }
        });
        builder.show();
    }
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        requireActivity().finish();
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