package com.joint.jointpolice.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.DialogChoiceAdapter;
import com.joint.jointpolice.widget.custom.CollectFieldItem;

import java.util.Date;
import java.util.List;

/**
 * Created by Joint229 on 2018/6/27.
 */

public class MyCustomDialog extends Dialog {
    private Context mContext;
    private int mHeight, mWidth;
    private boolean mCancelTouchout;
    private View mView;
    private List<String> mData;
    private Handler mHandler = new Handler();
    private int mCollectFieldItemId;
    private DialogChoiceAdapter mDialogChoiceAdapter;
    private String mCheckedText;

    private MyCustomDialog(Builder builder) {
        super(builder.context);
        initValue(builder);
    }

    private MyCustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        initValue(builder);
    }

    private void initValue(Builder builder) {
        mContext = builder.context;
        mHeight = builder.height;
        mWidth = builder.width;
        mCancelTouchout = builder.cancelTouchout;
        mView = builder.view;
        mData = builder.data;
        mCollectFieldItemId = builder.collectFeildItemId;
        mDialogChoiceAdapter = new DialogChoiceAdapter(mContext);
    }

    private void initExtra() {
        if (mData != null)
            bindRecyclerView(mData);
        TextView cancelTextView = findViewById(R.id.tv_dialog_cancel);
        if (cancelTextView != null)
            cancelTextView.setOnClickListener(v -> dismiss());//可以通过addViewOnclick添加，不过因为很多处使用到，不想每次都添加就统一初始化了
    }

    private void initDimension() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        if (mWidth != 0 && mHeight != 0) {
            layoutParams.width = mWidth;
            layoutParams.height = mHeight;
        } else {
            int screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
            int screenHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
            layoutParams.width = (int) (screenWidth * 0.9);
            if (mData.size() <= 6)
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            else
                layoutParams.height = (int) (screenHeight * 0.6);
        }
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(layoutParams);
    }

    public void setData(List<String> data) {
        this.mData = data;
        mDialogChoiceAdapter.setDataSource(mData);
        initDimension();
    }

    public void setCollectFieldItemId(int collectFieldItemIdId) {
        mCollectFieldItemId = collectFieldItemIdId;
    }

    public void setCheckedText(String checkedText) {
        mCheckedText = checkedText;
        mDialogChoiceAdapter.setCheckedText(mCheckedText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        setCanceledOnTouchOutside(mCancelTouchout);
        initDimension();
        initExtra();
    }

    public void bindRecyclerView(List<String> data) {
        RecyclerView recyclerView = findViewById(R.id.recy_choice);
        mDialogChoiceAdapter = new DialogChoiceAdapter(mContext);
        mDialogChoiceAdapter.setDataSource(data);
        mDialogChoiceAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 300);
                CheckBox checkBox = view.findViewById(R.id.checkbox);
                String checkedStr = checkBox.getText().toString();
                CollectFieldItem collectFieldItem = ((AppCompatActivity) mContext).findViewById(mCollectFieldItemId);
                collectFieldItem.setInputText(checkedStr);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mDialogChoiceAdapter);
    }

    public static final class Builder {
        private Context context;
        private int height, width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;
        private List<String> data;
        private int collectFeildItemId;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, int resStyle) {
            this.context = context;
            this.resStyle = resStyle;
        }

        public Builder setData(List<String> data) {
            this.data = data;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setCancelTouchout(boolean cancelTouchout) {
            this.cancelTouchout = cancelTouchout;
            return this;
        }

        public Builder setView(int viewRes) {
            this.view = view = LayoutInflater.from(context).inflate(viewRes, null);
            return this;
        }

        public Builder setCollectFieldItemId(int collectFieldItemIdId) {
            this.collectFeildItemId = collectFieldItemIdId;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public MyCustomDialog Build() {
            if (resStyle != -1)
                return new MyCustomDialog(this, resStyle);
            else
                return new MyCustomDialog(this);
        }
    }

}

