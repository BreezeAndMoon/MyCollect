package com.joint.jointpolice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.activity.collect.CollectCisborderPersonActivity;
import com.joint.jointpolice.activity.collect.CollectDoorplateActivity;
import com.joint.jointpolice.activity.collect.CollectNodoorPersonActivity;
import com.joint.jointpolice.activity.collect.CollectOutboundPersonActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.util.Id2StringUtil;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.StringUtil;

import static com.joint.jointpolice.util.DateUtil.formatDate;

/**
 * Created by Joint229 on 2018/6/8.
 */

public class ActualPersonAdapter extends BaseRecycleAdapter<Person> {
    private Context mContext;
    String mNameHint;
    String mTypeHint;
    String mTimeHint;
    public static final int REQ_CODE_Person = 0;

    public ActualPersonAdapter(Context context) {
        super(context);
        mContext = context;
        mNameHint = mContext.getResources().getString(R.string.person_name_hint);
        mTypeHint = mContext.getResources().getString(R.string.person_type_hint);
        mTimeHint = mContext.getResources().getString(R.string.time_hint);
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_actual_person;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, Person item) {
        TextView nameTextView = viewHolder.getView(R.id.tv_person_name);
        nameTextView.setText(mNameHint + StringUtil.formatString( item.getName()));
        TextView typeTextView = viewHolder.getView(R.id.tv_person_type);
        typeTextView.setText(mTypeHint + Id2StringUtil.convertPersonType(item.getPersonTypeID()));
        TextView timeTextView = viewHolder.getView(R.id.tv_update_time);
        timeTextView.setText(mTimeHint + formatDate(item.getModifyDate()));
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int typeID = item.getPersonTypeID();
                Intent intent = ((Activity) mContext).getIntent();
                intent.putExtra(Constant.Person, item);
                intent.putExtra(Constant.IsAdd,false);
                switch (typeID) {
                    case Constant.Person_Outbound:
                        intent.setClass(mContext, CollectOutboundPersonActivity.class);
                        break;
                    case Constant.Person_Cisborder:
                        intent.setClass(mContext, CollectCisborderPersonActivity.class);
                        break;
                    case Constant.Person_Nodoor:
                        intent.setClass(mContext, CollectNodoorPersonActivity.class);
                        break;
                    default:
                        LUtils.toast("该数据非采集数据");
                        return;
                }
                mContext.startActivity(intent);
            }
        });
    }
}
