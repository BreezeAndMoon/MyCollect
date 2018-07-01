package com.joint.jointpolice.widget.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;

/**
 * Created by Joint229 on 2018/5/3.
 */

public class CollectFieldItem extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private TextView mStarTextView;
    private EditText mEditText;
    private OnEditTextClickListener mOnEditTextClickListener;

    public CollectFieldItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_collect_field, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollectFieldItem);
        initWidget(typedArray);
    }

    private void initWidget(TypedArray typedArray) {
        mEditText = findViewById(R.id.edt_field_input);
        mTextView = findViewById(R.id.tv_field_name);
        mStarTextView = findViewById(R.id.tv_red_star);
        String hint = typedArray.getString(R.styleable.CollectFieldItem_edt_hint);
        String text = typedArray.getString(R.styleable.CollectFieldItem_tv_text);
        int visible = typedArray.getInt(R.styleable.CollectFieldItem_tv_star_visible, View.INVISIBLE);
        boolean focusable = typedArray.getBoolean(R.styleable.CollectFieldItem_edt_focusable, true);
        mTextView.setText(text);
        mEditText.setHint(hint);
        mEditText.setFocusable(focusable);
        mEditText.setOnClickListener(this);
        mStarTextView.setVisibility(visible);
        typedArray.recycle();
    }

    public String getInputText() {
        return mEditText.getText().toString();
    }

    public void setInputText(String text) {
        mEditText.setText(text);
    }

    public interface OnEditTextClickListener {
        void onEditTextClicked(View view);
    }

    public void setOnEditTextClickListener(OnEditTextClickListener onEditTextClickListener) {
        mOnEditTextClickListener = onEditTextClickListener;
        mEditText.setTag(this.getId());
    }

    @Override
    public void onClick(View v) {
        if (mOnEditTextClickListener != null) {
            mOnEditTextClickListener.onEditTextClicked(v);
        }
    }


}
