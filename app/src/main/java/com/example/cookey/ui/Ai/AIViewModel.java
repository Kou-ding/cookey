package com.example.cookey.ui.Ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AIViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public AIViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is AI fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
