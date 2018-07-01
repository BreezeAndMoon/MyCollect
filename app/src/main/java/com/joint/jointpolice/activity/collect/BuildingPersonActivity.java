package com.joint.jointpolice.activity.collect;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;

import org.w3c.dom.Text;

/**
 * Created by Joint229 on 2018/4/13.
 */

public class BuildingPersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_house_person);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.toolbar_tv_title)).setText("居住人口");
        findViewById(R.id.tv_add_normal_person).setOnClickListener(this);
        findViewById(R.id.tv_add_temp_person).setOnClickListener(this);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_normal_person:
                break;
            case R.id.tv_add_temp_person:
                break;
        }
    }
}
