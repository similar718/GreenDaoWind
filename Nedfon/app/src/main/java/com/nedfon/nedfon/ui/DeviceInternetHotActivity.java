//package com.nedfon.nedfon.ui;
//
//import android.content.Context;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//
//import java.lang.reflect.Method;
//
//public class DeviceInternetHotActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mConnectPrompttv;
//    private RelativeLayout mConnectRl;
//
//
//    private WifiManager wifiManager;
//    private boolean flag=false;
//
//    private String name = "nedfon001";
//    private String pwd = "nedfon123456";
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_internet_hot);
//
//        initView();
//    }
//
//    private void initView() {
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        mConnectPrompttv = this.findViewById(R.id.activity_device_internet_hot_connect_prompt_tv);
//        mConnectRl = this.findViewById(R.id.activity_device_internet_hot_connect_rl);
//
//        mConnectRl.setOnClickListener(this);
//
//        Boolean open = setWifiApEnabled(flag);
//        if (open){
//            mConnectPrompttv.setText("热点成功开启！");
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_device_internet_hot_connect_rl:
//                //连接设备
//                break;
//            default:
//                break;
//        }
//    }
//
//    // wifi热点开关
//    public boolean setWifiApEnabled(boolean enabled) {
//        if (enabled) { // disable WiFi in any case
//            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
//            wifiManager.setWifiEnabled(false);
//        }
//        try {
//            //热点的配置类
//            WifiConfiguration apConfig = new WifiConfiguration();
//            //配置热点的名称(可以在名字后面加点随机数什么的)
//            apConfig.SSID = name;
//            //配置热点的密码
//            apConfig.preSharedKey = pwd;
//            //通过反射调用设置热点
//            Method method = wifiManager.getClass().getMethod(
//                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
//            //返回热点打开状态
//            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
