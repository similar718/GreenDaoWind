package com.nedfon.nedfon.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;


public class DeviceReSetNameDialog extends Dialog {

    private RelativeLayout mSureRl;
    private TextView mCancelTv;
    private EditText mNameTxt;
    private TextView mSubmitTv;
    private GetEt mGetEt;

    public DeviceReSetNameDialog(Activity context) {
        super(context, R.style.CustomProgressDialog);
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.dialog_set_name, null);
        mSureRl = view.findViewById(R.id.dialog_set_name_sure_rl);
        mCancelTv = view.findViewById(R.id.dialog_set_name_cancel_tv);
        mNameTxt = view.findViewById(R.id.dialog_set_name_et);
        mSubmitTv = view.findViewById(R.id.dialog_set_name_submit_tv);
        //dialog添加视图
        setContentView(view);
    }

    public void ShowD(View.OnClickListener mSureOnclick){

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetEt.OptionEt(mNameTxt.getText().toString());
            }
        });
        mSureRl.setOnClickListener(mSureOnclick);
        //TODO  需要获取得到输入框里面的值
        show();
    }

    public static interface GetEt{
        void OptionEt(String txt);
    }
    public void setOnItemGetEtListener(GetEt GetEt) {
        this.mGetEt = GetEt;
    }
}
