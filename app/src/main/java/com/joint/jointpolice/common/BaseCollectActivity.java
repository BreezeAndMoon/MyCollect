package com.joint.jointpolice.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.FileModel;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.PersonInfo;
import com.joint.jointpolice.model.CollectModels.SaveParameter;
import com.joint.jointpolice.util.DateUtil;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.PictureUtil;
import com.joint.jointpolice.util.SpUtil;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Entity;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.Request;

import static com.joint.jointpolice.util.SpUtil.get;

/**
 * Created by Joint229 on 2018/6/7.
 */

public abstract class BaseCollectActivity<T> extends BaseActivity implements View.OnClickListener,CollectFieldItem.OnEditTextClickListener {
    protected PictureSelectUtil imgSelectUtil;
    protected int themeId = R.style.picture_default_style;
    protected int chooseMode = PictureMimeType.ofAll();
    protected int chooseRequest = ActivityRequestCode.PICTURE_SELECT_IMG;
    protected int maxSelectNum = 9;
    private List<LocalMedia> mTempUrl = new ArrayList<>();
    private List<LocalMedia> mImageUrl = new ArrayList<>();
    List<String> mRelativePicPath = new ArrayList<>();
    private T mEntity;
    private Gson mGson = new Gson();
    boolean mIsUpdate;
    boolean mIsPerson;
    TimePickerView mTimePickerView;
    int mViewID;

