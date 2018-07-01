package com.joint.jointpolice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.http.HttpManager;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.http.exception.CommonResponse;
import com.joint.jointpolice.model.User;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.RxUtil;
import com.joint.jointpolice.widget.UserTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:18
 * @描述: 登录页面
 */

public class LoginActivity extends BaseActivity {
    public static int userID = 1;
    @BindView(R.id.edt_user_name)
    EditText mEdtUserName;
    @BindView(R.id.edt_pwd)
    EditText mEdtPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    @Override
    protected void initView() {

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCanLogin())
                    return;
                String jsonStr = buildData();
                OkHttpClientManager.postAsyn(getResources().getString(R.string.login_getuser_url), jsonStr, new OkHttpClientManager.ResultCallback<User>() {
                    @Override
                    public void onResponse(User response) {
                        if(response!=null){
                            userID= response.getId();
                            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                            finish();
                        }
                    }
                    public void onBefore(){
                        showDialogProgress();
                    }
                    public void onAfter(){
                        dismissDialogProgress();
                    }
                });

            }

        });
        UserTextWatcher userTextWatcher = new UserTextWatcher(mBtnLogin, mEdtUserName, mEdtPwd);
        mBtnLogin.addTextChangedListener(userTextWatcher);
        mEdtUserName.addTextChangedListener(userTextWatcher);
        mEdtPwd.addTextChangedListener(userTextWatcher);
    }

    private String buildData() {
        User user = new User();
        user.setUserName(mEdtUserName.getText().toString());
        user.setPassword(mEdtPwd.getText().toString());
        String jsonStr = mGson.toJson(user);
        return jsonStr;
    }

    private boolean isCanLogin() {
        String userName = mEdtUserName.getText().toString().trim();
        String pwd = mEdtPwd.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            LUtils.toast("用户名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(pwd)) {
            LUtils.toast("密码不能为空");
            return false;
        }

        return true;
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }


}
