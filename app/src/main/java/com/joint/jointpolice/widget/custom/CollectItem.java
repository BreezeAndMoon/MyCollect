package com.joint.jointpolice.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.joint.jointpolice.R;

/**
 * Created by Joint229 on 2018/4/11.
 */

public class CollectItem extends LinearLayout {
    private EditText mEditText;
    private TextView mTextView;

    public CollectItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_collect_item, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollectItem);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        mEditText = findViewById(R.id.edt_collect_item);
        mTextView = findViewById(R.id.tv_collect_item);
        String hint = typedArray.getString(R.styleable.CollectItem_hint);
        String text = typedArray.getString(R.styleable.CollectItem_text);
        mEditText.setHint(hint);
        mTextView.setText(text);
        typedArray.recycle();
    }

    public String getInputText() {
        return mEditText.getText().toString();
    }

    public void setInputText(String text) {
        mEditText.setText(text);
    }
}

