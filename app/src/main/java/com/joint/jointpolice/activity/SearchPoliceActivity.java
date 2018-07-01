package com.joint.jointpolice.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.PoliceAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.TestData;
import com.joint.jointpolice.util.LUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:29
 * @描述: 搜索警情
 */

public class SearchPoliceActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.toolbar_tv_right)
    TextView mToolbarTvRight;
    @BindView(R.id.tv_police_sort)
    TextView mTvPoliceSort;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_reson)
    EditText mTvReson;
    @BindView(R.id.tv_address)
    EditText mTvAddress;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;


    private final static int START_TIME = 1;
    private final static int END_TIME = 2;
    private TimePickerView mTimePickerView;
    private TimePickerView mEndTimePickerView;

    private PoliceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_search_police);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("搜索警情");
        mToolbarTvRight.setVisibility(View.VISIBLE);
        mToolbarTvRight.setText("确定");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PoliceAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }

    @OnClick({R.id.tv_police_sort, R.id.toolbar_tv_right, R.id.tv_start_time, R.id.tv_end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_police_sort:
                startIntent(SelectPoliceSortActivity.class);
                break;
            case R.id.toolbar_tv_right:
                LUtils.toast("wwwww");
//                if (isCanSearch()) {
                mSmartRefresh.setVisibility(View.VISIBLE);
                initSearchData();
//                }
                break;
            case R.id.tv_start_time:
                showTimePicker();
                break;
            case R.id.tv_end_time:
                showEndTimePicker();
                break;
        }
    }


    private void showTimePicker() {
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    mTvStartTime.setText(format.format(date));
                }
            }).setSubmitText(getString(R.string.btn_complete))
                    .setSubmitColor(R.color.colorPrimary)
                    .setCancelText(getString(R.string.btn_cancel)).setCancelColor(R.color.colorPrimary)
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("", "", "", "", "", "")
                    .build();
        }
        mTimePickerView.show();
    }

    private void showEndTimePicker() {
        if (mEndTimePickerView == null) {
            mEndTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    mTvEndTime.setText(format.format(date));
                }
            }).setType(new boolean[]{true, true, true, false, false, false})
                    .setSubmitText(getString(R.string.btn_complete))
                    .setSubmitColor(R.color.colorPrimary)
                    .setCancelText(getString(R.string.btn_cancel)).setCancelColor(R.color.colorPrimary)
                    .setLabel("", "", "", "", "", "")
                    .build();
        }
        mEndTimePickerView.show();
    }

    private boolean isCanSearch() {
        if (TextUtils.isEmpty(mTvStartTime.getText())) {
            LUtils.toast("开始时间不能为空");
            return false;
        }
        return true;
    }


    private void updateSearchData(String startTime, String endTime, String type, String reason, String address) {


    }


    private void initSearchData() {
        mAdapter.setDataSource(TestData.setData());
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.onItemClickListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                LUtils.toast("11111");
            }
        });

    }


}
