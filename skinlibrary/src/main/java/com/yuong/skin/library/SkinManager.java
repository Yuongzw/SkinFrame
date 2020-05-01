package com.yuong.skin.library;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.yuong.skin.library.model.SkinCache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SkinManager {
    private static SkinManager instance;
    private Application application;
    private static Resources appResources;  //本应用的resource
    private static Resources skinResource;  //皮肤包的resource
    private static String skinPackageName;  //皮肤包的包名
    private boolean isDefaultSkin = true;    //是否默认的皮肤（APP内置的皮肤）
    private static final String ADD_ASSEST_PATH = "addAssetPath";//方法名
    private Map<String, SkinCache> cacheSkin;

    private SkinManager(Application application) {
        this.application = application;
        if (this.application != null) {
            appResources = this.application.getResources();
        }
        cacheSkin = new HashMap<>();
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        return instance;
    }

    /**
     * 加载外部皮肤包资源
     *
     * @param skinPath 皮肤包路径，如果为空，就加载内置的资源
     */
    public void loadSkinResource(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            isDefaultSkin = true;
            return;
        }
        if (cacheSkin.containsKey(skinPath)) {
            isDefaultSkin = false;
            SkinCache skinCache = cacheSkin.get(skinPath);
            if (null != skinCache) {
                skinResource = skinCache.getSkinResource();
                skinPackageName = skinCache.getSkinPackageName();
                return;
            }
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //addAssetPath 方法在系统api中是Hide属性，所以只能通过反射来获取
            Method addAssetPath = assetManager.getClass().getDeclaredMethod(ADD_ASSEST_PATH, String.class);
            addAssetPath.setAccessible(true);
            //执行方法
            addAssetPath.invoke(assetManager, skinPath);
            //如果担心@hide 限制，可以反射调用 addAssetPathInternal()方法，addAssetPath()方法最终还是条用前面的方法。

            //加载外部皮肤包的资源Resource
            skinResource = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());

            //获取皮肤包的包名
            skinPackageName = application.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
            //如果无法获取皮肤包的包名，就用内置的皮肤
            isDefaultSkin = TextUtils.isEmpty(skinPackageName);
            if (!isDefaultSkin) {
                cacheSkin.put(skinPath, new SkinCache(skinResource, skinPackageName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            isDefaultSkin = true;
        }
    }

    /**
     * 通过本应用的 resourceId获取皮肤包的resourceId，如果返回为0或者小于0的数，就取原来的id
     *
     * @param resourceId 本应用的resourceId
     * @return
     */
    private int getSkinResourceId(int resourceId) {
        int resultResourceId = resourceId;
        //获取本应用的对应的资源id的资源名
        String resourceName = appResources.getResourceName(resourceId);
        if (!isDefaultSkin) {
            String[] strings = resourceName.split(":");
            resourceName = skinPackageName + ":" + strings[1];
        }
        //获取本应用的对应的的资源id的资源类型，如drawable、mipmap等
        String resourceTypeName = appResources.getResourceTypeName(resourceId);

        if (skinResource != null) {
            resultResourceId = skinResource.getIdentifier(resourceName, resourceTypeName, skinPackageName);
            isDefaultSkin = resultResourceId == 0;
        }
        return resultResourceId == 0 ? resourceId : resultResourceId;
    }

    public boolean isDefaultSkin() {
        return isDefaultSkin;
    }

    public int getColor(int resourceId) {
        int ids = getSkinResourceId(resourceId);
        return isDefaultSkin ? appResources.getColor(ids) : skinResource.getColor(ids);
    }

    public ColorStateList getColorStateList(int resourceId) {
        int ids = getSkinResourceId(resourceId);
        return isDefaultSkin ? appResources.getColorStateList(ids) : skinResource.getColorStateList(ids);
    }

    public Drawable getDrawableOrMipmap(int resourceId) {
        int ids = getSkinResourceId(resourceId);
        return isDefaultSkin ? appResources.getDrawable(ids) : skinResource.getDrawable(ids);
    }

    public String getString(int resourceId) {
        int ids = getSkinResourceId(resourceId);
        return isDefaultSkin ? appResources.getString(ids) : skinResource.getString(ids);
    }

    public Object getBackgroundOrSrc(int resourceId) {
        String resourceTypeName = appResources.getResourceTypeName(resourceId);
        switch (resourceTypeName) {
            case "color":
                return getColor(resourceId);
            case "mipmap":
            case "drawable":
                return getDrawableOrMipmap(resourceId);
            default:
                break;
        }
        return null;
    }

    //获得字体
    public Typeface getTypeface(int resourceId) {
        String skinTypefacePath = getString(resourceId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        return isDefaultSkin ? Typeface.createFromAsset(appResources.getAssets(), skinTypefacePath)
                : Typeface.createFromAsset(skinResource.getAssets(), skinTypefacePath);
    }
}
