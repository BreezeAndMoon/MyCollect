package com.joint.jointpolice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/25 15:04
 * @描述:
 */

public class RecyScollView extends ScrollView {

    private int downX;

    private int downY;

    private int mTouchSlop;

    public RecyScollView(Context context) {

        super(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }


    public RecyScollView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    public RecyScollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(e);

    }

}

