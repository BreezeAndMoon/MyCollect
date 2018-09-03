package com.joint.jointpolice.activity.collect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

public class CollectUnitActivity extends BaseCollectActivity<Enterprise> {
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
    private CollectFieldItem mUnitOcodeItem;//组织机构代码
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
        mUnitNameItem.setInputText(mEnterprise.getName());
        mUnitPropertyItem.setInputText(mEnterprise.getProperty());
        mUnitTypeItem.setInputText(mEnterprise.getType());
        mUnitUniformCodeItem.setInputText(mEnterprise.getUniformCode());
        mUnitLicenseNoItem.setInputText(mEnterprise.getLicenseNo());
        mUnitOcodeItem.setInputText(mEnterprise.getOcode());
        mUnitContactPhoneItem.setInputText(mEnterprise.getContactPhone());
        mUnitRemarkItem.setInputText(mEnterprise.getRemark());
        mUnitCorCerTypeItem.setInputText(mEnterprise.getCorCerType());
        mUnitCorCerNoItem.setInputText(mEnterprise.getCorCerNo());
        mUnitCorNameItem.setInputText(mEnterprise.getCorName());
        mUnitCorPhoneItem.setInputText(mEnterprise.getCorPhone());
        mUnitChargeCerTypeItem.setInputText(mEnterprise.getChargeCerType());
        mUnitChargeCerNoItem.setInputText(mEnterprise.getChargeCerNo());
        mUnitChargeNameItem.setInputText(mEnterprise.getChargeName());
        mUnitChargePhoneItem.setInputText(mEnterprise.getChargePhone());
    }

    @Override
    protected Enterprise getEntity() {
        mEnterprise = getIntent().getParcelableExtra(Constant.Enterprise);
        return mEnterprise;
    }

    @Override
    protected void initViewExtra() {
        ((CollectFieldItem) findViewById(R.id.item_unit_property)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_unit_type)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_unit_chargeCerType)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_unit_corCerType)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_unit_uniformCode)).setOnEditTextPhotoTouchListener(this);

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
        enterprise.setUniformCode(mUnitUniformCodeItem.getInputText());
        enterprise.setLicenseNo(mUnitLicenseNoItem.getInputText());
        enterprise.setOcode(mUnitOcodeItem.getInputText());
        enterprise.setContactPhone(mUnitContactPhoneItem.getInputText());
        enterprise.setRemark(mUnitRemarkItem.getInputText());
        enterprise.setCorCerType(mUnitCorCerTypeItem.getInputText());
        enterprise.setCorCerNo(mUnitCorCerNoItem.getInputText());
        enterprise.setCorName(mUnitCorNameItem.getInputText());
        enterprise.setCorPhone(mUnitCorPhoneItem.getInputText());
        enterprise.setChargeCerType(mUnitChargeCerTypeItem.getInputText());
        enterprise.setChargeCerNo(mUnitChargeCerNoItem.getInputText());
        enterprise.setChargeName(mUnitChargeNameItem.getInputText());
        enterprise.setChargePhone(mUnitChargePhoneItem.getInputText());
    }

    @Override
    protected Class<Enterprise> getType() {
        return Enterprise.class;
    }

    @Override
    protected int getItemId() {
        return mEnterprise.getSmID();
    }

}
