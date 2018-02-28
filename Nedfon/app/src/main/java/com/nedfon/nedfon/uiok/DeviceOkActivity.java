package com.nedfon.nedfon.uiok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.DeviceInfoAll;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class DeviceOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mShiwaiTv,mTemperatureTv,mHumidityTv,mPm25Tv;
    private RelativeLayout mExhaustVentilationRl,mParamsRl,mTimerRl;

    private RelativeLayout mFuliziRl;
    private ImageView mFuliziKai,mFuliziGuan;

    private RelativeLayout mPaiqiHaunqiRl;
    private ImageView mPaiqiHaunqiAuto,mPaiqiHaunqiNoAuto;

    private RelativeLayout mFengjiPowerRl;
    private ImageView mFengjiPowerKai,mFengjiPowerGuan;

    private TextView mWuRanTxtTv;
    private TextView mBigTemperatureTv;

    private TextView mShiduBigTv,mPm25BigTv;

    private DeviceInfo info = null;
    private int fulizi = 0;
    private int auto = 0;
    private int power = 0;

    private boolean mIsStopThread = false;
    private boolean isEnd = false;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_ok);
        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("设 备");

        initView();
        mThread = new GetDeviceInfoThread();
        mThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doDeviceInfoGet(CommonUtils.token,CommonUtils.bean.deviceid);
        mIsStopThread = false;
    }

    private class GetDeviceInfoThread extends Thread{
        @Override
        public void run() {
            while(!isEnd) {
                while (!mIsStopThread) {
                    try {
                        Thread.sleep(10 * 1000);//每10秒刷新一次
                        doDeviceInfoGet(CommonUtils.token, CommonUtils.bean.deviceid);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        mIsStopThread = false;
        doDeviceInfoGet(CommonUtils.token,CommonUtils.bean.deviceid);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsStopThread = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isEnd = true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(DeviceOkActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        DeviceOkActivity.this.finish();
    }

    private void initView() {
        mShiwaiTv = this.findViewById(R.id.activity_device_shiwai_tv);
        mTemperatureTv = this.findViewById(R.id.activity_device_temperature_tv);
        mHumidityTv = this.findViewById(R.id.activity_device_humidity_tv);
        mPm25Tv = this.findViewById(R.id.activity_device_pm25_tv);
        mExhaustVentilationRl = this.findViewById(R.id.activity_exhaust_ventilation_rl);
        mParamsRl = this.findViewById(R.id.activity_param_set_rl);
        mTimerRl = this.findViewById(R.id.activity_timer_set_rl);

        mFuliziRl = this.findViewById(R.id.fulizi_bottom_rl);
        mFuliziKai = this.findViewById(R.id.activity_device_fulizi_kai_iv);
        mFuliziGuan = this.findViewById(R.id.activity_device_fulizi_guan_iv);

        mFengjiPowerRl = this.findViewById(R.id.fengji_power_rl);
        mFengjiPowerKai = this.findViewById(R.id.activity_device_fengji_power_kai_iv);
        mFengjiPowerGuan = this.findViewById(R.id.activity_device_fengji_power_guan_iv);

        mPaiqiHaunqiRl = this.findViewById(R.id.paiqi_huanqi_bottom_rl);
        mPaiqiHaunqiAuto = this.findViewById(R.id.activity_device_paiqi_huanqi_zidong_iv);
        mPaiqiHaunqiNoAuto = this.findViewById(R.id.activity_device_paiqi_huanqi_shoudong_iv);

        mWuRanTxtTv = this.findViewById(R.id.activity_device_wuran_tv);
        mBigTemperatureTv = this.findViewById(R.id.activity_device_big_temperature_tv);

        mShiduBigTv = this.findViewById(R.id.activity_device_shidu_big_tv);
        mPm25BigTv = this.findViewById(R.id.activity_device_pm25_big_tv);

        mExhaustVentilationRl.setOnClickListener(this);
        mParamsRl.setOnClickListener(this);
        mTimerRl.setOnClickListener(this);
        mFuliziRl.setOnClickListener(this);
        mPaiqiHaunqiRl.setOnClickListener(this);
        mFengjiPowerRl.setOnClickListener(this);
        initData();
    }

    private void initData() {
        Log.e("oooooooooo", "onCreateView: token = "+ getSharedPreferences("nedfon",MODE_PRIVATE).getString("token", ""));
        if (null == CommonUtils.bean){
            if (null == CommonUtils.token || "".equals(CommonUtils.token))
                CommonUtils.token = getSharedPreferences("nedfon",MODE_PRIVATE).getString("token", "");
            doDeviceInfoGet(CommonUtils.token,getSharedPreferences("nedfon",MODE_PRIVATE).getString("deviceid", ""));
            return;
        }
        info = CommonUtils.bean;
        if (info.status!=0){
            ToastUtils.show(DeviceOkActivity.this,"设备异常");
        }
        Log.e("oooooooooo","deviceid = "+info.deviceid);
//        ToastUtils.show(DeviceOkActivity.this,info.deviceid);
        mTemperatureTv.setText(info.outtmp+"℃");
        mHumidityTv.setText(info.outsweet+"%");
        mPm25Tv.setText(info.outpm25+"");

        if ((info.inpm25/2)<=25){
            mWuRanTxtTv.setText("干净");
        } else if ((info.inpm25/2)<=75 && (info.inpm25/2)>25){
            mWuRanTxtTv.setText("轻度污染");
        } else if ((info.inpm25/2)<=100 && (info.inpm25/2)>75){
            mWuRanTxtTv.setText("中度污染");
        } else if ((info.inpm25/2)>100){
            mWuRanTxtTv.setText("重度污染");
        }
        mBigTemperatureTv.setText(((int)info.intmp)+"°");
        mShiduBigTv.setText(((int)info.insweet)+"%");
        mPm25BigTv.setText(((int)info.inpm25)+"");
        //负离子否开关
        fulizi = info.ionsflag;
        //排气换气是否自动 2 自动 1 手动
        auto = info.changeOrPushModel;
        //电源开关获取 0 关机 1 开机
//        power = info.windstatus;
        power = info.workmodel;


        //排气换气自动 手动
        boolean paiqihuanqiisopen = auto==2?true:false; //2自动 1手动
        if (paiqihuanqiisopen){
            mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
            mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
        } else {
            mPaiqiHaunqiAuto.setVisibility(View.GONE);
            mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
        }
        //负离子开关
        boolean fuliziisopen = info.ionsflag==0?false:true;
        if (fuliziisopen){
            mFuliziKai.setVisibility(View.VISIBLE);
            mFuliziGuan.setVisibility(View.GONE);
        } else {
            mFuliziKai.setVisibility(View.GONE);
            mFuliziGuan.setVisibility(View.VISIBLE);
        }
        //电源开关
        boolean poweropen = power ==0?false:true;
        if (poweropen){
            mFengjiPowerKai.setVisibility(View.VISIBLE);
            mFengjiPowerGuan.setVisibility(View.GONE);
        } else {
            mFengjiPowerKai.setVisibility(View.GONE);
            mFengjiPowerGuan.setVisibility(View.VISIBLE);
        }
    }

    private boolean mIsPower = false;
    private boolean mIsIonsFlag = false;
    private boolean mIsAuto = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_exhaust_ventilation_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机处于关机状态，不能进行设置参数！");
                    return;
                }
//                Log.e("oooooooooooo", "onClick: auto = "+auto);
                if (auto == 2){
                    ToastUtils.show(DeviceOkActivity.this,"排气换气处于自动模式，不能进行设置参数！");
                    return;
                }
                info.ionsflag = fulizi;
                info.changeOrPushModel = auto;
//                info.windstatus = power;
                info.workmodel = power;
                CommonUtils.bean = null;
                CommonUtils.bean = info;
                Intent exhaustintent = new Intent(DeviceOkActivity.this,ExhaustVentilatorOkActivity.class);
                startActivity(exhaustintent);
                DeviceOkActivity.this.finish();
                break;
            case R.id.activity_param_set_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    return;
                }
                info.ionsflag = fulizi;
                info.changeOrPushModel = auto;
//                info.windstatus = power;
                info.workmodel = power;
                CommonUtils.bean = null;
                CommonUtils.bean = info;
                Intent paramtintent = new Intent(DeviceOkActivity.this,ParamsSettingOkActivity.class);
                startActivity(paramtintent);
                DeviceOkActivity.this.finish();
                break;
            case R.id.activity_timer_set_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    return;
                }
                info.ionsflag = fulizi;
                info.changeOrPushModel = auto;
//                info.windstatus = power;
                info.workmodel = power;
                CommonUtils.bean = null;
                CommonUtils.bean = info;
                Intent timerintent = new Intent(DeviceOkActivity.this,TimerSettingOkActivity.class);
                startActivity(timerintent);
                DeviceOkActivity.this.finish();
                break;
            case R.id.fulizi_bottom_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    return;
                }
                mIsIonsFlag = true;
                mIsPower = false;
                mIsAuto = false;
                //int auto1 = auto==0?1:0;
                int fulizi1 = fulizi==0?1:0;
                //int power1 = power==0?1:0;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        info.windgear+"",power+"",fulizi1+"","1",power+"");
                break;
            case R.id.paiqi_huanqi_bottom_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    return;
                }
                mIsIonsFlag = false;
                mIsPower = false;
                mIsAuto = true;
                int auto11 = auto==1?2:1;
