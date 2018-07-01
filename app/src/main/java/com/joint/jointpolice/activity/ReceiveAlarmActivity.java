package com.joint.jointpolice.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.widget.RecyScollView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:28
 * @描述: 现场接警
 */

public class ReceiveAlarmActivity extends BaseActivity {
    @BindView(R.id.ll_receive_alarm_people)
    LinearLayout mLlReceiveAlarmPeople;
    @BindView(R.id.ll_alarm_phone)
    LinearLayout mLlAlarmPhone;
    @BindView(R.id.tv_picture_num)
    TextView mTvPictureNum;
    @BindView(R.id.recy_locale_img)
    RecyclerView mRecyLocaleImg;
    @BindView(R.id.tv_picture_voice_num)
    TextView mTvPictureVoiceNum;
    @BindView(R.id.recy_locale_voice)
    RecyclerView mRecyLocaleVoice;
    @BindView(R.id.tv_picture_video_num)
    TextView mTvPictureVideoNum;
    @BindView(R.id.recy_locale_video)
    RecyclerView mRecyLocaleVideo;
    @BindView(R.id.tv_upload)
    TextView mTvUpload;
    @BindView(R.id.item_picture_select)
    RecyScollView mItemPictureSelect;
    @BindView(R.id.tv_receive_address)
    TextView mTvReceiveAddress;
    @BindView(R.id.ll_alarm_address)
    LinearLayout mLlAlarmAddress;
    @BindView(R.id.ll_alarm_reason)
    LinearLayout mLlAlarmReason;
    @BindView(R.id.ll_alarm_sort)
    LinearLayout mLlAlarmSort;
    @BindView(R.id.tv_receive_grade)
    TextView mTvReceiveGrade;
    @BindView(R.id.tv_receive_obtain_evidence)
    TextView mTvReceiveObtainEvidence;
    @BindView(R.id.tv_receive_sure)
    TextView mTvReceiveSure;
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_receive_alarm);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mTvUpload.setVisibility(View.GONE);
        mToolbarTvTitle.setText(getString(R.string.text_receive_alarm));
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }
}
