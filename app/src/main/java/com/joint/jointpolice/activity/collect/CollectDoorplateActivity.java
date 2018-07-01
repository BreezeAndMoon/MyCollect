package com.joint.jointpolice.activity.collect;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.joint.jointpolice.R;
import com.joint.jointpolice.common.BaseActivity;

/**
 * Created by Joint229 on 2018/5/14.
 */

public class CollectDoorplateActivity extends BaseActivity {
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState, String tag) {
        setContentView(R.layout.activity_collect_doorplate);
    }

    @Override
    protected void initView() {
        mMapView = findViewById(R.id.mapview);
        ((TextView) findViewById(R.id.toolbar_tv_title)).setText("采集门牌");
        initializeMap();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initValue() {

    }

    private void initializeMap() {
//        showDialogProgress();

        //ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer(getResources().getString(R.string.nd_vectorMap_service));
        ArcGISTiledLayer gisMapImageLayer = new ArcGISTiledLayer(getResources().getString(R.string.nd_imgMap_service));

        Basemap basemap = new Basemap(gisMapImageLayer);

        ArcGISMap map = new ArcGISMap(basemap);

        mMapView.setMap(map);


//        dismissDialogProgress();
    }
}
