package com.joint.jointpolice.activity.collect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseCollectActivity;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.FileModel;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.SaveFlatParameter;
import com.joint.jointpolice.model.CollectModels.SaveParameter;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.PictureUtil;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Joint229 on 2018/5/4.
 */

public class CollectUnitActivity extends BaseCollectActivity<Enterprise>{
    //    private PictureSelectUtil imgSelectUtil;
//    private int themeId = R.style.picture_default_style;
//    private int chooseMode = PictureMimeType.ofAll();
//    private int chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
//    private int maxSelectNum = 9;
//    private Enterprise mEnterprise;
//    Gson mGson = new Gson();
//    List<String> mRelativePicPath = new ArrayList<>();
//    private List<LocalMedia> mTempUrl = new ArrayList<>();
//    private List<LocalMedia> mImageUrl = new ArrayList<>();
    private static final int REQ_CODE_PERMISSION = 0x1111;
    private CollectFieldItem mUnitNameItem;
    private CollectFieldItem mUnitPropertyItem;
    private CollectFieldItem mUnitTypeItem;
    private CollectFieldItem mUnitUniformCodeItem;
    private CollectFieldItem mUnitLicenseNoItem;
    private CollectFieldItem mUnitOcodeItem;
    private CollectFieldItem mUnitContactPhoneItem;
    private CollectFieldItem mUnitRemarkItem;
    private CollectFieldItem mUnitCorCerTypeItem;
    private CollectFieldItem mUnitCorCerNoItem;
    private CollectFieldItem mUnitCorNameItem;
    private CollectFieldItem mUnitCorPhoneItem;
    private CollectFieldItem mUnitChargeCerTypeItem;
    private CollectFieldItem mUnitChargeCerNoItem;
    private CollectFieldItem mUnitChargeNameItem;
    private CollectFieldItem mUnitChargePhoneItem;
    private Enterprise mEnterprise;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_collect_unit);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void findView() {
        mUnitNameItem = findViewById(R.id.item_unit_name);
        mUnitPropertyItem = findViewById(R.id.item_unit_property);
        mUnitTypeItem = findViewById(R.id.item_unit_type);
        mUnitUniformCodeItem = findViewById(R.id.item_unit_uniformCode);
        mUnitLicenseNoItem = findViewById(R.id.item_unit_licenseNo);
        mUnitOcodeItem = findViewById(R.id.item_unit_ocode);
        mUnitContactPhoneItem = findViewById(R.id.item_unit_contactPhone);
        mUnitRemarkItem = findViewById(R.id.item_unit_remark);
        mUnitCorCerTypeItem = findViewById(R.id.item_unit_corCerType);
        mUnitCorCerNoItem = findViewById(R.id.item_unit_corCerNo);
        mUnitCorNameItem = findViewById(R.id.item_unit_corName);
        mUnitCorPhoneItem = findViewById(R.id.item_unit_corPhone);
        mUnitChargeCerTypeItem = findViewById(R.id.item_unit_chargeCerType);
        mUnitChargeCerNoItem = findViewById(R.id.item_unit_chargeCerNo);
        mUnitChargeNameItem = findViewById(R.id.item_unit_chargeName);
        mUnitChargePhoneItem = findViewById(R.id.item_unit_chargePhone);
    }

    @Override
    protected void bindData() {
        mUnitTypeItem.setInputText(mEnterprise.getType());
        mUnitPropertyItem.setInputText(mEnterprise.getProperty());
        mUnitNameItem.setInputText(mEnterprise.getName());
        //todo
    }

    @Override
    protected Enterprise getEntity() {
        mEnterprise = getIntent().getParcelableExtra(Constant.Enterprise);
        return mEnterprise;
    }

    @Override
    protected void initViewExtra() {

    }

    @Override
    protected String getToolbarTitle() {
        return "采集单位";
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recy_building_img;
    }

    @Override
    protected int getGetFileModelsUrlIdt() {
        return R.string.get_unit_filemodels_url;
    }

    @Override
    protected int getSaveItemUrlId() {
        return R.string.save_unit_url;
    }

    @Override
    protected void buildEntity(Enterprise enterprise) {
        enterprise.setSmUserID(App.UserID);//必填项，暂时先默认1
        enterprise.setName(mUnitNameItem.getInputText());
        enterprise.setProperty(mUnitPropertyItem.getInputText());
        enterprise.setType(mUnitTypeItem.getInputText());
        enterprise.setType(mUnitUniformCodeItem.getInputText());
        enterprise.setType(mUnitLicenseNoItem.getInputText());
        enterprise.setType(mUnitOcodeItem.getInputText());
        enterprise.setType(mUnitContactPhoneItem.getInputText());
        enterprise.setType(mUnitRemarkItem.getInputText());
        enterprise.setType(mUnitCorCerTypeItem.getInputText());
        enterprise.setType(mUnitCorCerNoItem.getInputText());
        enterprise.setType(mUnitCorNameItem.getInputText());
        enterprise.setType(mUnitCorPhoneItem.getInputText());
        enterprise.setType(mUnitChargeCerTypeItem.getInputText());
        enterprise.setType(mUnitChargeCerNoItem.getInputText());
        enterprise.setType(mUnitChargeNameItem.getInputText());
        enterprise.setType(mUnitChargePhoneItem.getInputText());
    }

    @Override
    protected Class<Enterprise> getType() {
        return Enterprise.class;
    }

    @Override
    protected int getItemId() {
        return mEnterprise.getSmID();
    }

