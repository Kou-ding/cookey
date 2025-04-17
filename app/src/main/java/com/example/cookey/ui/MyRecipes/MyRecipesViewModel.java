package com.example.cookey.ui.MyRecipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyRecipesViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public MyRecipesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyRecipes fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }

}
