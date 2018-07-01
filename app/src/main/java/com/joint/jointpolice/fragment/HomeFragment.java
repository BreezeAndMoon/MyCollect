package com.joint.jointpolice.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.joint.jointpolice.R;
import com.joint.jointpolice.activity.ReceiveAlarmActivity;
import com.joint.jointpolice.activity.collect.ActualUnitActivity;
import com.joint.jointpolice.activity.collect.CollectBuildingActivity;
import com.joint.jointpolice.common.BaseFragment;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.Meters2Degrees;
import com.joint.jointpolice.widget.dialog.CustomDialog;
import com.joint.jointpolice.widget.dialog.CustomDialogInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/4 15:04
 * @描述: 首页
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.edt_search_place)
    EditText mEdtSearchPlace;
    @BindView(R.id.img_location)
    ImageView mImgLocation;
    @BindView(R.id.tv_help)
    TextView mTvHelp;
    @BindView(R.id.tv_receive_alarm)
    TextView mTvReceiveAlarm;
    @BindView(R.id.tv_police_strength)
    TextView mTvPoliceStrength;
    @BindView(R.id.tv_path_plan)
    TextView mTvPathPlan;
    ServiceFeatureTable jq_serviceFeatureTable;
    ServiceFeatureTable jc_serviceFeatureTable;
    LocationDisplay mLocationDisplay;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    int requestCode = 2;
    Unbinder unbinder;
    private AlertDialog mAlertDialog;

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_main_home;
    }

    @Override
    public void initView() {
        jq_serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.jq_featureServer));
        jc_serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.jc_featureServer));
        initializeMap();
        initAlertDialog();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initValue() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_police_strength)
    public void queryPolice_strength() {
        try {
            // create a buffer from the point
            Point point = mLocationDisplay.getLocation().getPosition();
            if (point == null) {
                Toast.makeText(getContext(), "未能获取当前位置", Toast.LENGTH_LONG).show();
                return;
            }
            double distance = Meters2Degrees.metersToDecimalDegrees(3000, point.getY());
            final Polygon searchGeometry = GeometryEngine.buffer(point, distance);

// create a query to find which features are contained by the query geometry
            QueryParameters queryParams = new QueryParameters();
            queryParams.setGeometry(searchGeometry);
            queryParams.setSpatialRelationship(QueryParameters.SpatialRelationship.CONTAINS);
// select based on the query
            final ListenableFuture<FeatureQueryResult> selectFuture = jc_serviceFeatureTable.getFeatureLayer().selectFeaturesAsync(queryParams, FeatureLayer.SelectionMode.ADD);
        } catch (Exception ex) {
            Toast.makeText(getContext(), "查询失败", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.img_location)
    public void displayLocation() {
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();
    }

    private void initializeMap() {
//        showDialogProgress();
        mLocationDisplay = mMapView.getLocationDisplay();
        //ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer(getResources().getString(R.string.nd_vectorMap_service));
        ArcGISTiledLayer gisMapImageLayer = new ArcGISTiledLayer(getResources().getString(R.string.nd_imgMap_service));

        Basemap basemap = new Basemap(gisMapImageLayer);

        FeatureLayer jq_featureLayer = new FeatureLayer(jq_serviceFeatureTable);
        FeatureLayer jc_featureLayer = new FeatureLayer(jc_serviceFeatureTable);
        //渲染警情
        SimpleMarkerSymbol jq_simpleSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 12);
        SimpleRenderer jq_simpleRenderer = new SimpleRenderer(jq_simpleSymbol);
        jq_featureLayer.setRenderer(jq_simpleRenderer);
        //渲染警察
        try {
            setRenderer(jc_featureLayer);
        } catch (Exception ex) {
//todo
        }
        ArcGISMap map = new ArcGISMap(basemap);


        //Point centerPoint = new Point(119.575208, 26.69161, SpatialReference.create(4490));//4490或者4326皆可，无则crash.Point相当于.Net中MapPoint
        //map.setInitialViewpoint(new Viewpoint( centerPoint,8000));

        map.getOperationalLayers().add(jc_featureLayer);
        map.getOperationalLayers().add(jq_featureLayer);
        mMapView.setMap(map);
        //mMapView.setViewpointCenterAsync(centerPoint,8000);//与setInitialViewpoint()不同，必须放在setMap()之后，
        //mMapView.setOnTouchListener(new IdentifyFeatureLayerTouchListener(this, mMapView, jq_featureLayer));
        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(getActivity(), reqPermissions, requestCode);
                } else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device.
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                }
            }
        });

//        dismissDialogProgress();
    }

    private void setRenderer(final FeatureLayer jc_featureLayer) {
        BitmapDrawable policemanDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.policeman);
        try {
            final PictureMarkerSymbol policemanSymbol = PictureMarkerSymbol.createAsync(policemanDrawable).get();
            policemanSymbol.setHeight(20);
            policemanSymbol.setWidth(20);
            policemanSymbol.setOffsetY(10);
            policemanSymbol.loadAsync();
            policemanSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    SimpleRenderer jc_simpleRenderer = new SimpleRenderer(policemanSymbol);
                    if (jc_featureLayer != null)
                        jc_featureLayer.setRenderer(jc_simpleRenderer);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            mLocationDisplay.startAsync();
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(getContext(), getResources().getString(R.string.location_permission_denied), Toast
                    .LENGTH_SHORT).show();
        }
    }

    private void initAlertDialog() {
        final CharSequence[] menu = {"实有房屋", "实有单位", };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        mAlertDialog = builder.setTitle("请选择采集类型")
                .setSingleChoiceItems(menu, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        switch (which) {
                            case 0:
                                intent.setClass(getContext(),CollectBuildingActivity.class);
                                break;
                            case 1:
                                intent.setClass(getContext(), ActualUnitActivity.class);
                                break;
                        }
                        startActivity(intent);
                        mAlertDialog.dismiss();
                    }
                }).create();
    }

    @OnClick({R.id.tv_help, R.id.tv_receive_alarm, R.id.tv_police_strength, R.id.img_location, R.id.tv_path_plan, R.id.tv_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_help:

                break;
            case R.id.tv_receive_alarm:
                startIntent(ReceiveAlarmActivity.class);
                break;
            case R.id.tv_police_strength:
                break;
            case R.id.img_location:
                break;
            case R.id.tv_path_plan:
                break;
            case R.id.tv_collect:
                mAlertDialog.show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveNotification(String message) {

        LUtils.log(message + "=====");
        if (message != null) {
            final CustomDialog.Bulider cus = new CustomDialog.Bulider();
            cus.setTitle("新的警情!").setOnLeftButtOnListener(R.string.btn_cancel, new CustomDialogInterface.OnClickListener() {
                @Override
                public void click(CustomDialogInterface customDialogInterface) {
                    customDialogInterface.cancel();
                }
            }).setOnRightButtonListener(R.string.btn_confirm, new CustomDialogInterface.OnClickListener() {
                @Override
                public void click(CustomDialogInterface customDialogInterface) {
                    customDialogInterface.cancel();
                    EventBus.getDefault().post(1);
                }
            }).bulid(getActivity()).show();
        }

    }
}
