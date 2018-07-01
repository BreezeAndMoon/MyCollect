package com.joint.jointpolice.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.util.LUtils;

import java.io.File;

/**
 * @作者: ChenJunHui
 * @日期: 2018/3/1 12:00
 * @描述:
 */

public class OpenMobileMapActivity extends AppCompatActivity {

    private static final String TAG = "tpk";
    private static final String FILE_EXTENSION = ".tpk";
    private static File extStorDir;
    private static String extSDCardDirName;
    private static String filename;
    private static String mmpkFilePath;
    private MapView mMapView;
    private MobileMapPackage mapPackage;

    // define permission to request
    String[] reqPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int requestCode = 2;
    private final String extern = Environment.getExternalStorageDirectory().getPath();

    /**
     * Create the mobile map package file location and name structure
     */
    private static String createMobileMapPackageFilePath(){
        LUtils.log(extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION);
        return extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_map);

        // get sdcard resource name
        extStorDir = Environment.getExternalStorageDirectory();
        // get the directory
        extSDCardDirName = this.getResources().getString(R.string.config_data_sdcard_offline_dir);
        // get mobile map package filename
        filename = this.getResources().getString(R.string.config_mmpk_name);
        // create the full path to the mobile map package file
        mmpkFilePath = createMobileMapPackageFilePath();

        // retrieve the MapView from layout
        mMapView = (MapView) findViewById(R.id.mapView);
        // For API level 23+ request permission at runtime
        if(ContextCompat.checkSelfPermission(OpenMobileMapActivity.this, reqPermission[0]) == PackageManager.PERMISSION_GRANTED){
            createTpk();
//            loadMobileMapPackage(mmpkFilePath);
        }else{
            // request permission
            ActivityCompat.requestPermissions(OpenMobileMapActivity.this, reqPermission, requestCode);
        }

    }

    /**
     * Handle the permissions request response
     *
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

//            loadMobileMapPackage(mmpkFilePath);
        }else{
            // report to user that permission was denied
            Toast.makeText(OpenMobileMapActivity.this, getResources().getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load a mobile map package into a MapView
     *
     * @param mmpkFile Full path to mmpk file
     */
    private void loadMobileMapPackage(String mmpkFile){
        //[DocRef: Name=Open Mobile Map Package-android, Category=Work with maps, Topic=Create an offline map]
        // create the mobile map package
        mapPackage = new MobileMapPackage(mmpkFile);
        // load the mobile map package asynchronously
        mapPackage.loadAsync();

        // add done listener which will invoke when mobile map package has loaded
        mapPackage.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                // check load status and that the mobile map package has maps
                if(mapPackage.getLoadStatus() == LoadStatus.LOADED && mapPackage.getMaps().size() > 0){
                    // add the map from the mobile map package to the MapView
                    mMapView.setMap(mapPackage.getMaps().get(0));
                }else{
                    // Log an issue if the mobile map package fails to load
                    Log.e(TAG, mapPackage.getLoadError().getMessage());
                }
            }
        });
        //[DocRef: END]
    }


    private void createTpk() {
        // create a basemap from a local tile package
        TileCache tileCache = new TileCache(extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION );
        ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(tileCache);
        Basemap basemap = new Basemap(tiledLayer);

        // create ArcGISMap with imagery basemap
        ArcGISMap mMap = new ArcGISMap(basemap);

        mMapView.setMap(mMap);
    }


    @Override
    protected void onPause(){
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mMapView.resume();
    }
    
    
}
