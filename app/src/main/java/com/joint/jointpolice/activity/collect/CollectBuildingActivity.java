package com.joint.jointpolice.activity.collect;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.google.gson.Gson;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.TestData;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.constants.HttpConstant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.FileModel;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.SaveFlatParameter;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.PictureUtil;
import com.joint.jointpolice.util.SpUtil;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectBuildingActivity extends BaseActivity implements View.OnClickListener, CollectFieldItem.OnEditTextClickListener {
    private PictureSelectUtil imgSelectUtil;
    private int themeId = R.style.picture_default_style;
    private int chooseMode = PictureMimeType.ofAll();
    private int chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
    private int maxSelectNum = 9;
    private Spinner mRentTypeSpinner;
    private Flat mFlat;
    Gson mGson = new Gson();
    List<String> mRelativePicPath = new ArrayList<>();
    private List<LocalMedia> mTempUrl = new ArrayList<>();
    private List<LocalMedia> mImageUrl = new ArrayList<>();
    private static final int REQ_CODE_PERMISSION = 0x1111;
    OkHttpClient mOkHttpClient;
    private CollectFieldItem mCurrentSituationItem;
    private CollectFieldItem mIsRentingHouseItem;
    private CollectFieldItem mHouseTypeItem;
    private CollectFieldItem mHouseNatureItem;
    private CollectFieldItem mHousePurposeItem;
    private CollectFieldItem mHouseStructureItem;
    private CollectFieldItem mLandlordCertificateTypeItem;
    private CollectFieldItem mLandlordCertificateNumberItem;
    private CollectFieldItem mLandlordNameItem;
    private CollectFieldItem mLandlordPhoneItem;
    private CollectFieldItem mAgentCertificateTypeItem;
    private CollectFieldItem mAgentCertificateNumberItem;
    private CollectFieldItem mAgentNameItem;
    private CollectFieldItem mAgentPhoneItem;
    private MyCustomDialog mHouseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_collect_building);
    }

    private void resizeTvDrawable(int id) {
        TextView textView = findViewById(id);
        Drawable[] drawables = textView.getCompoundDrawables();
        drawables[0].setBounds(0, 0, 64, 64);
        textView.setCompoundDrawables(drawables[0], null, null, null);
    }

    @Override
    protected void initView() {
        resizeTvDrawable(R.id.tv_title_address);
        resizeTvDrawable(R.id.tv_title_house);
        TextView titleTv = findViewById(R.id.toolbar_tv_title);
        titleTv.setText("采集房屋");
        TextView toolbarTv = findViewById(R.id.toolbar_tv_right);
        toolbarTv.setText("扫一扫");
        toolbarTv.setVisibility(View.VISIBLE);
        toolbarTv.setOnClickListener((v) -> {
            //todo 扫码
            //Toast.makeText(this, "扫码", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(CollectBuildingActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Do not have the permission of camera, request it.
                ActivityCompat.requestPermissions(CollectBuildingActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
            } else {
                // Have gotten the permission
                startCaptureActivityForResult();
            }
        });
        mHouseDialog = new MyCustomDialog.Builder(this)
                .setCancelTouchout(true)
                .setView(R.layout.dialog_select_search)
                .Build();
        findViewById(R.id.tv_save).setOnClickListener(this);
        mCurrentSituationItem.setOnEditTextClickListener(this);
        mIsRentingHouseItem.setOnEditTextClickListener(this);
        mHouseTypeItem.setOnEditTextClickListener(this);
        initPictureImg();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityRequestCode.PICTURE_SELECT_IMG:
                    List<LocalMedia> imgLists = PictureSelector.obtainMultipleResult(data);
                    mImageUrl = imgLists;
                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
                    String path = mImageUrl.get(0).getCompressPath();//在app缓存目录中

                    //uploadAndRecognize(path);
                    break;
            }
        }
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        //LUtils.toast(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        String result = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        String standardAdd = result.substring(result.indexOf("id=") + 3);
                        //mCollectStandardAddItem.setInputText(standardAdd);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Document document = Jsoup.connect(result).get();
                                    Elements elements = document.select("div.zam_dznr_m");//zam_dznr_m这个div的class经常会变。。现在是dzksfd1_lz2
                                    String address = elements.get(0).text();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //mCollectAddItem.setInputText(address);
                                        }
                                    });
                                } catch (IOException e) {
                                    LUtils.log(e.getMessage());
                                }

                            }
                        }).start();

                        //tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            LUtils.toast(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));

                            // tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    Toast.makeText(this, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void initValue() {
        findView();
        mFlat = (Flat) getIntent().getSerializableExtra("Flat");
        if (mFlat != null) {
            mCurrentSituationItem.setInputText(mFlat.getHouseStatus());
            mIsRentingHouseItem.setInputText(mFlat.getIsRent() ? "是" : "否");
            mHouseTypeItem.setInputText(mFlat.getHouseType());
            mHouseNatureItem.setInputText(mFlat.getHouseNature());
            mHousePurposeItem.setInputText(mFlat.getHousePurpose());
            mHouseStructureItem.setInputText(mFlat.getHouseStructure());
            mLandlordCertificateTypeItem.setInputText(mFlat.getLandlordCerType());
            mLandlordCertificateNumberItem.setInputText(mFlat.getLandlordCerNo());
            mLandlordNameItem.setInputText(mFlat.getLandlordName());
            mLandlordPhoneItem.setInputText(mFlat.getLandlordPhone());
            mAgentCertificateTypeItem.setInputText(mFlat.getAgentCerType());
            mAgentCertificateNumberItem.setInputText(mFlat.getAgentCerNo());
            mAgentNameItem.setInputText(mFlat.getAgentName());
            mAgentPhoneItem.setInputText(mFlat.getAgentPhone());
        }
        initPicture();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                showDialogProgress();
                Iterator<LocalMedia> iterator = mImageUrl.iterator();
                while (iterator.hasNext()) {
                    if (TextUtils.isEmpty(iterator.next().getCompressPath())) {
                        iterator.remove();
                    }
                }
                if (mImageUrl.size() > 0) {
                    for (LocalMedia localMedia : mImageUrl) {
                        String filePath = localMedia.getCompressPath();
                        OkHttpClientManager.postFileAsyn(getResources().getString(R.string.upload_file_url), filePath, new OkHttpClientManager.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                super.onError(request, e);
                                dismissDialogProgress();
                            }

                            @Override
                            public void onResponse(String response) {//返回的是"\/Upload\/temp\/15a6f1fa-f0df-4251-94f8-0029e2b43931.jpg",所以要做处理,否则保存到数据库也是该数据
                                String path = response.replace("\"", "").replace("\\", "");
                                mRelativePicPath.add(path);
                                LUtils.log(response);
                                //所有图片先上传完毕，再调用保存Flat的接口
                                if (mRelativePicPath.size() == mImageUrl.size()) {
                                    String jsonStr = buildData();
                                    OkHttpClientManager.postAsyn(getResources().getString(R.string.save_flat_url), jsonStr, new OkHttpClientManager.ResultCallback() {
                                        @Override
                                        public void onResponse(Object response) {
                                            LUtils.toast("保存成功");
                                            setResult(RESULT_OK);
                                            addCollectSum();
                                            finish();
                                        }

                                        @Override
                                        public void onAfter() {
                                            dismissDialogProgress();
                                        }

                                        @Override
                                        public void onBefore() {
                                            showDialogProgress();
                                        }

                                        public void onError(Request request, Exception e) {
                                            super.onError(request, e);
                                            mRelativePicPath.clear();//失败后必须清空列表
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {
                    String jsonStr = buildData();
                    OkHttpClientManager.postAsyn(getResources().getString(R.string.save_flat_url), jsonStr, new OkHttpClientManager.ResultCallback() {
                        @Override
                        public void onResponse(Object response) {
                            LUtils.toast("保存成功");
                            setResult(RESULT_OK);
                            addCollectSum();
                            finish();
                        }

                        @Override
                        public void onAfter() {
                            dismissDialogProgress();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onEditTextClicked(View view) {
        int viewResId = (int) view.getTag();
        switch (viewResId) {
            case R.id.item_current_situation:

//                mHouseDialog.setCheckedText(mCurrentSituationItem.getInputText());
//                mHouseDialog.setCollectFieldItemId(viewResId);
//                mHouseDialog.setData(Arrays.asList(getResources().getStringArray(R.array.house_situation_array)));
                mHouseDialog.show();
                break;
            case R.id.item_is_renting_house:
//                mHouseDialog.setCheckedText(mIsRentingHouseItem.getInputText());
//                mHouseDialog.setCollectFieldItemId(viewResId);
//                mHouseDialog.setData(Arrays.asList(getResources().getStringArray(R.array.house_isrent_array)));
                mHouseDialog.show();
                break;
            case R.id.item_house_type:
//                mHouseDialog.setCheckedText(mHouseTypeItem.getInputText());
//                mHouseDialog.setCollectFieldItemId(viewResId);
//                mHouseDialog.setData(Arrays.asList(getResources().getStringArray(R.array.house_type_array)));
                mHouseDialog.show();
                break;
        }
    }

    private void initPicture() {
        String jsonStr = String.valueOf(mFlat.getId());
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_filemodels_url), jsonStr, new OkHttpClientManager.ResultCallback<List<FileModel>>() {
            @Override
            public void onResponse(List<FileModel> response) {
                if (response != null) {
                    List<String> urls = new ArrayList<>();
                    for (FileModel fileModel : response) {
                        urls.add(fileModel.getRelativePath());
                    }
                    mImageUrl.addAll(PictureUtil.setLocalMedia(urls));
                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
                }
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

    private void findView() {
        mCurrentSituationItem = findViewById(R.id.item_current_situation);
        mIsRentingHouseItem = findViewById(R.id.item_is_renting_house);
        mHouseTypeItem = findViewById(R.id.item_house_type);
        mHouseNatureItem = findViewById(R.id.item_house_nature);
        mHousePurposeItem = findViewById(R.id.item_house_purpose);
        mHouseStructureItem = findViewById(R.id.item_house_structure);
        mLandlordCertificateTypeItem = findViewById(R.id.item_landlord_certificate_type);
        mLandlordCertificateNumberItem = findViewById(R.id.item_landlord_certificate_number);
        mLandlordNameItem = findViewById(R.id.item_landlord_name);
        mLandlordPhoneItem = findViewById(R.id.item_landlord_phone);
        mAgentCertificateTypeItem = findViewById(R.id.item_agent_certificate_type);
        mAgentCertificateNumberItem = findViewById(R.id.item_agent_certificate_number);
        mAgentNameItem = findViewById(R.id.item_agent_name);
        mAgentPhoneItem = findViewById(R.id.item_agent_phone);
    }

    /**
     * 房屋只有更新,因此每次更新后采集数也相应增加1
     */
    private void addCollectSum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr = simpleDateFormat.format(new Date());
        int houseSum = (int) SpUtil.get(this, Constant.Collect_House, 0);
        houseSum++;
        SpUtil.put(this, Constant.Collect_House, houseSum);
        SpUtil.put(this, Constant.LAST_MODIFYDATE, nowDateStr);
    }

    private String buildData() {
        //todo 添加标准地址字段(通过扫码获取值)
        mFlat.setHouseStatus(mCurrentSituationItem.getInputText());
        mFlat.setIsRent(mIsRentingHouseItem.getInputText() == "是" ? true : false);
        mFlat.setHouseType(mHouseTypeItem.getInputText());
        mFlat.setHouseNature(mHouseNatureItem.getInputText());
        mFlat.setHousePurpose(mHousePurposeItem.getInputText());
        mFlat.setHouseStructure(mHouseStructureItem.getInputText());
        mFlat.setLandlordCerType(mLandlordCertificateTypeItem.getInputText());
        mFlat.setLandlordCerNo(mLandlordCertificateNumberItem.getInputText());
        mFlat.setLandlordName(mLandlordNameItem.getInputText());
        mFlat.setLandlordPhone(mLandlordPhoneItem.getInputText());
        mFlat.setAgentCerType(mAgentCertificateTypeItem.getInputText());
        mFlat.setAgentCerNo(mAgentCertificateNumberItem.getInputText());
        mFlat.setAgentName(mAgentNameItem.getInputText());
        mFlat.setAgentPhone(mAgentPhoneItem.getInputText());
        mFlat.setModifyDate(null);//服务端先转成json再返回string的，所转的格式不符合上传的格式，且不需要保存，该时间服务端会存
        //todo 页面上很多字段有待数据库添加
        SaveFlatParameter parameter = new SaveFlatParameter();
        List<FileModel> fileModels = new ArrayList<>();
        for (String path : mRelativePicPath) {
            FileModel fileModel = new FileModel();
            fileModel.setRelativePath(path);
            fileModels.add(fileModel);
        }
        parameter.setToUploadFiles(fileModels);
        parameter.setItem(mFlat);
        parameter.setUserID(LoginActivity.userID);
        String jsonStr = mGson.toJson(parameter);
        return jsonStr;
    }

    private void initPictureImg() {
        RecyclerView recyBuildingImg = findViewById(R.id.recy_building_img);
        imgSelectUtil = new PictureSelectUtil(this, recyBuildingImg);
        //imgMedia.addAll(TestData.setLocalMedia(PictureConfig.TYPE_IMAGE, Constant.IMG_URL));
        //imgSelectUtil.getGridImageAdapter().setList(imgMedia);
        imgSelectUtil.setOpenPhoneGallery(new PictureSelectUtil.OpenPhoneGallery() {
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
                PictureSelector.create(CollectBuildingActivity.this).externalPicturePreview(position, mImageUrl);
            }

            @Override
            public void onItemDelClick(int position) {
                //mTvPictureNum.setText(getString(R.string.text_picture_img_num,mImageUrl.size()));

            }
        });
    }

    private void setSelectImgData() {
        PictureSelector.create(this)
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
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .selectionMedia(mTempUrl)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .compress(true)
                .forResult(chooseRequest);//结果回调onActivityResult code
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

}
