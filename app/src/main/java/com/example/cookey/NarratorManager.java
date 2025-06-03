package com.example.cookey;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

//THIS WAS A REQUEST MADE BY PERSON WITH VISION IMPAIRMENT. SHE'S AWESOME ✨
public class NarratorManager {
    private static boolean enabled;
    private static TextToSpeech tts;

    public static void init(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = prefs.getBoolean("narrator_enabled", false);
        tts = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
            }
        });
    }

    public static void speakIfEnabled(Context context, String text) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = prefs.getBoolean("narrator_enabled", false);

        if (enabled && tts != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public static void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

}
