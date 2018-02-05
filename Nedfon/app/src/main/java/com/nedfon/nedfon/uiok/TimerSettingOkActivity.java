package com.nedfon.nedfon.uiok;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfo;
//import com.nedfon.nedfon.ui.TimerSettingActivity;
import com.nedfon.nedfon.bean.Entity;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.nedfon.nedfon.view.TimerChoiceDialog;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class TimerSettingOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mStartBtnTv,mStartTv,mEndBtnTv,mEndTv;

    private TextView mWeekOneTv,mWeekTwoTv,mWeekThreeTv,mWeekFourTv,mWeekFiveTv,mWeekSixTv,mWeekSevenTv;

    private RelativeLayout mSureRl;
    private ImageView mSureIv;

    private boolean mIsSelected = false;

    private DeviceInfo info = null;

    private TimerChoiceDialog mDialog;

    private int week = Entity.Mon;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("定时设置");

        info = CommonUtils.bean;
        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_timer_setting_ok;
    }

    @Override
    protected void setBackOnClick() {
        this.finish();
    }


    private void initView() {
        mSureRl = this.findViewById(R.id.activity_timer_setting_sure_rl);
        mSureIv = this.findViewById(R.id.activity_timer_setting_sure_iv);

        mStartBtnTv = this.findViewById(R.id.activity_timer_setting_start_btn_tv);
        mStartTv = this.findViewById(R.id.activity_timer_setting_start_tv);

        mEndBtnTv = this.findViewById(R.id.activity_timer_setting_end_btn_tv);
        mEndTv = this.findViewById(R.id.activity_timer_setting_end_tv);

        mWeekOneTv = this.findViewById(R.id.activity_timer_setting_week_one_tv);
        mWeekTwoTv = this.findViewById(R.id.activity_timer_setting_week_two_tv);
        mWeekThreeTv = this.findViewById(R.id.activity_timer_setting_week_three_tv);
        mWeekFourTv = this.findViewById(R.id.activity_timer_setting_week_four_tv);
        mWeekFiveTv = this.findViewById(R.id.activity_timer_setting_week_five_tv);
        mWeekSixTv = this.findViewById(R.id.activity_timer_setting_week_six_tv);
        mWeekSevenTv = this.findViewById(R.id.activity_timer_setting_week_seven_tv);

        mSureRl.setOnClickListener(this);

        mStartBtnTv.setOnClickListener(this);
        mEndBtnTv.setOnClickListener(this);
        mWeekOneTv.setOnClickListener(this);
        mWeekTwoTv.setOnClickListener(this);
        mWeekThreeTv.setOnClickListener(this);
        mWeekFourTv.setOnClickListener(this);
        mWeekFiveTv.setOnClickListener(this);
        mWeekSixTv.setOnClickListener(this);
        mWeekSevenTv.setOnClickListener(this);

        initData();

        mDialog = new TimerChoiceDialog(TimerSettingOkActivity.this);
    }

    private void initData() {
        mStartTv.setText(info.opentime);
        mEndTv.setText(info.closetime);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_timer_setting_sure_rl: //确定
                if (mIsSelected) {
                    doControlTimerCmdGet(CommonUtils.token,mStartTv.getText().toString(),mEndTv.getText().toString(),week+"",info.sn);
                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
                }
                break;

            case R.id.activity_timer_setting_start_btn_tv: //开机时间设置
                mDialog.show(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String time = mDialog.getTime();
                        mStartTv.setText(time);
                        mDialog.dismiss();
                    }
                });
                setSureBtn();
                break;

            case R.id.activity_timer_setting_end_btn_tv: //关机时间设置
                mDialog.show(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String time = mDialog.getTime();
                        mEndTv.setText(time);
                        mDialog.dismiss();
                    }
                });
                setSureBtn();
                break;

            case R.id.activity_timer_setting_week_one_tv:
                week = Entity.Mon;
                chooseWeekDefault();
                mWeekOneTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekOneTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_two_tv:
                week = Entity.Tus;
                chooseWeekDefault();
                mWeekTwoTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekTwoTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_three_tv:
                week = Entity.Wen;
                chooseWeekDefault();
                mWeekThreeTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekThreeTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_four_tv:
                week = Entity.Thu;
                chooseWeekDefault();
                mWeekFourTv.setTextColor(Color.parseColor("#ffffff") );
                mWeekFourTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_five_tv:
                week = Entity.Fri;
                chooseWeekDefault();
                mWeekFiveTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekFiveTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_six_tv:
                week = Entity.Sat;
                chooseWeekDefault();
                mWeekSixTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekSixTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;
            case R.id.activity_timer_setting_week_seven_tv:
                week = Entity.sun;
                chooseWeekDefault();
                mWeekSevenTv.setTextColor(Color.parseColor("#ffffff"));
                mWeekSevenTv.setBackgroundResource(R.drawable.week_selected_bg);
                setSureBtn();
                break;

            default:
                break;
        }
    }
    private void setSureBtn(){
        if ( info.opentime == mStartTv.getText().toString() && info.closetime == mEndTv.getText().toString() && week == 1){
            mIsSelected = false;
            mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
            mSureIv.setImageResource(R.drawable.sure_selected_gou);
        } else {
            mIsSelected = true;
            mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
            mSureIv.setImageResource(R.drawable.sure_unselect_gou);
        }
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();

    private void doControlTimerCmdGet(String token,String opentime,String closetime,String setting,String deviceSN){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //controlTimerCmd?token=abcvTkdjsd_1209990ijhyty&deviceSN=12345678&opentime=08:30&closetime=23:30&setting=31
        String url = CommonUtils.localhost+"mobileapi/controlTimerCmd?token="+token+"&deviceSN="+deviceSN+"&opentime="+opentime+"&closetime="+closetime+"&setting="+setting+"&settime=1";
        Log.e("ooooooooooooo", "doControlTimerCmdGet: url = "+url );
        //settime 1启用定时操作  0未启用定时操作
        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlTimerCmd?token="+token+"&deviceSN="+deviceSN+"&opentime="+opentime+"&closetime="+closetime+"&setting="+setting+"&settime=1").get().build();
        executeControlTimerCmdRequest(request);
    }

    private void executeControlTimerCmdRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Response response) throws IOException {
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
                    ToastUtils.show(TimerSettingOkActivity.this,"设置成功！");
                    break;
                case 2 :
                    ToastUtils.show(TimerSettingOkActivity.this,"其他错误");
                    break;
                case 5 :
                    ToastUtils.show(TimerSettingOkActivity.this,"设置失败");
                    break;
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    private void chooseWeekDefault(){
        mWeekOneTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekTwoTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekThreeTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekFourTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekFiveTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekSixTv.setBackgroundResource(R.drawable.week_unselect_bg);
        mWeekSevenTv.setBackgroundResource(R.drawable.week_unselect_bg);

        mWeekOneTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekTwoTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekThreeTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekFourTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekFiveTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekSixTv.setTextColor(Color.parseColor("#b9b9b9"));
        mWeekSevenTv.setTextColor(Color.parseColor("#b9b9b9"));
    }
}
