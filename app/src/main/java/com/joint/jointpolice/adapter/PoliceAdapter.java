package com.joint.jointpolice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.PoliceDetailActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.UserInfo;

import java.util.ArrayList;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/17 11:20
 * @描述:
 */

public class PoliceAdapter extends BaseRecycleAdapter<UserInfo> {

    private Context mContext;
    public PoliceAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_police_list;
    }

    @Override
        public void getItemView(RecycleViewHolder viewHolder, int position, UserInfo item) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, PoliceDetailActivity.class));
            }
        });
    }
}
