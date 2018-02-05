//package com.nedfon.nedfon.ui;
//
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.bean.DeviceInfo;
//import com.nedfon.nedfon.bean.DeviceInfoAll;
//import com.nedfon.nedfon.utils.CommonUtils;
//import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import java.io.IOException;
//
//public class AddDeviceInfoActivity extends AppCompatActivity implements View.OnClickListener {
//
//
//    private TextView mBackTv;
//    private TextView mSNTv,mTypeTv,mAddressTv,mDateTv,mStatusTv;
//    private TextView mBindPromptTv;
//    private RelativeLayout mBindRl;
//
//    private DeviceInfo info = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_device_info);
//        info = CommonUtils.bean;
//        initView();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_add_device_info_back_tv);
//
//        mSNTv = this.findViewById(R.id.activity_add_device_info_sn_tv);
//        mTypeTv = this.findViewById(R.id.activity_add_device_info_type_tv);
//        mAddressTv = this.findViewById(R.id.activity_add_device_info_address_tv);
//        mDateTv = this.findViewById(R.id.activity_add_device_info_date_tv);
//        mStatusTv = this.findViewById(R.id.activity_add_device_info_status_tv);
//        mBindPromptTv = this.findViewById(R.id.activity_app_device_info_bind_prompt_tv);
//        mBindRl = this.findViewById(R.id.activity_add_device_info_bind_rl);
//
//        mBackTv.setOnClickListener(this);
//        mBindRl.setOnClickListener(this);
//        initData();
//    }
//
//    private void initData() {
//        mSNTv.setText(info.sn);
//        mTypeTv .setText(info.terminal);
//        mAddressTv.setText(info.location);
//        mDateTv.setText(info.clienttime);
//        mStatusTv.setText(info.status==0?"未绑定":"已绑定");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_add_device_info_back_tv:
//                this.finish();
//                break;
//
//            case R.id.activity_add_device_info_bind_rl:
//                doBindingDeviceGet(CommonUtils.token,info.sn);
//                break;
//
//            default:
//                break;
//        }
//    }
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//
//    private void doBindingDeviceGet(String token,String deviceSN){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request]
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/bindingDevice?token="+token+"&deviceSN="+deviceSN).get().build();
//        executeBindingDeviceRequest(request);
//    }
//    private void executeBindingDeviceRequest(Request request) {
//        //3.将Request封装为Call
//        Call call = okhttpclient.newCall(request);
//        //异步使用CallBack  同步用call.execute()
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":1,")){
//                    DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
//                    CommonUtils.bean = info.data;
//                    mHandler.sendEmptyMessage(1);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(5);
//                } else {
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        });
//    }
//    public Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 1:
//                    ToastUtils.show(AddDeviceInfoActivity.this,"绑定成功！");
//                    mBindPromptTv.setText("绑定成功");
//                    break;
//                case 2 :
//                    ToastUtils.show(AddDeviceInfoActivity.this,"其他错误");
//                    mBindPromptTv.setText("其他错误");
//                    break;
//                case 5 :
//                    ToastUtils.show(AddDeviceInfoActivity.this,"设置失败");
//                    mBindPromptTv.setText("设备绑定失败！");
//                    break;
//            }
//        }
//    };
//}
