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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.adapter.DeviceBindAdapter;
import com.nedfon.nedfon.bean.DeviceBindBean_test;
import com.nedfon.nedfon.bean.DeviceListAll;
import com.nedfon.nedfon.bean.GetCurrentWeatherAll;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.NetWorkUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.nedfon.nedfon.view.DeviceSetInternetDialog;
import com.nedfon.nedfon.view.SwitchButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeviceBindOkActivity extends BaseBottomActivity {

    private ImageView mWeatherIv;//天气图标
    private TextView mWeatherNumTv; //天气温度度数
    private TextView mWeatherTxtTv; //天气文字描述  多云|微风
    private TextView mShiduTv; //湿度百分比
    private TextView mPM25Tv; //pm25
    private TextView mCityTv; //城市

    private RelativeLayout mBindDeviceRl;
    private ListView mListView;

    private DeviceBindAdapter mAdapter;
    private List<DeviceBindBean_test> mList;

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
        if (null == mlistbean.data || 1 > mlistbean.data.size()){
            return;
        }
        mList = new ArrayList<>();
        mList.clear();
        String[] list = new String[mlistbean.data.size()];
        for (int i=0;i<mlistbean.data.size();i++){
            DeviceBindBean_test bean = new DeviceBindBean_test();
            bean.name = mlistbean.data.get(i).deviceid;
            bean.online = mlistbean.data.get(i).commStatus==0?"离线":"在线";
            bean.wendu = mlistbean.data.get(i).intmp+"°";
            bean.shidu = mlistbean.data.get(i).insweet+"%";
            bean.pm25 = mlistbean.data.get(i).inpm25+"";
            mList.add(bean);
            list[i] = mlistbean.data.get(i).deviceid;
        }
        CommonUtils.mDeviceList = list;
        Log.e("oooooooooooo","get list size = "+CommonUtils.mDeviceList.length+"  get 1 = "+CommonUtils.mDeviceList[0]);
        mAdapter.setData(mList);
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

        mAdapter = new DeviceBindAdapter(DeviceBindOkActivity.this);
        mAdapter.setData(mList);
        mListView.setAdapter(mAdapter);

        mBindDeviceRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(DeviceBindOkActivity.this, AddDeviceOkActivity.class);
                startActivity(add);
                DeviceBindOkActivity.this.finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                if (mlistbean.data.get(position).commStatus==1){ //在线
                    CommonUtils.bean = null;
                    CommonUtils.bean = mlistbean.data.get(position);
                    SharedPreferences sp = getSharedPreferences("nedfon",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("deviceid",mlistbean.data.get(position).deviceid);
                    editor.commit();
                    Intent intent = new Intent(DeviceBindOkActivity.this,DeviceOkActivity.class);
                    startActivity(intent);
                    DeviceBindOkActivity.this.finish();
//                } else { //离线
//                    final DeviceSetInternetDialog dialog = new DeviceSetInternetDialog(DeviceBindOkActivity.this, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            isWIFIOrOther();
//                        }
//                    });
//                }
            }
        });
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
            ToastUtils.show(DeviceBindOkActivity.this, "当前网络不可用");//TODO 使用该软件需要连接网络
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


    private static OkHttpClient okhttpclient = new OkHttpClient();
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
                if (res.contains(":1,")){
                    mlistbean = new Gson().fromJson(res,DeviceListAll.class);
                    mHandler.sendEmptyMessage(4);
                } else if (res.contains(":0,")){
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
                if (res.contains(":1,")){
                    bean = new Gson().fromJson(res,GetCurrentWeatherAll.class);
                    mHandler.sendEmptyMessage(3);
                } else if (res.contains(":0,")){
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
            }
        }
    };

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

}
