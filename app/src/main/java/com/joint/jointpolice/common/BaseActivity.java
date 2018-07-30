package com.joint.jointpolice.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.joint.jointpolice.R;
import com.joint.jointpolice.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by poweruser on 2018/1/3.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = getClass().getName();
    private LoadingDialog mLoadingDialog;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onCreate(savedInstanceState, TAG);
        ButterKnife.bind(this);
        initToolbar();
        this.initPresenter();
        this.initValue();
        this.initView();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void startIntent(Class<?> clss) {
        startIntent(this, clss);
    }

    public void startIntent(Context context, Class<?> clss) {
        Intent intent = new Intent(context, clss);
        context.startActivity(intent);
    }

    protected abstract void onCreate(Bundle savedInstanceState, String tag);

    protected abstract void initView();

    protected abstract void initPresenter();

    protected abstract void initValue();


    public void showDialogProgress() {
        showDialogProgress("");
    }

    public void showDialogProgress(String str) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setLoadStr(str);
        }
        mLoadingDialog.show();
    }

    public void dismissDialogProgress() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }


}



