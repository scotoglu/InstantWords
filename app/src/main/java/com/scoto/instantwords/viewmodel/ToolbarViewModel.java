package com.scoto.instantwords.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ToolbarViewModel extends ViewModel {

    private final MutableLiveData<Boolean> toolbarStates = new MutableLiveData<>();

    public MutableLiveData<Boolean> getToolbarState() {
        return toolbarStates;
    }

    public void setToolbarState(boolean toolbarState) {
        toolbarStates.setValue(toolbarState);
    }

}