//    @Override
//    protected void initValue() {
//        mUnitNameItem = findViewById(R.id.item_unit_name);
//        mUnitPropertyItem = findViewById(R.id.item_unit_property);
//        mUnitTypeItem = findViewById(R.id.item_unit_type);
//        mUnitUniformCodeItem = findViewById(R.id.item_unit_uniformCode);
//        mUnitLicenseNoItem = findViewById(R.id.item_unit_licenseNo);
//        mUnitOcodeItem = findViewById(R.id.item_unit_ocode);
//        mUnitContactPhoneItem = findViewById(R.id.item_unit_contactPhone);
//        mUnitRemarkItem = findViewById(R.id.item_unit_remark);
//        mUnitCorCerTypeItem = findViewById(R.id.item_unit_corCerType);
//        mUnitCorCerNoItem = findViewById(R.id.item_unit_corCerNo);
//        mUnitCorNameItem = findViewById(R.id.item_unit_corName);
//        mUnitCorPhoneItem = findViewById(R.id.item_unit_corPhone);
//        mUnitChargeCerTypeItem = findViewById(R.id.item_unit_chargeCerType);
//        mUnitChargeCerNoItem = findViewById(R.id.item_unit_chargeCerNo);
//        mUnitChargeNameItem = findViewById(R.id.item_unit_chargeName);
//        mUnitChargePhoneItem = findViewById(R.id.item_unit_chargePhone);
//        if (getIntent().getBooleanExtra(Constant.IsAdd, false))
//            return;
//        mEnterprise = getIntent().getParcelableExtra(Constant.Enterprise);
//        mUnitTypeItem.setInputText(mEnterprise.getType());
//        mUnitPropertyItem.setInputText(mEnterprise.getProperty());
//        mUnitNameItem.setInputText(mEnterprise.getName());
//        initPicture();
//    }

