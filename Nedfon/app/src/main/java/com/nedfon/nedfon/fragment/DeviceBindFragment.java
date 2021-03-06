//package com.nedfon.nedfon.fragment;
//
//import android.app.AlertDialog;
//import android.app.Fragment;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.adapter.DeviceBindAdapter;
//import com.nedfon.nedfon.bean.DeviceBindBean_test;
//import com.nedfon.nedfon.bean.DeviceListAll;
//import com.nedfon.nedfon.bean.GetCurrentWeatherAll;
//import com.nedfon.nedfon.ui.AddDeviceActivity;
//import com.nedfon.nedfon.ui.DeviceActivity;
//import com.nedfon.nedfon.ui.DeviceInternetHotActivity;
//import com.nedfon.nedfon.ui.DeviceInternetWIFIActivity;
//import com.nedfon.nedfon.utils.CommonUtils;
//import com.nedfon.nedfon.utils.NetWorkUtils;
//import com.nedfon.nedfon.utils.ToastUtils;
//import com.nedfon.nedfon.view.DeviceSetInternetDialog;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 我的
// * Created by zst on 2017/3/2.
// */
//
//public class DeviceBindFragment extends Fragment {
//
//    private ImageView mWeatherIv;//天气图标
//    private TextView mWeatherNumTv; //天气温度度数
//    private TextView mWeatherTxtTv; //天气文字描述  多云|微风
//    private TextView mShiduTv; //湿度百分比
//    private TextView mPM25Tv; //pm25
//    private TextView mCityTv; //城市
//
//    private RelativeLayout mBindDeviceRl;
//    private ListView mListView;
//
//    private DeviceBindAdapter mAdapter;
//    private List<DeviceBindBean_test> mList;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_device_bind, container, false);
////        initData();
//        initView(view);
//        Log.e("oooooooooo", "onCreateView: token = "+ CommonUtils.token);
//        doGetCurrentWeatherGet(CommonUtils.token);
//        return view;
//    }
//
//
//
//    private void initData() {
//        if (null == mlistbean.data || 1 > mlistbean.data.size()){
//            return;
//        }
//        mList = new ArrayList<>();
//        mList.clear();
//        for (int i=0;i<mlistbean.data.size();i++){
//            DeviceBindBean_test bean = new DeviceBindBean_test();
//            bean.name = mlistbean.data.get(i).terminal;
//            bean.online = mlistbean.data.get(i).commStatus==0?"离线":"在线";
//            bean.wendu = mlistbean.data.get(i).intmp+"°";
//            bean.shidu = mlistbean.data.get(i).insweet+"%";
//            bean.pm25 = mlistbean.data.get(i).inpm25+"";
//            mList.add(bean);
//        }
//        mAdapter.setData(mList);
//    }
//
//    private void initView(View view) {
//        mWeatherIv = view.findViewById(R.id.fragment_device_bind_weather_iv);
//        mWeatherNumTv = view.findViewById(R.id.fragment_device_bind_weather_num_tv);
//        mWeatherTxtTv = view.findViewById(R.id.fragment_device_bind_weather_txt_tv);
//        mShiduTv = view.findViewById(R.id.fragment_device_bind_shidu_tv);
//        mPM25Tv = view.findViewById(R.id.fragment_device_bind_pm25_tv);
//        mCityTv = view.findViewById(R.id.fragment_device_city_tv);
//        mBindDeviceRl = view.findViewById(R.id.fragmet_device_bind_add_device_rl);
//        mListView = view.findViewById(R.id.fragment_device_bind_device_lv);
//
//        mAdapter = new DeviceBindAdapter(getActivity());
//        mAdapter.setData(mList);
//        mListView.setAdapter(mAdapter);
//
//        mBindDeviceRl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent add = new Intent(getActivity(), AddDeviceActivity.class);
//                startActivity(add);
//            }
//        });
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemDeviceOptionClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                if (mlistbean.data.get(position).commStatus==1){ //在线
//                    CommonUtils.bean = mlistbean.data.get(position);
////                    MainFragmentActivity frgment = new MainFragmentActivity();
////                    frgment.showDeviceFragment();
//                    Intent intent = new Intent(getActivity(),DeviceActivity.class);
//                    startActivity(intent);
//                } else { //离线
//                    DeviceSetInternetDialog dialog = new DeviceSetInternetDialog(getActivity(), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            isWIFIOrOther();
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void isWIFIOrOther(){
//        int type = NetWorkUtils.getAPNType(getActivity());
//        if (type == 1) {
//            ToastUtils.show(getActivity(), "当前属于连接WiFi状态");
//            Intent wifi = new Intent(getActivity(), DeviceInternetWIFIActivity.class);
//            startActivity(wifi);
//            return;
//        } else if(type == 0) {
//            ToastUtils.show(getActivity(), "当前网络不可用");//TODO 使用该软件需要连接网络
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("提示").setMessage("您已经进入到了没有网络的异次元，请打开您的网络或者连接WiFi。");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
//        } else if (type == 2 || type == 3){
//            ToastUtils.show(getActivity(), "3G网络 或者 2G网络");
//            Intent hot = new Intent(getActivity(), DeviceInternetHotActivity.class);
//            startActivity(hot);
//        }else {
//            ToastUtils.show(getActivity(),"当前不知道什么网络");
//        }
//    }
//
//
//
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//    /**
//     *     获取设备列表
//     */
//    private  void doDeviceListGet(String token){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/deviceList?token="+token).get().build();
//        executeDeviceListRequest(request);
//    }
//
//    private DeviceListAll mlistbean = null;
//    private void executeDeviceListRequest(Request request) {
//        //3.将Request封装为Call
//        Call call = okhttpclient.newCall(request);
//        //异步使用CallBack  同步用call.execute()
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":1,")){
//                    mlistbean = new Gson().fromJson(res,DeviceListAll.class);
//                    mHandler.sendEmptyMessage(4);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(5);
//                } else {
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        });
//    }
//
//    /**
//     *     获取当天天气信息
//     */
//    private  void doGetCurrentWeatherGet(String token){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/getCurrentWeather?token="+token).get().build();
//        executeGetCurrentWeatherRequest(request);
//    }
//
//    private GetCurrentWeatherAll bean = null;
//
//    private void executeGetCurrentWeatherRequest(Request request) {
//        //3.将Request封装为Call
//        Call call = okhttpclient.newCall(request);
//        //异步使用CallBack  同步用call.execute()
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":1,")){
//                    bean = new Gson().fromJson(res,GetCurrentWeatherAll.class);
//                    mHandler.sendEmptyMessage(3);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(1);
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
//                    ToastUtils.show(getActivity(),"获取天气信息失败！");
//                    break;
//                case 2 :
//                    ToastUtils.show(getActivity(),"其他错误");
//                    break;
//                case 3 :
//                    updateView();
//                    break;
//                case 5 :
//                    ToastUtils.show(getActivity(),"获取设备列表失败");
//                    break;
//                case 4 :
//                    updateListView();
//                    break;
//                case 6 :
//                    doDeviceListGet(CommonUtils.token);
//                    break;
//            }
//        }
//    };
//
//    private void updateListView() {
//        initData();
//    }
//
//    private void updateView() {
//        mWeatherNumTv.setText(bean.weather.wd+"°");
//        mCityTv.setText(bean.weather.cityname);
//        mWeatherTxtTv.setText(bean.weather.qx+" | "+bean.weather.fx);
//        mShiduTv.setText("湿度"+bean.weather.sd+"%");
//        mPM25Tv.setText("PM2.5 "+bean.weather.pm25+"");
//
//        mHandler.sendEmptyMessage(6);
//    }
//
//}
