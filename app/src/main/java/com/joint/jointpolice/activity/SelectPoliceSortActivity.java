package com.joint.jointpolice.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.PoliceSort;
import com.joint.jointpolice.util.LUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:44
 * @描述: 选择警情类别
 */

public class SelectPoliceSortActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.rlv_police_sort)
    RecyclerView mRecycleView;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    List<PoliceSort> mSortList;
    BaseDataAdapter mBaseDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_select_police_sort);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("选择警情类别");
        mBaseDataAdapter = new BaseDataAdapter(this);
        mRecycleView.setAdapter(mBaseDataAdapter);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycleView.addItemDecoration(new GridSpacingItemDecoration(3, 25, true));
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mSortList = new ArrayList<PoliceSort>();
        for (int i = 0; i < 20; i++) {
            PoliceSort policeSort = new PoliceSort();
            policeSort.setSort("交通事故");
            mSortList.add(policeSort);
        }
        mBaseDataAdapter.setDataSource(mSortList);
    }

     class BaseDataAdapter extends BaseRecycleAdapter<PoliceSort> {
        public BaseDataAdapter(Context context) {
            super(context);
        }

        @Override
        public int getResourceView(int resource) {
            return R.layout.item_police_sort_list;
        }

        @Override
        public void getItemView(RecycleViewHolder viewHolder, int position, PoliceSort item) {
            final TextView tv = viewHolder.getView(R.id.tv_police_sort);
            tv.setText(mSortList.get(position).getSort());
            final View itemView = viewHolder.getConvertView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.isEnabled()) {
                        tv.setEnabled(false);
                    } else {
                        tv.setEnabled(true);
                    }
                    LUtils.toast(((TextView) itemView.findViewById(R.id.tv_police_sort)).getText());
                }
            });
        }
    }



}


