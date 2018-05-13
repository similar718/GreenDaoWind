package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfoAll;
//import com.nedfon.nedfon.ui.AddDeviceActivity;
import com.nedfon.nedfon.ui.ZxingScanActivity;
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

public class AddDeviceOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private EditText mSearchSnEt;
    private TextView mQrcodeTv;
    private TextView mSearchPromptTv;
    private RelativeLayout mSearchRl;

    private int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_device_ok);

        NAME = AddDeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("设备绑定");

        initView();
    }

    private void initView() {
        mSearchSnEt = this.findViewById(R.id.activity_add_device_sn_et);
        mQrcodeTv = this.findViewById(R.id.activity_app_device_qrcode_tv);
        mSearchPromptTv = this.findViewById(R.id.activity_app_device_search_prompt_tv);
        mSearchRl = this.findViewById(R.id.activity_add_device_search_rl);

        mQrcodeTv.setOnClickListener(this);
        mSearchRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.activity_app_device_qrcode_tv:
                Intent photoPickerIntent = new Intent(AddDeviceOkActivity.this,ZxingScanActivity.class);
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;


            case R.id.activity_add_device_search_rl:
                String search = mSearchSnEt.getText().toString();
                if ("".equals(search) || null == search){
                    mSearchPromptTv.setText("请输入设备SN码！");
                    return;
                }
                doDeviceInfoGet(CommonUtils.token,search);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2 && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            String result = data.getStringExtra("result");
            //设置结果显示框的显示数值
            mSearchSnEt.setText(result);
        }
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();

    private void doDeviceInfoGet(String token,String deviceSN){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request]
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/deviceInfo?token="+token+"&deviceSN="+deviceSN).get().build();
        executeDeviceInfoRequest(request);
    }
    private void executeDeviceInfoRequest(Request request) {
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
                if (res.contains(CommonUtils.mSuccess)){
                    DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
                    CommonUtils.bean = null;
                    CommonUtils.bean = info.data;
                    mHandler.sendEmptyMessage(1);
                } else if (res.contains(CommonUtils.mFailed)){
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
                    ToastUtils.show(AddDeviceOkActivity.this,"成功搜索到！");
                    mSearchPromptTv.setText("成功搜索到！");
                    Intent intent = new Intent(AddDeviceOkActivity.this,AddDeviceInfoOkActivity.class);
                    startActivity(intent);
                    AddDeviceOkActivity.this.finish();
                    break;
                case 2 :
                    ToastUtils.show(AddDeviceOkActivity.this,"其他错误");
                    mSearchPromptTv.setText("其他错误");
                    break;
                case 5 :
                    ToastUtils.show(AddDeviceOkActivity.this,"获取设备失败");
                    mSearchPromptTv.setText("搜索不到此设备，输入SN码有误！");
                    break;
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_device_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(AddDeviceOkActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        AddDeviceOkActivity.this.finish();
    }
}
