package com.joint.jointpolice.widget.dialog;

import android.view.View;

/**
 * @作者: ChenJunHui
 * @日期: 2018/3/7 17:08
 * @描述:
 */

public abstract interface CustomDialogInterface {

//    /**
//     * 设置标题
//     */
//    public abstract void setTitleText(CharSequence title);
//
//    /**
//     * 设置左边按钮
//     */
//    public abstract void setLeftButton(int resId, View.OnClickListener onClickListener);
//
//    /**
//     * 设置右边按钮
//     */
//    public abstract void setRightButton(int resId, View.OnClickListener onClickListener);

    void cancel();

    void dismiss();

    /**
     * 取消dialog
     * */
    public  interface  OnCancelListener{
        void dismissDialog(CustomDialogInterface customDialogInterface);
    }

    public interface OnClickListener {
        void click(CustomDialogInterface customDialogInterface);
    }
}
