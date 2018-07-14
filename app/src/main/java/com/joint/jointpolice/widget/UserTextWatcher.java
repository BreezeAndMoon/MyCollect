package com.joint.jointpolice.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.joint.jointpolice.activity.EndFeedBackActivity;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/16 14:18
 * @描述:
 */

public class UserTextWatcher implements TextWatcher {
    private Button mButton;
    private TextView[] mEditText;

    public  UserTextWatcher(Button button, TextView... editTexts) {
        this.mButton = button;
        this.mEditText = editTexts;mEditText = editTexts;
//        mEditText = new TextView[editTexts.length];
//        for(int i=0;i<editTexts.length;i++) {
//            mEditText[i] = editTexts[i];
//        }
        mEditText = editTexts;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean enabled = true;
        for (TextView editText : mEditText) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) enabled = false;
        }
        mButton.setEnabled(enabled);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
