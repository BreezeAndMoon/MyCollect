package com.joint.jointpolice.common;

import android.app.Activity;
import android.app.Dialog;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import com.joint.jointpolice.util.StringUtil;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;
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
import static com.joint.jointpolice.util.StringUtil.getHtmlMsg;
import static com.joint.jointpolice.util.StringUtil.getListFromArrayRes;

/**
 * Created by Joint229 on 2018/6/7.
 */

public abstract class BaseCollectActivity<T> extends BaseActivity implements View.OnClickListener, CollectFieldItem.OnEditTextClickListener, CollectFieldItem.OnEditTextPhotoTouchListener {
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
    private MyCustomDialog mMyDialog;
    final int CISBORDERPERSON_ID_CARD = 4;
    final int BUSINESS_LICENCE = 5;
    final int UNIT_CORCER_ID_CARD = 6;
    final int UNIT_CHARGE_ID_CARD = 7;
    final int UNIT_UNIFORM_CODE = 8;

    @Override
    protected void initView() {
        resizeTvDrawable(R.id.tv_title_address);
        resizeTvDrawable(R.id.tv_title_house);
        findViewById(R.id.tv_save).setOnClickListener(this);
        setToolbarTitle(getToolbarTitle());
        mMyDialog = new MyCustomDialog.Builder(this)
                .setCancelTouchout(true)
                .setView(R.layout.dialog_select_search)
                .Build();
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

    private void onRecognized(int idResID, int nameResID, Intent data) {
        LocalMedia idCardPicture = PictureSelector.obtainMultipleResult(data).get(0);
        OkHttpClientManager.postFormDataAsync(idCardPicture.getCompressPath(), new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                Document parse = Jsoup.parse(response);
                final Elements select = parse.select("div#ocrresult");
                String id = getHtmlMsg(select.get(1).text(), "公民身份号码:", "签发机关");
                ((CollectFieldItem) findViewById(idResID)).setInputText(id);
                String name = getHtmlMsg(select.text(), "姓名:", "性别");
                ((CollectFieldItem) findViewById(nameResID)).setInputText(name);
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

    private void onUniformCodeRecognized(int resID, Intent data) {
        LocalMedia idCardPicture = PictureSelector.obtainMultipleResult(data).get(0);
        String url = getResources().getString(R.string.recognize_card);
        String[] filePaths = new String[]{idCardPicture.getCompressPath()};
        String[] fileKeys = new String[]{"img"};//必须要传正确的key值
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("action","template");
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("callbackurl","/idcard/");
        OkHttpClientManager.Param[] params= new OkHttpClientManager.Param[]{param1,param2};
        OkHttpClientManager.postFormDataAsync(url, filePaths, fileKeys, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                Document parse = Jsoup.parse(response);
                final Elements select = parse.select("div#ocrresult");
                String resultJson = select.get(1).text();
                JsonObject jsonObject = new JsonParser().parse(resultJson).getAsJsonObject();
                JsonArray jsonArray = jsonObject.get("childs").getAsJsonArray();
                String uniformCode= jsonArray.get(1).getAsJsonObject().get("childs").getAsJsonArray().get(0).getAsJsonObject().get("Text").getAsString();
                ((CollectFieldItem) findViewById(resID)).setInputText(uniformCode);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ActivityRequestCode.PICTURE_SELECT_IMG:
                    List<LocalMedia> imgLists = PictureSelector.obtainMultipleResult(data);
                    mImageUrl = imgLists;
                    imgSelectUtil.getGridImageAdapter().setList(mImageUrl);
                    break;
                case CISBORDERPERSON_ID_CARD:
                    onRecognized(R.id.item_id_number, R.id.item_name, data);
                    break;
                case UNIT_CHARGE_ID_CARD:
                    onRecognized(R.id.item_unit_chargeCerNo, R.id.item_unit_chargeName, data);
                    break;
                case UNIT_CORCER_ID_CARD:
                    onRecognized(R.id.item_unit_corCerNo, R.id.item_unit_corName, data);
                    break;
                case UNIT_UNIFORM_CODE:
                    onUniformCodeRecognized(R.id.item_unit_uniformCode,data);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                saveEntity();
                break;
        }

    }

    @Override
    public void onEditTextPhotoTouch(View view) {
        switch ((int) view.getTag()) {
            case R.id.item_id_number:
                chooseRequest = CISBORDERPERSON_ID_CARD;
                setSelectImgData();
                break;
            case R.id.item_unit_corCerNo:
                chooseRequest = UNIT_CORCER_ID_CARD;
                setSelectImgData();
                break;
            case R.id.item_unit_chargeCerNo:
                chooseRequest = UNIT_CHARGE_ID_CARD;
                setSelectImgData();
                break;
            case R.id.item_unit_uniformCode:
                chooseRequest = UNIT_UNIFORM_CODE;
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
            case R.id.item_live_cause:
                mMyDialog.resetData(getListFromArrayRes(R.array.live_cause), mViewID);
                mMyDialog.show("居住事由");
                break;
            case R.id.item_live_place:
                mMyDialog.resetData(getListFromArrayRes(R.array.live_place), mViewID);
                mMyDialog.show("居住处所");
                break;
            case R.id.item_sex:
                mMyDialog.resetData(getListFromArrayRes(R.array.sex), mViewID);
                mMyDialog.show("性别");
                break;
            case R.id.item_nation:
                mMyDialog.resetData(getListFromArrayRes(R.array.nation), mViewID);
                mMyDialog.show("民族");
                break;
            case R.id.item_roommate_relation:
                mMyDialog.resetData(getListFromArrayRes(R.array.roommate_relation), mViewID);
                mMyDialog.show("同住人关系");
                break;
            case R.id.item_unit_property:
                mMyDialog.resetData(getListFromArrayRes(R.array.unit_property), mViewID);
                mMyDialog.show("单位性质");
                break;
            case R.id.item_unit_type:
                mMyDialog.resetData(getListFromArrayRes(R.array.unit_type), mViewID);
                mMyDialog.show("单位类型");
                break;
            case R.id.item_unit_corCerType:
                mMyDialog.resetData(getListFromArrayRes(R.array.certificate_type), mViewID);
                mMyDialog.setOnCheckedListener(new MyCustomDialog.OnCheckedListener() {
                    @Override
                    public void onChecked(String checkedStr) {
                        ((CollectFieldItem) findViewById(R.id.item_unit_corCerNo)).setPhotoVisible(("居民身份证".equals(checkedStr)));
                    }
                });
                mMyDialog.show("证件类型");
                break;
            case R.id.item_unit_chargeCerType:
                mMyDialog.resetData(getListFromArrayRes(R.array.certificate_type), mViewID);
                mMyDialog.setOnCheckedListener(new MyCustomDialog.OnCheckedListener() {
                    @Override
                    public void onChecked(String checkedStr) {
                        ((CollectFieldItem) findViewById(R.id.item_unit_chargeCerNo)).setPhotoVisible(("居民身份证".equals(checkedStr)));
                    }
                });
                mMyDialog.show("证件类型");
                break;
        }
    }

    private String buildData() {
        if (mEntity != null) {
            mIsUpdate = true;//更新
            if (mEntity.getClass() == PersonInfo.class) {
                Flat flat = (Flat) getIntent().getSerializableExtra(Constant.Flat);
                ((PersonInfo) mEntity).setTableName(Constant.T_PERSON);//重写的buildEntity中不用再设置tablename
                //todo:PersonInfo需要传Flat，但是通过搜索结果跳转过来的没有在intent中传递flat，需要判断上面flat是否为空，为null则根据personid获取flat的接口获取flat，暂时没有接口,单位的不需要
                ((PersonInfo) mEntity).setFlat(flat);//重写的buildEntity中不用再设置flat
                ((PersonInfo) mEntity).getPerson().setModifyDate(null);//时间服务端会更新,这里不需要更新
            } else
                ((Enterprise) mEntity).setModifyDate(null);
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
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        dismissDialogProgress();
                    }

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
