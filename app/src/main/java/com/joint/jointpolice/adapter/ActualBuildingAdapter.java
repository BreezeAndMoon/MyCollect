package com.joint.jointpolice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.collect.ActualBuildingActivity;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.CollectModels.Flat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.validation.Validator;

import static com.joint.jointpolice.util.DateUtil.formatDate;

/**
 * Created by Joint229 on 2018/5/31.
 */

public class ActualBuildingAdapter extends BaseRecycleAdapter<Flat> {
    private Context mContext;
    String mNatureHint;
    String mTypeHint;
    String mTimeHint;
    public static final int REQ_CODE_Flat = 0;
    public ActualBuildingAdapter(Context context) {
        super(context);
        mContext = context;
        mNatureHint = mContext.getResources().getString(R.string.nature_hint);
        mTypeHint = mContext.getResources().getString(R.string.type_hint);
        mTimeHint = mContext.getResources().getString(R.string.time_hint);
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_actual_building;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, Flat item) {
        TextView natureTextView = viewHolder.getView(R.id.tv_house_nature);
        natureTextView.setText(mNatureHint + item.getHouseNature());
        TextView typeTextView = viewHolder.getView(R.id.tv_house_type);
        typeTextView.setText(mTypeHint + item.getHouseType());
        TextView timeTextView = viewHolder.getView(R.id.tv_update_time);
        timeTextView.setText(mTimeHint + formatDate(item.getModifyDate()));
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CollectBuildingActivity.class);
                intent.putExtra("Flat", item);
                ((Activity) mContext).startActivityForResult(intent, ActualBuildingAdapter.REQ_CODE_Flat);
            }
        });
//        viewHolder.getView(R.id.msgview_change).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CollectBuildingActivity.class);
//                intent.putExtra("Flat", item);
//                ((Activity) mContext).startActivityForResult(intent, ActualBuildingAdapter.REQ_CODE_Flat);
//            }
//        });
    }


}
