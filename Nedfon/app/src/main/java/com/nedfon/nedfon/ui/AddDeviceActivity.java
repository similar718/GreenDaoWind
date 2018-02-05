//package com.nedfon.nedfon.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.nedfon.nedfon.R;
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
//import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
//import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
//
//public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//    private EditText mSearchSnEt;
//    private TextView mQrcodeTv;
//    private TextView mSearchPromptTv;
//    private RelativeLayout mSearchRl;
//
//    private int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_device);
//
//        initView();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_add_device_back_tv);
//        mSearchSnEt = this.findViewById(R.id.activity_add_device_sn_et);
//        mQrcodeTv = this.findViewById(R.id.activity_app_device_qrcode_tv);
//        mSearchPromptTv = this.findViewById(R.id.activity_app_device_search_prompt_tv);
//        mSearchRl = this.findViewById(R.id.activity_add_device_search_rl);
//
//        mBackTv.setOnClickListener(this);
//        mQrcodeTv.setOnClickListener(this);
//        mSearchRl.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_add_device_back_tv:
//                this.finish();
//                break;
//
//            case R.id.activity_app_device_qrcode_tv:
//                Intent photoPickerIntent = new Intent(AddDeviceActivity.this,ZxingScanActivity.class);
//                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//                break;
//
//
//            case R.id.activity_add_device_search_rl:
//                String search = mSearchSnEt.getText().toString();
//                if ("".equals(search) || null == search){
//                    mSearchPromptTv.setText("请输入设备SN码！");
//                    return;
//                }
//                doDeviceInfoGet(CommonUtils.token,search);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == 2 && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            String result = data.getStringExtra("result");
//            //设置结果显示框的显示数值
//            mSearchSnEt.setText(result);
//        }
//    }
//
//
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//
//    private void doDeviceInfoGet(String token,String deviceSN){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request]
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/deviceInfo?token="+token+"&deviceSN="+deviceSN).get().build();
//        executeDeviceInfoRequest(request);
//    }
//    private void executeDeviceInfoRequest(Request request) {
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
//                    ToastUtils.show(AddDeviceActivity.this,"成功搜索到！");
//                    mSearchPromptTv.setText("成功搜索到！");
//                    Intent intent = new Intent(AddDeviceActivity.this,AddDeviceInfoActivity.class);
//                    startActivity(intent);
//                    break;
//                case 2 :
//                    ToastUtils.show(AddDeviceActivity.this,"其他错误");
//                    mSearchPromptTv.setText("其他错误");
//                    break;
//                case 5 :
//                    ToastUtils.show(AddDeviceActivity.this,"设置失败");
//                    mSearchPromptTv.setText("搜索不到此设备，输入SN码有误！");
//                    break;
//            }
//        }
//    };
//}
