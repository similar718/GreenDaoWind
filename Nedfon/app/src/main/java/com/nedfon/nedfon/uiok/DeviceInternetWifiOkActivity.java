package com.nedfon.nedfon.uiok;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.demo_activity.EspWifiAdminSimple;
import com.espressif.iot.esptouch.demo_activity.EsptouchDemoActivity;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.nedfon.nedfon.R;

import java.util.List;

public class DeviceInternetWifiOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mWifiNameTv;
    private EditText mWifiPwdEt;
    private TextView mConnectPrompttv;
    private RelativeLayout mConnectRl;

    private String WIFINAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("设备配网");
        NAME = DeviceInternetWifiOkActivity.class.getSimpleName();
        initView();
        setImage(2);
        mWifiAdmin = new EspWifiAdminSimple(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_device_internet_wifi_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(DeviceInternetWifiOkActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        DeviceInternetWifiOkActivity.this.finish();
    }


    private String getConnectWifiSsid(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    private void initView() {
        mWifiNameTv = this.findViewById(R.id.activity_device_internet_wifi_name_tv);
        mWifiPwdEt = this.findViewById(R.id.activity_device_internet_wifi_pwd_et);
        mConnectPrompttv = this.findViewById(R.id.activity_device_internet_wifi_connect_prompt_tv);
        mConnectRl = this.findViewById(R.id.activity_device_internet_wifi_connect_rl);

        WIFINAME = getConnectWifiSsid();
        mWifiNameTv.setText("".equals(WIFINAME)?"空":WIFINAME.substring(1,WIFINAME.length()-1));

        mConnectRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_device_internet_wifi_connect_rl:
                //连接设备
                setConnectDevice();
                break;
            default:
                break;
        }
    }


    private EspWifiAdminSimple mWifiAdmin;

    private void setConnectDevice(){
        String apSsid = WIFINAME.substring(1,WIFINAME.length()-1);
        String apPassword = mWifiPwdEt.getText().toString();
        String apBssid = mWifiAdmin.getWifiConnectedBssid();
        String taskResultCountStr = Integer.toString(4);
        if (__IEsptouchTask.DEBUG) {
            Log.e("ooooooooooooo", "mBtnConfirm is clicked, mEdtApSsid = " + apSsid+" apBssid = "+apBssid
                    + ", " + " mEdtApPassword = " + apPassword);
        }
        Log.e("ooooooooooooo", "mBtnConfirm is clicked, mEdtApSsid = " + apSsid +" apBssid = "+apBssid
                + ", " + " mEdtApPassword = " + apPassword);
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
                Toast.makeText(DeviceInternetWifiOkActivity.this, "配网成功",
                        Toast.LENGTH_LONG).show();
                mConnectPrompttv.setText("连接成功");
                setBackOnClick();
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
            mProgressDialog = new ProgressDialog(DeviceInternetWifiOkActivity.this);
            mProgressDialog
                    .setMessage("设备正在配网中，请稍等…");
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
                    "等待...", new DialogInterface.OnClickListener() {
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
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, DeviceInternetWifiOkActivity.this);
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
                    "确定");
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
                    mProgressDialog.setMessage("设备配网成功");
//                    mProgressDialog.dismiss();
                } else {
                    mConnectPrompttv.setText("连接失败");
                    mProgressDialog.setMessage("设备配网失败");
//                    mProgressDialog.dismiss();
                }
            }
        }
    }

}
