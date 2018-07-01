package com.joint.jointpolice.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseFragment;
import com.joint.jointpolice.widget.tablayout.SlidingTabLayout;
import com.joint.jointpolice.widget.tablayout.listener.OnTabSelectListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/4 15:05
 * @描述: 消息主页
 */
public class MessageFragment extends BaseFragment {


    @BindView(R.id.tablayout_msg)
    SlidingTabLayout mTablayoutMsg;
    @BindView(R.id.viewPager_msg)
    ViewPager mViewPagerMsg;
    Unbinder unbinder;
    private MsgViewPagerAdapter mAdapter;

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_main_message;
    }

    @Override
    public void initView() {
        mAdapter = new MsgViewPagerAdapter(getChildFragmentManager());
        mViewPagerMsg.setAdapter(mAdapter);
        mTablayoutMsg.setViewPager(mViewPagerMsg);
        mTablayoutMsg.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPagerMsg.setCurrentItem(position);
            }

            @Override
            public void onTabDoubleClick(int position) {

            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {

    }

    private class MsgViewPagerAdapter extends FragmentStatePagerAdapter {
        Map<Integer, Fragment> mFragmentMaps = new HashMap<>();

        public MsgViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (!mFragmentMaps.containsKey(position)) {
               String clss= MESSAGE.fromIndex(position).mClass.getName();
                mFragmentMaps.put(position, Fragment.instantiate(getActivity(), clss));
            }
            return mFragmentMaps.get(position);
        }

        @Override
        public int getCount() {
            return MESSAGE.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MESSAGE.fromIndex(position).mtabTitle;
        }
    }

    private enum MESSAGE {

        NOTIFICATION(0, "通知", MsgNotificationFragment.class),
        NOTICE(1, "公告", MsgNoticeFragment.class),
        HELP(2, "求助", MsgHelpFragment.class),
        VIOLATOR(3, "违规", MsgViolatorFragment.class);

        public final int mtabIndex;
        private final String mtabTitle;
        private final Class<?> mClass;

        MESSAGE(int tabIndex, String tabTitle, Class<?> clss) {
            this.mtabIndex = tabIndex;
            this.mtabTitle = tabTitle;
            this.mClass = clss;
        }

        public static MESSAGE fromIndex(int index) {
            for (MESSAGE message : MESSAGE.values()) {
                if (message.mtabIndex == index) {
                    return message;
                }
            }
            return NOTIFICATION;
        }
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
