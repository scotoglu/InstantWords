package com.scoto.instantwords.ui.adapter;


import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.databinding.BindingAdapter;

import com.scoto.instantwords.R;

public class BindAdapter {

    @BindingAdapter("setBgColor")
    public static void setBgColor(LinearLayout linearLayout, String bgColorCode) {
        if (!bgColorCode.isEmpty())
            linearLayout.setBackgroundColor(Color.parseColor(bgColorCode));
        else
            linearLayout.setBackgroundColor(Color.WHITE);
    }
    @BindingAdapter("contentBgColor")
    public static void contentBgColor(RelativeLayout relativeLayout,String colorCode){
        if (!colorCode.isEmpty())
            relativeLayout.setBackgroundColor(Color.parseColor(colorCode));
        else
            relativeLayout.setBackgroundColor(Color.WHITE);
    }


}
