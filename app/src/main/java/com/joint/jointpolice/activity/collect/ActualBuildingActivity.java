package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.ActualBuildingAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.FlatParameter;
import com.joint.jointpolice.util.LUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Joint229 on 2018/5/3.
 */

public class ActualBuildingActivity extends BaseActivity implements View.OnClickListener {
    ActualBuildingAdapter mActualBuildingAdapter;
    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingActionButton;
    List<Flat> mFlatList = new ArrayList();
    long mFlatID;
    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_actual_building);
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("实有房屋信息");
        mFloatingActionButton = findViewById(R.id.fab_add);
        mFloatingActionButton.setOnClickListener(this);
        mFloatingActionButton.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        bindRecycleView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ActualBuildingAdapter.REQ_CODE_Flat:
                if (resultCode == RESULT_OK) {
                    String jsonStr = buildData();
                    OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_byid_url), jsonStr, new OkHttpClientManager.ResultCallback<Flat>() {
                        @Override
                        public void onResponse(Flat response) {
                            mActualBuildingAdapter.getDatas().clear();
                            mActualBuildingAdapter.getDatas().add(response);
                            mActualBuildingAdapter.notifyDataSetChanged();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent();
//        intent.putExtra("Flat", getIntent().getSerializableExtra("Flat"));
//        switch (getIntent().getStringExtra("CollectType")) {
//            case Constant.Collect_House:
//                intent.setClass(ActualBuildingActivity.this, CollectBuildingActivity.class);
//                startActivity(intent);
//                break;
//        }
    }

    private void bindRecycleView() {
        //目前设计的是一个Flat地址对应一个Flat，只对其做更新操作，没有新增
        Flat flat = (Flat) getIntent().getSerializableExtra("Flat");
        if (flat == null)
            return;
        mFlatID = flat.getId();
        mFlatList.add(flat);
        mActualBuildingAdapter = new ActualBuildingAdapter(this);
        mActualBuildingAdapter.setDataSource(mFlatList);
        mRecyclerView = findViewById(R.id.recy_actual_building);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActualBuildingActivity.this));
        mRecyclerView.setAdapter(mActualBuildingAdapter);
    }

    private String buildData() {
        FlatParameter param = new FlatParameter();
        param.setPageIndex(1);
        param.setPageSize(1);
        param.setID((int) mFlatID);
        String jsonStr = mGson.toJson(param);
        return jsonStr;
    }
}
