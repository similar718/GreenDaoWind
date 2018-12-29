package com.nedfon.nedfon.uiok;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.adapter.DeviceBindAdapter;
import com.nedfon.nedfon.adapter.DeviceBindUpdateAdapter;
import com.nedfon.nedfon.bean.DeviceBindBean_test;
import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.DeviceInfoAll;
import com.nedfon.nedfon.bean.DeviceListAll;
import com.nedfon.nedfon.bean.GetCurrentWeatherAll;
import com.nedfon.nedfon.bean.GetPersonInfoAllBean;
import com.nedfon.nedfon.db.MyDBHelper;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.NetWorkUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.nedfon.nedfon.view.DeviceSetInternetDialog;

import org.java_websocket.WebSocket;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import stomp.Stomp;
import stomp.client.StompClient;
import stomp.client.StompMessage;

public class DeviceBindOkActivity extends BaseBottomActivity {

    private ImageView mWeatherIv;//天气图标
    private TextView mWeatherNumTv; //天气温度度数
    private TextView mWeatherTxtTv; //天气文字描述  多云|微风
    private TextView mShiduTv; //湿度百分比
    private TextView mPM25Tv; //pm25
    private TextView mCityTv; //城市

    private RelativeLayout mBindDeviceRl;
    private ListView mListView;

//    private DeviceBindAdapter mAdapter;
    private DeviceBindUpdateAdapter mAdapter;
    private List<DeviceBindBean_test> mList;
    private List<DeviceInfo> mListData;

    private Thread mThread;
    private boolean mIsStopThread = false;
    private boolean isEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        mThread = new GetDeviceInfoThread();
        mThread.start();

        NAME = DeviceBindOkActivity.class.getSimpleName();
        setImage(3);
        Log.e("oooooooooo", "onCreateView: token = "+ CommonUtils.token);
        if (null == CommonUtils.token || "".equals(CommonUtils.token))
            CommonUtils.token = getSharedPreferences("nedfon",MODE_PRIVATE).getString("token", "");
        doGetCurrentWeatherGet(CommonUtils.token);


