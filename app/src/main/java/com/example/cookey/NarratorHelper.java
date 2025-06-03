package com.example.cookey;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NarratorHelper {

    public static void enableNarrationForAllTextViews(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                enableNarrationForAllTextViews(group.getChildAt(i));
            }
        } else if (root instanceof TextView) {
            TextView textView = (TextView) root;
            textView.setOnClickListener(v -> {
                CharSequence text = textView.getText();
                if (text != null && text.length() > 0) {
                    NarratorManager.speakIfEnabled(v.getContext(), text.toString());
                }
            });
        }
    }
}
