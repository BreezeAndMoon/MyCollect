package com.joint.jointpolice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.SearchPoliceActivity;
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
 * @描述: 警情主页
 */

public class PoliceFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    Unbinder unbinder;
    PoliceViewPagerAdapter mPoliceViewPagerAdapter;
    @BindView(R.id.rl_search_police)
    RelativeLayout mRlSearchPolice;
    @BindView(R.id.ll_search_police)
    LinearLayout mLlSearchPolice;

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_main_police;
    }

    @Override
    public void initView() {
        mPoliceViewPagerAdapter = new PoliceViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPoliceViewPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabDoubleClick(int position) {

            }
        });
        mLlSearchPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchPoliceActivity.class));
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {

    }

    public class PoliceViewPagerAdapter extends FragmentStatePagerAdapter {

        private Map<Integer, Fragment> fragmentMap = new HashMap<>();

        public PoliceViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            String className = POLICE.fromIndex(position).mClass.getName();
            if (!fragmentMap.containsKey(position)) {
                fragmentMap.put(position, Fragment.instantiate(getActivity(), className));
            }

            return fragmentMap.get(position);
        }

        @Override
        public int getCount() {
            return POLICE.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return POLICE.fromIndex(position).mTitle;
        }
    }

    private enum POLICE {
        NOSIGIN(0, "未签警情", NoSignFragment.class),
        POLICELIST(1, "警情列表", PoliceListFragment.class),
        TODAYPOLICE(2, "今日警情", TodayPoliceFragment.class),
        YESTERDAYPOLICE(3, "昨日警情", YesterdayPoliceFragment.class);
        public final int mIndex;
        public final String mTitle;
        public final Class<?> mClass;

        POLICE(int index, String title, Class<?> mClass) {
            this.mIndex = index;
            this.mTitle = title;
            this.mClass = mClass;
        }

        public static POLICE fromIndex(int index) {
            for (POLICE police : POLICE.values()) {
                if (police.mIndex == index) {
                    return police;
                }
            }
            return NOSIGIN;
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
