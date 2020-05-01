package com.yuong.skin.library.utils;

import android.content.res.TypedArray;

import androidx.appcompat.app.AppCompatActivity;

public class NavigationBarUtil {
    public static void forNavigationBar(AppCompatActivity activity) {
        TypedArray typedArray = activity.getTheme().obtainStyledAttributes(0, new int[] {
                android.R.attr.statusBarColor
        });
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        activity.getWindow().setNavigationBarColor(color);
    }

    public static void forNavigationBar(AppCompatActivity activity, int skinColor) {
        activity.getWindow().setNavigationBarColor(skinColor);
    }
}
