package com.joint.jointpolice.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.util.CollectionUtil;
import com.joint.jointpolice.util.LUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/17 10:27
 * @描述: 简单封装recycleView的adapter
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    private ArrayList<View> mHeadViews = new ArrayList<>();
    private ArrayList<View> mFootViews = new ArrayList<>();
    private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
    private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();

    private List<T> mDatas = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;

    public BaseRecycleAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public BaseRecycleAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public BaseRecycleAdapter(Context context, List<T> datas, RecyclerView.LayoutManager layoutManager) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        this.mLayoutManager = layoutManager;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderViewTypes.contains(viewType)) {
            return new HeadHolder(mHeadViews.get(viewType / 100000));
        }

        if (mFooterViewTypes.contains(viewType)) {
            int index = viewType / 100000 - getAdvanceItemCount() - mHeadViews.size();
            return new FootHolder(mFootViews.get(index));
        }

        return onCreateAdvanceViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {

        if (mFootViews.size() > 0 && position > getAdvanceItemCount() - 1 + mHeadViews.size()) {
            return;
        }

        if (mHeadViews.size() > 0) {
            if (position < mHeadViews.size()) {
                return;
            }
            onBindAdvanceViewHolder(holder, position - mHeadViews.size());
            return;
        }


        onBindAdvanceViewHolder(holder, position);

    }

    class HeadHolder extends RecycleViewHolder {

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    class FootHolder extends RecycleViewHolder {
        public FootHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * 设置适配器的数据
     */
    public void setDataSource(List<T> dataList) {
        mDatas.clear();
        if (dataList != null) {
            mDatas.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public ArrayList<T> getDatas() {
        return (ArrayList<T>) mDatas;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 添加头视图
     *
     * @param headerView
     */

    public void addHeaderView(View headerView) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeadViews.add(headerView);
    }

    /**
     * 清除除头部视图
     */
    public void removeHeaderView() {
        mHeadViews.clear();
    }


    /**
     * 添加底部视图
     *
     * @param footerView
     */

    public void addFooterView(View footerView) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFootViews.add(footerView);
    }

    /**
     * 清除底部视图
     */

    public void removeFooterView() {
        mFootViews.clear();
    }

    public int getHeadViewCount() {
        return mHeadViews.size();
    }

    public int getFootViewCount() {
        return mFootViews.size();
    }


    @Override
    public int getItemCount() {
        return getAdvanceItemCount();
    }

    private int getAdvanceItemCount() {
        return mDatas != null&&mDatas.size() != 0 ? mDatas.size() : 0;
    }


    private RecycleViewHolder onCreateAdvanceViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getResourceView(viewType), parent, false);
        return new RecycleViewHolder(view, this);

    }


    private void onBindAdvanceViewHolder(RecycleViewHolder holder, int position) {
        getItemView(holder, position, mDatas.get(position));
    }

    @Override
    public int getItemViewType(int position) {

        if (mHeadViews.size() > 0 && position < mHeadViews.size()) {
            //用position作为HeaderView 的   ViewType标记
            //记录每个ViewType标记
            mHeaderViewTypes.add(position * 100000);
            return position * 100000;
        }

        if (mFootViews.size() > 0 && position > getAdvanceItemCount() - 1 + mHeadViews.size()) {
            mFooterViewTypes.add(position * 100000);
            return position * 100000;
        }
        if (mHeadViews.size() > 0) {
            return getAdvanceItemViewType(position - mHeadViews.size());
        }
        return getAdvanceItemViewType(position);

    }

    /**
     * Item页布局类型个数，默认为1
     */
    private int getAdvanceItemViewType(int position) {
        return 1;
    }

    public abstract int getResourceView(int resource);

    public abstract void getItemView(RecycleViewHolder viewHolder, int position, T item);

    /**
     * 设置items数据
     */
    public void setItems(List<T> mDatas) {
        if (!CollectionUtil.isEmpty(mDatas)) {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }
    }

    /**
     * 添加items数据
     */
    public void addItems(List<T> mDatas) {
        if (!CollectionUtil.isEmpty(mDatas)) {
            this.mDatas.addAll(mDatas);
            notifyDataSetChanged();
        }
    }

    /**
     * 清除items数据
     */
    public void cleanItems() {
        mDatas.clear();
        notifyDataSetChanged();
    }


    public interface onItemClickListener {
        void setOnItemClick(View view, int position);
    }

    private onItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        LUtils.toast("44444");
        mOnItemClickListener = onItemClickListener;
    }

    public onItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public interface onItemLongClickListener {
        void setOnLongItemClicck(View view, int position);
    }

    private onItemLongClickListener mOnItemLongClickListener;

    public onItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
}

