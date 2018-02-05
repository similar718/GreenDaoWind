package com.nedfon.nedfon.uiok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.demo_activity.EspWifiAdminSimple;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.ToastUtils;

import java.lang.reflect.Method;
import java.util.List;

public class DeviceInternetHotOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mConnectPrompttv;
    private RelativeLayout mConnectRl;


    private WifiManager wifiManager;
    private boolean flag=false;

    private String name = "nedfon001";
    private String pwd = "nedfon123456";

    private boolean isopen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitleText("设备配网");
        NAME = DeviceInternetHotOkActivity.class.getSimpleName();
        setImage(2);
        mWifiAdmin = new EspWifiAdminSimple(this);

        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_internet_hot_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(DeviceInternetHotOkActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        DeviceInternetHotOkActivity.this.finish();
    }

    private void initView() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mConnectPrompttv = this.findViewById(R.id.activity_device_internet_hot_connect_prompt_tv);
        mConnectRl = this.findViewById(R.id.activity_device_internet_hot_connect_rl);

        mConnectRl.setOnClickListener(this);

        Boolean open = setWifiApEnabled(flag);
        if (open){
            mConnectPrompttv.setText("热点成功开启！");
            isopen = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_device_internet_hot_connect_rl:
                //连接设备
                if (isopen){
                    setConnectDevice();
                } else {
                    ToastUtils.show(DeviceInternetHotOkActivity.this,"热点开始失败,请稍后再试");
                    Boolean open = setWifiApEnabled(flag);
                    if (open){
                        mConnectPrompttv.setText("热点成功开启！");
                        isopen = true;
                    }
                }
                break;
            default:
                break;
        }
    }

    // wifi热点开关
    public boolean setWifiApEnabled(boolean enabled) {
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = name;
            //配置热点的密码
            apConfig.preSharedKey = pwd;
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch (Exception e) {
            return false;
        }
    }

    private EspWifiAdminSimple mWifiAdmin;

    private void setConnectDevice(){
        String apSsid = name;
        String apPassword = pwd;
        String apBssid = mWifiAdmin.getWifiConnectedBssid();
        String taskResultCountStr = Integer.toString(1);
        if (__IEsptouchTask.DEBUG) {
            Log.d("ooooooooooooo", "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
                    + ", " + " mEdtApPassword = " + apPassword);
        }
        new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, taskResultCountStr);
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(DeviceInternetHotOkActivity.this, text,
                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

        private ProgressDialog mProgressDialog;

        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(DeviceInternetHotOkActivity.this);
            mProgressDialog
                    .setMessage("Esptouch is configuring, please wait for a moment...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {
                        if (__IEsptouchTask.DEBUG) {
                            Log.i("oooooooooooo", "progress dialog is canceled");
                        }
                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Waiting...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(false);
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                // !!!NOTICE
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, DeviceInternetHotOkActivity.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
                    "Confirm");
            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = "
                                + resultInList.getBssid()
                                + ",InetAddress = "
                                + resultInList.getInetAddress()
                                .getHostAddress() + "\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
                    mConnectPrompttv.setText("连接成功");
                    mProgressDialog.setMessage(sb.toString());
                } else {
                    mConnectPrompttv.setText("连接失败");
                    mProgressDialog.setMessage("Esptouch fail");
                }
            }
        }
    }
}
