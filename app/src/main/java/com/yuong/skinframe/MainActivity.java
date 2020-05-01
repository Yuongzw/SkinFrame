package com.yuong.skinframe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.yuong.skin.library.BaseSkinActivity;
import com.yuong.skin.library.utils.PreferencesUtils;

import java.io.File;

public class MainActivity extends BaseSkinActivity {

    private String skinPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // File.separator含义：拼接 /
        skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "yuongzw.skin";

        // 运行时权限申请（6.0+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

        if (("yuongzw").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            skinDynamic(skinPath, R.color.skin_item_color);
        } else {
            defaultSkin(R.color.colorPrimary);
        }
    }

    // 换肤按钮（api限制：5.0版本）
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void skinDynamic(View view) {
        // 真实项目中：需要先判断当前皮肤，避免重复操作！
        if (!("yuongzw").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("netease >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            skinDynamic(skinPath, R.color.skin_item_color);
            PreferencesUtils.putString(this, "currentSkin", "yuongzw");

            long end = System.currentTimeMillis() - start;
            Log.e("netease >>> ", "换肤耗时（毫秒）：" + end);
            Log.e("netease >>> ", "-------------end---------------");
        }
    }

    // 默认按钮（api限制：5.0版本）
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void skinDefault(View view) {
        if (!("default").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("netease >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            defaultSkin(R.color.colorPrimary);
            PreferencesUtils.putString(this, "currentSkin", "default");

            long end = System.currentTimeMillis() - start;
            Log.e("netease >>> ", "还原耗时（毫秒）：" + end);
            Log.e("netease >>> ", "-------------end---------------");
        }
    }

    @Override
    protected boolean isNeedChangeSkin() {
        return true;
    }
}
