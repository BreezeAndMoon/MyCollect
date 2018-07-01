package com.joint.jointpolice.activity.collect;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;

import com.acker.simplezxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.HousePersonAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.ActivityRequestCode;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.FlatParameter;
import com.joint.jointpolice.model.CollectModels.NBuilding;
import com.joint.jointpolice.util.LUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Joint229 on 2018/5/4.
 */

public class AddressActivity extends BaseActivity implements OnLoadmoreListener, OnRefreshListener, View.OnClickListener {
    HousePersonAdapter mHousePersonAdapter;
    List<Flat> mFlats = new ArrayList<>();
    RecyclerView mRecyclerView;
    EditText mSearchEditText;
    SmartRefreshLayout mSmartRefresh;
    boolean isSearched = false;
    int mPageIndex = 1;
    int mPageSize = 20;
    private static final int REQ_CODE_PERMISSION = 0x1111;
    Gson mGson = new Gson();
    boolean mIsPause;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_address);
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("地址");
        mSmartRefresh = findViewById(R.id.smart_refresh);
        mSmartRefresh.setOnLoadmoreListener(this);
        mSmartRefresh.setOnRefreshListener(this);
        findViewById(R.id.tv_scan).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mSearchEditText = findViewById(R.id.edt_search);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        bindRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsPause) {
            mIsPause = false;
            updateItem();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPause = true;
    }

    private void bindRecycleView() {
        String jsonStr = buildData();
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Flat>>() {
            @Override
            public void onResponse(List<Flat> response) {
                mFlats = response;
                mHousePersonAdapter = new HousePersonAdapter(AddressActivity.this);
                mHousePersonAdapter.setDataSource(mFlats);
                mRecyclerView = findViewById(R.id.recy_address);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
                mRecyclerView.setAdapter(mHousePersonAdapter);
            }

            public void onBefore() {
                showDialogProgress();
            }

            public void onAfter() {
                dismissDialogProgress();
            }
        });

//        OkHttpClient client = new OkHttpClient();
//        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS);
//        //MediaType JSON = MediaType.parse(HttpConstant.CONTENTTYPE_JSON);
//        Request request = new Request.Builder()
//                .url(getResources().getString(R.string.get_nubilding_list_url))
//                .post(Util.EMPTY_REQUEST)
//                .build();
//        showDialogProgress();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                if (e instanceof SocketTimeoutException) {
//                    LUtils.toast("连接超时");
//                }
//                if (e instanceof ConnectException) {
//                    LUtils.toast("连接异常");
//                } else {
//                    LUtils.toast("请求失败");
//                }
//                LUtils.log(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String jsonArray = response.body().string();
//                    Gson gson = new Gson();
//                    mNBuildingList = gson.fromJson(jsonArray, new TypeToken<List<NBuilding>>() {
//                    }.getType());
//                    mHousePersonAdapter = new HousePersonAdapter(AddressActivity.this);
//                    mHousePersonAdapter.setDataSource(mNBuildingList);
//                } catch (Exception ex) {
//                    LUtils.log(ex.getMessage());
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRecyclerView = findViewById(R.id.recy_address);
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
//                        mRecyclerView.setAdapter(mHousePersonAdapter);
//                        dismissDialogProgress();
//                    }
//                });
//            }
//        });

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPageIndex++;
        String jsonStr;
        if (!isSearched)
            jsonStr = buildData();
        else
            jsonStr = buildData(mSearchEditText.getText().toString(), mPageIndex);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Flat>>() {
            @Override
            public void onResponse(List<Flat> response) {
                mHousePersonAdapter.addItems(response);
                int positionStart = mHousePersonAdapter.getItemCount();
                mHousePersonAdapter.notifyItemRangeInserted(positionStart, response.size());
            }

            public void onAfter() {
                mSmartRefresh.finishLoadmore(0, true);

            }
        });
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPageIndex = 1;
        String jsonStr;
        if (!isSearched)
            jsonStr = buildData();
        else
            jsonStr = buildData(mSearchEditText.getText().toString(), 1);//每次刷新显示第一页
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Flat>>() {
            @Override
            public void onResponse(List<Flat> response) {
                mHousePersonAdapter.cleanItems();
                mHousePersonAdapter.addItems(response);
                //int positionStart = mHousePersonAdapter.getItemCount();
                //mHousePersonAdapter.notifyItemRangeInserted(positionStart, response.size());
                mHousePersonAdapter.notifyDataSetChanged();
            }

            public void onAfter() {
                mSmartRefresh.finishRefresh(0, true);
            }
        });
    }

    private String buildData() {
        FlatParameter param = new FlatParameter();
        param.setPageIndex(mPageIndex);
        param.setPageSize(mPageSize);
        String jsonStr = mGson.toJson(param);
        return jsonStr;
    }

    private String buildData(String searchInfo, int pageIndex) {
        FlatParameter param = new FlatParameter();
        param.setPageIndex(pageIndex);
        param.setPageSize(mPageSize);
        param.setSearchInfo(searchInfo);
        String jsonStr = mGson.toJson(param);
        return jsonStr;
    }

    private String buildData(int flatID) {
        FlatParameter param = new FlatParameter();
        param.setPageIndex(1);
        param.setPageSize(1);
        param.setID(flatID);
        String jsonStr = mGson.toJson(param);
        return jsonStr;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        String result = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Document document = Jsoup.connect(result).get();
                                    Elements elements = document.select("div.dzksfd1_lz2");//http://www.fjadd.com/shhyy/addr_list.jsp?systemid=25CE7FE2-8A7B-2437-E054-90E2BA510A0C
                                    String address = elements.get(0).text();
                                    String jsonStr = buildData(address, 1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            queryAddress(jsonStr);
                                        }
                                    });
                                } catch (IOException e) {
                                    LUtils.log("Jsoup Exception" + e.getMessage());
                                }
                            }
                        }).start();
                        //tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            LUtils.toast(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            // tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_scan:
                if (ContextCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(AddressActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }
                break;
            case R.id.btn_search:
                isSearched = true;
                String jsonStr = buildData(mSearchEditText.getText().toString(), 1);
                queryAddress(jsonStr);
                break;
        }

    }

    private void updateItem() {
        int position = mHousePersonAdapter.mSelectedPosition;
        int flatID = (int) mHousePersonAdapter.getDatas().get(position).getId();
        String jsonStr = buildData(flatID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_byid_url), jsonStr, new OkHttpClientManager.ResultCallback<Flat>() {
            @Override
            public void onResponse(Flat response) {
                mHousePersonAdapter.getDatas().set(position, response);
                mHousePersonAdapter.notifyItemChanged(position);
            }
        });
    }

    private void queryAddress(String jsonStr) {
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<List<Flat>>() {
            @Override
            public void onResponse(List<Flat> response) {
                if (response == null || response.size() <= 0)
                    LUtils.toast("不存在该地址");
                else {
                    mHousePersonAdapter.cleanItems();
                    mHousePersonAdapter.setDataSource(response);
                    mHousePersonAdapter.notifyDataSetChanged();
                }
            }

            public void onBefore() {
                showDialogProgress();
            }

            public void onAfter() {
                dismissDialogProgress();
            }
        });
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

}
