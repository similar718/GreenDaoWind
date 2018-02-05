//package com.nedfon.nedfon.ui;
//
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.bean.DeviceInfo;
//import com.nedfon.nedfon.utils.CommonUtils;
//import com.nedfon.nedfon.utils.StatusBarCompat;
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
//public class ParamsSettingActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//
//    private ImageView mTemperatureAddIv,mTemperatureReduceIv;
//    private TextView mTemperatureTv;
//
//    private ImageView mHumidityAddIv,mHumidityReduceIv;
//    private TextView mHumidityTv;
//
//    private ImageView mPm25AddIv,mPm25ReduceIv;
//    private TextView mPm25Tv;
//
//    private RelativeLayout mSureRl;
//    private ImageView mSureIv;
//
//    private boolean mIsSelected = false;
//
//    private int temperature = 18;
//    private int humidity = 80;
//    private int pm25 = 120;
//
//    private int mtemperature = 18;
//    private int mhumidity = 80;
//    private int mpm25 = 120;
//
//    private DeviceInfo info = null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(ParamsSettingActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_params_setting);
//        info = CommonUtils.bean;
//
//        initView();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_params_setting_back_tv);
//        mSureRl = this.findViewById(R.id.activity_params_setting_sure_rl);
//        mSureIv = this.findViewById(R.id.activity_params_setting_sure_iv);
//
//        mTemperatureAddIv = this.findViewById(R.id.activity_params_setting_teperature_add_iv);
//        mTemperatureReduceIv = this.findViewById(R.id.activity_params_setting_teperature_reduce_iv);
//        mTemperatureTv = this.findViewById(R.id.activity_params_setting_teperature_tv);
//
//        mHumidityAddIv = this.findViewById(R.id.activity_params_setting_humidity_add_iv);
//        mHumidityReduceIv = this.findViewById(R.id.activity_params_setting_humidity_reduce_iv);
//        mHumidityTv = this.findViewById(R.id.activity_params_setting_humidity_tv);
//
//        mPm25AddIv = this.findViewById(R.id.activity_params_setting_pm25_add_iv);
//        mPm25ReduceIv = this.findViewById(R.id.activity_params_setting_pm25_reduce_iv);
//        mPm25Tv = this.findViewById(R.id.activity_params_setting_pm25_tv);
//
//        mBackTv.setOnClickListener(this);
//        mSureRl.setOnClickListener(this);
//
//        mTemperatureAddIv.setOnClickListener(this);
//        mTemperatureReduceIv.setOnClickListener(this);
//        mHumidityAddIv.setOnClickListener(this);
//        mHumidityReduceIv.setOnClickListener(this);
//        mPm25AddIv.setOnClickListener(this);
//        mPm25ReduceIv.setOnClickListener(this);
//
//        initData();
//    }
//
//    private void initData() {
//        mtemperature = (int) info.intmpalarmdata;
//        mhumidity = (int) info.sweetalarmdata;
//        mpm25 = (int) info.inpm25alarmdata;
//
//        temperature = (int) info.intmpalarmdata;
//        humidity = (int) info.sweetalarmdata;
//        pm25 = (int) info.inpm25alarmdata;
//
//        mTemperatureTv.setText(mtemperature+"℃");
//        mHumidityTv.setText(mhumidity+"%");
//        mPm25Tv.setText(mpm25+"");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_params_setting_back_tv:
//                this.finish();
//                break;
//
//            case R.id.activity_params_setting_sure_rl: //确定
//                if (mIsSelected) {
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                    doControlThresholdCmdGet(CommonUtils.token,info.sn,mtemperature+"",mhumidity+"",mpm25+"");
//                }
//                break;
//
//            case R.id.activity_params_setting_teperature_add_iv: //温度报警 add
//                mtemperature++;
//                mTemperatureTv.setText(mtemperature+"℃");
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//
//            case R.id.activity_params_setting_teperature_reduce_iv: //温度报警 reduce
//                if (mtemperature == 0){
//                    return;
//                }
//                mtemperature--;
//                mTemperatureTv.setText(mtemperature+"℃");
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//
//            case R.id.activity_params_setting_humidity_add_iv: //湿度报警 add
//                if (mhumidity == 100){
//                    return;
//                }
//                mhumidity ++;
//                mHumidityTv.setText(mhumidity+"%");
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//
//            case R.id.activity_params_setting_humidity_reduce_iv: //湿度报警 reduce
//                if (mhumidity == 0){
//                    return;
//                }
//                mhumidity --;
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//
//            case R.id.activity_params_setting_pm25_add_iv: //pm2.5报警 add
//                mpm25 ++;
//                mPm25Tv.setText(mpm25+"");
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//
//            case R.id.activity_params_setting_pm25_reduce_iv: //pm2.5报警 reduce
//                if (mpm25 == 0){
//                    return;
//                }
//                mpm25 --;
//                mPm25Tv.setText(mpm25+"");
//                if (mtemperature == temperature && mpm25 == pm25 && mhumidity == humidity){
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                } else {
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//    private void doControlThresholdCmdGet(String token,String deviceSN,String temp,String sweet,String pm25){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        String url = CommonUtils.localhost+"mobileapi/controlThresholdCmd?token="+token+"&deviceSN="+deviceSN+"&temp="+temp+"&sweet="+sweet+"&pm25="+pm25;
//        Log.e("oooooooooooo","Params setting url = "+url);
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlThresholdCmd?token="+token+"&deviceSN="+deviceSN+"&temp="+temp+"&sweet="+sweet+"&pm25="+pm25).get().build();
//        executeControlThresholdCmdRequest(request);
//    }
//
//    private void executeControlThresholdCmdRequest(Request request) {
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
//                    ToastUtils.show(ParamsSettingActivity.this,"设置成功！");
//                    break;
//                case 2 :
//                    ToastUtils.show(ParamsSettingActivity.this,"其他错误");
//                    break;
//                case 5 :
//                    ToastUtils.show(ParamsSettingActivity.this,"设置失败");
//                    break;
//            }
//        }
//    };
//}