//    private void initPicture() {
//        String jsonStr = String.valueOf(mEnterprise.getSmID());
//        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_unit_filemodels_url), jsonStr, new OkHttpClientManager.ResultCallback<List<FileModel>>() {
//            @Override
//            public void onResponse(List<FileModel> response) {
//                if (response != null) {
//                    List<String> urls = new ArrayList<>();
//                    for (FileModel fileModel : response) {
//                        urls.add(fileModel.getRelativePath());
//                    }
//                    mImageUrl.addAll(PictureUtil.setLocalMedia(urls));
//                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
//                }
//            }
//
//            @Override
//            public void onBefore() {
//                showDialogProgress();
//            }
//
//            @Override
//            public void onAfter() {
//                dismissDialogProgress();
//            }
//        });
//    }
//
//    private void resizeTvDrawable(int id) {
//        TextView textView = findViewById(id);
//        Drawable[] drawables = textView.getCompoundDrawables();
//        drawables[0].setBounds(0, 0, 64, 64);
//        textView.setCompoundDrawables(drawables[0], null, null, null);
//    }
//
//    private void initPictureImg() {
//        RecyclerView recyBuildingImg = findViewById(R.id.recy_building_img);
//        imgSelectUtil = new PictureSelectUtil(this, recyBuildingImg);
//        imgSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
//            @Override
//            public void onAddPicClick() {
//                //点击新增
//                chooseMode = PictureMimeType.ofImage();
//                chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
//                mTempUrl = mImageUrl;
//                setSelectImgData();
//            }
//
//            @Override
//            public void onItemClick(int position) {
//                PictureSelector.create(CollectUnitActivity.this).externalPicturePreview(position, mImageUrl);
//            }
//
//            @Override
//            public void onItemDelClick(int position) {
//                //mTvPictureNum.setText(getString(R.string.text_picture_img_num,mImageUrl.size()));
//            }
//        });
//    }
//
//    private void setSelectImgData() {
//        PictureSelector.create(this)
//                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
//                .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                .minSelectNum(1)// 最小选择数量
//                .imageSpanCount(4)// 每行显示个数
//                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                .previewImage(true)// 是否可预览图片
//                .previewVideo(true)
//                .enablePreviewAudio(true)
//                .compress(true)
//                .isCamera(true)// 是否显示拍照按钮
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//                .selectionMedia(mTempUrl)// 是否传入已选图片
//                .minimumCompressSize(100)// 小于100kb的图片不压缩
//                .forResult(chooseRequest);//结果回调onActivityResult code
//    }
//
//    private void startCaptureActivityForResult() {
//        Intent intent = new Intent(this, CaptureActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
//        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
//        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
//        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
//        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
//        startActivityForResult(intent, CaptureActivity.REQ_CODE);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case ActivityRequestCode.PICTURE_SELECT_IMG:
//                    List<LocalMedia> imgLists = PictureSelector.obtainMultipleResult(data);
//                    mImageUrl = imgLists;
//                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        //todo 和采集房屋一样先保存照片
//        switch (v.getId()) {
//            case R.id.tv_save:
//                showDialogProgress();
//                if (mImageUrl.size() > 0) {
//                    for (LocalMedia localMedia : mImageUrl) {
//                        String filePath = localMedia.getCompressPath();
//                        OkHttpClientManager.postFileAsyn(getResources().getString(R.string.upload_file_url), filePath, new OkHttpClientManager.ResultCallback<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                String path = response.replace("\"", "").replace("\\", "");
//                                mRelativePicPath.add(path);
//                                LUtils.log(response);
//                                //所有图片先上传完毕，再调用保存Flat的接口
//                                if (mRelativePicPath.size() == mImageUrl.size()) {
//                                    String jsonStr = buildData();
//                                    OkHttpClientManager.postAsyn(getResources().getString(R.string.save_unit_url), jsonStr, new OkHttpClientManager.ResultCallback() {
//                                        @Override
//                                        public void onResponse(Object response) {
//                                            LUtils.toast("保存成功");
//                                            setResult(RESULT_OK);
//                                            finish();
//                                        }
//
//                                        @Override
//                                        public void onAfter() {
//                                            dismissDialogProgress();
//                                        }
//
//                                        public void onError(Request request, Exception e) {
//                                            if (e instanceof SocketTimeoutException) {
//                                                LUtils.toast("连接超时");
//                                            }
//                                            if (e instanceof ConnectException) {
//                                                LUtils.toast("连接异常");
//                                            }
//                                            if (e instanceof com.google.gson.JsonParseException) {
//                                                LUtils.toast("解析异常");
//                                            } else {
//                                                LUtils.toast("请求失败");
//                                            }
//                                            LUtils.log(e.getMessage());//打印异常日志
//                                            mRelativePicPath.clear();//失败后必须清空列表
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    String jsonStr = buildData();
//                    OkHttpClientManager.postAsyn(getResources().getString(R.string.save_unit_url), jsonStr, new OkHttpClientManager.ResultCallback() {
//                        @Override
//                        public void onResponse(Object response) {
//                            LUtils.toast("保存成功");
//                            setResult(RESULT_OK);
//                            finish();
//                        }
//                        @Override
//                        public void onAfter() {
//                            dismissDialogProgress();
//                        }
//                    });
//                }
//                break;
//        }
//    }
//
//    private String buildData() {
//        int flatId;
//        if (mEnterprise != null) {
//            flatId = mEnterprise.getFlatID();
//        } else {
//            Flat flat = (Flat) getIntent().getSerializableExtra("Flat");
//            flatId = (int) flat.getId();
//            mEnterprise = new Enterprise();
//        }
//        mEnterprise.setSmUserID(App.UserID);//必填项，暂时先默认1
//        mEnterprise.setFlatID(flatId);//获取坐标需要填flatid
//        mEnterprise.setName(mUnitNameItem.getInputText());
//        mEnterprise.setProperty(mUnitPropertyItem.getInputText());
//        mEnterprise.setType(mUnitTypeItem.getInputText());
//        mEnterprise.setType(mUnitUniformCodeItem.getInputText());
//        mEnterprise.setType(mUnitLicenseNoItem.getInputText());
//        mEnterprise.setType(mUnitOcodeItem.getInputText());
//        mEnterprise.setType(mUnitContactPhoneItem.getInputText());
//        mEnterprise.setType(mUnitRemarkItem.getInputText());
//        mEnterprise.setType(mUnitCorCerTypeItem.getInputText());
//        mEnterprise.setType(mUnitCorCerNoItem.getInputText());
//        mEnterprise.setType(mUnitCorNameItem.getInputText());
//        mEnterprise.setType(mUnitCorPhoneItem.getInputText());
//        mEnterprise.setType(mUnitChargeCerTypeItem.getInputText());
//        mEnterprise.setType(mUnitChargeCerNoItem.getInputText());
//        mEnterprise.setType(mUnitChargeNameItem.getInputText());
//        mEnterprise.setType(mUnitChargePhoneItem.getInputText());
//        SaveParameter<Enterprise> saveParameter = new SaveParameter<>();
//        saveParameter.setItem(mEnterprise);
//        List<FileModel> fileModels = new ArrayList<>();
//        for (String path : mRelativePicPath) {
//            FileModel fileModel = new FileModel();
//            fileModel.setRelativePath(path);
//            fileModels.add(fileModel);
//        }
//        saveParameter.setToUploadFiles(fileModels);
//        String jsonStr = mGson.toJson(saveParameter);
//        return jsonStr;
//    }
}
