//package com.nedfon.nedfon.ui;
//
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.bean.DeviceInfo;
//import com.nedfon.nedfon.utils.CommonUtils;
//import com.nedfon.nedfon.utils.StatusBarCompat;
//import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import org.w3c.dom.Comment;
//
//import java.io.IOException;
//
//public class ExhaustVentilatorActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//
//    private RelativeLayout mDiRl,mHignRl;
//    private ImageView mDiIv,mHignIv;
//
//    private RelativeLayout mSureRl;
//    private ImageView mSureIv;
//
//    private boolean mIsSelected = false;
//    private DeviceInfo info = null;
//
//    private boolean isdi = true;
//    private boolean misdi = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(ExhaustVentilatorActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_exhaust_ventilator);
//        info = CommonUtils.bean;
//        initView();
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_exhaust_ventilation_back_tv);
//        mDiRl = this.findViewById(R.id.activity_exhaust_ventilation_f1_rl);
//        mHignRl = this.findViewById(R.id.activity_exhaust_ventilation_f2_rl);
//        mDiIv = this.findViewById(R.id.activity_exhaust_ventilation_f1_iv);
//        mHignIv = this.findViewById(R.id.activity_exhaust_ventilation_f2_iv);
//
//        mSureRl = this.findViewById(R.id.activity_exhaust_ventilation_sure_rl);
//        mSureIv = this.findViewById(R.id.activity_exhaust_ventilation_sure_iv);
//
//        mBackTv.setOnClickListener(this);
//        mDiRl.setOnClickListener(this);
//        mHignRl.setOnClickListener(this);
//        mSureRl.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_exhaust_ventilation_back_tv:
//                this.finish();
//                break;
//            case R.id.activity_exhaust_ventilation_f1_rl:
//                isdi = true;
//                mDiIv.setImageResource(R.drawable.stall_btn_selected);
//                mHignIv.setImageResource(R.drawable.stall_btn_unselect);
//                if (misdi != isdi){
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                } else {
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                }
//                break;
//            case R.id.activity_exhaust_ventilation_f2_rl:
//                isdi = false;
//                mDiIv.setImageResource(R.drawable.stall_btn_unselect);
//                mHignIv.setImageResource(R.drawable.stall_btn_selected);
//                if (misdi != isdi){
//                    mIsSelected = true;
//                    mSureRl.setBackgroundResource(R.drawable.sure_unselect_bg);
//                    mSureIv.setImageResource(R.drawable.sure_unselect_gou);
//                } else {
//                    mIsSelected = false;
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                }
//                break;
//            case R.id.activity_exhaust_ventilation_sure_rl:
//                if (mIsSelected) {
//                    mSureRl.setBackgroundResource(R.drawable.sure_selected_bg);
//                    mSureIv.setImageResource(R.drawable.sure_selected_gou);
//                    doControlWindCmdGet(CommonUtils.token,info.sn,info.ionsflag+"",
//                            isdi?"1":"2","1",info.workmodel+"","1",info.status+"");
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private static OkHttpClient okhttpclient = new OkHttpClient();
//    /*
//    http://localhost/mobileapi/controlWindCmd?token=abcvTkdjsd_1209990ijhyty&deviceSN=000000001&fanModel=30&fanLevel=1&fanOnOff=1&ionsOnOff=1&warnflag=1
//    参数说明：
//    工作模式 ：1自动   2手动     工作状态和工作模式是配合使用   只用工作状态处于开机(状态值为1)时 ， 才可以选择自动  手动工作模式
//
//    如果风机开关处于待机状态  则风机转速也必须处于0状态, 如果风机处于3开启状态  则风机转速则可选 1  2档（即高低档）
//    Token：登录token
//    deviceSN：设备SN
//    OnOff：0待机,1开机
//    fanModel：工作模式 1自动  2手动
//    fanLevel： 风机档位 0关机   1抵挡  2高档
//    fanOnOff： 风机开关 0排气换气关机    3排气换气开机
//    ionsOnOff：负离子 0关     1开
//    warnflag：清洗时间提醒  0关闭   1开启
//
//    返回值：
//    成功：{'result':1,'msg':'设置成功'}
//
//    失败：{'result':0,'msg':'设置失败'}
//     */
//    private void doControlWindCmdGet(String token,String deviceSN,String fanModel,String fanLevel,String fanOnOff,String ionsOnOff,String warnflag,String OnOff){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        String url = CommonUtils.localhost+"mobileapi/controlWindCmd?token="+token+"&deviceSN="+deviceSN+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag;
//        Log.e("ooooooooooooo", "doControlTimerCmdGet: url = "+url );
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/controlWindCmd?" +
//                "token="+token+"&deviceSN="+deviceSN+"&OnOff="+OnOff+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff+"&warnflag="+warnflag).get().build();
//        executeControlWindCmdRequest(request);
//    }
//
//    private void executeControlWindCmdRequest(Request request) {
//        //3.将Request封装为Call
//        Call call = okhttpclient.newCall(request);
//        //异步使用CallBack  同步用call.execute()
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":1,")){
//                    mHandler.sendEmptyMessage(1);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(5);
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
//                    ToastUtils.show(ExhaustVentilatorActivity.this,"设置成功！");
//                    break;
//                case 2 :
//                    ToastUtils.show(ExhaustVentilatorActivity.this,"其他错误");
//                    break;
//                case 5 :
//                    ToastUtils.show(ExhaustVentilatorActivity.this,"设置失败");
//                    break;
//            }
//        }
//    };
//}
