package com.joint.jointpolice.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joint.jointpolice.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/4 15:06
 * @描述:
 */

public abstract class BaseFragment extends Fragment {
    private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initView();
        this.initPresenter();
        this.initValue();
    }

    public void startIntent(Class<?> clss) {
        startIntent(getActivity(), clss);
    }

    public void startIntent(Context context, Class<?> clss) {
        Intent intent = new Intent(context, clss);
        context.startActivity(intent);
    }

    public abstract int getFragmentLayout();

    public abstract void initView();

    public abstract void initPresenter();

    public abstract void initValue();

    public void showDialogProgress() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
            mLoadingDialog.show();
        }
    }

    public void dismissDialogProgress() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }


}
