package com.nedfon.nedfon.uiok;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.nedfon.nedfon.ui.LoginActivity;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.MacUtils;
import com.nedfon.nedfon.utils.ToastUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.CHANGE_NETWORK_STATE;
import static android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;

public class DeviceInternetHotOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mConnectPrompttv;
    private RelativeLayout mConnectRl;

    private String name = "nedfon001";
    private String pwd = "nedfon123456";

    private boolean isopen = false;
//    private String Bssid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitleText("设备配网");
        NAME = DeviceInternetHotOkActivity.class.getSimpleName();
        setImage(2);

        primissionAsk();

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

        startWifiAp();
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
                    startWifiAp();
                }
                break;
            default:
                break;
        }
    }

    private WifiManager wifiManager = null;

    private void startWifiAp() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method.setAccessible(true);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = name;
            netConfig.preSharedKey =  pwd;
            netConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);

            method.invoke(wifiManager, netConfig,true);
            mConnectPrompttv.setText("热点成功开启！");
            isopen = true;
        } catch (Exception e) {
            Log.e("ooooooooo", "startWifiAp: "+e.getMessage());
            mConnectPrompttv.setText("热点开启失败！");
            isopen = false;
        }
    }

    private EspWifiAdminSimple mWifiAdmin;

    private void setConnectDevice(){
        String apSsid = name;
        String apPassword = pwd;
        String apBssid = MacUtils.getMac();
//        Bssid = apBssid;
        String taskResultCountStr = Integer.toString(4);
        if (__IEsptouchTask.DEBUG) {
            Log.d("ooooooooooooo", "mBtnConfirm is clicked, mEdtApSsid = " + apSsid+ " apBssid = "+apBssid
                    + ", " + " mEdtApPassword = " + apPassword);
        }
        Log.d("ooooooooooooo", "mBtnConfirm is clicked, mEdtApSsid = " + apSsid +" apBssid = "+apBssid
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
//                Toast.makeText(DeviceInternetHotOkActivity.this, "配网成功",
//                        Toast.LENGTH_LONG).show();
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                mConnectPrompttv.setText("连接成功");
                douploadDeviceMacGet(CommonUtils.token,result.getBssid());
//                douploadDeviceMacGet(CommonUtils.token,Bssid);
//                setBackOnClick();
            }

        });
    }


    private ProgressDialog mProgressDialog;

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

//        private ProgressDialog mProgressDialog;

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
//                        Bssid = resultInList.getBssid();
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's " + (result.size() - count)
                                + " more result(s) without showing\n");
                    }
//                    douploadDeviceMacGet(CommonUtils.token,Bssid);
                    mConnectPrompttv.setText("连接成功");
                    mProgressDialog.setMessage("设备配网成功");
                } else {
                    mConnectPrompttv.setText("连接失败");
                    mProgressDialog.setMessage("设备配网失败");
                }
            }
        }
    }

    private void primissionAsk() {
        Log.e("权限判断", "开始" );
        if(
                ContextCompat.checkSelfPermission(this, CHANGE_WIFI_MULTICAST_STATE) != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this, WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(this, CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            //若果是第一次
            new AlertDialog.Builder(this).setMessage("为了保证应用正常运行，需要内存卡访问权限以及位置信息获取权限！")
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(DeviceInternetHotOkActivity.this, new String[]{
                                    CHANGE_WIFI_MULTICAST_STATE,
                                    WRITE_SETTINGS,
                                    CHANGE_NETWORK_STATE}, 001);
                        }
                    }).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 001) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED&&grantResults[2] == PackageManager.PERMISSION_GRANTED) {                                                                //(3)
            } else {
                ToastUtils.show(DeviceInternetHotOkActivity.this,"授权失败");
                setBackOnClick();
//                finish();
//                //0表示正常退出，1表示异常退出
//                System.exit(0);
            }
        }
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
    /*
    上传设备bssid
    http://localhost/mobileapi/uploadDeviceMac?token=abcvTkdjsd_1209990ijhyty&deviceMac=000000001  GET
    参数说明：
    token：登录token
    deviceMac:配网成功返回设备的BSSID值
    成功：{'result':1,'msg':'绑定设备成功'}
    失败：{'result'-1,'msg':'绑定设备不存在'}
    失败：{'result':-2,'msg':'该设备已经绑定'}
    失败：{'result':0,'msg':'绑定设备失败'}
     */
    private void douploadDeviceMacGet(String token,String deviceMac){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/uploadDeviceMac?token="+token+"&deviceMac="+deviceMac).get().build();
        executeuploadDeviceMacRequest(request);
    }

    private String uploadres = "";
    private void executeuploadDeviceMacRequest(Request request) {
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
            public void onResponse(Call request, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":1,")){
                    uploadres = res;
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
                    ToastUtils.show(DeviceInternetHotOkActivity.this,"上传成功！ "+uploadres);
                    setBackOnClick();
                    break;
                case 2 :
                    ToastUtils.show(DeviceInternetHotOkActivity.this,"其他错误");
                    break;
                case 5 :
                    ToastUtils.show(DeviceInternetHotOkActivity.this,"上传失败！");
                    break;
            }
        }
    };
}
