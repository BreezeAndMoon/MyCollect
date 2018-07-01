package com.joint.jointpolice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:39
 * @描述: 处置反馈
 */

public class EndFeedBackActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.tv_start_time)
    TextView mTvTime;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    private TimePickerView mTimePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_end_feedback);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("处置反馈");
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initValue() {

    }

    @OnClick(R.id.btn_submit)
    public void submit() {

    }

    @OnClick(R.id.tv_start_time)
    public void onViewClicked() {
        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    mTvTime.setText(format.format(date));
                }
            }).setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("", "", "", "", "", "")
                    .setCancelText(getString(R.string.btn_cancel))
                    .setCancelColor(R.color.colorPrimary)
                    .setSubmitText(getString(R.string.btn_complete))
                    .setSubmitColor(R.color.colorPrimary)
                    .build();
        }
        mTimePickerView.show();
    }

}
