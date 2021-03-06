package com.joint.jointpolice.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.collect.AddressActivity;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.activity.collect.CollectHistoryActivity;
import com.joint.jointpolice.activity.collect.CollectUnitActivity;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.services.UploadLocationService;
import com.joint.jointpolice.util.DensityUtil;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.SpUtil;
import com.joint.jointpolice.widget.dialog.MyCustomDialog;
import com.squareup.picasso.Picasso;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.apache.http.client.HttpClient;
import org.w3c.dom.Text;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main2Activity extends BaseActivity implements View.OnClickListener {
    List<String> mImages = new ArrayList();
    List<String> mTitles = new ArrayList();
    TextView mSumTextView;
    TextView mPersonSumTextView;
    TextView mUnitSumTextView;
    TextView mHouseSumTextView;
    boolean mIsPause;
    long time;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;

    private void initBanner() {
        Banner banner = findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Uri uri = Uri.parse((String) path);
                Picasso.get().load(uri).into(imageView);
            }
        });
        banner.setBannerAnimation(Transformer.DepthPage).setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true).setDelayTime(2000);
        banner.setImages(mImages).setBannerTitles(mTitles);

//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                try {
//                    LUtils.toast("点击了" + String.valueOf(position));
//                } catch (Exception ex) {
//                    LUtils.log(ex.getMessage());
//                }
//            }
//        });
        banner.start();
    }

    private void initMarqueeView() {
        MarqueeView marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        List<String> info = new ArrayList<>();
        info.add("示例广告1");
        info.add("示例广告2");
        info.add("示例广告3");
        marqueeView.startWithList(info);
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                LUtils.toast(String.valueOf(position));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void initView() {
//        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("首页");
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setCheckedItem(R.id.nav_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        findViewById(R.id.tv_house_sum).setOnClickListener(this);
        findViewById(R.id.tv_unit_sum).setOnClickListener(this);
        findViewById(R.id.tv_person_sum).setOnClickListener(this);
        findViewById(R.id.tv_house).setOnClickListener(this);
        findViewById(R.id.tv_person).setOnClickListener(this);
        findViewById(R.id.tv_unit).setOnClickListener(this);
        findViewById(R.id.tv_more).setOnClickListener(this);
//        resizeTvDrawable(R.id.tv_person, 128);
//
//        resizeTvDrawable(R.id.tv_unit, 128);
//        resizeTvDrawable(R.id.tv_house, 128);
//        resizeTvDrawable(R.id.tv_doorplate, 128);
////        resizeTvDrawable(R.id.tv_main, 64);
////        resizeTvDrawable(R.id.tv_my, 64);
//todo 调整大小
        float density = getResources().getDisplayMetrics().density;
        resizeTvDrawable(R.id.tv_house_sum, (int)(32*density));
        resizeTvDrawable(R.id.tv_person_sum, (int)(32*density));
        resizeTvDrawable(R.id.tv_unit_sum, (int)(32*density));

        initMarqueeView();
        initBanner();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        mSumTextView = findViewById(R.id.tv_collect_sum);
        mPersonSumTextView = findViewById(R.id.tv_person_sum);
        mUnitSumTextView = findViewById(R.id.tv_unit_sum);
        mHouseSumTextView = findViewById(R.id.tv_house_sum);
        mTitles.add("标题1");
        mTitles.add("标题2");
        mTitles.add("标题3");
        mImages.add("https://upload-images.jianshu.io/upload_images/589909-d046f5ca2abbd31e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/640");
        mImages.add("https://upload-images.jianshu.io/upload_images/589909-fad4a3da8703501c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/640");
        mImages.add("https://upload-images.jianshu.io/upload_images/589909-da8eaee55c62a4dd.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/640");
        try {
            initCollectCount();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent serviceIntent = new Intent(this, UploadLocationService.class);
        startService(serviceIntent);
    }

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
    public void onClick(View v) {
        Intent collectHistoryIntent = new Intent(this, CollectHistoryActivity.class);
        switch (v.getId()) {
//            case R.id.tv_my:
//                startActivity(new Intent(this, MyActivity.class));
//                break;
            case R.id.tv_house_sum:
                collectHistoryIntent.putExtra(Constant.CollectType,Constant.Collect_House);
                startActivity(collectHistoryIntent);
                break;
            case R.id.tv_unit_sum:
                collectHistoryIntent.putExtra(Constant.CollectType,Constant.Collect_Unit);
                startActivity(collectHistoryIntent);
                break;
            case R.id.tv_person_sum:
                collectHistoryIntent.putExtra(Constant.CollectType,Constant.Collect_Person);
                startActivity(collectHistoryIntent);
                break;
            case R.id.tv_house:
                Intent houseIntent = new Intent(this, AddressActivity.class);
                houseIntent.putExtra("CollectType", Constant.Collect_House);
                startActivity(houseIntent);
                break;
            case R.id.tv_person:
                Intent personIntent = new Intent(this, AddressActivity.class);
                personIntent.putExtra("CollectType", Constant.Collect_Person);
                startActivity(personIntent);
                break;
            case R.id.tv_unit:
                Intent unitIntent = new Intent(this, AddressActivity.class);
                unitIntent.putExtra("CollectType", Constant.Collect_Unit);
                startActivity(unitIntent);
                break;
            case R.id.tv_more:
                LUtils.toast("待添加");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsPause) {
            mIsPause = false;
            try {
                initCollectCount();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void resizeTvDrawable(int id, int size) {
        TextView textView = findViewById(id);
        Drawable[] drawables = textView.getCompoundDrawables();
        drawables[1].setBounds(0, 0, size, size);
        textView.setCompoundDrawables(null, drawables[1], null, null);
    }

    private void initCollectCount() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr = simpleDateFormat.format(new Date());
        String lastModifyDateStr = (String) SpUtil.get(this, Constant.LAST_MODIFYDATE, nowDateStr);
        Date nowDate = simpleDateFormat.parse(nowDateStr);
        Date lastModifyDate = simpleDateFormat.parse(lastModifyDateStr);
        if (nowDate.compareTo(lastModifyDate) != 0)//时间不相等
            SpUtil.clear(this);
        int houseSum = (int) SpUtil.get(this, Constant.Collect_House, 0);
        int unitSum = (int) SpUtil.get(this, Constant.Collect_Unit, 0);
        int personSum = (int) SpUtil.get(this, Constant.Collect_Person, 0);
        int totalSum = houseSum + unitSum + personSum;
        mSumTextView.setText(getResources().getString(R.string.text_collect_sum) + totalSum);
        mPersonSumTextView.setText(String.valueOf(personSum));
        mUnitSumTextView.setText(String.valueOf(unitSum));
        mHouseSumTextView.setText(String.valueOf(houseSum));
    }
}

