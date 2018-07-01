package com.joint.jointpolice.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joint.jointpolice.adapter.GridImageAdapter;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/23 16:36
 * @描述:
 */

public class PictureSelectUtil {

    private OpenPhoneGallery mOpenPhoneGallery;
    private GridLayoutManager mGridLayoutManager;
    private GridImageAdapter mGridImageAdapter;

    public interface OpenPhoneGallery {
        void onAddPicClick();

        void onItemClick(int position);

        void onItemDelClick(int position);
    }


    public PictureSelectUtil(Context context,  RecyclerView recyclerView) {

       GridImageAdapter gridImageAdapter = new GridImageAdapter(context, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                if (mOpenPhoneGallery != null) {
                    mOpenPhoneGallery.onAddPicClick();
                }
            }
        });
        gridImageAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (mOpenPhoneGallery != null) {
                    mOpenPhoneGallery.onItemClick(position);
                }
            }
        });

        gridImageAdapter.setOnItemDelClickListener(new GridImageAdapter.OnItemDelClickListener() {
            @Override
            public void onItemDelClick(int position) {
                if (mOpenPhoneGallery != null) {
                    mOpenPhoneGallery.onItemDelClick(position);
                }
            }
        });

        this.mGridImageAdapter = gridImageAdapter;
        FullyGridLayoutManager fullyGridLayoutManager=new FullyGridLayoutManager(context,3,FullyGridLayoutManager.VERTICAL,false);
        fullyGridLayoutManager.setScrollEnable(false);
        recyclerView.setLayoutManager(fullyGridLayoutManager);
        recyclerView.setAdapter(gridImageAdapter);

    }


    public OpenPhoneGallery getOpenPhoneGallery() {
        return mOpenPhoneGallery;
    }


    public void setOpenPhoneGallery(OpenPhoneGallery openPhoneGallery) {
        mOpenPhoneGallery = openPhoneGallery;
    }

    public GridImageAdapter getGridImageAdapter() {
        return mGridImageAdapter;
    }

    public void setGridImageAdapter(GridImageAdapter gridImageAdapter) {
        mGridImageAdapter = gridImageAdapter;
    }
}
