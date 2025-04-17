package com.example.cookey.ui.MyList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyListViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public MyListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myList fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }
}
