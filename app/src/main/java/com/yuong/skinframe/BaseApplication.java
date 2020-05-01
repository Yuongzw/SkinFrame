package com.yuong.skinframe;

import android.app.Application;

import com.yuong.skin.library.SkinManager;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