    @Override
    protected void initView() {
        resizeTvDrawable(R.id.tv_title_address);
        resizeTvDrawable(R.id.tv_title_house);
        findViewById(R.id.tv_save).setOnClickListener(this);
        TextView titleTv = findViewById(R.id.toolbar_tv_title);
        titleTv.setText(getToolbarTitle());
        initPictureImg();
        initViewExtra();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mTimePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((CollectFieldItem) findViewById(mViewID)).setInputText(DateUtil.formatDate(date));
            }
        }).build();
        findView();
        if (getIntent().getBooleanExtra(Constant.IsAdd, false))
            return;
        mEntity = getEntity();
        bindData();
        initPicture();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityRequestCode.PICTURE_SELECT_IMG:
                    List<LocalMedia> imgLists = PictureSelector.obtainMultipleResult(data);
                    mImageUrl = imgLists;
                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
                    break;
                case ActivityRequestCode.PICTURE_ID_CARD:
                    LocalMedia idCardPicture = PictureSelector.obtainMultipleResult(data).get(0);
                    OkHttpClientManager.postFormDataAsync(idCardPicture.getCompressPath(), new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onResponse(String response) {
                            Document parse = Jsoup.parse(response);
                            final Elements select = parse.select("div#ocrresult");
                            String id = getHtmlMsg(select.get(1).text(), "公民身份号码:", "签发机关");
                            ((CollectFieldItem) findViewById(R.id.item_id_number)).setInputText(id);
                            String name = getHtmlMsg(select.text(), "姓名:", "性别");
                            ((CollectFieldItem) findViewById(R.id.item_name)).setInputText(name);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                saveEntity();
                break;
            case R.id.tv_scan_id_card:
                chooseRequest = ActivityRequestCode.PICTURE_ID_CARD;
                setSelectImgData();
                break;

        }

    }

    @Override
    public void onEditTextClicked(View v) {
        mViewID = (int) v.getTag();
        switch (mViewID) {
            case R.id.item_live_time:
                mTimePickerView.show();
                break;
            case R.id.item_birth_date:
                mTimePickerView.show();
                break;
            case R.id.item_stay_expiration_date:
                mTimePickerView.show();
                break;
            case R.id.item_certificate_expiration_date:
                mTimePickerView.show();
                break;
            case R.id.item_enter_country_date:
                mTimePickerView.show();
                break;
        }
    }

    private String buildData() {
        if (mEntity != null) {
            mIsUpdate = true;//更新
            if (mEntity.getClass() == PersonInfo.class) {
                Flat flat = (Flat) getIntent().getSerializableExtra(Constant.Flat);
                ((PersonInfo) mEntity).setTableName(Constant.T_PERSON);//重写的buildEntity中不用再设置tablename
                ((PersonInfo) mEntity).setFlat(flat);//重写的buildEntity中不用再设置flat
            }
            buildEntity(mEntity);
        } else {
            try {
                mEntity = getType().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (mEntity.getClass() == Enterprise.class) {
                Flat flat = (Flat) getIntent().getSerializableExtra(Constant.Flat);
                int flatId = (int) flat.getId();
                ((Enterprise) mEntity).setFlatID(flatId);//重写的buildEntity中不用再对设置flatid,flatid不是必填，只是为了通过flatid获取单位坐标
            }
            if (mEntity.getClass() == PersonInfo.class) {
                mIsPerson = true;//采集对象为人
                Flat flat = (Flat) getIntent().getSerializableExtra(Constant.Flat);
                ((PersonInfo) mEntity).setTableName(Constant.T_PERSON);//重写的buildEntity中不用再设置tablename
                ((PersonInfo) mEntity).setFlat(flat);//重写的buildEntity中不用再设置flat
                Person person = new Person();
                ((PersonInfo) mEntity).setPerson(person);
            }
            buildEntity(mEntity);
        }
        SaveParameter<T> saveParameter = new SaveParameter<>();
        saveParameter.setItem(mEntity);
        if (mEntity.getClass() == PersonInfo.class) {
            saveParameter.setPersonType("T_Person");
        }
        List<FileModel> fileModels = new ArrayList<>();
        for (String path : mRelativePicPath) {
            FileModel fileModel = new FileModel();
            fileModel.setRelativePath(path);
            fileModels.add(fileModel);
        }
        saveParameter.setToUploadFiles(fileModels);
        saveParameter.setUserID(LoginActivity.userID);
        String jsonStr = mGson.toJson(saveParameter);
        return jsonStr;
    }

    private void addCollectSum() {
        //仅新增才加1
        if (!mIsUpdate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String nowDateStr = simpleDateFormat.format(new Date());
            if (mIsPerson) {
                int personSum = (int) SpUtil.get(this, Constant.Collect_Person, 0);
                personSum++;
                SpUtil.put(this, Constant.Collect_Person, personSum);
                SpUtil.put(this, Constant.LAST_MODIFYDATE, nowDateStr);
            } else {
                int unitSum = (int) SpUtil.get(this, Constant.Collect_Unit, 0);
                unitSum++;
                SpUtil.put(this, Constant.Collect_Unit, unitSum);
                SpUtil.put(this, Constant.LAST_MODIFYDATE, nowDateStr);
            }
        }
    }

    private void initPicture() {
        String jsonStr = String.valueOf(getItemId());
        OkHttpClientManager.postAsyn(getResources().getString(getGetFileModelsUrlIdt()), jsonStr, new OkHttpClientManager.ResultCallback<List<FileModel>>() {
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

    private void resizeTvDrawable(int id) {
        TextView textView = findViewById(id);
        Drawable[] drawables = textView.getCompoundDrawables();
        drawables[0].setBounds(0, 0, 64, 64);
        textView.setCompoundDrawables(drawables[0], null, null, null);
    }

    private void initPictureImg() {
        RecyclerView recyBuildingImg = findViewById(getRecyclerViewId());
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
                //todo
                PictureSelector.create(BaseCollectActivity.this).externalPicturePreview(position, mImageUrl);
            }

            @Override
            public void onItemDelClick(int position) {
                //mTvPictureNum.setText(getString(R.string.text_picture_img_num,mImageUrl.size()));

            }
        });
    }

    private String getHtmlMsg(String s, String startContent, String endContent) {
        String[] id = s.split(startContent);
        String[] idTrue = id[1].split(endContent);
        return idTrue[0];
    }

    private void selectTime(int id) {

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

    private void saveEntity() {
        showDialogProgress();
        Iterator<LocalMedia> iterator = mImageUrl.iterator();
        while (iterator.hasNext()) {
            if (TextUtils.isEmpty(iterator.next().getCompressPath())) {
                iterator.remove();
            }
        }//先删除通过url展示的图片(即上次保存的图片)，这样上次保存的图片将不会再次保存而被服务端清除(每次更新都先清空旧的图片),如果需要再次保存旧的可以提取图片url后面的udid添加到touploadfiles中
        if (mImageUrl.size() > 0) {
            for (LocalMedia localMedia : mImageUrl) {
                String filePath = localMedia.getCompressPath();
                OkHttpClientManager.postFileAsyn(getResources().getString(R.string.upload_file_url), filePath, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String response) {
                        String path = response.replace("\"", "").replace("\\", "");
                        mRelativePicPath.add(path);
                        // LUtils.log(response);
                        //所有图片先上传完毕，再调用保存item的接口
                        if (mRelativePicPath.size() == mImageUrl.size()) {
                            String jsonStr = buildData();
                            OkHttpClientManager.postAsyn(getResources().getString(getSaveItemUrlId()), jsonStr, new OkHttpClientManager.ResultCallback() {
                                @Override
                                public void onResponse(Object response) {
                                    LUtils.toast("保存成功");
                                    addCollectSum();
                                    finish();
                                }

                                @Override
                                public void onAfter() {
                                    dismissDialogProgress();
                                }

                                public void onError(Request request, Exception e) {
                                    if (e instanceof SocketTimeoutException) {
                                        LUtils.toast("连接超时");
                                    }
                                    if (e instanceof ConnectException) {
                                        LUtils.toast("连接异常");
                                    }
                                    if (e instanceof com.google.gson.JsonParseException) {
                                        LUtils.toast("解析异常");
                                    } else {
                                        LUtils.toast("请求失败");
                                    }
                                    LUtils.log(e.getMessage());//打印异常日志
                                    mRelativePicPath.clear();//失败后必须清空列表
                                }
                            });
                        }
                    }
                });
            }
        } else {
            String jsonStr = buildData();
            OkHttpClientManager.postAsyn(getResources().getString(getSaveItemUrlId()), jsonStr, new OkHttpClientManager.ResultCallback() {
                @Override
                public void onResponse(Object response) {
                    LUtils.toast("保存成功");
                    addCollectSum();
                    finish();
                }

                @Override
                public void onAfter() {
                    dismissDialogProgress();
                }
            });
        }
        dismissDialogProgress();
    }

    protected abstract void findView();

    protected abstract void bindData();

    protected abstract void initViewExtra();

    protected abstract String getToolbarTitle();

    protected abstract int getRecyclerViewId();

    protected abstract void buildEntity(T entity);

    protected abstract Class<T> getType();

    protected abstract int getItemId();

    protected abstract int getSaveItemUrlId();

    protected abstract int getGetFileModelsUrlIdt();

    //protected abstract Context getContext();
    protected abstract T getEntity();

}
