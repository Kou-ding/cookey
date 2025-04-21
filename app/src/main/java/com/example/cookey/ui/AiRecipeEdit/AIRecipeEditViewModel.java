package com.example.cookey.ui.AiRecipeEdit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AIRecipeEditViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public AIRecipeEditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Ai Recipe Edit fragment");
    }
    public LiveData<String> getText() {return mText;}
}
