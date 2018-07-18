package com.joint.jointpolice.adapter;

import android.content.Context;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;

import org.w3c.dom.Text;

import java.util.logging.Handler;

/**
 * Created by Joint229 on 2018/7/17.
 */

public class DialogChoiceAdapter extends BaseRecycleAdapter<String> {
    private Context mContext;
    private CheckBox mLastCheckedBox;
    private View.OnClickListener mOnClickListener;
    private String mCheckedText;

    public DialogChoiceAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setCheckedText(String checkedText) {
        mCheckedText = checkedText;
    }

    @Override
    public int getResourceView(int resource) {
        return R.layout.item_select_dialog;
    }

    @Override
    public void getItemView(RecycleViewHolder viewHolder, int position, String item) {
        CheckBox checkBox = viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(false);
        checkBox.setText(item);
        if (item.equals(mCheckedText)) {
            checkBox.setChecked(true);
            mLastCheckedBox = checkBox;
        }
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastCheckedBox != null)
                    mLastCheckedBox.setChecked(false);
                checkBox.setChecked(true);
                if (mOnClickListener != null)
                    mOnClickListener.onClick(v);
            }
        });
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }
}
