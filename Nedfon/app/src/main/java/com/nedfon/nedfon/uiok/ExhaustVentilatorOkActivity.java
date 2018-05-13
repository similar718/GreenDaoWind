package com.nedfon.nedfon.uiok;

import android.content.Intent;
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
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExhaustVentilatorOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private RelativeLayout mDiRl,mHignRl;
    private ImageView mDiIv,mHignIv;

    private RelativeLayout mSureRl;
    private ImageView mSureIv;

    private boolean mIsSelected = false;
    private DeviceInfo info = null;

    private int isdi = 0;
    private int misdi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(3);
        setTitleText("送风排风设置");
        info = CommonUtils.bean;
        initView();

        initData();
    }

    private void initData() {
//        Log.e("oooooooooo","deviceid = "+info.deviceid);
//        ToastUtils.show(ExhaustVentilatorOkActivity.this,info.deviceid);
        isdi = info.workgear;
        misdi = isdi;
        if (isdi==1){
            mDiIv.setImageResource(R.drawable.stall_btn_selected);
            mHignIv.setImageResource(R.drawable.stall_btn_unselect);
        } else if (isdi==2) {
            mDiIv.setImageResource(R.drawable.stall_btn_unselect);
            mHignIv.setImageResource(R.drawable.stall_btn_selected);
        } else {
            mDiIv.setImageResource(R.drawable.stall_btn_unselect);
            mHignIv.setImageResource(R.drawable.stall_btn_unselect);
        }
    }

    private void initView() {
        mDiRl = this.findViewById(R.id.activity_exhaust_ventilation_f1_rl);
        mHignRl = this.findViewById(R.id.activity_exhaust_ventilation_f2_rl);
        mDiIv = this.findViewById(R.id.activity_exhaust_ventilation_f1_iv);
        mHignIv = this.findViewById(R.id.activity_exhaust_ventilation_f2_iv);

        mSureRl = this.findViewById(R.id.activity_exhaust_ventilation_sure_rl);
        mSureIv = this.findViewById(R.id.activity_exhaust_ventilation_sure_iv);

        mDiRl.setOnClickListener(this);
        mHignRl.setOnClickListener(this);
        mSureRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_exhaust_ventilation_f1_rl:
                isdi = 1;
                mDiIv.setImageResource(R.drawable.stall_btn_selected);
                mHignIv.setImageResource(R.drawable.stall_btn_unselect);
                if (misdi != isdi){
                    mIsSelected = true;
                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
                } else {
                    mIsSelected = false;
                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
                }
                break;
            case R.id.activity_exhaust_ventilation_f2_rl:
                isdi = 2;
                mDiIv.setImageResource(R.drawable.stall_btn_unselect);
                mHignIv.setImageResource(R.drawable.stall_btn_selected);
                if (misdi != isdi){
                    mIsSelected = true;
                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
                } else {
                    mIsSelected = false;
                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
                }
                break;
            case R.id.activity_exhaust_ventilation_sure_rl:
                if (mIsSelected) {
                    doControlWindCmdGet(CommonUtils.token,info.deviceid,info.changeOrPushModel+"",
                            isdi+"",info.workmodel+"",info.ionsflag+"","1",info.workmodel+"");
                }
                break;
            default:
                break;
        }
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
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
        String url = CommonUtils.localhost+"mobileapi/controlWindCmd?token="+token+"&deviceSN="+deviceSN+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag;
        Log.e("ooooooooooooo", "doControlTimerCmdGet: url = "+url );
        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlWindCmd?" +
                "token="+token+"&deviceSN="+deviceSN+"&OnOff="+OnOff+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag).get().build();
        executeControlWindCmdRequest(request);
    }

    private void executeControlWindCmdRequest(Request request) {
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
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mSuccess)){
                    mHandler.sendEmptyMessage(1);
                } else if (res.contains(CommonUtils.mFailed)){
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
                    ToastUtils.show(ExhaustVentilatorOkActivity.this,"设置成功！");
                    misdi = isdi;
                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
                    mIsSelected = false;
                    break;
                case 2 :
                    ToastUtils.show(ExhaustVentilatorOkActivity.this,"其他错误");
                    break;
                case 5 :
                    ToastUtils.show(ExhaustVentilatorOkActivity.this,"设置失败");
                    break;
            }
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_exhaust_ventilator_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(ExhaustVentilatorOkActivity.this,DeviceOkActivity.class);
        startActivity(deviceIntent);
        this.finish();
    }
}
