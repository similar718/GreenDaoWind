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
import android.widget.Toast;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.BaseCallBack;
import com.nedfon.nedfon.utils.BaseOkHttpClient;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.CountDownTimerUtils;
//import com.nedfon.nedfon.utils.GetHttpDataFunction;
import com.nedfon.nedfon.utils.StatusBarCompat;
import com.nedfon.nedfon.utils.ToastUtils;

//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mForGotPwdSendRl;
    private Button mSendInvalidBtn;
    private EditText mInvalidEt,mPhoneEt,mPwdEt,mSurePwd;
    private TextView mSurePwdPromptTv,mBackTv;
    private ImageView mRemovePhoneIv;

    private static final int PHONE_IS_NOT_NULL = 0;
    private static final int SEND_INVALID_FAILED = 1;
    private static final int SEND_INVALID_SUCCESS = 4;
    private static final int INPUT_PHONE_NUMBER = 2;
    private static final int SERVER_EXCEPTION = 3;
    private static final int REGISTER_NOT_NULL = 5;
    private static final int REGISTER_FAILED = 6;
    private static final int REGISTER_SUCCESS = 7;
    private static final int OTHER_ERROR = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(ForgotPasswordActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_forgot_password);
        
        initView();
    }

    private void initView() {
        mForGotPwdSendRl = this.findViewById(R.id.activity_forgotpwd_sent_rl);
        mSendInvalidBtn = this.findViewById(R.id.activity_forgotpwd_send_btn);
        mInvalidEt = this.findViewById(R.id.activity_forgotpwd_invalid_et);
        mPhoneEt = this.findViewById(R.id.activity_forgotpwd_phone_et);
        mPwdEt = this.findViewById(R.id.activity_forgotpwd_pwd_et);
        mSurePwd = this.findViewById(R.id.activity_forgotpwd_sure_pwd_et);
        mBackTv = this.findViewById(R.id.activity_forgotpwd_back_tv);
        mSurePwdPromptTv = this.findViewById(R.id.activity_forgotpwd_sure_pwd_tv);
        mRemovePhoneIv = this.findViewById(R.id.activity_forgotpwd_remove_phone_iv);

        mForGotPwdSendRl.setOnClickListener(this);
        mSendInvalidBtn.setOnClickListener(this);
        mRemovePhoneIv.setOnClickListener(this);
        mBackTv.setOnClickListener(this);
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PHONE_IS_NOT_NULL:
                    ToastUtils.show(ForgotPasswordActivity.this,"电话号码不能为空");
                    break;
                case SEND_INVALID_FAILED :
                    ToastUtils.show(ForgotPasswordActivity.this,"发送验证码失败！");
                    break;
                case SEND_INVALID_SUCCESS :
                    ToastUtils.show(ForgotPasswordActivity.this,"发送验证码成功");
                    break;
                case INPUT_PHONE_NUMBER :
                    ToastUtils.show(ForgotPasswordActivity.this,"请输入正确的电话号码！");
                    break;
                case SERVER_EXCEPTION :
                    ToastUtils.show(ForgotPasswordActivity.this,"服务器异常");
                    break;
                case REGISTER_NOT_NULL :
                    ToastUtils.show(ForgotPasswordActivity.this,"手机号码/密码/确认密码/验证码不能为空！");
                    break;
                case REGISTER_FAILED :
                    ToastUtils.show(ForgotPasswordActivity.this,"找回密码失败！");
                    break;
                case REGISTER_SUCCESS :
                    //ToastUtils.show(ForgotPasswordActivity.this,"找回密码成功！");
                    Intent success = new Intent(ForgotPasswordActivity.this,FindPwdSuccessActivity.class);
                    startActivity(success);
                    ForgotPasswordActivity.this.finish();
                    break;
                case OTHER_ERROR :
                    ToastUtils.show(ForgotPasswordActivity.this,"其他错误！");
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forgotpwd_sent_rl://sure
                sureFindPwd();
                break;
            case R.id.activity_forgotpwd_send_btn://send invalid to server
                sendInvalid();
                break;
            case R.id.activity_forgotpwd_remove_phone_iv://remove phone text
                mPhoneEt.setText("");
                break;
            case R.id.activity_forgotpwd_back_tv://back
                ForgotPasswordActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void sendInvalid(){
        String phone = mPhoneEt.getText().toString();
        if (phone.equals("")){
            mHandler.sendEmptyMessage(PHONE_IS_NOT_NULL);
            return;
        }
        if (CommonUtils.isPhone(phone)){
            doRegisterQequestInvalidIDGet(phone);
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mSendInvalidBtn, 60000, 1000);
            mCountDownTimerUtils.start();
        } else {
            mHandler.sendEmptyMessage(INPUT_PHONE_NUMBER);
        }
    }

    private void sureFindPwd(){
        String phone = mPhoneEt.getText().toString();
        String pwd = mPwdEt.getText().toString();
        String surePwd = mSurePwd.getText().toString();
        String invalid = mInvalidEt.getText().toString();
        if (phone.equals("") || "".equals(pwd) || "".equals(invalid) || "".equals(surePwd)){
            mHandler.sendEmptyMessage(REGISTER_NOT_NULL);
            return;
        }
        if(!pwd.equals(surePwd)){
            mSurePwdPromptTv.setVisibility(View.VISIBLE);
            return;
        } else {
            mSurePwdPromptTv.setVisibility(View.GONE);
        }
        if (CommonUtils.isPhone(phone)){
            doResetPwdPost(phone,pwd,invalid);//获取gps数据
        } else {
            mHandler.sendEmptyMessage(INPUT_PHONE_NUMBER);
        }
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();

//
//    private void getData() {
//        String url = CommonUtils.localhost+"mobileapi/register?";
//        BaseOkHttpClient.newBuilder()
//                .addParam("cat_id", 14137)
//                .addParam("cur_page", 1)
//                .addParam("size", 10)
//                .get()
//                .url(url)
//                .build()
//                .enqueue(new BaseCallBack() {
//                    @Override
//                    public void onSuccess(Object o) {
////                        Toast.makeText(MainActivity.this, "成功：" + o.toString(), Toast.LENGTH_SHORT).show();
////                        txtContent.setText(o.toString());
//                    }
//
//                    @Override
//                    public void onError(int code) {
////                        Toast.makeText(MainActivity.this, "错误编码：" + code, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call call, IOException e) {
////                        Toast.makeText(MainActivity.this, "失败：" + e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


    private void doRegisterQequestInvalidIDGet(String phone){
        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/register?phone=" +phone).get().build();
        executeRequest(request);
    }
    private void executeRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(SEND_INVALID_FAILED);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(SERVER_EXCEPTION);
                } else if (res.contains(CommonUtils.mSuccess)){
                    mHandler.sendEmptyMessage(SEND_INVALID_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(OTHER_ERROR);
                }
            }

//            @Override
//            public void onFailure(Request request, IOException e) {
//                e.printStackTrace();
//            }
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final String res = response.body().string();
//                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(SEND_INVALID_FAILED);
//                } else if (res.equals("") || null == res) {
//                    mHandler.sendEmptyMessage(SERVER_EXCEPTION);
//                } else if (res.contains(":1,")){
//                    mHandler.sendEmptyMessage(SEND_INVALID_SUCCESS);
//                } else {
//                    mHandler.sendEmptyMessage(OTHER_ERROR);
//                }
//            }
        });
    }

    private void doResetPwdPost(String phone,String pwd,String verifycode){
        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        RequestBody requestBody = requestBodyBuilder
                .add("phone",phone)
                .add("verifyCode",verifycode)
                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/resetPwd").post(requestBody).build();
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
                if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(REGISTER_FAILED);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(SERVER_EXCEPTION);
                } else if (res.contains(CommonUtils.mSuccess)){
                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(4);
                }
            }
        });
    }
}
