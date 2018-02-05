//package com.nedfon.nedfon.ui;
//
//import android.content.Context;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//
//public class DeviceInternetWIFIActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//    private TextView mWifiNameTv;
//    private EditText mWifiPwdEt;
//    private TextView mConnectPrompttv;
//    private RelativeLayout mConnectRl;
//
//    private String WIFINAME = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_internet_wifi);
//
//        initView();
//    }
//
//    private String getConnectWifiSsid(){
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        return wifiInfo.getSSID();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_device_internet_wifi_back_tv);
//        mWifiNameTv = this.findViewById(R.id.activity_device_internet_wifi_name_tv);
//        mWifiPwdEt = this.findViewById(R.id.activity_device_internet_wifi_pwd_et);
//        mConnectPrompttv = this.findViewById(R.id.activity_device_internet_wifi_connect_prompt_tv);
//        mConnectRl = this.findViewById(R.id.activity_device_internet_wifi_connect_rl);
//
//        WIFINAME = getConnectWifiSsid();
//        mWifiNameTv.setText("".equals(WIFINAME)?"空":WIFINAME.substring(1,WIFINAME.length()-1));
//
//        mBackTv.setOnClickListener(this);
//        mConnectRl.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_device_internet_wifi_back_tv:
//                this.finish();
//                break;
//            case R.id.activity_device_internet_wifi_connect_rl:
//                //连接设备
//                break;
//            default:
//                break;
//        }
//    }
//}
