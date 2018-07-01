package com.joint.jointpolice.activity.collect;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
    private CollectFieldItem mRelationshipWithLandlordItem;
    private CollectFieldItem mUnitNameItem;
    private CollectFieldItem mUnitContactPersonItem;
    private CollectFieldItem mUnitContactPhoneItem;
    private CollectFieldItem mUnitContactAddressItem;
    private CollectFieldItem mRemarkItem;

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
        mRelationshipWithLandlordItem = findViewById(R.id.item_relationship_with_landlord);
        mUnitNameItem = findViewById(R.id.item_unit_name);
        mUnitContactPersonItem = findViewById(R.id.item_unit_contact_person);
        mUnitContactPhoneItem = findViewById(R.id.item_unit_contactPhone);
        mUnitContactAddressItem = findViewById(R.id.item_unit_contact_address);
        mRemarkItem = findViewById(R.id.item_remark);
    }

    @Override
    protected void bindData() {
        mChineseNameItem.setInputText(mPerson.getName());
        mPersonTypeItem.setInputText(mPerson.getPersonCategory());
        //...
    }

    @Override
    protected void initViewExtra() {

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
        //...

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
