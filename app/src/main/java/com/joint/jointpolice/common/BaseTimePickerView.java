package com.joint.jointpolice.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.joint.jointpolice.R;
import com.joint.jointpolice.constants.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/26 14:55
 * @描述:
 */

public class BaseTimePickerView {
    public static TimePickerView mTimePickerView;
    public static void initTimePicker(Context context) {

        if (mTimePickerView == null) {
            mTimePickerView = new TimePickerBuilder(context, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                    textView.setText(format.format(date));
                    if (mOnTimeSelect != null) {
                        mOnTimeSelect.setCompletText(date);
                    }
                }
            }).setType(new boolean[]{true,true,true,false,false,false})
                    .setLabel("","","","","","")
                    .setCancelText(context.getResources().getString(R.string.btn_cancel))
                    .setCancelColor(R.color.colorPrimary)
                    .setSubmitText(context.getResources().getString(R.string.btn_complete))
                    .setSubmitColor(R.color.colorPrimary)
                    .build();
        }
        mTimePickerView.show();
    }

    public interface OnTimeSelect {
        void setCompletText(Date date);
    }

   public  static  OnTimeSelect mOnTimeSelect;

    public static  void setOnTimeSelect(OnTimeSelect onTimeSelect) {
        mOnTimeSelect = onTimeSelect;
    }
}
