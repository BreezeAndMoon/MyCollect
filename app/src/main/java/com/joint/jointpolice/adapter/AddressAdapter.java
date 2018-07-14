package com.joint.jointpolice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.collect.ActualBuildingActivity;
import com.joint.jointpolice.activity.collect.ActualPersonActivity;
import com.joint.jointpolice.activity.collect.ActualUnitActivity;
import com.joint.jointpolice.activity.collect.AddressActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.NBuilding;

import io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;

/**
 * Created by Joint229 on 2018/4/16.
 */

public class AddressAdapter extends BaseRecycleAdapter<Flat> {
    Context mContext;
    public int mSelectedPosition;

    public AddressAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_address_list;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, Flat item) {
        TextView textView = viewHolder.getView(R.id.tv_nbuilding_address);
        textView.setText(item.getLocationName());
        Intent intent = ((Activity) mContext).getIntent();
        String type = intent.getStringExtra(Constant.CollectType);
        viewHolder.getConvertView().setOnClickListener(view -> {
            mSelectedPosition = position;
            Intent newIntent = new Intent();
            item.setModifyDate(item.getModifyDate().replace("\\",""));//需要满足传递所需的MicrosoftDateFormat时间格式
            newIntent.putExtra("Flat", item);
            newIntent.putExtra(Constant.CollectType, type);
            switch (type) {
                case Constant.Collect_House:
                    newIntent.setClass(mContext, ActualBuildingActivity.class);
                    mContext.startActivity(newIntent);
                    break;
                case Constant.Collect_Person:
                    newIntent.setClass(mContext, ActualPersonActivity.class);
                    mContext.startActivity(newIntent);
                    break;
                case Constant.Collect_Unit:
                    newIntent.setClass(mContext, ActualUnitActivity.class);
                    mContext.startActivity(newIntent);
                    break;
            }

        });
    }
}
