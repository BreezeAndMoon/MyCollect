package com.joint.jointpolice.activity.collect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.acker.simplezxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.joint.jointpolice.R;
import com.joint.jointpolice.adapter.AddressAdapter;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.common.ParameterizedTypeImpl;
import com.joint.jointpolice.constants.Constant;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.Enterprise;
import com.joint.jointpolice.model.CollectModels.Flat;
import com.joint.jointpolice.model.CollectModels.FlatParameter;
import com.joint.jointpolice.model.CollectModels.Person;
import com.joint.jointpolice.model.CollectModels.QueryResult;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joint229 on 2018/5/4.
 */

public class AddressActivity extends BaseActivity implements OnLoadmoreListener, OnRefreshListener, View.OnClickListener {
    AddressAdapter mAddressAdapter;
    List<Flat> mFlats = new ArrayList<>();
    RecyclerView mRecyclerView;
   // EditText mSearchAutoComplete;

    SmartRefreshLayout mSmartRefresh;
    boolean isSearched = false;
    int mPageIndex = 1;
    int mPageSize = 20;
    private static final int REQ_CODE_PERMISSION = 0x1111;
    Gson mGson = new Gson();
    boolean mIsPause;
    int mSearchType;
    boolean mIsQueryFlat;
    Drawable mPhotoDrawable;
    SearchView.SearchAutoComplete mSearchAutoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_address);
         mPhotoDrawable = getResources().getDrawable(R.drawable.ic_photo);
    }

    @Override
    protected void initView() {
        setToolbarTitle("地址");
        mSmartRefresh = findViewById(R.id.smart_refresh);
        mSmartRefresh.setOnLoadmoreListener(this);
        mSmartRefresh.setOnRefreshListener(this);
//        findViewById(R.id.tv_scan).setOnClickListener(this);
//        findViewById(R.id.btn_search).setOnClickListener(this);
//        mSearchEditText = findViewById(R.id.edt_search);
        String collectType = getIntent().getStringExtra("CollectType");
//        if (Constant.Collect_Person.equals(collectType))
//            mSearchEditText.setHint("请输入地址或人名");
//        if (Constant.Collect_Unit.equals(collectType)) {
//            mSearchEditText.setHint("请输入地址或单位名");
//        }
        if (Constant.Collect_Unit.equals(null))
            LUtils.toast("test");
//        mSearchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                search();
//            }
//        });
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {
        String collectType = getIntent().getStringExtra(Constant.CollectType);
        if (Constant.Collect_House.equals(collectType))
            mSearchType = 3;//只查Flat地址
        else
            mSearchType = Constant.Collect_Person.equals(collectType) ? 1 : 2;
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
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                List<Flat> list = getResultList(response, Flat.class);
                mFlats = list;
                mAddressAdapter = new AddressAdapter(AddressActivity.this);
                mAddressAdapter.setDataSource(mFlats);
                mRecyclerView = findViewById(R.id.recy_address);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
                mRecyclerView.setAdapter(mAddressAdapter);
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
//                    mAddressAdapter = new AddressAdapter(AddressActivity.this);
//                    mAddressAdapter.setDataSource(mNBuildingList);
//                } catch (Exception ex) {
//                    LUtils.log(ex.getMessage());
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRecyclerView = findViewById(R.id.recy_address);
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
//                        mRecyclerView.setAdapter(mAddressAdapter);
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
        if (!isSearched || TextUtils.isEmpty(mSearchAutoComplete.getText().toString()))
            jsonStr = buildData();
        else if (mIsQueryFlat)
            jsonStr = buildData(mSearchAutoComplete.getText().toString(), mPageIndex);
        else {
            LUtils.toast("请先清空条件再加载");
            mSmartRefresh.finishLoadmore(0, false);
            return;
        }
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                List<Flat> list = getResultList(response, Flat.class);
                mAddressAdapter.addItems(list);
                int positionStart = mAddressAdapter.getItemCount();
                mAddressAdapter.notifyItemRangeInserted(positionStart, list.size());
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
        if (!isSearched || TextUtils.isEmpty(mSearchAutoComplete.getText().toString()))//不管是否属于查询,字符串为空或者""时限制只查Flat地址
            jsonStr = buildData();
        else if (mIsQueryFlat)
            jsonStr = buildData(mSearchAutoComplete.getText().toString(), 1);//每次刷新显示第一页
        else {
            LUtils.toast("请先清空条件再刷新");
            mSmartRefresh.finishRefresh(0, false);
            return;//如果查询的结果不是Flat地址，则不刷新
        }
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                List<Flat> list = getResultList(response, Flat.class);
                mAddressAdapter.setDataSource(list);
                //mAddressAdapter.notifyDataSetChanged();
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
        param.setSearchType(3);//初始化地址列表统一都是3
        String jsonStr = mGson.toJson(param);
        return jsonStr;
    }

    private String buildData(String searchInfo, int pageIndex) {
        FlatParameter param = new FlatParameter();
        param.setPageIndex(pageIndex);
        param.setPageSize(mPageSize);
        param.setSearchInfo(searchInfo);
        param.setSearchType(mSearchType);
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
                                    String address = elements.get(0).text();//todo 关键字搜索地址
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

    }

    private void search() {
        isSearched = true;
        String jsonStr;
        if (TextUtils.isEmpty(mSearchAutoComplete.getText().toString()))//查询条件为空或""则只查Flat地址
            jsonStr = buildData();
        else
            jsonStr = buildData(mSearchAutoComplete.getText().toString(), 1);
        queryAddress(jsonStr);
    }

    private void updateItem() {
        int position = mAddressAdapter.mSelectedPosition;
        int flatID = (int) mAddressAdapter.getDatas().get(position).getId();
        String jsonStr = buildData(flatID);
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_byid_url), jsonStr, new OkHttpClientManager.ResultCallback<Flat>() {
            @Override
            public void onResponse(Flat response) {
                mAddressAdapter.getDatas().set(position, response);
                mAddressAdapter.notifyItemChanged(position);
            }
        });
    }

    private void queryAddress(String jsonStr) {
        OkHttpClientManager.postAsyn(getResources().getString(R.string.get_flat_list_url), jsonStr, new OkHttpClientManager.ResultCallback<String>() {
            //            @Override
//            public void onResponse(List<Flat> response) {
//                if (response == null || response.size() <= 0)
//                    LUtils.toast("不存在该地址");
//                else {
//                    mAddressAdapter.setDataSource(response);
//
//                }
//            }
            @Override
            public void onResponse(String responseJson) {
                int type = getSearchType(responseJson);
                Intent intent = new Intent();
                switch (type) {
                    case 1://人员
                        mIsQueryFlat = false;
                        List<Person> personList = getResultList(responseJson, Person.class);
                        intent.setClass(AddressActivity.this, ActualPersonActivity.class);
                        intent.putExtra(Constant.SEARCH_RESULT, (Serializable) personList);
                        startActivity(intent);
                        break;
                    case 2://单位
                        mIsQueryFlat = false;
                        List<Enterprise> enterpriseList = getResultList(responseJson, Enterprise.class);
                        intent.setClass(AddressActivity.this, ActualUnitActivity.class);
                        intent.putExtra(Constant.SEARCH_RESULT, (Serializable) enterpriseList);
                        startActivity(intent);
                        break;
                    case 3://Flat
                        mIsQueryFlat = true;
                        List<Flat> flatList = getResultList(responseJson, Flat.class);
                        mAddressAdapter.setDataSource(flatList);
                        if (flatList.size() <= 0)
                            LUtils.toast("无相关地址");
                        break;
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

    private <T> List<T> getResultList(String jsonStr, Class<T> clazz) {
        jsonStr = StringUtil.formatJsonString(jsonStr);
        Type type = new ParameterizedTypeImpl(QueryResult.class, new Type[]{clazz});
        QueryResult queryResult = mGson.fromJson(jsonStr, type);
        List<T> list = queryResult.getData();
        return list;
    }

    private int getSearchType(String responseJson) {
        responseJson = StringUtil.formatJsonString(responseJson);
        JsonObject jsonObject = new JsonParser().parse(responseJson).getAsJsonObject();
        int searchType = jsonObject.get("flatType").getAsInt();
        return searchType;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()){
            case R.id.menu_item_scan:
                if (ContextCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(AddressActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.address_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_searchview);
        SearchView searchView = (SearchView) menuItem.getActionView();
         mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
//            f.set(searchAutoComplete,R.drawable.shape_cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setQueryHint("请输入地址");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search();
                //LUtils.toast("按下搜索了");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
