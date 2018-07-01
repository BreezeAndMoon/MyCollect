package com.joint.jointpolice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.collect.ActualBuildingActivity;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.activity.collect.CollectUnitActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.CollectModels.Enterprise;

import static com.joint.jointpolice.util.DateUtil.formatDate;

/**
 * Created by Joint229 on 2018/6/6.
 */

public class ActualUnitAdapter extends BaseRecycleAdapter<Enterprise> {
    private Context mContext;
    String mNameHint;
    String mTypeHint;
    String mTimeHint;
    public static final int REQ_CODE_Enterprise = 0;

    public ActualUnitAdapter(Context context) {
        super(context);
        mContext = context;
        mNameHint = mContext.getResources().getString(R.string.unit_name_hint);
        mTypeHint = mContext.getResources().getString(R.string.unit_type_hint);
        mTimeHint = mContext.getResources().getString(R.string.time_hint);
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_actual_unit;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, Enterprise item) {
        TextView nameTextView = viewHolder.getView(R.id.tv_unit_name);
        nameTextView.setText(mNameHint + item.getName());
        TextView typeTextView = viewHolder.getView(R.id.tv_unit_type);
        typeTextView.setText(mTypeHint + item.getType());
        TextView timeTextView = viewHolder.getView(R.id.tv_update_time);
        timeTextView.setText(mTimeHint + formatDate(item.getModifyDate()));
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CollectUnitActivity.class);
                intent.putExtra("Enterprise", item);
                mContext.startActivity(intent);
            }
        });
    }
}