        //创建client 实例
        createStompClient();
        //订阅消息
        registerStompTopic();
    }

    private class GetDeviceInfoThread extends Thread{
        @Override
        public void run() {
            while(!isEnd) {
                while (!mIsStopThread) {
                    try {
                        Thread.sleep(5 * 1000);//每10秒刷新一次  更改为5秒
                        doDeviceListGet(CommonUtils.token);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_bind_ok;
    }

    private void initData() {
        if (mlistbean.data == mListData){
            return;
        }
        if (null == mlistbean.data || 1 > mlistbean.data.size()){
            CommonUtils.mDeviceList = null;
            return;
        }
        mList = new ArrayList<>();
        mList.clear();
        String[] list = new String[mlistbean.data.size()];
        for (int i=0;i<mlistbean.data.size();i++){
            DeviceBindBean_test bean = new DeviceBindBean_test();
            bean.name = mlistbean.data.get(i).deviceid+"  "+mlistbean.data.get(i).terminal;
            bean.online = mlistbean.data.get(i).commStatus==0?"离线":"在线";
            bean.wendu = mlistbean.data.get(i).intmp+"°";
            bean.shidu = mlistbean.data.get(i).insweet+"%";
            bean.pm25 = mlistbean.data.get(i).inpm25+"";
            mList.add(bean);
            list[i] = mlistbean.data.get(i).terminal+ " " + mlistbean.data.get(i).sn ; //TODO 严工定的显示数据的
        }
        CommonUtils.mDeviceList = list;//查询数据时显示的list
        mListData = mlistbean.data;
        mAdapter.setData(mListData);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mIsStopThread = false;
        mHandler.sendEmptyMessage(6);
    }

    private void initView() {
        mWeatherIv = this.findViewById(R.id.fragment_device_bind_weather_iv);
        mWeatherNumTv = this.findViewById(R.id.fragment_device_bind_weather_num_tv);
        mWeatherTxtTv = this.findViewById(R.id.fragment_device_bind_weather_txt_tv);
        mShiduTv = this.findViewById(R.id.fragment_device_bind_shidu_tv);
        mPM25Tv = this.findViewById(R.id.fragment_device_bind_pm25_tv);
        mCityTv = this.findViewById(R.id.fragment_device_city_tv);
        mBindDeviceRl = this.findViewById(R.id.fragmet_device_bind_add_device_rl);
        mListView = this.findViewById(R.id.fragment_device_bind_device_lv);

        mAdapter = new DeviceBindUpdateAdapter(DeviceBindOkActivity.this);
        mAdapter.setData(mListData);
        mListView.setAdapter(mAdapter);

        mBindDeviceRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(DeviceBindOkActivity.this, AddDeviceOkActivity.class);
                startActivity(add);
                DeviceBindOkActivity.this.finish();
            }
        });

        mAdapter.setOnItemDeviceOptionClickListener(new DeviceBindUpdateAdapter.OnItemDeviceOptionClickListener() { //item里面数据的点击事件
            @Override
            public void onItemPowerClick(int position, DeviceInfo data) {
//                ToastUtils.show(DeviceBindOkActivity.this,"time = " + System.currentTimeMillis());
//                Log.e("ooooooooo","time start = " + System.currentTimeMillis());
                //服务器接口的操作 TODO 判断设备是否在线 在线情况下才能进行操作
                //workModel 表示是否关机  workgear  高低
                if (data.commStatus != 0) {
                    int power = data.workmodel == 0 ? 1 : 0;//电源的开关
                    doControlWindCmdGet(CommonUtils.token, data.deviceid, data.changeOrPushModel + "",
                            data.workgear + "", power + "", data.ionsflag + "", 1 + "", power + "");
                    mListData.get(position).workmodel = power;
                } else {
                    ToastUtils.show(DeviceBindOkActivity.this,"设备不在线，设置失败");
                }
            }

            @Override
            public void onItemExhuastVentilationClick(int position, DeviceInfo data) {
                //服务器接口的操作 TODO 判断设备是否在线 在线情况下才能进行操作
//                Log.e("ooooooooo","time start1 = " + System.currentTimeMillis());
                //workModel 表示是否关机  workgear  高低
                if (data.commStatus != 0) { //判断设备是否在线
                    if (data.workmodel != 0) { //判断设备是否开机
                        if (data.changeOrPushModel != 2) { //判断风机是自动 还是手动  手动才可以设置高低 TODO
                            if (data.workgear != 0) { //判断设备排风送风是否打开
                                int isdi = data.workgear == 1 ? 2 : 1;//风速的高低
                                doControlWindCmdGet(CommonUtils.token, data.deviceid, data.changeOrPushModel + "",
                                        isdi + "", data.workmodel + "", data.ionsflag + "", 1 + "", data.workmodel + "");
                                mListData.get(position).workgear = isdi;
                            } else {
                                ToastUtils.show(DeviceBindOkActivity.this, "当前设备排风送风没有进行设置，不能进行操作");
                            }
                        } else {
                            ToastUtils.show(DeviceBindOkActivity.this,"排气换气处于自动模式，不能进行设置参数！");
                        }
                    } else {
                        ToastUtils.show(DeviceBindOkActivity.this, "设备未开机，不能进行操作");
                    }
                } else {
                    ToastUtils.show(DeviceBindOkActivity.this,"设备不在线，设置失败");
                }
            }

            @Override
            public void onItemDetailClick(int position, DeviceInfo data) {
                CommonUtils.bean = null;
                CommonUtils.bean = mlistbean.data.get(position);
                SharedPreferences sp = getSharedPreferences("nedfon",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("deviceid",mlistbean.data.get(position).deviceid);
                editor.commit();
                Intent intent = new Intent(DeviceBindOkActivity.this,DeviceOkActivity.class);
                startActivity(intent);
                DeviceBindOkActivity.this.finish();
            }
        });

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
////                if (mlistbean.data.get(position).commStatus==1){ //在线
//                    CommonUtils.bean = null;
//                    CommonUtils.bean = mlistbean.data.get(position);
//                    SharedPreferences sp = getSharedPreferences("nedfon",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("deviceid",mlistbean.data.get(position).deviceid);
//                    editor.commit();
//                    Intent intent = new Intent(DeviceBindOkActivity.this,DeviceOkActivity.class);
//                    startActivity(intent);
//                    DeviceBindOkActivity.this.finish();
////                } else { //离线
////                    final DeviceSetInternetDialog dialog = new DeviceSetInternetDialog(DeviceBindOkActivity.this, new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            isWIFIOrOther();
////                        }
////                    });
////                }
//            }
//        });

        if (CommonUtils.mIsFlagShowRedPoint) {
            doGetPersonInfoGet(CommonUtils.token);
            CommonUtils.mIsFlagShowRedPoint = false;
        }
    }

    private void isWIFIOrOther(){
        int type = NetWorkUtils.getAPNType(DeviceBindOkActivity.this);
        if (type == 1) {
            ToastUtils.show(DeviceBindOkActivity.this, "当前属于连接WiFi状态");
            Intent wifi = new Intent(DeviceBindOkActivity.this, DeviceInternetWifiOkActivity.class);
            startActivity(wifi);
            this.finish();
            return;
        } else if(type == 0) {
            ToastUtils.show(DeviceBindOkActivity.this, "当前网络不可用");
            AlertDialog.Builder builder = new AlertDialog.Builder(DeviceBindOkActivity.this);
            builder.setTitle("提示").setMessage("您已经进入到了没有网络的异次元，请打开您的网络或者连接WiFi。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (type == 2 || type == 3){
            ToastUtils.show(DeviceBindOkActivity.this, "3G网络 或者 2G网络");
            Intent hot = new Intent(DeviceBindOkActivity.this, DeviceInternetHotOkActivity.class);
            startActivity(hot);
            this.finish();
        }else {
            ToastUtils.show(DeviceBindOkActivity.this,"当前不知道什么网络");
        }
    }

    @Override
    protected void setBackOnClick() {
        DeviceBindOkActivity.this.finish();
    }


    /**
     *     获取设备列表
     */
    private  void doDeviceListGet(String token){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/deviceList?token="+token).get().build();
        executeDeviceListRequest(request);
    }

    private DeviceListAll mlistbean = null;
    private void executeDeviceListRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mSuccess)){
                    mlistbean = new Gson().fromJson(res,DeviceListAll.class);
                    mHandler.sendEmptyMessage(4);
                } else if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(5);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsStopThread = false;
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

    /**
     *     获取当天天气信息
     */
    private  void doGetCurrentWeatherGet(String token){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/getCurrentWeather?token="+token).get().build();
        executeGetCurrentWeatherRequest(request);
    }

    private GetCurrentWeatherAll bean = null;

    private void executeGetCurrentWeatherRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mSuccess)){
                    bean = new Gson().fromJson(res,GetCurrentWeatherAll.class);
                    mHandler.sendEmptyMessage(3);
                } else if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(1);
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
                    ToastUtils.show(DeviceBindOkActivity.this,"获取天气信息失败！");
                    break;
                case 21:
