package com.joint.jointpolice.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;

/**
 * Created by Joint229 on 2018/5/1.
 */

public class MyActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.fragment_main_my);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.toolbar_tv_title)).setText("我的");
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }
}
