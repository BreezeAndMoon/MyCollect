package com.joint.jointpolice.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hitown.sdk.data.CoreData;
import com.joint.jointpolice.R;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.fragment.HomeFragment;
import com.joint.jointpolice.fragment.MessageFragment;
import com.joint.jointpolice.fragment.MyFragment;
import com.joint.jointpolice.fragment.NoSignFragment;
import com.joint.jointpolice.fragment.PoliceFragment;
import com.joint.jointpolice.util.LUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    /**
     * 首页
     */
    private static final int TAB_HOME = 0;
    /**
     * 警情
     */
    private static final int TAB_POLICE = 1;
    /**
     * 消息
     */
    private static final int TAB_MSG = 2;
    /**
     * 我的
     */
    private static final int TAB_MY = 3;
    @BindView(R.id.main_home_rb)
    RadioButton mMainHomeRb;
    @BindView(R.id.main_police_rb)
    RadioButton mMainPoliceRb;
    @BindView(R.id.main_msg_rb)
    RadioButton mMainMsgRb;
    @BindView(R.id.main_my_rb)
    RadioButton mMainMyRb;
    @BindView(R.id.main_radio_group)
    RadioGroup mMainRadioGroup;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;

    private Fragment mFragment = null;
    private FragmentTransaction mTransaction;

    private HomeFragment mHomeFragment;
    private PoliceFragment mPoliceFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        mMainRadioGroup.setOnCheckedChangeListener(this);
        mMainHomeRb.setChecked(true);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        switch (checkId) {
            case R.id.main_home_rb:
                replaceView(TAB_HOME);
                break;
            case R.id.main_police_rb:
                replaceView(TAB_POLICE);
                break;
            case R.id.main_msg_rb:
                replaceView(TAB_MSG);
                break;
            case R.id.main_my_rb:
                replaceView(TAB_MY);
                break;

        }
    }



    /**
     * 切换视图
     */
    private synchronized void replaceView(int curItem) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        if (mFragment != null) {
            mTransaction.hide(mFragment);
        }

        switch (curItem) {
            case TAB_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mTransaction.add(R.id.frameLayout, mHomeFragment);
                }
                mFragment = mHomeFragment;
                break;
            case TAB_POLICE:
                if (mPoliceFragment == null) {
                    mPoliceFragment = new PoliceFragment();
                    mTransaction.add(R.id.frameLayout, mPoliceFragment);
                }
                mFragment = mPoliceFragment;
                break;
            case TAB_MSG:
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    mTransaction.add(R.id.frameLayout, mMessageFragment);
                }
                mFragment = mMessageFragment;
                break;
            case TAB_MY:
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    mTransaction.add(R.id.frameLayout, mMyFragment);
                }
                mFragment = mMyFragment;
                break;
        }
        mTransaction.show(mFragment);
        mTransaction.commitAllowingStateLoss();

    }

    private long time;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time < 2000) {
            super.onBackPressed();
        } else {
            LUtils.toast("再按一次,退出程序");
            time = System.currentTimeMillis();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivePolice(Integer policeType) {
        if (policeType != 0) {
            mMainPoliceRb.setChecked(true);
            replaceView(TAB_POLICE);
        }
    }


}
