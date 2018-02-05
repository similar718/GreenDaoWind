package com.nedfon.nedfon.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;


public class DeviceSetInternetDialog extends Dialog {

    private RelativeLayout mSureRl;
    private TextView mCancelTv;

    public DeviceSetInternetDialog(Activity context,View.OnClickListener mSureOnclick) {
        super(context, R.style.CustomProgressDialog);
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.dialog_set_internet, null);
        mSureRl = view.findViewById(R.id.dialog_set_internet_sure_rl);
        mCancelTv = view.findViewById(R.id.dialog_set_internet_cancel_tv);
        //dialog添加视图
        setContentView(view);

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSureRl.setOnClickListener(mSureOnclick);

        show();
    }
}
