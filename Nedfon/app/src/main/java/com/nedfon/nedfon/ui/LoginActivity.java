package com.nedfon.nedfon.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DbDean;
import com.nedfon.nedfon.bean.LoginSuccess;
import com.nedfon.nedfon.db.MyDBHelper;
import com.nedfon.nedfon.uiok.DeviceBindOkActivity;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.GetHttpDataFunction;
import com.nedfon.nedfon.utils.StatusBarCompat;
import com.nedfon.nedfon.utils.ToastUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPhoneEt;
    private EditText mPwdEt;
    private TextView mForgotPwdTv;
    private RelativeLayout mLoginRl;
    private RelativeLayout mRegitserRl;

    private MyDBHelper myDBHelper;
    List<DbDean> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(LoginActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_login);
        myDBHelper = new MyDBHelper(this);
        list = myDBHelper.query();
        if (list.size()!=0){
            if (list.get(0).time<System.currentTimeMillis()) {
                doCertificateUpdatePost(list.get(0).token);
            }
        }
        initView();
    }
    private void initView(){
        mPhoneEt = (EditText) this.findViewById(R.id.activity_login_phone_et);
        mPwdEt = (EditText) this.findViewById(R.id.activity_login_pwd_et);
        mLoginRl = (RelativeLayout) this.findViewById(R.id.activity_login_sent_rl);
        mRegitserRl = (RelativeLayout) this.findViewById(R.id.activity_login_register_rl);

        mForgotPwdTv = (TextView)this.findViewById(R.id.activity_login_forgotpwd_tv);
        mForgotPwdTv.setText(Html.fromHtml("<u>"+getResources().getString(R.string.activity_login_forgotpwd_txt)+"</u>"));

        mLoginRl.setOnClickListener(this);
        mRegitserRl.setOnClickListener(this);
        mForgotPwdTv.setOnClickListener(this);

        if (null != list && list.size()>0){
            mPhoneEt.setText(list.get(0).phone);
            mPwdEt.setText(list.get(0).pwd);
        }

    }

    private void startActivityToMain(DbDean bean, boolean flag){
        if (flag)
            if (myDBHelper.query(bean.phone)) { //如果有就只更新token
                myDBHelper.updateUser(bean.token, bean.phone);
            } else {
                myDBHelper.insert1(bean);
            }
        startpage(bean.token);
    }

    private void startpage(String token){
        CommonUtils.token = token;
        Intent main = new Intent(LoginActivity.this, DeviceBindOkActivity.class);
        startActivity(main);
        LoginActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_forgotpwd_tv: //忘记密码
                Intent forgot = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgot);
                break;
            case R.id.activity_login_register_rl: //注册
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.activity_login_sent_rl: //登陆
                String phone = mPhoneEt.getText().toString();
                String pwd = mPwdEt.getText().toString();
                if (phone.equals("") || pwd.equals("")){
                    ToastUtils.show(LoginActivity.this,"电话号码或密码不能为空");
                    return;
                }
                if (CommonUtils.isPhone(phone)){
                    doLoginPost(phone,pwd);
                } else {
                    ToastUtils.show(LoginActivity.this,"请输入正确的电话号码！");
                }
                break;
            default:
                break;
        }
    }

    private void doLoginPost(String phone,String pwd){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        RequestBody requestBody = requestBodyBuilder
                .add("phone",phone)
                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/login").post(requestBody).build();
        executeRequest(request);
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();
    private void executeRequest(Request request) {
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
                if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(1);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(2);
                } else if (res.contains(":1,")){
                    CommonUtils.mIsLogin = true;
                    mHandler.sendEmptyMessage(3);
                    LoginSuccess bean = new Gson().fromJson(res,LoginSuccess.class);
                    startActivityToMain(new DbDean(mPhoneEt.getText().toString(),mPwdEt.getText().toString(),bean.token, (System.currentTimeMillis()+(60*60*24*7))),true);
                } else {
                    mHandler.sendEmptyMessage(4);
                }
            }
        });
    }

    private void doCertificateUpdatePost(String token){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/certificateUpdate?token="+token).get().build();
        executeCertificateUpdateRequest(request);
    }
    private void executeCertificateUpdateRequest(Request request) {
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
                if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(5);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(2);
                } else if (res.contains(":1,")){
                    LoginSuccess bean = new Gson().fromJson(res,LoginSuccess.class);
                    startActivityToMain(new DbDean(list.get(0).phone,list.get(0).pwd,bean.token,list.get(0).time),true);
                    mHandler.sendEmptyMessage(6);
                } else {
                    mHandler.sendEmptyMessage(4);
                }
            }
        });
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtils.show(LoginActivity.this,"登陆失败！");
                    break;
                case 2 :
                    ToastUtils.show(LoginActivity.this,"服务器异常");
                    break;
                case 3 :
                    ToastUtils.show(LoginActivity.this,"登陆成功");
                    break;
                case 4 :
                    ToastUtils.show(LoginActivity.this,"其他问题错误！");
                    break;
                case 5 :
                    ToastUtils.show(LoginActivity.this,"请求更新会话状态失败");
                    break;
                case 6 :
                    ToastUtils.show(LoginActivity.this,"请求更新会话状态成功");
                    break;
            }
        }
    };


}
