package com.joint.jointpolice.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.view.TimePickerView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.BaseRecycleAdapter;
import com.joint.jointpolice.common.BaseTimePickerView;
import com.joint.jointpolice.common.RecycleViewHolder;
import com.joint.jointpolice.model.PoliceSort;
import com.joint.jointpolice.util.LUtils;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/9 14:39
 * @描述: 现场反馈
 */

public class StartFeedBackActivity extends BaseActivity {
    @BindView(R.id.toolbar_tv_title)
    TextView mToolbarTvTitle;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.tv_start)
    TextView mTvStartTime;
    @BindView(R.id.tv_arrive)
    TextView mTvArriveTime;
    @BindView(R.id.rlv_description)
    RecyclerView mRecyclerViewDescription;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.edt_description)
    EditText mEdtDescription;
    BaseDataAdapter mBaseDataAdapter;
    private TimePickerView mTimePickerView;
    List<PoliceSort> mSortList;

    //    private TimePickerView mArriveTimePickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_start_feedback);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        mToolbarTvTitle.setText("到场反馈");
        mBaseDataAdapter = new BaseDataAdapter(this);
        mRecyclerViewDescription.setAdapter(mBaseDataAdapter);
        mRecyclerViewDescription.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerViewDescription.addItemDecoration(new GridSpacingItemDecoration(3, 25, true));
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mSortList = new ArrayList<PoliceSort>();
        for (int i = 0; i < 5; i++) {
            PoliceSort policeSort = new PoliceSort();
            policeSort.setSort("已到场");
            mSortList.add(policeSort);
        }
        mBaseDataAdapter.setDataSource(mSortList);
    }

    @OnClick(R.id.btn_submit)
    public void submit() {

    }

    @OnClick(R.id.tv_start)
    public void onStartClicked() {
        BaseTimePickerView.initTimePicker(this);
        BaseTimePickerView.setOnTimeSelect(new BaseTimePickerView.OnTimeSelect() {
            @Override
            public void setCompletText(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mTvStartTime.setText(format.format(date));
            }
        });
    }

    @OnClick(R.id.tv_arrive)
    public void onArriveClicked() {
        BaseTimePickerView.initTimePicker(this);
        BaseTimePickerView.setOnTimeSelect(new BaseTimePickerView.OnTimeSelect() {
            @Override
            public void setCompletText(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mTvArriveTime.setText(format.format(date));
            }
        });

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
            final TextView tvSelected = viewHolder.getView(R.id.tv_police_sort);
            tvSelected.setText(mSortList.get(position).getSort());
            final View itemView = viewHolder.getConvertView();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEdtDescription.setText(tvSelected.getText());
                }
            });
        }
    }

}
