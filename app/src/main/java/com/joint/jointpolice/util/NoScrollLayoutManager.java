package com.joint.jointpolice.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/24 14:22
 * @描述:
 */

public class NoScrollLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnable = true;

    public NoScrollLayoutManager(Context context) {
        super(context);
    }

    private void setEnableScroll(boolean flag) {
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnable && super.canScrollHorizontally();
    }
}
