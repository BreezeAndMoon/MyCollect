package com.joint.jointpolice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.MessageAdapter;
import com.joint.jointpolice.common.BaseFragment;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.Message;
import com.joint.jointpolice.util.LUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/11 15:21
 * @描述: 通知主页
 */

public class MsgNotificationFragment extends BaseFragment {
    @BindView(R.id.rlv_msg_notice)
    RecyclerView mRecycleView;
    MessageAdapter mMessageAdapter;
    Unbinder unbinder;
    List<Message> mMessageList;
    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_msg_notification;
    }

    @Override
    public void initView() {
        mMessageAdapter = new MessageAdapter(getContext());
        mRecycleView.setAdapter(mMessageAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {
        mMessageList = new ArrayList<Message>();
        for (int i = 0; i < 20; i++) {
            Message message = new Message();
            message.setSort("消息xxx");
            mMessageList.add(message);
        }
        mMessageAdapter.setDataSource(mMessageList);
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
}
