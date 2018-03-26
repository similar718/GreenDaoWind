package com.nedfon.nedfon.uiok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.DeviceInfoAll;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.nedfon.nedfon.view.SwitchButton;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import org.java_websocket.WebSocket;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import stomp.LifecycleEvent;
import stomp.Stomp;
import stomp.client.StompClient;
import stomp.client.StompMessage;

public class DeviceOkActivity extends BaseTopBottomActivity implements View.OnClickListener{

    private TextView mShiwaiTv,mTemperatureTv,mHumidityTv,mPm25Tv;
    private RelativeLayout /*mExhaustVentilationRl,*/mParamsRl,mTimerRl;

    private RelativeLayout mExhuastVentilationRlDi,mExhuastVentilationRlGao;

    private RelativeLayout mFuliziRl;
    private ImageView mFuliziKai,mFuliziGuan;

    private RelativeLayout mPaiqiHaunqiRl;
    private ImageView mPaiqiHaunqiAuto,mPaiqiHaunqiNoAuto;

    private RelativeLayout mFengjiPowerRl;
    private ImageView mFengjiPowerKai,mFengjiPowerGuan;


    private SwitchButton mPowerSb;
    private SwitchButton mPaiqiHuanqiSb;
    private SwitchButton mFuliziSb;

    private TextView mWuRanTxtTv;
    private TextView mBigTemperatureTv;

    private TextView mShiduBigTv,mPm25BigTv;

    private DeviceInfo info = null;
    private int fulizi = 0;
    private int auto = 0;
    private int power = 0;

    private int isdi = 0;
    private int misdi = 0;

    private boolean mIsStopThread = false;
    private boolean isEnd = false;
    private Thread mThread;

    private StompClient mStompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("设 备");

        initView();
//        mThread = new GetDeviceInfoThread();
//        mThread.start();

        //创建client 实例
        createStompClient();
        //订阅消息
        registerStompTopic();
    }


    private void createStompClient() {
        mStompClient = Stomp.over(WebSocket.class, "ws://111.231.234.151:9090/endpointWisely/websocket");//ws://111.231.234.151:9090
        mStompClient.connect();
        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d("oooooooo", "Stomp connection opened");
                            //toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e("oooooooooooo", "Stomp connection error", lifecycleEvent.getException());
                            //toast("Stomp connection error");
                            break;
                        case CLOSED:
                            Log.d("oooooooo", "Stomp connection closed");
                            //toast("Stomp connection closed");
                            break;
                    }
                });
    }
private boolean isInit = true;

    // 接收/user/xiaoli/message路径发布的消息
    private void registerStompTopic() {
        mStompClient.topic("/user/"+info.userphone+"/msg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.e("oooooooooo", "Received " + topicMessage.getPayload());
                    String res = topicMessage.getPayload();
                    if (res.contains(":1,")){
                        DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
                        if (CommonUtils.bean != info.data) { //当程序里面保存的数据与获取的数据不一样的时候 更新数据
                            CommonUtils.bean = null;
                            CommonUtils.bean = info.data;
                            mHandler.sendEmptyMessage(3);
                        }
                    }
                    showMessage(topicMessage);
                });
        if (isInit) {
            isInit = false;
            mThread = new GetDeviceInfoThread();
            mThread.start();
        }
    }

    private static int IsIntervalSendNum = 0;
    private static int num = 0;

    private void sendMessage(){
        // 向/app/cheat发送Json数据
        mStompClient.send("/ws-push/welcome","{'name':"+info.userphone+"}").subscribe(new Subscriber<Void>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("DeviceOkActivity","sendMessage = OnError");
                    }
                    @Override
                    public void onComplete() {
                        IsIntervalSendNum = 0;
                        Log.e("DeviceOkActivity","sendMessage = onComplete");
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                        IsIntervalSendNum++;
                        Log.e("DeviceOkActivity","sendMessage = onSubscribe");
                        if (IsIntervalSendNum>10){
                            if (num<5) {
                                num++;
                                mHandler.sendEmptyMessage(12);
                            } else {
                                setBackOnClick();
                            }
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.e("DeviceOkActivity","sendMessage = onNext");
                    }
                });
    }

    private void showMessage(final StompMessage stompMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView text = new TextView(DeviceOkActivity.this);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setText(System.currentTimeMillis() +" body is --->"+stompMessage.getPayload());
            }
        });
    }


    private void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.show(DeviceOkActivity.this,message);
            }
        });
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
                        Thread.sleep(5 * 1000);//每10秒刷新一次  更改为5秒
