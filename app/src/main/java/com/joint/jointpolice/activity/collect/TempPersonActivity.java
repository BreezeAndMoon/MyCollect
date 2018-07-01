package com.joint.jointpolice.activity.collect;

import android.os.Bundle;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;

/**
 * Created by Joint229 on 2018/4/15.
 */

public class TempPersonActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_temp_person);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.toolbar_tv_title)).setText("流动人口");

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }
}
