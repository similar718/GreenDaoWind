//package com.nedfon.nedfon.ui;
//
//import android.content.Intent;
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
//import com.google.gson.Gson;
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.bean.DeviceInfo;
//import com.nedfon.nedfon.bean.DeviceListAll;
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
//
//public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//    private TextView mShiwaiTv,mTemperatureTv,mHumidityTv,mPm25Tv;
//    private RelativeLayout mExhaustVentilationRl,mParamsRl,mTimerRl;
//
//    private RelativeLayout mFuliziRl;
//    private ImageView mFuliziKai,mFuliziGuan;
//
//    private RelativeLayout mPaiqiHaunqiRl;
//    private ImageView mPaiqiHaunqiAuto,mPaiqiHaunqiNoAuto;
//
//    private RelativeLayout mFengjiPowerRl;
//    private ImageView mFengjiPowerKai,mFengjiPowerGuan;
//
//    private TextView mWuRanTxtTv;
//    private TextView mBigTemperatureTv;
//    private TextView mShineiTv;
//
//    private TextView mShiduBigTv,mPm25BigTv;
//
//    private DeviceInfo info = null;
//    private int fulizi = 0;
//    private int auto = 0;
//    private int power = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(DeviceActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_device);
//        info = CommonUtils.bean;
//        initView();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_device_back_tv);
//        mShiwaiTv = this.findViewById(R.id.activity_device_shiwai_tv);
//        mTemperatureTv = this.findViewById(R.id.activity_device_temperature_tv);
//        mHumidityTv = this.findViewById(R.id.activity_device_humidity_tv);
//        mPm25Tv = this.findViewById(R.id.activity_device_pm25_tv);
//        mExhaustVentilationRl = this.findViewById(R.id.activity_exhaust_ventilation_rl);
//        mParamsRl = this.findViewById(R.id.activity_param_set_rl);
//        mTimerRl = this.findViewById(R.id.activity_timer_set_rl);
//
//        mFuliziRl = this.findViewById(R.id.fulizi_bottom_rl);
//        mFuliziKai = this.findViewById(R.id.activity_device_fulizi_kai_iv);
//        mFuliziGuan = this.findViewById(R.id.activity_device_fulizi_guan_iv);
//
//        mFengjiPowerRl = this.findViewById(R.id.fengji_power_rl);
//        mFengjiPowerKai = this.findViewById(R.id.activity_device_fengji_power_kai_iv);
//        mFengjiPowerGuan = this.findViewById(R.id.activity_device_fengji_power_guan_iv);
//
//        mPaiqiHaunqiRl = this.findViewById(R.id.paiqi_huanqi_bottom_rl);
//        mPaiqiHaunqiAuto = this.findViewById(R.id.activity_device_paiqi_huanqi_zidong_iv);
//        mPaiqiHaunqiNoAuto = this.findViewById(R.id.activity_device_paiqi_huanqi_shoudong_iv);
//
//        mWuRanTxtTv = this.findViewById(R.id.activity_device_wuran_tv);
//        mBigTemperatureTv = this.findViewById(R.id.activity_device_big_temperature_tv);
//        mShineiTv = this.findViewById(R.id.activity_device_shinei_tv);
//
//        mShiduBigTv = this.findViewById(R.id.activity_device_shidu_big_tv);
//        mPm25BigTv = this.findViewById(R.id.activity_device_pm25_big_tv);
//
//        mBackTv.setOnClickListener(this);
//        mExhaustVentilationRl.setOnClickListener(this);
//        mParamsRl.setOnClickListener(this);
//        mTimerRl.setOnClickListener(this);
//        mFuliziRl.setOnClickListener(this);
//        mPaiqiHaunqiRl.setOnClickListener(this);
//        mFengjiPowerRl.setOnClickListener(this);
//
//        initData();
//    }
//
//    private void initData() {
//        mTemperatureTv.setText(info.outtmp+"℃");
//        mHumidityTv.setText(info.outsweet+"%");
//        mPm25Tv.setText(info.outpm25+"");
//        mWuRanTxtTv.setText("室内轻度污染");
//        mBigTemperatureTv.setText(info.intmp+"℃");
//        mShiduBigTv.setText(info.insweet+"%");
//        mPm25BigTv.setText(info.inpm25+"");
//        //电源否开关
////        boolean powerisopen = info.ionsflag==0?false:true;
//
//        fulizi = info.ionsflag;
//        auto = info.workmodel;
//        power = info.status;
//
//
//        //排气换气自动 手动
//        boolean paiqihuanqiisopen = info.workmodel==0?false:true; //0自动 1手动
//        if (paiqihuanqiisopen){
//            mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
//            mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
//        } else {
//            mPaiqiHaunqiAuto.setVisibility(View.GONE);
//            mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
//        }
//        //负离子开关
//        boolean fuliziisopen = info.ionsflag==0?false:true;
//        if (!fuliziisopen){
//            mFuliziKai.setVisibility(View.VISIBLE);
//            mFuliziGuan.setVisibility(View.GONE);
//        } else {
//            mFuliziKai.setVisibility(View.GONE);
//            mFuliziGuan.setVisibility(View.VISIBLE);
//        }
//        //电源开关
//        boolean poweropen = power ==0?false:true;
//        if (!fuliziisopen){
//            mFengjiPowerKai.setVisibility(View.VISIBLE);
//            mFengjiPowerGuan.setVisibility(View.GONE);
//        } else {
//            mFengjiPowerKai.setVisibility(View.GONE);
//            mFengjiPowerGuan.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_device_back_tv:
//                this.finish();
//                break;
//            case R.id.activity_exhaust_ventilation_rl:
//                info.ionsflag = fulizi;
//                info.workmodel = auto;
//                info.status = power;
//                CommonUtils.bean = info;
//                Intent exhaustintent = new Intent(DeviceActivity.this,ExhaustVentilatorActivity.class);
//                startActivity(exhaustintent);
//                break;
//            case R.id.activity_param_set_rl:
//                info.ionsflag = fulizi;
//                info.workmodel = auto;
//                info.status = power;
//                CommonUtils.bean = info;
//                Intent paramtintent = new Intent(DeviceActivity.this,ParamsSettingActivity.class);
//                startActivity(paramtintent);
//                break;
//            case R.id.activity_timer_set_rl:
//                info.ionsflag = fulizi;
//                info.workmodel = auto;
//                info.status = power;
//                CommonUtils.bean = info;
//                Intent timerintent = new Intent(DeviceActivity.this,TimerSettingActivity.class);
//                startActivity(timerintent);
//                break;
//            case R.id.fulizi_bottom_rl:
//                if (mFuliziKai.getVisibility() == View.GONE){
//                    mFuliziKai.setVisibility(View.VISIBLE);
//                    mFuliziGuan.setVisibility(View.GONE);
//                    fulizi = 1;
//                    doControlWindCmdGet(CommonUtils.token,info.sn,auto+"",
//                            "1","1",fulizi+"","1",power+"");
//                } else {
//                    mFuliziKai.setVisibility(View.GONE);
//                    mFuliziGuan.setVisibility(View.VISIBLE);
//                    fulizi = 0;
//                    doControlWindCmdGet(CommonUtils.token,info.sn,auto+"",
//                            "1","1",fulizi+"","1",power+"");
//                }
//                break;
//            case R.id.paiqi_huanqi_bottom_rl:
//                if (mPaiqiHaunqiAuto.getVisibility() == View.GONE){
//                    mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
//                    mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
//                    auto = 0;
//                    doControlWindCmdGet(CommonUtils.token,info.sn,auto+"",
//                            "1","1",fulizi+"","1",power+"");
//                } else {
//                    auto = 1;
//                    mPaiqiHaunqiAuto.setVisibility(View.GONE);
//                    mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
//                }
//                break;
//            case R.id.fengji_power_rl:
//                if (mFengjiPowerKai.getVisibility() == View.GONE){
//                    mFengjiPowerKai.setVisibility(View.VISIBLE);
//                    mFengjiPowerGuan.setVisibility(View.GONE);
//                    power = 1;
//                    doControlWindCmdGet(CommonUtils.token,info.sn,auto+"",
//                            "1","1",fulizi+"","1",power+"");
//                } else {
//                    mFengjiPowerKai.setVisibility(View.GONE);
//                    mFengjiPowerGuan.setVisibility(View.VISIBLE);
//                    power = 0;
//                    doControlWindCmdGet(CommonUtils.token,info.sn,auto+"",
//                            "1","1",fulizi+"","1",power+"");
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//    /*
//    http://localhost/mobileapi/controlWindCmd?token=abcvTkdjsd_1209990ijhyty&deviceSN=000000001&fanModel=30&fanLevel=1&fanOnOff=1&ionsOnOff=1&warnflag=1
//    参数说明：
//    工作模式 ：1自动   2手动     工作状态和工作模式是配合使用   只用工作状态处于开机(状态值为1)时 ， 才可以选择自动  手动工作模式
//
//    如果风机开关处于待机状态  则风机转速也必须处于0状态, 如果风机处于3开启状态  则风机转速则可选 1  2档（即高低档）
//    Token：登录token
//    deviceSN：设备SN
//    OnOff：0待机,1开机
//    fanModel：工作模式 1自动  2手动
//    fanLevel： 风机档位 0关机   1抵挡  2高档
//    fanOnOff： 风机开关 0排气换气关机    3排气换气开机
//    ionsOnOff：负离子 0关     1开
//    warnflag：清洗时间提醒  0关闭   1开启
//
//    返回值：
//    成功：{'result':1,'msg':'设置成功'}
//
//    失败：{'result':0,'msg':'设置失败'}
//     */
//    private void doControlWindCmdGet(String token,String deviceSN,String fanModel,String fanLevel,String fanOnOff,String ionsOnOff,String warnflag,String OnOff){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlWindCmd?" +
//                "token="+token+"&deviceSN="+deviceSN+"&OnOff="+OnOff+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag).get().build();
//        executeControlWindCmdRequest(request);
//    }
//
//    private void executeControlWindCmdRequest(Request request) {
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
//                    ToastUtils.show(DeviceActivity.this,"设置成功！");
//                    break;
//                case 2 :
//                    ToastUtils.show(DeviceActivity.this,"其他错误");
//                    break;
//                case 5 :
//                    ToastUtils.show(DeviceActivity.this,"设置失败");
//                    break;
//            }
//        }
//    };
//}
