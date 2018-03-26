package com.nedfon.nedfon.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DbDean;
import com.nedfon.nedfon.db.MyDBHelper;
import com.nedfon.nedfon.uiok.PersonalCenterOkActivity;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.CountDownTimerUtils;
import com.nedfon.nedfon.utils.StatusBarCompat;
import com.nedfon.nedfon.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mForGotPwdSendRl;
    private EditText mPwdEt,mSurePwd;
    private TextView mSurePwdPromptTv,mBackTv;

    private static final int REGISTER_NOT_NULL = 5;
    private static final int REGISTER_FAILED = 6;
    private static final int REGISTER_SUCCESS = 7;
    private static final int OTHER_ERROR = 8;


    private MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(ResetPwdActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_reset_pwd);

        initView();
    }

    private void initView() {
        mForGotPwdSendRl = this.findViewById(R.id.activity_resetpwd_sent_rl);
        mPwdEt = this.findViewById(R.id.activity_resetpwd_pwd_et);
        mSurePwd = this.findViewById(R.id.activity_resetpwd_sure_pwd_et);
        mBackTv = this.findViewById(R.id.activity_resetpwd_back_tv);
        mSurePwdPromptTv = this.findViewById(R.id.activity_resetpwd_sure_pwd_tv);

        mForGotPwdSendRl.setOnClickListener(this);
        mBackTv.setOnClickListener(this);

        myDBHelper = new MyDBHelper(this);
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1 :
                    ToastUtils.show(ResetPwdActivity.this,"密码与确认密码不符合！");
                    break;
                case REGISTER_NOT_NULL :
                    ToastUtils.show(ResetPwdActivity.this,"密码/确认密码不能为空！");
                    break;
                case REGISTER_FAILED :
                    ToastUtils.show(ResetPwdActivity.this,"修改密码失败！");
                    break;
                case REGISTER_SUCCESS :
                    ToastUtils.show(ResetPwdActivity.this,"修改密码成功！");
//                    Intent success = new Intent(ResetPwdActivity.this,FindPwdSuccessActivity.class);
//                    startActivity(success);
                    if (myDBHelper.query().size()>0){
                        DbDean bean = myDBHelper.query().get(0);
                        myDBHelper.delete(bean.phone);
                        Intent login = new Intent(ResetPwdActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                    ResetPwdActivity.this.finish();
                    break;
                case OTHER_ERROR :
                    ToastUtils.show(ResetPwdActivity.this,"其他错误！");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_resetpwd_sent_rl://sure
                sureRestPwd();
                break;
            case R.id.activity_resetpwd_back_tv://back
                Intent deviceBind = new Intent(ResetPwdActivity.this,PersonalCenterOkActivity.class);
                startActivity(deviceBind);
                ResetPwdActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void sureRestPwd(){
        String pwd = mPwdEt.getText().toString();
        String surePwd = mSurePwd.getText().toString();
        if ("".equals(pwd) || "".equals(surePwd)){
            mHandler.sendEmptyMessage(REGISTER_NOT_NULL);
            return;
        }
        if(!pwd.equals(surePwd)){
            mSurePwdPromptTv.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessage(1);
            return;
        } else {
            mSurePwdPromptTv.setVisibility(View.GONE);
        }
        doResetPwdPost(CommonUtils.token,pwd);//获取gps数据
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();

    private void doResetPwdPost(String token,String pwd){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        RequestBody requestBody = requestBodyBuilder
                .add("token",token)
                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/resetPwdAfterLogin").post(requestBody).build();
        executeResetPwdRequest(request);
    }

    private void executeResetPwdRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":1,")){
                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                } else if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(REGISTER_FAILED);
                } else {
                    mHandler.sendEmptyMessage(OTHER_ERROR);
                }
            }
        });
    }
}