//                    ToastUtils.show(DeviceBindOkActivity.this,"设置成功");
                    mAdapter.setData(mListData);
                    break;
                case 2 :
                    ToastUtils.show(DeviceBindOkActivity.this,"其他错误");
                    break;
                case 3 :
                    updateView();
                    break;
                case 5 :
                    ToastUtils.show(DeviceBindOkActivity.this,"获取设备列表失败");
                    break;
                case 4 :
                    updateListView();
                    break;
                case 6 :
                    doDeviceListGet(CommonUtils.token);
                    break;

                case 13 : //TODO
                    if (mBean.data.loginCount!=0&&!"".equals(mBean.data.loginCount+"")) {
                        CommonUtils.mIsShowRedPoint = true;
                        setShowRedDot();
                    }
                    break;

                case 17: //超时异常
                    ToastUtils.show(DeviceBindOkActivity.this,"超时异常",1000);
                    break;

                case 14: //连接超时
                    ToastUtils.show(DeviceBindOkActivity.this,"连接异常",1000);
                    break;

                case 15:
                    ToastUtils.show(DeviceBindOkActivity.this,mError,1000);
                    break;
            }
        }
    };

    private  void doGetPersonInfoGet(String token){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        // http://localhost:9090/mobileapi/getPersonInfo?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTk5NTg5MjksInVzZXJuYW1lIjoi
        // MTM1MTI3NzQ3NjAifQ.DP22dBsyMqnPoQyMw0KV51WN_OImBxI8rfphBS-eWfs
        Request request = builder.url(CommonUtils.localhost+"mobileapi/getPersonInfo?token="+token).get().build();
        executeGetPersonInfoRequest(request);
    }

    private GetPersonInfoAllBean mBean = null;

    private void executeGetPersonInfoRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("ooooooooooooooo",res);
                if (res.contains(CommonUtils.mSuccess)){
                    mBean = new Gson().fromJson(res,GetPersonInfoAllBean.class);
                    mHandler.sendEmptyMessage(13);
                } else if (res.contains(CommonUtils.mFailed)){
                } else {
                }
            }
        });
    }

    private void updateListView() {
        initData();
    }

    private void updateView() {
        mWeatherNumTv.setText(bean.weather.wd+"°");
        mCityTv.setText(bean.weather.cityname);
        mWeatherTxtTv.setText(bean.weather.qx+" | "+bean.weather.fx);
        mShiduTv.setText("湿度"+bean.weather.sd+"");
        mPM25Tv.setText("PM2.5 "+bean.weather.pm25+"");
        mHandler.sendEmptyMessage(6);
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();;
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
        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlWindCmd?" + "token="+token+"&deviceSN="+deviceSN+"&OnOff="
                +OnOff+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag).get().build();
        executeControlWindCmdRequest(request);
    }
    String mError = "";
    private void executeControlWindCmdRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                if (e instanceof SocketTimeoutException) {
                    // 判断超时异常
                    mHandler.sendEmptyMessage(17);
                }
                if (e instanceof ConnectException) {
                    // 判断连接异常
                    mHandler.sendEmptyMessage(14);
                }
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
//                Log.e("ooooooooo","time end = " + System.currentTimeMillis());
                final String res = response.body().string();
                if (res.contains(CommonUtils.mSuccess)){
                    mHandler.sendEmptyMessage(21);
                } else if (res.contains(CommonUtils.mFailed)){
                    DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
                    mError = info.msg;
                    mHandler.sendEmptyMessage(15);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    private StompClient mStompClient;

    private void createStompClient() {
        mStompClient = Stomp.over(WebSocket.class, CommonUtils.localhostwebsocket);//ws://111.231.234.151:9090
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

    private String getUserPhone(){
        MyDBHelper dbHelper = new MyDBHelper(this);
        return dbHelper.query().get(0).phone;
    }

    // 接收/user/xiaoli/message路径发布的消息
    private void registerStompTopic() {
        if (CommonUtils.phone == null || "".equals(CommonUtils.phone)){
            CommonUtils.phone = getUserPhone();
        }
        mStompClient.topic("/user/"+CommonUtils.phone+"/msg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.e("oooooooooo", "Received " + topicMessage.getPayload());
//                    String res = topicMessage.getPayload();
//                    if (res.contains(CommonUtils.mSuccess)){
//                        //TODO  获取服务器那边推送过来的数据
//                        DeviceInfoAll info = new Gson().fromJson(res,DeviceInfoAll.class);
//                        if (info == null){
//                            return;
//                        }
////                        if (CommonUtils.bean != info.data && info.data.deviceid.equals(DeviceSN)) { //当程序里面保存的数据与获取的数据不一样的时候 更新数据
////                            mIsLoading = false;
////                            CommonUtils.bean = null;
////                            CommonUtils.bean = info.data;
////                            mHandler.sendEmptyMessage(3);
////                        }
//                    }
//                    showMessage(topicMessage);
                    doDeviceListGet(CommonUtils.token);
                });
        if (isInit) {
            isInit = false;
            mThread = new GetDeviceInfoThread1();
            mThread.start();
        }
    }
    private void showMessage(final StompMessage stompMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView text = new TextView(DeviceBindOkActivity.this);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setText(System.currentTimeMillis() +" body is --->"+stompMessage.getPayload());
            }
        });
    }
    private boolean isInit = true;
    private static int IsIntervalSendNum = 0;
    private static int num = 0;

    private void sendMessage(){
        if (CommonUtils.phone == null || "".equals(CommonUtils.phone)){
            CommonUtils.phone = getUserPhone();
        }
        // 向/app/cheat发送Json数据
        mStompClient.send("/ws-push/welcome","{'name':'"+CommonUtils.phone+"'}").subscribe(new Subscriber<Void>() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.e("DeviceBindOkActivity","sendMessage = OnError");
            }
            @Override
            public void onComplete() {
                IsIntervalSendNum = 0;
                Log.e("DeviceBindOkActivity","sendMessage = onComplete");
            }

            @Override
            public void onSubscribe(Subscription s) {
                IsIntervalSendNum++;
                Log.e("DeviceBindOkActivity","sendMessage = onSubscribe");
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
                Log.e("DeviceBindOkActivity","sendMessage = onNext");
            }
        });
    }
    private class GetDeviceInfoThread1 extends Thread{
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
}
