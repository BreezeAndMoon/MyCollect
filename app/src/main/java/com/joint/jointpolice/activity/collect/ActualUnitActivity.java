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
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.adapter.ActualBuildingAdapter;
import com.joint.jointpolice.adapter.ActualUnitAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.DeleteParameter;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.SwipeRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joint229 on 2018/4/16.
 */

public class ActualUnitActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener {
    SwipeMenuRecyclerView mRecyclerView;
    SmartRefreshLayout mSmartRefresh;
    int mEnterpriseID;
    ActualUnitAdapter mActualUnitAdapter;
    List<Enterprise> mEnterpriseList = new ArrayList<>();
    boolean mIsPause;
    int mFlatID;
    Gson mGson = new Gson();
    int mFabVisibility = View.VISIBLE;
    FloatingActionButton mAddFab;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_actual_unit);
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("实有单位");
        mAddFab =findViewById(R.id.fab_add);
        mAddFab.setOnClickListener(this);
        mAddFab.setVisibility(mFabVisibility);
        mSmartRefresh = findViewById(R.id.smart_refresh);
        mSmartRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        bindRecycleView();
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent().setClass(ActualUnitActivity.this, CollectUnitActivity.class);
        intent.putExtra(Constant.IsAdd, true);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsPause) {
            mIsPause = false;
            updateList();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        updateList();
    }

    private void updateList() {
        String jsonStr = String.valueOf(mFlatID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_unit_byflatid_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Enterprise>>() {
            @Override
            public void onResponse(List<Enterprise> response) {
                mActualUnitAdapter.setDataSource(response);
            }

            @Override
            public void onAfter() {
                if (!mIsPause)
                    mSmartRefresh.finishRefresh(0, true);
            }
        });
    }

    private void bindRecycleView() {
        List<Enterprise> list = (List<Enterprise>) getIntent().getSerializableExtra(Constant.SEARCH_RESULT);
        if (list != null && list.size() > 0) {
            initRecyclerView(list);
            mFabVisibility = View.INVISIBLE;//隐藏添加按钮
        } else {
            Flat flat = (Flat) getIntent().getSerializableExtra("Flat");
            if (flat == null)
                return;
            mFlatID = (int) flat.getId();
            String jsonStr = String.valueOf(mFlatID);
            OkHttpClientManager.postAsyn(getResources().getString(R.string.get_unit_byflatid_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Enterprise>>() {
                @Override
                public void onResponse(List<Enterprise> response) {
                    initRecyclerView(response);
                }

                @Override
                public void onBefore() {
                    showDialogProgress();
                }

                @Override
                public void onAfter() {
                    dismissDialogProgress();
                }
            });
        }
    }

    private void deleteItem(int positionID) {
        long enterpriseID = mActualUnitAdapter.getDatas().get(positionID).getSmID();
        String jsonStr = buildDeleteParamJson(enterpriseID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.delete_unit_byid_url), jsonStr, new OkHttpClientManager.ResultCallback() {
            @Override
            public void onResponse(Object response) {
                LUtils.toast("删除成功");
                mActualUnitAdapter.getDatas().remove(positionID);
                mActualUnitAdapter.notifyItemRemoved(positionID);
            }

            @Override
            public void onBefore() {
                showDialogProgress();
            }

            @Override
            public void onAfter() {
                dismissDialogProgress();
            }
        });
    }

    private void initRecyclerView(List<Enterprise> enterpriseList) {
        mActualUnitAdapter = new ActualUnitAdapter(ActualUnitActivity.this);
        mActualUnitAdapter.setDataSource(enterpriseList);
        mRecyclerView = findViewById(R.id.recy_actual_unit);
        SwipeRecyclerViewUtil.setSwipeMenu(ActualUnitActivity.this, mRecyclerView);
        SwipeRecyclerViewUtil.setOnItemClick(mRecyclerView, (positionID) -> {
            deleteItem(positionID);
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActualUnitActivity.this));
        mRecyclerView.setAdapter(mActualUnitAdapter);
    }

    private String buildDeleteParamJson(long enterpriseID) {
        DeleteParameter deleteParameter = new DeleteParameter();
        deleteParameter.setItemID(enterpriseID);
        deleteParameter.setUserID(LoginActivity.userID);
        String jsonStr = mGson.toJson(deleteParameter);
        return jsonStr;
    }
}
