package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.adapter.DataSearchLeftAdapter;
import com.nedfon.nedfon.adapter.DataSearchRightAdapter;
import com.nedfon.nedfon.bean.SensorCollectionDataAllBean;
import com.nedfon.nedfon.bean.SensorDataAllBean;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataSearchActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private RelativeLayout mLeftIncludeRl, mRightStatusRl;
    private TextView mLeftIncludeTv,mRightStatusTv;
    private ImageView mLeftIncludeIv,mRightStatusIv;

    private ListView mIncludeLv,mStatusLv;
    private Button m7Btn,m1Btn,m3Btn;
    private LinearLayout mIncludeLL,mStatusLL;
    private TextView mEmptyTv;

    private boolean mIncludeShow = true;
    private boolean mIsFirst = true;

    private DataSearchLeftAdapter mLeftAdapter;
    private DataSearchRightAdapter mRightAdapter;

    private boolean mLeftIsEmpty = false;
    private boolean mRightIsEmpty = false;
    private int leftstatus = 1;
    private int rightstatus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestLastSenvenDay();
        initView();
        setTitleText("数据查询");
        NAME = DataSearchActivity.class.getSimpleName();
        setImage(1);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_data_search_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(DataSearchActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        DataSearchActivity.this.finish();
    }

    private void initView() {
        mLeftIncludeRl = this.findViewById(R.id.activity_data_search_tableft_rl);
        mRightStatusRl = this.findViewById(R.id.activity_data_search_tabright_rl);
        mLeftIncludeTv = this.findViewById(R.id.activity_data_search_tableft_tv);
        mRightStatusTv = this.findViewById(R.id.activity_data_search_tabright_tv);
        mLeftIncludeIv = this.findViewById(R.id.activity_data_search_tableft_iv);
        mRightStatusIv = this.findViewById(R.id.activity_data_search_tabright_iv);

        mIncludeLv = this.findViewById(R.id.activity_data_search_include_lv);
        mStatusLv = this.findViewById(R.id.activity_data_search_status_lv);

        m7Btn = this.findViewById(R.id.activity_data_search_7_btn);
        m1Btn = this.findViewById(R.id.activity_data_search_1_btn);
        m3Btn = this.findViewById(R.id.activity_data_search_3_btn);

        mIncludeLL = this.findViewById(R.id.activity_data_search_include_ll);
        mStatusLL = this.findViewById(R.id.activity_data_search_status_ll);
        mEmptyTv = this.findViewById(R.id.empty_tv);

        IsShowInclude();
        mLeftAdapter = new DataSearchLeftAdapter(this);
        mRightAdapter = new DataSearchRightAdapter(this);

        mIncludeLv.setAdapter(mLeftAdapter);
        mStatusLv.setAdapter(mRightAdapter);


        mLeftIncludeRl.setOnClickListener(this);
        mRightStatusRl.setOnClickListener(this);
        m7Btn.setOnClickListener(this);
        m1Btn.setOnClickListener(this);
        m3Btn.setOnClickListener(this);
    }

    private void initData(){
        if (mSensorCollectionBean != null && mSensorCollectionBean.rows.size()>0){
            mLeftAdapter.setData(mSensorCollectionBean.rows);
            mLeftIsEmpty = false;
        } else {
            mLeftIsEmpty = true;
            //数据为空
        }
        if (mIsFirst){
            mHandler.sendEmptyMessage(5);
            mIsFirst = false;
        }
    }

    private void initDataRight(){
        if (mSensorBean != null && mSensorBean.rows.size()>0){
            mRightAdapter.setData(mSensorBean.rows);
            mRightIsEmpty = false;
        } else {
            mRightIsEmpty = true;
        }
        IsShowInclude();
    }

    private void IsShowInclude(){
        if (mIncludeShow){
            mIncludeLL.setVisibility(View.VISIBLE);
            mIncludeLv.setVisibility(View.VISIBLE);

            mStatusLv.setVisibility(View.GONE);
            mStatusLL.setVisibility(View.GONE);

            mLeftIncludeTv.setTextColor(Color.parseColor("#585858"));
            mLeftIncludeIv.setBackgroundColor(Color.parseColor("#22d5b5"));

            mRightStatusTv.setTextColor(Color.parseColor("#b9b9b9"));
            mRightStatusIv.setBackgroundColor(Color.parseColor("#b9b9b9"));

            if (mLeftIsEmpty){
                mEmptyTv.setVisibility(View.VISIBLE);
                mIncludeLv.setVisibility(View.GONE);
            } else {
                mEmptyTv.setVisibility(View.GONE);
                mIncludeLv.setVisibility(View.VISIBLE);
            }
        } else {
            mIncludeLL.setVisibility(View.GONE);
            mIncludeLv.setVisibility(View.GONE);

            mStatusLv.setVisibility(View.VISIBLE);
            mStatusLL.setVisibility(View.VISIBLE);

            mLeftIncludeTv.setTextColor(Color.parseColor("#b9b9b9"));
            mLeftIncludeIv.setBackgroundColor(Color.parseColor("#b9b9b9"));

            mRightStatusTv.setTextColor(Color.parseColor("#585858"));
            mRightStatusIv.setBackgroundColor(Color.parseColor("#22d5b5"));

            if (!mRightIsEmpty){
                mEmptyTv.setVisibility(View.GONE);
                mStatusLv.setVisibility(View.VISIBLE);
            } else {
                mEmptyTv.setVisibility(View.VISIBLE);
                mStatusLv.setVisibility(View.GONE);
            }
        }
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
    /**
     *     token:登录token
     deviceSN:设备ID
     sensor:测值类型，1---室内温度，2--室内湿度，3---室内PM25值
     start:查询开始时间
     end:查询结束时间
     */
    private  void doGetsensorcollectiondataGet(String token,String deviceSn,String start,String end){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        // http://localhost:9090/mobileapi/sensorcollectiondata?token=123123&deviceSN=00002&start=2018-2-28&end=2018-3-2
        Request request = builder.url(CommonUtils.localhost+"mobileapi/sensorcollectiondata?token="+token+
                "&deviceSN="+deviceSn+"&start="+start+"&end="+end).get().build();
        executeGetsensorcollectiondataRequest(request);
    }

    private SensorCollectionDataAllBean mSensorCollectionBean = null;

    private void executeGetsensorcollectiondataRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res1 = "+res );
                if (res.contains("\"total\":")){
                    mSensorCollectionBean = new Gson().fromJson(res,SensorCollectionDataAllBean.class);
                    mHandler.sendEmptyMessage(3);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(1);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    /**
     *     token:登录token
     deviceSN:设备ID
     sensor:测值类型，1---室内温度，2--室内湿度，3---室内PM25值
     start:查询开始时间
     end:查询结束时间
     */
    private  void doGetsensordataGet(String token,String deviceSn,String start,String end){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        // http://localhost:9090/mobileapi/sensordata?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTk5NTg5MjksInVzZXJuYW1lIjoiMTM1MTI3NzQ3NjAifQ.DP22dBsyMqnPoQyMw0KV51WN_OImBxI8rfphBS-eWfs
        // &deviceSN=2016617&sensor=11&start=2018-1-1&end=2018-2-2
        //在取值状态的时候赋值11 sensor=11
        Request request = builder.url(CommonUtils.localhost+"mobileapi/sensordata?token="+token+
                "&deviceSN="+deviceSn+"&sensor=11&start="+start+"&end="+end).get().build();
        executeGetsensordataRequest(request);
    }

    private SensorDataAllBean mSensorBean = null;

    private void executeGetsensordataRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res2 = "+res );
                if (res.contains("\"total\":")){
                    mSensorBean = new Gson().fromJson(res,SensorDataAllBean.class);
                    mHandler.sendEmptyMessage(6);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(4);
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
                    ToastUtils.show(DataSearchActivity.this,"获取数据记录失败！");
                    break;
                case 2 :
                    ToastUtils.show(DataSearchActivity.this,"其他错误");
                    break;
                case 3 :
                    initData();
                    break;
                case 4 :
                    ToastUtils.show(DataSearchActivity.this,"获取运行状态记录失败");
                    break;
                case 5 :
                    requestRightSenvenDay();
                    break;
                case 6 :
                    initDataRight();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_data_search_tableft_rl://数据查询
                mIncludeShow = true;
                IsShowInclude();
                if (mIncludeShow){
                    if (leftstatus == 1){
                        lateBackgroundShow();
                        m7Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else if (leftstatus ==2){
                        lateBackgroundShow();
                        m1Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else {
                        lateBackgroundShow();
                        m3Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    }
                } else {
                    if (rightstatus == 1){
                        lateBackgroundShow();
                        m7Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else if (rightstatus ==2){
                        lateBackgroundShow();
                        m1Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else {
                        lateBackgroundShow();
                        m3Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    }
                }
                break;

            case R.id.activity_data_search_tabright_rl: //运行状态
                mIncludeShow = false;
                IsShowInclude();
                if (mIncludeShow){
                    if (leftstatus == 1){
                        lateBackgroundShow();
                        m7Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else if (leftstatus ==2){
                        lateBackgroundShow();
                        m1Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else {
                        lateBackgroundShow();
                        m3Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    }
                } else {
                    if (rightstatus == 1){
                        lateBackgroundShow();
                        m7Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else if (rightstatus ==2){
                        lateBackgroundShow();
                        m1Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    } else {
                        lateBackgroundShow();
                        m3Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                    }
                }
                break;

            case R.id.activity_data_search_7_btn://最近7天
                if (mIncludeShow){
                    leftstatus = 1;
                } else {
                    rightstatus = 1;
                }
                lateBackgroundShow();
                m7Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                requestLastSenvenDay();
                break;

            case R.id.activity_data_search_1_btn://最近1月

                if (mIncludeShow){
                    leftstatus = 2;
                } else {
                    rightstatus = 2;
                }
                lateBackgroundShow();
                m1Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                requestLastOneMonth();
                break;

            case R.id.activity_data_search_3_btn://最近3月
                if (mIncludeShow){
                    leftstatus = 3;
                } else {
                    rightstatus = 3;
                }
                lateBackgroundShow();
                m3Btn.setBackgroundColor(Color.parseColor("#22d5b5"));
                requestLastThreeMonth();
                break;

            default:
                break;
        }
    }

    private void lateBackgroundShow(){
        m7Btn.setBackgroundColor(Color.parseColor("#b9b9b9"));
        m1Btn.setBackgroundColor(Color.parseColor("#b9b9b9"));
        m3Btn.setBackgroundColor(Color.parseColor("#b9b9b9"));
    }

    private void requestLastSenvenDay(){
        String start = CommonUtils.getOldDate(-7);
        String end = CommonUtils.getOldDate(0);
        if (mIncludeShow){
            doGetsensorcollectiondataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        } else {
            doGetsensordataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        }
    }
    private void requestRightSenvenDay(){
        String start = CommonUtils.getOldDate(-7);
        String end = CommonUtils.getOldDate(0);
        doGetsensordataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
    }
    private void requestLastOneMonth(){
        String start = CommonUtils.getOldDate(-30);
        String end = CommonUtils.getOldDate(0);
        if (mIncludeShow){
            doGetsensorcollectiondataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        } else {
            doGetsensordataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        }
    }
    private void requestLastThreeMonth(){
        String start = CommonUtils.getOldDate(-90);
        String end = CommonUtils.getOldDate(0);
        if (mIncludeShow){
            doGetsensorcollectiondataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        } else {
            doGetsensordataGet(CommonUtils.token,CommonUtils.deviceSN,start,end);
        }
    }
}
