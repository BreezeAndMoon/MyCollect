package com.joint.jointpolice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.activity.UpdatePasswordActivity;
import com.joint.jointpolice.common.BaseFragment;
import com.joint.jointpolice.util.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/4 15:05
 * @描述: 我的主页
 */

public class MyFragment extends BaseFragment {
    @BindView(R.id.tv_update_password)
    TextView mTvUpdatePassword;
    @BindView(R.id.ll_logout)
    LinearLayout mLlLogout;
    Unbinder unbinder;
    @BindView(R.id.tv_time_synchronization)
    TextView mTvTimeSynchronization;

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_main_my;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_update_password, R.id.ll_logout, R.id.tv_time_synchronization})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update_password:
                startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
                break;
            case R.id.ll_logout:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.tv_time_synchronization:
                DateUtil.setSystemTime(getContext(), "20131023.112800");
//                DateUtil.setSysTime(getContext(),11,10);
//                SystemClock.setCurrentTimeMillis(1516091003);
                break;
        }
    }

}
