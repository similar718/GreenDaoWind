package com.nedfon.nedfon.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nedfon.nedfon.R;


public class TimerChoiceDialog extends Dialog {

    private RelativeLayout mSureRl;
    private TextView mCancelTv;
    private TimePicker mTp;

    private String hour = "";
    private String minutes = "";

    public TimerChoiceDialog(Activity context) {
        super(context, R.style.CustomProgressDialog);
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.dialog_timer_choice, null);
        mSureRl = view.findViewById(R.id.dialog_timer_choice_sure_rl);
        mCancelTv = view.findViewById(R.id.dialog_timer_choice_cancel_tv);
        mTp = view.findViewById(R.id.dialog_timer_choice_tp);
        mTp.setIs24HourView(true);
        //dialog添加视图
        setContentView(view);

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay>9?hourOfDay+"":"0"+hourOfDay;
                minutes = minute>9?minute+"":"0"+minute;
            }
        });
    }
    public void show(View.OnClickListener mSureOnclick){
        mSureRl.setOnClickListener(mSureOnclick);
        show();
    }

    public String getTime(){
        return ("".equals(hour)?mTp.getCurrentHour()+"":hour)+":"+("".equals(minutes)?mTp.getCurrentMinute()+"":minutes);
    }
}
