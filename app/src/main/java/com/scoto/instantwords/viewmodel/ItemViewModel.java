package com.scoto.instantwords.viewmodel;

import android.content.ClipData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isToolbarOpen  =new MutableLiveData<>();
    public void setData(boolean isToolbarOpened){
        isToolbarOpen.setValue(isToolbarOpened);
    }
    public MutableLiveData<Boolean> getData(){
        return isToolbarOpen;
    }
}
