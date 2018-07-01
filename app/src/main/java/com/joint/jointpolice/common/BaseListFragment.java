package com.joint.jointpolice.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joint.jointpolice.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/11 11:09
 * @描述:
 */

public abstract class BaseListFragment<T> extends BaseFragment implements OnRefreshListener, OnRefreshLoadmoreListener {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout mSmartRefresh;


    private int curPage = 0;
    public BaseRecycleAdapter<T> mAdapter;

    @Override
    public int getFragmentLayout() {
        return 0;
    }

    @Override
    public void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseDataAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSmartRefresh.setOnRefreshListener(this);
        mSmartRefresh.setOnRefreshLoadmoreListener(this);

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {

    }


    class BaseDataAdapter extends BaseRecycleAdapter<T> {
        public BaseDataAdapter(Context context) {
            super(context);
        }

        @Override
        public int getResourceView(int resource) {
            return getItemRes();
        }

        @Override
        public void getItemView(RecycleViewHolder viewHolder, int position, T item) {
            setItemData(viewHolder, position, item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = null;
        if (getFragmentLayout() != 0) {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        } else {
            rootView= inflater.inflate(R.layout.common_listview, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        onLoadmore();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        onRefresh();
    }


    public abstract void onLoadmore();

    public abstract void onRefresh();

    public abstract int getItemRes();

    public abstract void setItemData(RecycleViewHolder viewHolder,int position,T item);

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SmartRefreshLayout getSmartRefresh() {
        return mSmartRefresh;
    }

    public BaseRecycleAdapter<T> getAdapter() {
        return mAdapter;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getCurPage() {
        return curPage;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
