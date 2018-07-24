package com.joint.jointpolice.activity.collect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseCollectActivity;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.PersonInfo;
import com.joint.jointpolice.util.DateUtil;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Joint229 on 2018/5/7.
 */

public class CollectCisborderPersonActivity extends BaseCollectActivity<PersonInfo> {
    private CollectFieldItem mIdNumberItem;
    private CollectFieldItem mNameItem;
    private CollectFieldItem mTelephoneItem;
    private CollectFieldItem mLiveCauseItem;
    private CollectFieldItem mLiveTimeItem;
    private CollectFieldItem mLivePlaceItem;//
    private CollectFieldItem mSexItem;
    private CollectFieldItem mNationItem;
    private CollectFieldItem mRoommateRelationItem;//
    private CollectFieldItem mProvinceCityItem;//
    private CollectFieldItem mNativePlaceAddressItem;
    private CollectFieldItem mServicePlaceItem;
    private CollectFieldItem mProfessionItem;//
    private RadioGroup mHealthRadioGroup;
    private final String HEALTH_NORMAL = "正常";
    private PersonInfo mPersonInfo;
    private Person mPerson;
    private String mHealth;
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_cisborder_person);
        mHealth = HEALTH_NORMAL;//初始默认勾选"正常"
    }

    @Override
    protected void findView() {
        mIdNumberItem = findViewById(R.id.item_id_number);
        mNameItem = findViewById(R.id.item_name);
        mTelephoneItem = findViewById(R.id.item_telephone);
        mLiveCauseItem = findViewById(R.id.item_live_cause);
        mLiveTimeItem = findViewById(R.id.item_live_time);
        mLivePlaceItem = findViewById(R.id.item_live_place);
        mSexItem = findViewById(R.id.item_sex);
        mNationItem = findViewById(R.id.item_nation);
        mRoommateRelationItem = findViewById(R.id.item_roommate_relation);
        mProvinceCityItem = findViewById(R.id.item_province_city);
        mNativePlaceAddressItem = findViewById(R.id.item_native_place_address);
        mServicePlaceItem = findViewById(R.id.item_service_place);
        mProfessionItem = findViewById(R.id.item_profession);
        mHealthRadioGroup = findViewById(R.id.radio_group_health);
    }

    @Override
    protected void bindData() {
        mHealth = mPerson.getHealthStatus();
        mIdNumberItem.setInputText(mPerson.getCardNo());
        mNameItem.setInputText(mPerson.getName());
        mTelephoneItem.setInputText(mPerson.getTelephone());
        mLiveCauseItem.setInputText(mPerson.getLiveCause());
        mLiveTimeItem.setInputText(mPerson.getLiveDate() == null ? "" : DateUtil.formatDate(mPerson.getLiveDate()));
        mLivePlaceItem.setInputText(mPerson.getLivePlace());
        mSexItem.setInputText(mPerson.getSex());
        mNationItem.setInputText(mPerson.getNature());
        mRoommateRelationItem.setInputText(mPerson.getRoommateRelation());
        mProvinceCityItem.setInputText(mPerson.getProvinceAndCity());
        mNativePlaceAddressItem.setInputText(mPerson.getDetailNativeAdd());
        mServicePlaceItem.setInputText(mPerson.getServicePlace());
        mProfessionItem.setInputText(mPerson.getProfession());
        mHealth = mPerson.getHealthStatus();
        if (mHealth.equals("正常")) {
            mHealthRadioGroup.check(R.id.radiobtn_normal);
        } else
            mHealthRadioGroup.check(R.id.radiobtn_psychosis);
    }

    @Override
    protected void initViewExtra() {
        mHealthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                mHealth = radioButton.getText().toString();
            }
        });
        ((CollectFieldItem)findViewById(R.id.item_id_number)).setOnEditTextPhotoTouchListener(this);
        ((CollectFieldItem) findViewById(R.id.item_live_time)).setOnEditTextClickListener(this);
        ((CollectFieldItem)findViewById(R.id.item_live_cause)).setOnEditTextClickListener(this);
        ((CollectFieldItem)findViewById(R.id.item_live_place)).setOnEditTextClickListener(this);
        ((CollectFieldItem)findViewById(R.id.item_sex)).setOnEditTextClickListener(this);
        ((CollectFieldItem)findViewById(R.id.item_nation)).setOnEditTextClickListener(this);
        ((CollectFieldItem)findViewById(R.id.item_roommate_relation)).setOnEditTextClickListener(this);

    }

    @Override
    protected String getToolbarTitle() {
        return "采集境内人员";
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recy_building_img;
    }

    @Override
    protected void buildEntity(PersonInfo entity) {
        Person person = entity.getPerson();
        person.setPersonTypeID(Constant.Person_Cisborder);//必填
        person.setName(mNameItem.getInputText());//必填
        person.setCardNo(mIdNumberItem.getInputText());
        person.setTelephone(mTelephoneItem.getInputText());
        person.setLiveCause(mLiveCauseItem.getInputText());
        person.setLiveDate(DateUtil.dateStr2Timestamp(mLiveTimeItem.getInputText()));
        person.setLivePlace(mLivePlaceItem.getInputText());
        person.setSex(mSexItem.getInputText());
        person.setNature(mNationItem.getInputText());
        person.setRoommateRelation(mRoommateRelationItem.getInputText());
        person.setProvinceAndCity(mProvinceCityItem.getInputText());
        person.setDetailNativeAdd(mNativePlaceAddressItem.getInputText());
        person.setServicePlace(mServicePlaceItem.getInputText());
        person.setProfession(mProfessionItem.getInputText());
        person.setHealthStatus(mHealth);
    }

    @Override
    protected Class getType() {
        return PersonInfo.class;
    }

    @Override
    protected int getItemId() {
        return mPerson.getId();
    }

    @Override
    protected int getSaveItemUrlId() {
        return R.string.save_person_url;
    }

    @Override
    protected int getGetFileModelsUrlIdt() {
        return R.string.get_person_filemodels_url;
    }

    @Override
    protected PersonInfo getEntity() {
        mPerson = (Person) getIntent().getSerializableExtra(Constant.Person);
        mPersonInfo = new PersonInfo();
        mPersonInfo.setPerson(mPerson);
        return mPersonInfo;
    }

}
