package com.joint.jointpolice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.widget.UserTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:33
 * @描述: 修改密码
 */

public class UpdatePasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.edt_old_password)
    EditText mEdtOldPassword;
    @BindView(R.id.edt_new_password)
    EditText mEdtNewPassword;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("修改密码");
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LUtils.toast("修改成功");
                finish();
            }
        });
        UserTextWatcher userTextWatcher = new UserTextWatcher(mBtnSubmit, mEdtNewPassword, mEdtOldPassword);
        mEdtNewPassword.addTextChangedListener(userTextWatcher);
        mEdtOldPassword.addTextChangedListener(userTextWatcher);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }


}
