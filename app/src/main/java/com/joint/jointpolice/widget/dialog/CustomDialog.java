package com.joint.jointpolice.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;
import com.joint.jointpolice.policenet.CommonNet;


/**
 * @作者: ChenJunHui
 * @日期: 2018/3/7 16:07
 * @描述:
 */

public class CustomDialog extends Dialog implements CustomDialogInterface {


    private boolean isShow = false;
    private String mTitle;
    private int mLeftText;
    private int mRightText;
    public CustomDialog mCustomDialog;
    private static Context mContext;
    private int defaultViewRes = R.layout.dialog_custom;
    private View dialogView;
    private CustomDialogInterface.OnCancelListener mOnCancelListener;
    private CustomDialogInterface.OnClickListener mOnLeftButtonListener;
    private CustomDialogInterface.OnClickListener mOnRightButtonListener;


    protected CustomDialog(Context context, Bulider bulider) {
        super(context, R.style.customDialog);
        this.mTitle = bulider.mTitle;
        this.mLeftText = bulider.mLeftText;
        this.mRightText = bulider.mRightText;
        this.mOnLeftButtonListener = bulider.mOnLeftButtonListener;
        this.mOnRightButtonListener = bulider.monRightButtonListener;
        dialogView = LayoutInflater.from(context).inflate(defaultViewRes, null);
        setTitle(mTitle);
        setOnLeftButtonListener(mLeftText,mOnLeftButtonListener);
        setOnRightButtonListener(mRightText, mOnRightButtonListener);

        setContentView(dialogView);
    }

    private void initView() {

    }

    public void setTitle(String title) {
        TextView textView = dialogView.findViewById(R.id.tv_dialog_title);
        textView.setText(title);
    }

    public void setOnLeftButtonListener(int resId, CustomDialogInterface.OnClickListener listener) {
        this.mOnLeftButtonListener = listener;
        TextView leftButton = dialogView.findViewById(R.id.tv_dialog_cancel);
        if (resId == 0) {
            resId = R.string.btn_cancel;
        }
        leftButton.setText(resId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnLeftButtonListener != null) {
                    mOnLeftButtonListener.click(new CustomDialogInterface() {
                        @Override
                        public void cancel() {
                            if (dialogView != null && isShowing()) {
                             CustomDialog.this.cancel();
                            }

                        }

                        @Override
                        public void dismiss() {
                            if (dialogView != null && isShowing()) {
                                CustomDialog.this.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }


    public void setOnRightButtonListener(int resId, CustomDialogInterface.OnClickListener onRightButtonListener) {
        mOnRightButtonListener = onRightButtonListener;
        TextView rightButton = dialogView.findViewById(R.id.tv_dialog_comfirm);
        if (resId == 0) {
            resId = R.string.btn_confirm;
        }
        rightButton.setText(resId);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnRightButtonListener != null) {
                    mOnRightButtonListener.click(new CustomDialogInterface() {
                        @Override
                        public void cancel() {
                            if (dialogView != null && isShowing()) {
                                CustomDialog.this.cancel();
                            }

                        }
                        @Override
                        public void dismiss() {
                            if (dialogView != null && isShowing()) {
                                CustomDialog.this.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void setOnCancelListener(CustomDialogInterface.OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }


    public static class Bulider {

        private String mTitle;
        private int mLeftText;
        private int mRightText;

        public Bulider setTitle(String title) {
            mTitle = title;
            return this;
        }

        private CustomDialogInterface.OnCancelListener mOnCancelListener;
        private CustomDialogInterface.OnClickListener mOnLeftButtonListener;
        private CustomDialogInterface.OnClickListener monRightButtonListener;

        public void setOnCancelListener(CustomDialogInterface.OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
        }

        public Bulider setOnLeftButtOnListener(int resId, CustomDialogInterface.OnClickListener onLeftButtOnListener) {
            mOnLeftButtonListener = onLeftButtOnListener;
            mLeftText = resId;
            return this;

        }

        public Bulider setOnRightButtonListener(int resId, CustomDialogInterface.OnClickListener monRightButtOnListener) {
            this.monRightButtonListener = monRightButtOnListener;
            mRightText = resId;
            return this;
        }

        public CustomDialog bulid(Context context) {
            return new CustomDialog(context, this);
        }

    }


}
