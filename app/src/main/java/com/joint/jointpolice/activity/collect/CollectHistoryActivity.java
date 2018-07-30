package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.ActualBuildingAdapter;
import com.joint.jointpolice.adapter.ActualPersonAdapter;
import com.joint.jointpolice.adapter.ActualUnitAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.ParameterizedTypeImpl;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.FlatParameter;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.QueryResult;
import com.joint.jointpolice.util.DateUtil;
import com.joint.jointpolice.util.StringUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by Joint229 on 2018/7/30.
 */

public class CollectHistoryActivity extends BaseActivity implements View.OnClickListener {
    TimePickerView mTimePickerView;
    int mViewID;
    Gson mGson = new Gson();
    int mPageSize = 20;
    TextView mStartTextView;
    TextView mEndTextView;
    RecyclerView mRecyclerView;
    String mUrl;
    Class mClass;
    BaseRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_collect_history);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle("采集历史");
        //setToolbarTitle("采集历史");
        mRecyclerView = findViewById(R.id.recy_history);
        mStartTextView = findViewById(R.id.tv_start_time);
        mEndTextView = findViewById(R.id.tv_end_time);
        mStartTextView.setText(DateUtil.getNowTime());
        mEndTextView.setText(DateUtil.getNowTime());
        mStartTextView.setOnClickListener(this);
        mEndTextView.setOnClickListener(this);

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((TextView) findViewById(mViewID)).setText(DateUtil.formatDate(date));
                queryByDate();
            }
        }).build();
        String collectType = getIntent().getStringExtra(Constant.CollectType);
        switch (collectType) {
            case Constant.Collect_House:
                mUrl = getResources().getString(R.string.query_house_history);
                mClass = Flat.class;
                mAdapter = new ActualBuildingAdapter(this);
                break;
            case Constant.Collect_Person:
                mUrl = getResources().getString(R.string.query_person_history);
                mClass = Person.class;
                mAdapter = new ActualPersonAdapter(this);
                break;
            case Constant.Collect_Unit:
                mUrl = getResources().getString(R.string.query_unit_history);
                mClass = Enterprise.class;
                mAdapter = new ActualUnitAdapter(this);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        mViewID = v.getId();
        switch (v.getId()) {
            case R.id.tv_start_time:
                mTimePickerView.show();
                break;
            case R.id.tv_end_time:
                mTimePickerView.show();
                break;
        }
    }

    private void queryByDate() {
        String jsonStr = buildData();
        OkHttpClientManager.postAsyn(mUrl, jsonStr, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
               List dataList = getResultList(response, mClass);
               bindRecyclerView(dataList);
            }

            public void onBefore() {
                showDialogProgress();
            }

            public void onAfter() {
                dismissDialogProgress();
            }
        });
    }

    private void bindRecyclerView(List dataList) {
        mAdapter.setDataSource(dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private String buildData() {
        FlatParameter flatParameter = new FlatParameter();
        flatParameter.setPageSize(mPageSize);
        flatParameter.setPageIndex(1);
        flatParameter.setBtime(mStartTextView.getText().toString());
        flatParameter.setEtime(mEndTextView.getText().toString());
        return mGson.toJson(flatParameter);
    }

    private <T> List<T> getResultList(String jsonStr, Class<T> clazz) {
        jsonStr = StringUtil.formatJsonString(jsonStr);
        Type type = new ParameterizedTypeImpl(QueryResult.class, new Type[]{clazz});
        QueryResult queryResult = mGson.fromJson(jsonStr, type);
        List<T> list = queryResult.getData();
        return list;
    }
}
