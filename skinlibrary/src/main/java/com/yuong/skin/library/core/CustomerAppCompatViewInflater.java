package com.yuong.skin.library.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatViewInflater;

import com.yuong.skin.library.views.SkinnableButton;
import com.yuong.skin.library.views.SkinnableConstraintLayout;
import com.yuong.skin.library.views.SkinnableImageView;
import com.yuong.skin.library.views.SkinnableLinearLayout;
import com.yuong.skin.library.views.SkinnableRelativeLayout;
import com.yuong.skin.library.views.SkinnableTextView;

/**
 * 继承 AppCompatViewInflater 类来拦截生成的view
 */
public class CustomerAppCompatViewInflater extends AppCompatViewInflater {
    private Context context;
    private String name;
    private AttributeSet attrs;

    public CustomerAppCompatViewInflater(Context context) {
        this.context = context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    public View autoCreateView() {
        View view = null;
        switch (name) {
            case "LinearLayout":
                view = new SkinnableLinearLayout(context, attrs);
                break;
            case "RelativeLayout":
                view = new SkinnableRelativeLayout(context, attrs);
                break;
            case "androidx.constraintlayout.widget.ConstraintLayout":
                view = new SkinnableConstraintLayout(context, attrs);
                break;
            case "TextView":
                view = new SkinnableTextView(context, attrs);
                break;
            case "ImageView":
                view = new SkinnableImageView(context, attrs);
                break;
            case "Button":
                view = new SkinnableButton(context, attrs);
                break;
            default:
                break;
                //如有其它View请自行添加
        }
//        verifyNotNull(view, name);
        return view;
    }

    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(this.getClass().getName()
                    + " asked to inflate view for <" + name + ">, but returned null");
        }
    }
}
