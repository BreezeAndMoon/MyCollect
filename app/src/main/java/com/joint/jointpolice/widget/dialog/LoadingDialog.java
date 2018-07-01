package com.joint.jointpolice.widget.dialog;

import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.joint.jointpolice.R;

/**
 * @作者: ChenJunHui
 * @日期: 2018/2/1 15:10
 * @描述:
 */

public class LoadingDialog extends Dialog {

    private Context mContext;
    View dialogView;
    private String loadStr;


    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialog);
        initView(context);
    }


    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

//    private void initView(Context context) {
//        initView(context, "");
//    }

    private void initView(Context context) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        setContentView(dialogView);
        setDialogView(dialogView);
        setCanceledOnTouchOutside(true);
    }

    public String getLoadStr() {
        return loadStr;
    }

    public void setLoadStr(String loadStr) {
        TextView loadText =getDialogView().findViewById(R.id.tv_loading_dialog);
        loadText.setText(loadStr);
    }

    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    @Override
    public void show() {
        super.show();
    }
}
