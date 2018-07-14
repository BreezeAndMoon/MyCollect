package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.AddressAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.model.CollectModels.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joint229 on 2018/4/15.
 */

public class HousePersonActivity extends BaseActivity implements View.OnClickListener {
    AddressAdapter mAddressAdapter;
    List<Person> mPersonList;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_house_person);
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("居住人口");
        findViewById(R.id.tv_add_normal_person).setOnClickListener(this);
        findViewById(R.id.tv_add_temp_person).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recy_house_person);
        mAddressAdapter = new AddressAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setAdapter(mAddressAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mPersonList = new ArrayList<Person>();
        for (int i = 0; i < 20; i++) {
            Person person = new Person();
            person.setName("张三");
            mPersonList.add(person);
        }
       // mAddressAdapter.setDataSource(mPersonList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_temp_person:
                startActivity(new Intent(this, TempPersonActivity.class));
                break;
            case R.id.tv_add_normal_person:
                startActivity(new Intent(this, NormalPersonActivity.class));
                break;
        }
    }
}