//                Log.e("ooooooooooooo", "onClick: auto = "+auto +" auto11 = "+auto11);
                //int fulizi11 = fulizi==0?1:0;
                //int power11 = power==0?1:0;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto11+"",
                        info.windgear+"",power+"",fulizi+"","1",power+"");
                break;
            case R.id.fengji_power_rl:
                mIsIonsFlag = false;
                mIsPower = true;
                mIsAuto = false;
                //int auto10 = auto==0?1:0;
                //int fulizi10 = fulizi==0?1:0;
                int power10 = power==0?1:0;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        info.windgear+"",power10+"",fulizi+"","1",power10+"");
                break;
            default:
                break;
        }
    }

    private void doDeviceInfoGet(String token,String deviceSN){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
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
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":1,")){
                    DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
                    if (CommonUtils.bean != info.data) { //当程序里面保存的数据与获取的数据不一样的时候 更新数据
                        CommonUtils.bean = null;
                        CommonUtils.bean = info.data;
                        mHandler.sendEmptyMessage(3);
                    }
                } else if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(5);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
    /*
    http://localhost/mobileapi/controlWindCmd?token=abcvTkdjsd_1209990ijhyty&deviceSN=000000001&fanModel=30&fanLevel=1&fanOnOff=1&ionsOnOff=1&warnflag=1
    参数说明：
    工作模式 ：1自动   2手动     工作状态和工作模式是配合使用   只用工作状态处于开机(状态值为1)时 ， 才可以选择自动  手动工作模式

    如果风机开关处于待机状态  则风机转速也必须处于0状态, 如果风机处于3开启状态  则风机转速则可选 1  2档（即高低档）
    Token：登录token
    deviceSN：设备SN
    OnOff：0待机,1开机
    fanModel：工作模式 1自动  2手动
    fanLevel： 风机档位 0关机   1抵挡  2高档
    fanOnOff： 风机开关 0排气换气关机    3排气换气开机
    ionsOnOff：负离子 0关     1开
    warnflag：清洗时间提醒  0关闭   1开启

    返回值：
    成功：{'result':1,'msg':'设置成功'}

    失败：{'result':0,'msg':'设置失败'}
     */
    private void doControlWindCmdGet(String token,String deviceSN,String fanModel,String fanLevel,String fanOnOff,String ionsOnOff,String warnflag,String OnOff){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlWindCmd?" +
                "token="+token+"&deviceSN="+deviceSN+"&OnOff="+OnOff+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag).get().build();
        executeControlWindCmdRequest(request);
    }

    private void executeControlWindCmdRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":1,")){
                    mHandler.sendEmptyMessage(1);
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
                    ToastUtils.show(DeviceOkActivity.this,"设置成功！");
                    if (mIsAuto){
                        if (mPaiqiHaunqiAuto.getVisibility() == View.GONE){
                            mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
                            mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
                            auto = 2;
                        } else {
                            auto = 1;
                            mPaiqiHaunqiAuto.setVisibility(View.GONE);
                            mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
                        }
                    } else if(mIsIonsFlag){
                        if (mFuliziKai.getVisibility() == View.GONE){
                            mFuliziKai.setVisibility(View.VISIBLE);
                            mFuliziGuan.setVisibility(View.GONE);
                            fulizi = 1;
                        } else {
                            mFuliziKai.setVisibility(View.GONE);
                            mFuliziGuan.setVisibility(View.VISIBLE);
                            fulizi = 0;
                        }
                    } else if(mIsPower){
                        if (mFengjiPowerKai.getVisibility() == View.GONE){
                            mFengjiPowerKai.setVisibility(View.VISIBLE);
                            mFengjiPowerGuan.setVisibility(View.GONE);
                            power = 1;
                        } else {
                            mFengjiPowerKai.setVisibility(View.GONE);
                            mFengjiPowerGuan.setVisibility(View.VISIBLE);
                            power = 0;
                        }
                    }
                    break;
                case 2 :
                    ToastUtils.show(DeviceOkActivity.this,"其他错误");
                    break;
                case 3 :
//                    ToastUtils.show(DeviceOkActivity.this,"获取成功");
                    initData();
                    break;
                case 5 :
                    ToastUtils.show(DeviceOkActivity.this,"设置失败");
                    break;
            }
        }
    };
}
