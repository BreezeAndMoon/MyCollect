package com.joint.jointpolice.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.AdapterView;

import com.joint.jointpolice.activity.collect.AddressActivity;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Joint229 on 2018/6/19.
 */

public class SwipeRecyclerViewUtil {
    public static void setSwipeMenu(Context context, SwipeMenuRecyclerView swipeMenuRecyclerView) {
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                deleteItem.setBackgroundColor(Color.RED);
                deleteItem.setText("删除").setTextColor(Color.WHITE);
                deleteItem.setHeight(MATCH_PARENT);
                deleteItem.setWidth(250);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        };
        swipeMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);

    }

    public static void setOnItemClick(SwipeMenuRecyclerView swipeMenuRecyclerView, OnItemDeleteClickListener listener) {
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if(listener!=null)
                    listener.onDeleteClick(adapterPosition);
            }
        };
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
    }

    public interface OnItemDeleteClickListener {
        void onDeleteClick(int position);
    }
}
