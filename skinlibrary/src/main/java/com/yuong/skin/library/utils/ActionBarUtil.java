package com.yuong.skin.library.utils;

import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActionBarUtil {
    public static void forActionBar(AppCompatActivity activity) {
        TypedArray typedArray = activity.getTheme().obtainStyledAttributes(0, new int[] {
           android.R.attr.colorPrimary
        });
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public static void forActionBar(AppCompatActivity activity, int skinColor) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(skinColor));
        }
    }
}
