package com.joint.jointpolice.activity.collect;

import android.os.Bundle;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseCollectActivity;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.PersonInfo;
import com.joint.jointpolice.widget.custom.CollectFieldItem;

/**
 * Created by Joint229 on 2018/5/23.
 */

public class CollectNodoorPersonActivity extends BaseCollectActivity<PersonInfo> {
    private PersonInfo mPersonInfo;
    private Person mPerson;
    private CollectFieldItem mNameItem;
    private CollectFieldItem mSexItem;
    private CollectFieldItem mBirthDateItem;
    private CollectFieldItem mContactPhoneItem;
    private CollectFieldItem mBeforeNameItem;
    private CollectFieldItem mNationItem;
    private CollectFieldItem mEducationItem;
    private CollectFieldItem mNativePlaveItem;
    private CollectFieldItem mCountryItem;
    private CollectFieldItem mMarryItem;
    private CollectFieldItem mLiveTimeItem;
    private CollectFieldItem mAddressItem;
    private CollectFieldItem mOrignalNativeAddressItem;
    private CollectFieldItem mGuarderIdcardNumberItem;
    private CollectFieldItem mGuarderNameItem;
    private CollectFieldItem mGuarderContactPhoneItem;
    private CollectFieldItem mGuarderRelationshipItem;
    private CollectFieldItem mGuardNativeAddressItem;
    private CollectFieldItem mNodoorTypeItem;
    private CollectFieldItem mNodoorCauseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_nodoor_person);
    }

    @Override
    protected void findView() {
        mNameItem = findViewById(R.id.item_name);
        mSexItem = findViewById(R.id.item_sex);
        //...
    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void initViewExtra() {

    }

    @Override
    protected String getToolbarTitle() {
        return "采集无户人员";
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.recy_building_img;
    }

    @Override
    protected void buildEntity(PersonInfo entity) {
        Person person = entity.getPerson();
        person.setPersonTypeID(Constant.Person_Nodoor);//必填
        person.setName(mNameItem.getInputText());//必填
        //...

    }

    @Override
    protected Class<PersonInfo> getType() {
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