//                        doDeviceInfoGet(CommonUtils.token, CommonUtils.bean.deviceid);
                        sendMessage();
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
        mStompClient.disconnect();
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

        mExhuastVentilationRlGao = this.findViewById(R.id.exhaust_ventilation_gao_rl);
        mExhuastVentilationRlDi = this.findViewById(R.id.exhaust_ventilation_di_rl);

        mPowerSb = this.findViewById(R.id.power_sb);
        mPaiqiHuanqiSb = this.findViewById(R.id.paiqi_huanqi_sb);
        mFuliziSb = this.findViewById(R.id.fulizi_sb);

        mExhuastVentilationRlDi.setOnClickListener(this);
        mExhuastVentilationRlGao.setOnClickListener(this);
        mParamsRl.setOnClickListener(this);
        mTimerRl.setOnClickListener(this);
        mFuliziRl.setOnClickListener(this);
        mPaiqiHaunqiRl.setOnClickListener(this);
        mFengjiPowerRl.setOnClickListener(this);

        mPowerSb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                mIsIonsFlag = false;
                mIsPower = true;
                mIsAuto = false;
                mIsHuanqi = false;
                int power10 = power == 0?1:0;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        isdi+"",power10+"",fulizi+"","1",power10+"");
            }
        });
        mPaiqiHuanqiSb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    mHandler.sendEmptyMessage(10);
                    return;
                }
                mIsIonsFlag = false;
                mIsPower = false;
                mIsAuto = true;
                mIsHuanqi = false;
                int auto11 = auto==2?1:2;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto11+"",
                        isdi+"",power+"",fulizi+"","1",power+"");
            }
        });

        mFuliziSb.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
                    mHandler.sendEmptyMessage(11);
                    return;
                }
                mIsIonsFlag = true;
                mIsPower = false;
                mIsAuto = false;
                mIsHuanqi = false;
                int fulizi1 = fulizi==0?1:0;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        isdi+"",power+"",fulizi1+"","1",power+"");
            }
        });

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
        power = info.workmodel;


        //排气换气自动 手动
        boolean paiqihuanqiisopen = auto==2?true:false; //2自动 1手动
        if (paiqihuanqiisopen){
//            mPaiqiHuanqiSb.setEnabled(true);
            SwitchButton.flag = true;
            mPaiqiHuanqiSb.setChecked(true);
//            mPaiqiHuanqiSb.setEnabled(false);
//            mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
//            mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
        } else {
//            mPaiqiHuanqiSb.setEnabled(true);
            SwitchButton.flag = true;
            mPaiqiHuanqiSb.setChecked(false);
//            mPaiqiHuanqiSb.setEnabled(false);
//            mPaiqiHaunqiAuto.setVisibility(View.GONE);
//            mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
        }
        //负离子开关
        boolean fuliziisopen = info.ionsflag==0?false:true;
        if (fuliziisopen){
//            mFuliziSb.setEnabled(true);
            SwitchButton.flag = true;
            mFuliziSb.setChecked(true);
//            mFuliziSb.setEnabled(false);
//            mFuliziKai.setVisibility(View.VISIBLE);
//            mFuliziGuan.setVisibility(View.GONE);
        } else {
//            mFuliziSb.setEnabled(true);
            SwitchButton.flag = true;
            mFuliziSb.setChecked(false);
//            mFuliziSb.setEnabled(false);
//            mFuliziKai.setVisibility(View.GONE);
//            mFuliziGuan.setVisibility(View.VISIBLE);
        }
        //电源开关
        boolean poweropen = power ==0?false:true;
        if (poweropen){
//            mPowerSb.setEnabled(true);
            SwitchButton.flag = true;
            mPowerSb.setChecked(true);
//            mPowerSb.setEnabled(false);
//            mFengjiPowerKai.setVisibility(View.VISIBLE);
//            mFengjiPowerGuan.setVisibility(View.GONE);
        } else {
//            mPowerSb.setEnabled(true);
            SwitchButton.flag = true;
            mPowerSb.setChecked(false);
//            mPowerSb.setEnabled(false);
//            mFengjiPowerKai.setVisibility(View.GONE);
//            mFengjiPowerGuan.setVisibility(View.VISIBLE);
        }

        isdi = info.workgear;
        if (isdi==1){
            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_selected);
            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_unselect);
        } else if (isdi==2) {
            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_unselect);
            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_selected);
        } else {
            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_unselect);
            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_unselect);
        }
    }

    private boolean mIsPower = false;
    private boolean mIsIonsFlag = false;
    private boolean mIsAuto = false;
    private boolean mIsHuanqi = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_exhaust_ventilation_rl:
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机处于关机状态，不能进行设置参数！");
                    return;
                }
                if (auto == 2){
                    ToastUtils.show(DeviceOkActivity.this,"排气换气处于自动模式，不能进行设置参数！");
                    return;
                }
                info.ionsflag = fulizi;
                info.changeOrPushModel = auto;
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
                info.workmodel = power;
                CommonUtils.bean = null;
                CommonUtils.bean = info;
                Intent timerintent = new Intent(DeviceOkActivity.this,TimerSettingOkActivity.class);
                startActivity(timerintent);
                DeviceOkActivity.this.finish();
                break;
            case R.id.fulizi_bottom_rl:
