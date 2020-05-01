package com.yuong.skin.library.model;

import android.content.res.Resources;

public class SkinCache {
    private Resources skinResource; //用于加载皮肤包资源
    private String skinPackageName; //皮肤包包名

    public SkinCache(Resources skinResource, String skinPackageName) {
        this.skinResource = skinResource;
        this.skinPackageName = skinPackageName;
    }

    public Resources getSkinResource() {
        return skinResource;
    }

    public String getSkinPackageName() {
        return skinPackageName;
    }
}
