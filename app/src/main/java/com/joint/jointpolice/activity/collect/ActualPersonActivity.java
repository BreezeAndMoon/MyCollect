package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.adapter.ActualPersonAdapter;
import com.joint.jointpolice.adapter.ActualUnitAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.DeleteParameter;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.util.DateUtil;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.SwipeRecyclerViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joint229 on 2018/5/7.
 */

public class ActualPersonActivity extends BaseActivity implements View.OnClickListener, OnRefreshListener {
    SwipeMenuRecyclerView mRecyclerView;
    SmartRefreshLayout mSmartRefresh;
    BottomSheetDialog mBottomSheetDialog;
    ActualPersonAdapter mActualPersonAdapter;
    List<Person> mPersonList = new ArrayList<>();
    int mFlatID;
    boolean mIsPause;
    Gson mGson = new Gson();
    FloatingActionButton mAddFab;
    int mFabVisibility = View.VISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_actual_person);
    }

    @Override
    protected void initView() {
        setToolbarTitle("实有人口");
        mAddFab = findViewById(R.id.fab_add);
        mAddFab.setVisibility(mFabVisibility);
        mAddFab.setOnClickListener(this);
        mSmartRefresh = findViewById(R.id.smart_refresh);
        mSmartRefresh.setOnRefreshListener(this);
        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        mBottomSheetDialog.findViewById(R.id.tv_cisborder_person).setOnClickListener(this);
        mBottomSheetDialog.findViewById(R.id.tv_outbound_person).setOnClickListener(this);
        mBottomSheetDialog.findViewById(R.id.tv_nodoor_person).setOnClickListener(this);
        mBottomSheetDialog.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initValue() {
        bindRecyclerView();
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        switch (v.getId()) {
            case R.id.tv_cisborder_person:
                intent.setClass(ActualPersonActivity.this, CollectCisborderPersonActivity.class);
                intent.putExtra(Constant.IsAdd, true);
                startActivity(intent);
                mBottomSheetDialog.dismiss();
                break;
            case R.id.tv_outbound_person:
                intent.setClass(ActualPersonActivity.this, CollectOutboundPersonActivity.class);
                intent.putExtra(Constant.IsAdd, true);
                startActivity(intent);
                mBottomSheetDialog.dismiss();
                break;
            case R.id.tv_nodoor_person:
                intent.setClass(ActualPersonActivity.this, CollectNodoorPersonActivity.class);
                intent.putExtra(Constant.IsAdd, true);
                startActivity(intent);
                mBottomSheetDialog.dismiss();
                break;
            case R.id.btn_cancel:
                mBottomSheetDialog.dismiss();
                break;
            default:
                mBottomSheetDialog.show();
        }

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
        //todo 需要根据条件重新搜索一遍来刷新
        String jsonStr = String.valueOf(mFlatID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_persons_byflatid_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Person>>() {
                    @Override
                    public void onResponse(List<Person> response) {
                        mActualPersonAdapter.setDataSource(response);
                    }

                    @Override
                    public void onAfter() {
                        if (!mIsPause)
                            mSmartRefresh.finishRefresh(0, true);
                    }
                }

        );
    }

    private void bindRecyclerView() {
        List<Person> list = (List<Person>) getIntent().getSerializableExtra(Constant.SEARCH_RESULT);
        if (list != null && list.size() > 0) {
            initRecyclerView(list);
            mFabVisibility = View.INVISIBLE;//隐藏添加按钮
        } else {
            Flat flat = (Flat) getIntent().getSerializableExtra("Flat");
            if (flat == null)
                return;
            mFlatID = (int) flat.getId();
            String jsonStr = String.valueOf(mFlatID);
            OkHttpClientManager.postAsyn(getResources().getString(R.string.get_persons_byflatid_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Person>>() {
                @Override
                public void onResponse(List<Person> response) {
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
        long personID = mActualPersonAdapter.getDatas().get(positionID).getId();
        String jsonStr = buildDeleteParamJson(personID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.delete_person_byid_url), jsonStr, new OkHttpClientManager.ResultCallback() {
            @Override
            public void onResponse(Object response) {
                LUtils.toast("删除成功");
                mActualPersonAdapter.getDatas().remove(positionID);
                mActualPersonAdapter.notifyItemRemoved(positionID);
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

    private void initRecyclerView(List<Person> personList) {
        mActualPersonAdapter = new ActualPersonAdapter(ActualPersonActivity.this);
        mActualPersonAdapter.setDataSource(personList);
        mRecyclerView = findViewById(R.id.recy_actual_person);
        SwipeRecyclerViewUtil.setSwipeMenu(ActualPersonActivity.this, mRecyclerView);
        SwipeRecyclerViewUtil.setOnItemClick(mRecyclerView, (positionID) -> {
            deleteItem(positionID);
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ActualPersonActivity.this));
        mRecyclerView.setAdapter(mActualPersonAdapter);
    }

    private String buildDeleteParamJson(long personID) {
        DeleteParameter deleteParameter = new DeleteParameter();
        deleteParameter.setItemID(personID);
        deleteParameter.setUserID(LoginActivity.userID);
        String jsonStr = mGson.toJson(deleteParameter);
        return jsonStr;
    }
}
