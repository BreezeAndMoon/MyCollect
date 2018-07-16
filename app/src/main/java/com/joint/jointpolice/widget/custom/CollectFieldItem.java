package com.joint.jointpolice.widget.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.util.LUtils;

/**
 * Created by Joint229 on 2018/5/3.
 */

public class CollectFieldItem extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;
    private TextView mStarTextView;
    private EditText mEditText;
    private OnEditTextClickListener mOnEditTextClickListener;
    private Drawable mPhotoDrawable;
    private OnEditTextPhotoTouchListener mOnEditTextPhotoTouchListener;
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
        int photoVisible = typedArray.getInt(R.styleable.CollectFieldItem_ic_photo_visiable, View.INVISIBLE);
        mTextView.setText(text);
        mEditText.setHint(hint);
        mEditText.setFocusable(focusable);
        mEditText.setOnClickListener(this);
        mEditText.setCompoundDrawablePadding(20);
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPhotoDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
                    int eventX = (int) event.getRawX();
                    int eventY = (int) event.getRawY();
                    Rect rect = new Rect();
                    getGlobalVisibleRect(rect);
                    int photoWidth = mPhotoDrawable.getBounds().width();
                    rect.left = rect.right - photoWidth;
                    if (rect.contains(eventX, eventY))
                        if(mOnEditTextPhotoTouchListener!=null)
                            mOnEditTextPhotoTouchListener.onEditTextPhotoTouch(v);
                }
                return false;
            }
        });
        mStarTextView.setVisibility(visible);
        mPhotoDrawable = getResources().getDrawable(R.drawable.ic_photo);
        if (photoVisible == View.VISIBLE) {
            mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mPhotoDrawable, null);
        }
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
        mEditText.setTag(this.getId());//通过view获取CollectFieldItem的id
    }
    public interface OnEditTextPhotoTouchListener{
        void onEditTextPhotoTouch(View view);
}
    public void setOnEditTextPhotoTouchListener(OnEditTextPhotoTouchListener onEditTextPhotoTouchListener){
        mOnEditTextPhotoTouchListener = onEditTextPhotoTouchListener;
        mEditText.setTag(this.getId());
    }
    @Override
    public void onClick(View v) {
        if (mOnEditTextClickListener != null) {
            mOnEditTextClickListener.onEditTextClicked(v);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mPhotoDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
//            int eventX = (int) event.getRawX();
//            int eventY = (int) event.getRawY();
//            Rect rect = new Rect();
//            rect.left = rect.right - mPhotoDrawable.getBounds().width();
//            if (rect.contains(eventX, eventY))
//                LUtils.toast("触摸成功2");
//        }
//        return super.onTouchEvent(event);
//    }

    public void setPhotoVisible(boolean photoVisible) {
        mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, photoVisible == true ? mPhotoDrawable: null, null);
    }

}
