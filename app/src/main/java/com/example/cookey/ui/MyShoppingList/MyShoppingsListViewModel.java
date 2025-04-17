package com.example.cookey.ui.MyShoppingList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyShoppingsListViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public MyShoppingsListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyShoppingList fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
