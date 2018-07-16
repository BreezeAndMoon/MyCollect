package com.joint.jointpolice.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.joint.jointpolice.R;

/**
 * Created by Joint229 on 2018/6/27.
 */

public class MyCustomDialog extends Dialog {
    private Context context;
    private int height, width;
    private boolean cancelTouchout;
    private View view;

    private MyCustomDialog(Builder builder) {
        super(builder.context);
        initValue(builder);
    }

    private MyCustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        initValue(builder);
    }

    private void initValue(Builder builder) {
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //layoutParams.gravity = Gravity.CENTER;
        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = (int) (screenWidth * 0.75);
        window.setAttributes(layoutParams);
    }

    public static final class Builder {
        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context,int resStyle) {
            this.context=context;
            this.resStyle = resStyle;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setCancelTouchout(boolean cancelTouchout) {
            this.cancelTouchout = cancelTouchout;
            return this;
        }

        public Builder setView(int viewRes) {
            this.view = view = LayoutInflater.from(context).inflate(viewRes, null);
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public MyCustomDialog Build() {
            if (resStyle != -1)
                return new MyCustomDialog(this, resStyle);
            else
                return new MyCustomDialog(this);
        }
    }

}

