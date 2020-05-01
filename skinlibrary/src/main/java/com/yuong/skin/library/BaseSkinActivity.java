package com.yuong.skin.library;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.yuong.skin.library.core.CustomerAppCompatViewInflater;
import com.yuong.skin.library.core.ViewsMatch;
import com.yuong.skin.library.utils.ActionBarUtil;
import com.yuong.skin.library.utils.NavigationBarUtil;
import com.yuong.skin.library.utils.StatusBarUtil;

public abstract class BaseSkinActivity extends AppCompatActivity {
    private CustomerAppCompatViewInflater viewInflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isNeedChangeSkin()) {
            //拦截系统设置的Factory
            LayoutInflater inflater = LayoutInflater.from(this);
            LayoutInflaterCompat.setFactory2(inflater, this);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * 重写onCreateView 拦截生成的View
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (isNeedChangeSkin()) {
            if (viewInflater == null) {
                viewInflater = new CustomerAppCompatViewInflater(context);
            }
            viewInflater.setName(name);
            viewInflater.setAttrs(attrs);
            View view = viewInflater.autoCreateView();
            if (view == null) {
                view = super.onCreateView(parent, name, context, attrs);
            }
            return view;
        }
        return super.onCreateView(parent, name, context, attrs);
    }

    /**
     * 让子类重写该方法
     * @return 是否需要支持换肤
     */
    protected abstract boolean isNeedChangeSkin();


    /**
     * 切换肤色
     * @param modeNight
     */
    protected void setDayNightMode(int modeNight) {
        boolean isPost21 = Build.VERSION.SDK_INT >= 21;
        getDelegate().setLocalNightMode(modeNight);
        if (isPost21) {
            StatusBarUtil.forStatusBar(this);
            ActionBarUtil.forActionBar(this);
            NavigationBarUtil.forNavigationBar(this);
        }
        View decorView = getWindow().getDecorView();
        applyDayNightForView(decorView);
    }

    private void applyDayNightForView(View view) {
        if (view instanceof ViewsMatch) {
            ((ViewsMatch) view).skinnableView();
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                applyDayNightForView(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    protected void defaultSkin(int themeColorId) {
        this.skinDynamic(null, themeColorId);
    }

    protected void skinDynamic(String skinPath, int themeColorId) {
        SkinManager.getInstance().loadSkinResource(skinPath);
        if (themeColorId != 0) {
            int themeColor = SkinManager.getInstance().getColor(themeColorId);
            StatusBarUtil.forStatusBar(this, themeColor);
            NavigationBarUtil.forNavigationBar(this, themeColor);
            ActionBarUtil.forActionBar(this, themeColor);
        }
        applyViews(getWindow().getDecorView());
    }

    private void applyViews(View view) {
        if (view instanceof ViewsMatch) {
            ((ViewsMatch) view).skinnableView();
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                applyViews(((ViewGroup) view).getChildAt(i));
            }
        }
    }
}
