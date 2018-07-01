package com.joint.jointpolice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.GridImageAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.TestData;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:21
 * @描述: 警情详情
 */

public class PoliceDetailActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.tv_police_start_feedback)
    TextView mTvStartFeedback;
    @BindView(R.id.tv_police_obtain_evidence)
    TextView mTvObtainEvidence;
    @BindView(R.id.tv_police_end_feedback)
    TextView mTvEndFeedback;
    @BindView(R.id.tv_alarm_name)
    TextView mTvAlarmName;
    @BindView(R.id.tv_alarm_phone)
    TextView mTvAlarmPhone;
    @BindView(R.id.recy_alarm_img)
    RecyclerView mRecyAlarmImg;
    @BindView(R.id.recy_alarm_voice)
    RecyclerView mRecyAlarmVoice;
    @BindView(R.id.recy_alarm_video)
    RecyclerView mRecyAlarmVideo;
    @BindView(R.id.tv_alarm_address)
    TextView mTvAlarmAddress;
    @BindView(R.id.tv_alarm_reason)
    TextView mTvAlarmReason;
    @BindView(R.id.tv_alarm_grade)
    TextView mTvAlarmGrade;
    @BindView(R.id.tv_alarm_circum_info)
    TextView mTvAlarmCircumInfo;
    GridImageAdapter gridImageAdapter;
    GridImageAdapter gridVoiceAdapter;
    GridImageAdapter gridVideoAdapter;


    private List<LocalMedia> imgMedia = new ArrayList<>();
    private List<LocalMedia> voiceMedia = new ArrayList<>();
    private List<LocalMedia> videoMedia = new ArrayList<>();
    private PictureSelectUtil imgSelectUtil;
    private PictureSelectUtil voiceSelectUtil;
    private PictureSelectUtil videoSelectUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_police_detail);
        ButterKnife.bind(this);
    }


    @Override
    protected void initView() {
        mToolbarTvTitle.setText("警情详情");
        mTvAlarmName.setText("蔡先生");
        mTvAlarmPhone.setText("1844545454");
        mTvAlarmAddress.setText("厦门软件园二期");
        mTvAlarmReason.setText("蔡金明偷了我一支笔!");
        mTvAlarmGrade.setText("很严重");
        initPictureImg();
        initPictureVoice();
        initPictureVideo();

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }


    @OnClick({R.id.tv_police_start_feedback, R.id.tv_police_obtain_evidence, R.id.tv_police_end_feedback})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_police_start_feedback:
                startActivity(new Intent(PoliceDetailActivity.this, StartFeedBackActivity.class));
                break;
            case R.id.tv_police_obtain_evidence:
                startActivity(new Intent(PoliceDetailActivity.this, ObtainEvidenceActivity.class));
                break;
            case R.id.tv_police_end_feedback:
                startActivity(new Intent(PoliceDetailActivity.this, EndFeedBackActivity.class));
                break;
        }
    }


    private void initPictureImg() {
        imgSelectUtil = new PictureSelectUtil(this, mRecyAlarmImg);
        imgMedia.addAll(TestData.setLocalMedia(PictureConfig.TYPE_IMAGE, Constant.IMG_URL));
        imgSelectUtil.getGridImageAdapter().setPreView(2, 2);//设置预览，默认为1
        imgSelectUtil.getGridImageAdapter().setList(imgMedia);
        imgSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
            }

            @Override
            public void onItemClick(int position) {
                PictureSelector.create(PoliceDetailActivity.this).externalPicturePreview(position, imgMedia);
                //PictureSelector.create(PoliceDetailActivity.this).externalPictureAudio(imgMedia.get(position).getPath());
            }

            @Override
            public void onItemDelClick(int position) {

            }
        });
    }

    private void initPictureVoice() {
        voiceSelectUtil = new PictureSelectUtil(this, mRecyAlarmVoice);
        voiceMedia.addAll(TestData.setLocalMedia(PictureConfig.TYPE_AUDIO, Constant.VOICE_URL));
        voiceSelectUtil.getGridImageAdapter().setPreView(2, 2);
        voiceSelectUtil.getGridImageAdapter().setList(voiceMedia);
        voiceSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
            }

            @Override
            public void onItemClick(int position) {
                PictureSelector.create(PoliceDetailActivity.this).externalPictureAudio(voiceMedia.get(position).getPath());
            }

            @Override
            public void onItemDelClick(int position) {

            }
        });
    }

    private void initPictureVideo() {
        videoSelectUtil = new PictureSelectUtil(this, mRecyAlarmVideo);
        videoMedia.addAll(TestData.setLocalMedia(PictureConfig.TYPE_VIDEO, Constant.VIDEO_URL));
        videoSelectUtil.getGridImageAdapter().setPreView(2, 2);
        videoSelectUtil.getGridImageAdapter().setList(videoMedia);
        videoSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
            }

            @Override
            public void onItemClick(int position) {
                PictureSelector.create(PoliceDetailActivity.this).externalPictureVideo(videoMedia.get(position).getPath());
            }

            @Override
            public void onItemDelClick(int position) {

            }
        });
    }


}
