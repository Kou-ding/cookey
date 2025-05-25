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
        String[] languages = {"English", "Ελληνικά", "한국어"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Choose Language");
        builder.setItems(languages, (dialog, which) -> {
            switch (which) {
                case 0: setLocale("en"); break;
                case 1: setLocale("el"); break;
                case 2: setLocale("ko"); break;
            }
        });
        builder.show();
    }

    private void saveLanguagePreference(String langCode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        prefs.edit().putString("app_lang", langCode).apply();
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

        // Restart MainActivity
        Intent refresh = new Intent(getActivity(), MainActivity.class);
        startActivity(refresh);
        requireActivity().finish();
    }
    private void changeTheme() {
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentTheme == Configuration.UI_MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        requireActivity().recreate();
    }
}