//                if (power == 0){
//                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
//                    return;
//                }
//                mIsIonsFlag = true;
//                mIsPower = false;
//                mIsAuto = false;
//                mIsHuanqi = false;
//                int fulizi1 = fulizi==0?1:0;
//                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
//                        isdi+"",power+"",fulizi1+"","1",power+"");
                break;
            case R.id.paiqi_huanqi_bottom_rl:
//                if (power == 0){
//                    ToastUtils.show(DeviceOkActivity.this,"风机出于关机状态，不能进行设置参数！");
//                    return;
//                }
//                mIsIonsFlag = false;
//                mIsPower = false;
//                mIsAuto = true;
//                mIsHuanqi = false;
//                int auto11 = auto==1?2:1;
//                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto11+"",
//                        isdi+"",power+"",fulizi+"","1",power+"");
                break;
            case R.id.fengji_power_rl:
//                mIsIonsFlag = false;
//                mIsPower = true;
//                mIsAuto = false;
//                mIsHuanqi = false;
//                int power10 = power==0?1:0;
//                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
//                        isdi+"",power10+"",fulizi+"","1",power10+"");
                break;

            case R.id.exhaust_ventilation_gao_rl://高档位
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机处于关机状态，不能进行设置参数！");
                    return;
                }
                if (auto == 2){
                    ToastUtils.show(DeviceOkActivity.this,"排气换气处于自动模式，不能进行设置参数！");
                    return;
                }
                if (isdi == 2){
                    return;
                }
                misdi = 2;
                mIsIonsFlag = false;
                mIsPower = false;
                mIsAuto = false;
                mIsHuanqi = true;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        misdi+"",power+"",fulizi+"","1",power+"");
                break;

            case R.id.exhaust_ventilation_di_rl: //低档位
                if (power == 0){
                    ToastUtils.show(DeviceOkActivity.this,"风机处于关机状态，不能进行设置参数！");
                    return;
                }
                if (auto == 2){
                    ToastUtils.show(DeviceOkActivity.this,"排气换气处于自动模式，不能进行设置参数！");
                    return;
                }
                if (isdi == 1){
                    return;
                }
                misdi = 1;
                mIsIonsFlag = false;
                mIsPower = false;
                mIsAuto = false;
                mIsHuanqi = true;
                doControlWindCmdGet(CommonUtils.token,info.deviceid,auto+"",
                        misdi+"",power+"",fulizi+"","1",power+"");
                break;

            default:
                break;
        }
    }

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
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
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
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
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
                        if (auto != 2){
                            SwitchButton.flag = true;
                            mPaiqiHuanqiSb.setChecked(false);
//                            mPaiqiHaunqiAuto.setVisibility(View.VISIBLE);
//                            mPaiqiHaunqiNoAuto.setVisibility(View.GONE);
                            auto = 2;
                        } else {
                            auto = 1;
                            SwitchButton.flag = true;
                            mPaiqiHuanqiSb.setChecked(true);
//                            mPaiqiHaunqiAuto.setVisibility(View.GONE);
//                            mPaiqiHaunqiNoAuto.setVisibility(View.VISIBLE);
                        }
                    } else if(mIsIonsFlag){
                        if (fulizi != 1){
                            SwitchButton.flag = true;
                            mFuliziSb.setChecked(true);
//                            mFuliziKai.setVisibility(View.VISIBLE);
//                            mFuliziGuan.setVisibility(View.GONE);
                            fulizi = 1;
                        } else {
                            SwitchButton.flag = true;
                            mFuliziSb.setChecked(false);
//                            mFuliziKai.setVisibility(View.GONE);
//                            mFuliziGuan.setVisibility(View.VISIBLE);
                            fulizi = 0;
                        }
                    } else if(mIsPower){
                        if (power != 1){
                            SwitchButton.flag = true;
                            mPowerSb.setChecked(true);
//                            mFengjiPowerKai.setVisibility(View.VISIBLE);
//                            mFengjiPowerGuan.setVisibility(View.GONE);
                            power = 1;
                        } else {
                            SwitchButton.flag = true;
                            mPowerSb.setChecked(false);
//                            mFengjiPowerKai.setVisibility(View.GONE);
//                            mFengjiPowerGuan.setVisibility(View.VISIBLE);
                            power = 0;
                        }
                    } else if(mIsHuanqi){
                        isdi = misdi;
                        if (isdi==1){
                            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_selected);
                            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_unselect);
                        } else if (isdi==2) {
                            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_unselect);
                            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_selected);
                        } else {
                            mExhuastVentilationRlDi.setBackgroundResource(R.drawable.stall_btn_unselect);
                            mExhuastVentilationRlGao.setBackgroundResource(R.drawable.stall_btn_unselect);

                        }
                    }
                    break;
                case 2 :
                    ToastUtils.show(DeviceOkActivity.this,"其他错误");
                    if (mIsAuto){
                        SwitchButton.flag = true;
                        mPaiqiHuanqiSb.setChecked(auto==2?true:false);
                    } else if(mIsIonsFlag){
                        SwitchButton.flag = true;
                        mFuliziSb.setChecked(fulizi==1?true:false);
                    } else if(mIsPower){
                        SwitchButton.flag = true;
                        mPowerSb.setChecked(power == 1?true:false);
                    }
                    break;
                case 3 :
                    initData();
                    break;
                case 5 :
                    ToastUtils.show(DeviceOkActivity.this,"设置失败,可能设备离线！");
                    if (mIsAuto){
                        SwitchButton.flag = true;
                        mPaiqiHuanqiSb.setChecked(auto==2?true:false);
                    } else if(mIsIonsFlag){
                        SwitchButton.flag = true;
                        mFuliziSb.setChecked(fulizi==1?true:false);
                    } else if(mIsPower){
                        SwitchButton.flag = true;
                        mPowerSb.setChecked(power == 1?true:false);
                    }
                    break;

                case 10:
                    SwitchButton.flag = true;
                    mPaiqiHuanqiSb.setChecked(auto == 2?true:false);
                    break;

                case 11:
                    SwitchButton.flag = true;
                    mFuliziSb.setChecked(fulizi==1?true:false);
                    break;

                case 12:
                    //创建client 实例
                    createStompClient();
                    //订阅消息
                    registerStompTopic();
                    break;
            }
        }
    };
}
