package com.joint.jointpolice.common;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;

import com.joint.jointpolice.util.LUtils;


/**
 * @作者: ChenJunHui
 * @日期: 2018/1/17 13:24
 * @描述:
 */

public class RecycleViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private BaseRecycleAdapter mAdapter;

    public RecycleViewHolder(View itemView) {
        super(itemView);
        this.mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public <T> RecycleViewHolder(final View itemView, final BaseRecycleAdapter<T> adapter) {
        super(itemView);
        this.mConvertView = itemView;
        mViews = new SparseArray<>();
        LUtils.log("getOnItemClickListener---","-===="+adapter.getOnItemClickListener()+"======");
        if (adapter.getOnItemClickListener() != null) {
            adapter.setOnItemClickListener(new BaseRecycleAdapter.onItemClickListener() {
                @Override
                public void setOnItemClick(View view, int position) {
                    LUtils.log(position+"RecycleViewHolder");
                    adapter.getOnItemClickListener().setOnItemClick(view,getAdapterPosition()-adapter.getHeadViewCount());
                }
            });
        }

        if (adapter.getOnItemLongClickListener() != null) {
            adapter.setOnItemLongClickListener(new BaseRecycleAdapter.onItemLongClickListener() {
                @Override
                public void setOnLongItemClicck(View view, int position) {
                    adapter.getOnItemLongClickListener().setOnLongItemClicck(view,getAdapterPosition()-adapter.getHeadViewCount());
                }
            });
        }

        this.mAdapter = adapter;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件ID获取对应的view,如果没有加入views
     *
     * @param viewId 组件id
     * @return T 当前组件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        try {
            return (T) view;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

}
