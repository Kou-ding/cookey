package com.example.cookey.ui.MyIngredients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyIngredientsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public MyIngredientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyIngredients fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
