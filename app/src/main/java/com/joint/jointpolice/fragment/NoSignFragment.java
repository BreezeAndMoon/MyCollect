package com.joint.jointpolice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.PoliceDetailActivity;
import com.joint.jointpolice.adapter.PoliceAdapter;
import com.joint.jointpolice.common.BaseFragment;
import com.joint.jointpolice.common.BaseListFragment;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.common.TestData;
import com.joint.jointpolice.model.UserInfo;
import com.joint.jointpolice.widget.tablayout.widget.MsgView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/11 9:07
 * @描述: 未签收警情
 */

public class NoSignFragment extends BaseListFragment<UserInfo> {
    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_police_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRefresh();
    }

    @Override
    public void onLoadmore() {
        setCurPage(getCurPage() + 1);
        getSmartRefresh().postDelayed(new Runnable() {
            @Override
            public void run() {
                List userInfos = TestData.setSencondData();
                getAdapter().addItems(userInfos);
                getSmartRefresh().finishRefresh();
                getSmartRefresh().finishLoadmore();

            }
        }, 500);
    }

    @Override
    public void onRefresh() {
        setCurPage(1);
        getSmartRefresh().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAdapter().cleanItems();
                List userInfos = TestData.setData();
                getAdapter().setDataSource(userInfos);
                getSmartRefresh().finishRefresh();
                getSmartRefresh().finishLoadmore();

            }
        }, 500);
    }

    @Override
    public int getItemRes() {
        return R.layout.item_police_list;
    }

    @Override
    public void setItemData(RecycleViewHolder viewHolder, int position, UserInfo userInfo) {
        ImageView mImgPoliceDanger = viewHolder.getView(R.id.img_police_danger);
        TextView mTvPoliceName = viewHolder.getView(R.id.tv_police_name);
        TextView mTvPoliceTime = viewHolder.getView(R.id.tv_police_time);
        MsgView mTvSign = viewHolder.getView(R.id.tv_sign);
        MsgView mTvNoSign = viewHolder.getView(R.id.tv_no_sign);
        RelativeLayout mRlItemPoliceList = viewHolder.getView(R.id.rl_item_police_list);

        mTvPoliceName.setText(userInfo.getUserName());
        mTvPoliceTime.setText("2018-01-11 11:35");
        mTvSign.setVisibility(View.VISIBLE);
        mTvNoSign.setVisibility(View.VISIBLE);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startIntent(PoliceDetailActivity.class);
            }
        });
    }


}
