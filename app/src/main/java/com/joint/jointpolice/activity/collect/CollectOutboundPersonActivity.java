package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseCollectActivity;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.PersonInfo;
import com.joint.jointpolice.util.DateUtil;
import com.joint.jointpolice.widget.PictureSelectUtil;
import com.joint.jointpolice.widget.custom.CollectFieldItem;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joint229 on 2018/5/9.
 */

public class CollectOutboundPersonActivity extends BaseCollectActivity<PersonInfo> {
    private PersonInfo mPersonInfo;
    private Person mPerson;
    private CollectFieldItem mPersonTypeItem;
    private CollectFieldItem mCertificateTypeItem;
    private CollectFieldItem mCountryItem;
    private CollectFieldItem mCertificateNumberItem;
    private RadioGroup mLiveTimeRadioGroup;
    private CollectFieldItem mEnglishSurnameItem;
    private CollectFieldItem mEnglishNameItem;
    private CollectFieldItem mChineseNameItem;
    private CollectFieldItem mBirthDateItem;
    private CollectFieldItem mSexItem;
    private CollectFieldItem mStayExpirationDateItem;
    private CollectFieldItem mLivePlaceItem;
    private CollectFieldItem mTempLiveCauseItem;
    private CollectFieldItem mBirthPlaceItem;
    private CollectFieldItem mCertificateExpirationDateItem;
    private CollectFieldItem mEnterCountryDateItem;
    private CollectFieldItem mEntryCountryPlaceItem;
    private RadioGroup mMessageTypeRadioGroup;
    private CollectFieldItem mMessageAccount;
    private CollectFieldItem mRelationshipWithLandlordItem;
    private CollectFieldItem mUnitNameItem;
    private CollectFieldItem mUnitContactPersonItem;
    private CollectFieldItem mUnitContactPhoneItem;
    private CollectFieldItem mUnitContactAddressItem;
    private CollectFieldItem mRemarkItem;
    private String mMessageType;
    private boolean mIsMoreThanSix;
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_outbound_person);
    }

    @Override
    protected void findView() {
        mPersonTypeItem = findViewById(R.id.item_person_type);
        mCertificateTypeItem = findViewById(R.id.item_certificate_type);
        mCountryItem = findViewById(R.id.item_country);
        mCertificateNumberItem = findViewById(R.id.item_certificate_number);
        mLiveTimeRadioGroup = findViewById(R.id.radio_group_live_time);
        mEnglishSurnameItem = findViewById(R.id.item_english_surname);
        mEnglishNameItem = findViewById(R.id.item_english_name);
        mChineseNameItem = findViewById(R.id.item_chinese_name);
        mBirthDateItem = findViewById(R.id.item_birth_date);
        mSexItem = findViewById(R.id.item_sex);
        mStayExpirationDateItem = findViewById(R.id.item_stay_expiration_date);
        mLivePlaceItem = findViewById(R.id.item_live_place);
        mTempLiveCauseItem = findViewById(R.id.item_temp_live_cause);
        mBirthPlaceItem = findViewById(R.id.item_birth_place);
        mCertificateExpirationDateItem = findViewById(R.id.item_certificate_expiration_date);
        mEnterCountryDateItem = findViewById(R.id.item_enter_country_date);
        mEntryCountryPlaceItem = findViewById(R.id.item_enter_country_place);
        mMessageTypeRadioGroup = findViewById(R.id.radio_group_message_type);
        mMessageAccount = findViewById(R.id.item_message_account);
        mRelationshipWithLandlordItem = findViewById(R.id.item_relationship_with_landlord);
        mUnitNameItem = findViewById(R.id.item_unit_name);
        mUnitContactPersonItem = findViewById(R.id.item_unit_contact_person);
        mUnitContactPhoneItem = findViewById(R.id.item_unit_contactPhone);
        mUnitContactAddressItem = findViewById(R.id.item_unit_contact_address);
        mRemarkItem = findViewById(R.id.item_remark);
    }

    @Override
    protected void bindData() {
        mMessageType = mPerson.getMessageType();
        mIsMoreThanSix = mPerson.getLiveMonth();
        mPersonTypeItem.setInputText(mPerson.getPersonCategory());
        mChineseNameItem.setInputText(mPerson.getName());
        mCertificateTypeItem.setInputText(mPerson.getCertificateType());
        mCountryItem.setInputText(mPerson.getCounty());
        mCertificateNumberItem.setInputText(mPerson.getCertificateNo());
        mLiveTimeRadioGroup.check(mPerson.getLiveMonth()?R.id.radiobtn_more_than_six:R.id.radiobtn_less_than_six);
        mEnglishSurnameItem.setInputText(mPerson.getSurNameEn());
        mEnglishNameItem.setInputText(mPerson.getNameEn());
        mBirthDateItem.setInputText(mPerson.getBirthDate() == null ? "" : DateUtil.formatDate(mPerson.getBirthDate()));
        mSexItem.setInputText(mPerson.getSex());
        mStayExpirationDateItem.setInputText(mPerson.getStopExpirationDate()==null?"":DateUtil.formatDate(mPerson.getStopExpirationDate()));
        mLivePlaceItem.setInputText(mPerson.getLivePlace());
        mTempLiveCauseItem.setInputText(mPerson.getTempLiveReason());
        mBirthPlaceItem.setInputText(mPerson.getBirthPlace());
        mCertificateExpirationDateItem.setInputText(mPerson.getPaperExpirationDate()==null?"":DateUtil.formatDate(mPerson.getPaperExpirationDate()));
        mEnterCountryDateItem.setInputText(mPerson.getEnterCountryDate()==null?"":DateUtil.formatDate(mPerson.getEnterCountryDate()));
        mEntryCountryPlaceItem.setInputText(mPerson.getEnterCountryPlace());
        if(mPerson.getMessageType()!=null){
            //check(-1)?
            mMessageTypeRadioGroup.check(mPerson.getMessageType().equals("微信")?R.id.radiobtn_wechat:R.id.radiobtn_other);
        }
        mMessageAccount.setInputText(mPerson.getMessageNo());
        mUnitNameItem.setInputText(mPerson.getUnitName());
        mUnitContactPersonItem.setInputText(mPerson.getUnitContactPerson());
        mUnitContactPhoneItem.setInputText(mPerson.getUnitContactPhone());
        mUnitContactAddressItem.setInputText(mPerson.getUnitContactAdd());
        mRemarkItem.setInputText(mPerson.getRemark());
    }

    @Override
    protected void initViewExtra() {
        mMessageTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                mMessageType = radioButton.getText().toString();
            }
        });
        mLiveTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mIsMoreThanSix = checkedId==R.id.radiobtn_more_than_six;
            }
        });
        ((CollectFieldItem) findViewById(R.id.item_birth_date)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_stay_expiration_date)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_certificate_expiration_date)).setOnEditTextClickListener(this);
        ((CollectFieldItem) findViewById(R.id.item_enter_country_date)).setOnEditTextClickListener(this);
    }

    @Override
    protected String getToolbarTitle() {
        return "采集境外人员";
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recy_building_img;
    }

    @Override
    protected void buildEntity(PersonInfo entity) {
        Person person = entity.getPerson();
        person.setPersonTypeID(Constant.Person_Outbound);//必填
        person.setName(mChineseNameItem.getInputText());//必填
        person.setPersonCategory(mPersonTypeItem.getInputText());
        person.setCounty(mCountryItem.getInputText());
        person.setCertificateType(mCertificateTypeItem.getInputText());
        person.setCertificateNo(mCertificateNumberItem.getInputText());
        person.setLiveMonth(mIsMoreThanSix);//todo 必填判断
        person.setSurNameEn(mEnglishSurnameItem.getInputText());
        person.setNameEn(mEnglishNameItem.getInputText());
        person.setBirthDate(DateUtil.dateStr2Timestamp(mBirthDateItem.getInputText()));
        person.setSex(mSexItem.getInputText());
        person.setStopExpirationDate(DateUtil.dateStr2Timestamp(mStayExpirationDateItem.getInputText()));
        person.setLivePlace(mLivePlaceItem.getInputText());
        person.setTempLiveReason(mTempLiveCauseItem.getInputText());
        person.setBirthPlace(mBirthPlaceItem.getInputText());
        person.setPaperExpirationDate(DateUtil.dateStr2Timestamp(mCertificateExpirationDateItem.getInputText()));
        person.setEnterCountryDate(DateUtil.dateStr2Timestamp(mEnterCountryDateItem.getInputText()));
        person.setEnterCountryPlace(mEntryCountryPlaceItem.getInputText());
        person.setMessageType(mMessageType);
        person.setMessageNo(mMessageAccount.getInputText());
        person.setLandlordRelation(mRelationshipWithLandlordItem.getInputText());
        person.setUnitName(mUnitNameItem.getInputText());
        person.setUnitContactPerson(mUnitContactPersonItem.getInputText());
        person.setUnitContactPhone(mUnitContactPhoneItem.getInputText());
        person.setUnitContactAdd(mUnitContactAddressItem.getInputText());
        person.setRemark(mRemarkItem.getInputText());
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
