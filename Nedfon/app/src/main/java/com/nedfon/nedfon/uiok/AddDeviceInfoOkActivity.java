package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.DeviceInfoAll;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddDeviceInfoOkActivity extends BaseTopBottomActivity implements View.OnClickListener {
    private TextView mSNTv,mTypeTv,mAddressTv,mDateTv,mStatusTv;
    private TextView mBindPromptTv;
    private RelativeLayout mBindRl;

    private DeviceInfo info = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("设备信息");
        info = CommonUtils.bean;
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_device_info_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(AddDeviceInfoOkActivity.this,AddDeviceOkActivity.class);
        startActivity(deviceIntent);
        this.finish();
    }
    private void initView() {

        mSNTv = this.findViewById(R.id.activity_add_device_info_sn_tv);
        mTypeTv = this.findViewById(R.id.activity_add_device_info_type_tv);
        mAddressTv = this.findViewById(R.id.activity_add_device_info_address_tv);
        mDateTv = this.findViewById(R.id.activity_add_device_info_date_tv);
        mStatusTv = this.findViewById(R.id.activity_add_device_info_status_tv);
        mBindPromptTv = this.findViewById(R.id.activity_app_device_info_bind_prompt_tv);
        mBindRl = this.findViewById(R.id.activity_add_device_info_bind_rl);

        mBindRl.setOnClickListener(this);
        initData();
    }

    private void initData() {
        mSNTv.setText(info.deviceid);
        mTypeTv .setText(info.terminal);
        mAddressTv.setText(info.location);
        mDateTv.setText(info.clienttime);
        mStatusTv.setText(info.status==0?"未绑定":"已绑定");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.activity_add_device_info_bind_rl:
                doBindingDeviceGet(CommonUtils.token,info.deviceid);
                break;

            default:
                break;
        }
    }
    private static OkHttpClient okhttpclient = new OkHttpClient();

    private void doBindingDeviceGet(String token,String deviceSN){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request]
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/bindingDevice?token="+token+"&deviceSN="+deviceSN).get().build();
        executeBindingDeviceRequest(request);
    }
    private void executeBindingDeviceRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":1,")){
                    DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
                    CommonUtils.bean = null;
                    CommonUtils.bean = info.data;
                    mHandler.sendEmptyMessage(1);
                } else if (res.contains(":-1,")){
                    mHandler.sendEmptyMessage(3);
                } else if (res.contains(":-2,")){
                    mHandler.sendEmptyMessage(4);
                } else if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(5);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtils.show(AddDeviceInfoOkActivity.this,"绑定成功！");
                    mBindPromptTv.setText("绑定成功");
                    break;
                case 2 :
                    ToastUtils.show(AddDeviceInfoOkActivity.this,"其他错误");
                    mBindPromptTv.setText("其他错误");
                    break;
                case 3 :
                    ToastUtils.show(AddDeviceInfoOkActivity.this,"绑定设备不存在");
                    mBindPromptTv.setText("绑定设备不存在！");
                    break;
                case 4 :
                    ToastUtils.show(AddDeviceInfoOkActivity.this,"该设备已经绑定");
                    mBindPromptTv.setText("该设备已经绑定！");
                    break;
                case 5 :
                    ToastUtils.show(AddDeviceInfoOkActivity.this,"设置失败");
                    mBindPromptTv.setText("设备绑定失败！");
                    break;
            }
        }
    };
}
