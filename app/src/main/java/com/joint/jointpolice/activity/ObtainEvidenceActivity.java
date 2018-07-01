package com.joint.jointpolice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.GridImageAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.PictureVideoPlayActivity;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:26
 * @描述: 现场取证
 */

public class ObtainEvidenceActivity extends BaseActivity {


    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.recy_locale_img)
    RecyclerView mRecyLocaleImg;
    @BindView(R.id.recy_locale_voice)
    RecyclerView mRecyLocaleVoice;
    @BindView(R.id.recy_locale_video)
    RecyclerView mRecyLocaleVideo;
    @BindView(R.id.tv_upload)
    TextView mTvUpload;
    @BindView(R.id.tv_picture_num)
    TextView mTvPictureNum;
    @BindView(R.id.tv_picture_voice_num)
    TextView mTvPictureVoiceNum;
    @BindView(R.id.tv_picture_video_num)
    TextView mTvPictureVideoNum;

    private GridImageAdapter mGridImageAdapter;
    private GridImageAdapter mGridVoiceAdapter;
    private GridImageAdapter mGridVideoAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<LocalMedia> mTempUrl = new ArrayList<>();
    private List<LocalMedia> mImageUrl = new ArrayList<>();
    private List<LocalMedia> mVoiceUrl = new ArrayList<>();
    private List<LocalMedia> mVideoUrl = new ArrayList<>();
    private int themeId = R.style.picture_default_style;
    private int chooseMode = PictureMimeType.ofAll();
    private int chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
    private int maxSelectNum = 9;
    PictureSelectUtil mPictureSelectUtil;
    PictureSelectUtil mVoiceSelectUtil;
    PictureSelectUtil mVideoSelectUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_obtain_evidence);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("现场取证");
        mTvPictureNum.setText(getString(R.string.text_picture_img_num,0));
        mTvPictureVoiceNum.setText(getString(R.string.text_picture_voice_num,0));
        mTvPictureVideoNum.setText(String.format(getString(R.string.text_picture_video_num),0));
        initImgDatas();
        initVoiceDatas();
        initVideoDatas();

    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
//        mGridImageAdapter.setList(mImageUrl);

    }

    @OnClick(R.id.tv_upload)
    public void onViewClicked() {
//上传
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityRequestCode.PICTURE_SELECT_IMG:
                    List<LocalMedia> imgLists = PictureSelector.obtainMultipleResult(data);
                    mImageUrl = imgLists;
                    mPictureSelectUtil.getGridImageAdapter().setList(mImageUrl);
                    mTvPictureNum.setText(String.format(getString(R.string.text_picture_img_num),mImageUrl.size()));
                    setSubmitEnable();
                    break;
                case ActivityRequestCode.PICTURE_SELECT_AUDIO:
                    List<LocalMedia> voiceLists = PictureSelector.obtainMultipleResult(data);
                    mVoiceUrl = voiceLists;
                    mVoiceSelectUtil.getGridImageAdapter().setList(mVoiceUrl);
                    mTvPictureVoiceNum.setText(String.format(getString(R.string.text_picture_voice_num),mVoiceUrl.size()));
                    setSubmitEnable();
                    break;
                case ActivityRequestCode.PICTURE_SELECT_VIDEO:
                    List<LocalMedia> videoLists = PictureSelector.obtainMultipleResult(data);
                    mVideoUrl = videoLists;
                    mVideoSelectUtil.getGridImageAdapter().setList(mVideoUrl);
                    mTvPictureVideoNum.setText(String.format(getString(R.string.text_picture_video_num),mVideoUrl.size()));
                    setSubmitEnable();
                    break;
            }
        }
    }

    private void initImgDatas() {
        mPictureSelectUtil = new PictureSelectUtil(this, mRecyLocaleImg);
        mPictureSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
                //点击新增
                chooseMode = PictureMimeType.ofImage();
                chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
                mTempUrl = mImageUrl;
                setSelectImgData();
            }

            @Override
            public void onItemClick(int position) {
                // 预览图片
                PictureSelector.create(ObtainEvidenceActivity.this).externalPicturePreview(position, mImageUrl);
            }

            @Override
            public void onItemDelClick(int position) {
                setSubmitEnable();
                mTvPictureNum.setText(getString(R.string.text_picture_img_num,mImageUrl.size()));
            }
        });
    }

    private void initVoiceDatas() {

        mVoiceSelectUtil = new PictureSelectUtil(this, mRecyLocaleVoice);
        mVoiceSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
                chooseMode = PictureMimeType.ofAudio();
                chooseRequest = ActivityRequestCode.PICTURE_SELECT_AUDIO;
                mTempUrl = mVoiceUrl;
                setSelectImgData();
            }

            @Override
            public void onItemClick(int position) {
                // 预览音频
                PictureSelector.create(ObtainEvidenceActivity.this).externalPictureAudio(mVoiceUrl.get(position).getPath());
            }

            @Override
            public void onItemDelClick(int position) {
                setSubmitEnable();
                mTvPictureVoiceNum.setText(getString(R.string.text_picture_voice_num,mVoiceUrl.size()));
            }
        });
    }

    private void initVideoDatas() {

        mVideoSelectUtil = new PictureSelectUtil(this, mRecyLocaleVideo);
        mVideoSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
            @Override
            public void onAddPicClick() {
                chooseMode = PictureMimeType.ofVideo();
                chooseRequest = ActivityRequestCode.PICTURE_SELECT_VIDEO;
                mTempUrl = mVideoUrl;
                setSelectImgData();
            }

            @Override
            public void onItemClick(int position) {
                // 预览视频
                PictureSelector.create(ObtainEvidenceActivity.this).externalPictureVideo(mVideoUrl.get(position).getPath());
            }

            @Override
            public void onItemDelClick(int position) {
                setSubmitEnable();
                mTvPictureVideoNum.setText(getString(R.string.text_picture_video_num,mVideoUrl.size()));
            }
        });
    }


    private void setSelectedData(int position) {
        LocalMedia media = mImageUrl.get(position);
        String pictureType = media.getPictureType();
        int mediaType = PictureMimeType.pictureToVideo(pictureType);
        switch (mediaType) {
            case 1:
                // 预览图片
                PictureSelector.create(ObtainEvidenceActivity.this).externalPicturePreview(position, mImageUrl);
                break;
            case 2:
                // 预览视频
                PictureSelector.create(ObtainEvidenceActivity.this).externalPictureVideo(media.getPath());
                break;
            case 3:
                // 预览音频
                PictureSelector.create(ObtainEvidenceActivity.this).externalPictureAudio(media.getPath());
                break;
        }
    }
    //选择media
    private void setSelectImgData() {
        PictureSelector.create(ObtainEvidenceActivity.this)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)
                .enablePreviewAudio(true)
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .selectionMedia(mTempUrl)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(chooseRequest);//结果回调onActivityResult code
    }

    private void setSubmitEnable() {
        if (mImageUrl.size() > 0 || mVoiceUrl.size() > 0 || mVideoUrl.size() > 0) {
            mTvUpload.setEnabled(true);
        } else {
            mTvUpload.setEnabled(false);

        }

    }


}
