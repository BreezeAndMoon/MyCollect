package com.joint.jointpolice.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.Message;
import com.joint.jointpolice.util.LUtils;

/**
 * Created by Joint229 on 2018/2/2.
 */

public class MessageAdapter extends BaseRecycleAdapter<Message> {
    public MessageAdapter(Context context){super(context);}
    @Override
    public int getResourceView(int resource) {
        return R.layout.item_msg_list;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, Message item) {
        final TextView tvMessage = viewHolder.getView(R.id.tv_message);
        tvMessage.setText(item.getmMessage());
        final View itemView = viewHolder.getConvertView();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LUtils.toast(((TextView) itemView.findViewById(R.id.tv_message)).getText());
            }
        });
    }

}