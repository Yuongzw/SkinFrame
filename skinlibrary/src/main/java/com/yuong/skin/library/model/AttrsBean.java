package com.yuong.skin.library.model;

import android.content.res.TypedArray;
import android.util.SparseIntArray;

/**
 * 需要换肤的属性的封装类
 */
public class AttrsBean {
    private SparseIntArray resourcesMap;//属性的集合
    private static final int DEFAULT_VALUE = -1;

    public AttrsBean() {
        resourcesMap = new SparseIntArray();
    }

    /**
     * 保存属性到集合中
     * @param typedArray
     * @param styleable
     */
    public void saveViewResource(TypedArray typedArray, int[] styleable) {
        for (int i = 0; i < typedArray.length(); i++) {
            int key = styleable[i];
            int resourceId = typedArray.getResourceId(i, DEFAULT_VALUE);
            resourcesMap.put(key, resourceId);
        }
    }

    public int getViewResource(int styleable) {
        return resourcesMap.get(styleable);
    }

}
