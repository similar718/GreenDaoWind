package com.nedfon.nedfon.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.adapter.DataSearchDialogAdapter;


public class DeviceDataSearchDialog extends Dialog {

    private ListView mDeviceListLv;
    private TextView mCancelTv;

    private DataSearchDialogAdapter mAdapter;

    private Context mContext;

    public DeviceDataSearchDialog(Activity context) {
        super(context, R.style.CustomProgressDialog);
        //加载布局文件
        mContext = context;
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.dialog_data_search, null);
        mDeviceListLv = view.findViewById(R.id.dialog_data_search_lv);
        mCancelTv = view.findViewById(R.id.dialog_data_search_cancel_tv);
        //dialog添加视图
        setContentView(view);
    }
    public void ShowDialog(String[] list, AdapterView.OnItemClickListener onItemClickListener){
        if (list == null){ //23881531  25594463  13512774760
            list = new String[]{"没有设备可查询，请使用【设备配网】来添加设备"};
//            list[0] = "没有设备可查询，请使用【设备配网】来添加设备";
        } else {
            mDeviceListLv.setOnItemClickListener(onItemClickListener);
        }

        mAdapter = new DataSearchDialogAdapter(mContext,list);
        mDeviceListLv.setAdapter(mAdapter);
        //mAdapter.setData(list);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        show();
    }
}
