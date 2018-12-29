package com.nedfon.nedfon.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        primissionAsk();
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
        if (CommonUtils.mPhoneNum != null && !"".equals(CommonUtils.mPhoneNum)){
            mPhoneEt.setText(CommonUtils.mPhoneNum);
        }
    }

    private void primissionAsk() {
        Log.e("权限判断", "开始" );
        if(
                ContextCompat.checkSelfPermission(this, CHANGE_WIFI_MULTICAST_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, BLUETOOTH) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, INTERNET) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, VIBRATE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //若果是第一次
            new AlertDialog.Builder(this).setMessage("为了保证应用正常运行，需要获取以下权限！")
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
                                    WRITE_EXTERNAL_STORAGE,
                                    CHANGE_WIFI_MULTICAST_STATE,
                                    WRITE_SETTINGS,
                                    CHANGE_NETWORK_STATE,
                                    BLUETOOTH_ADMIN,
                                    BLUETOOTH,
                                    ACCESS_LOCATION_EXTRA_COMMANDS,
                                    READ_PHONE_STATE,
                                    INTERNET,
                                    CHANGE_WIFI_STATE,
                                    ACCESS_WIFI_STATE,
                                    ACCESS_NETWORK_STATE,
                                    ACCESS_COARSE_LOCATION,
                                    READ_EXTERNAL_STORAGE,
                                    VIBRATE,
                                    ACCESS_FINE_LOCATION}, 001);
                        }
                    }).show();
        } else {
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
//            finish();                                                                        //(2)
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == 001) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
//                finish();                                                                      //(3)
            } else {
                finish();                                                                    //（1）
                //0表示正常退出，1表示异常退出
                System.exit(0);
            }
        }
    }



    private void startActivityToMain(DbDean bean, boolean flag){
        if (flag)
            if (myDBHelper.query(bean.phone)) { //如果有就只更新token
                myDBHelper.updateUser(bean.token, bean.phone);
            } else {
                myDBHelper.insert1(bean);
            }
        startpage(bean.token,bean.phone);
    }

    private void startpage(String token,String phone){
        CommonUtils.token = token;
        CommonUtils.phone = phone;
        SharedPreferences sp = getSharedPreferences("nedfon",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("token", token);
        edit.commit();
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
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
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
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(1);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(2);
                } else if (res.contains(CommonUtils.mSuccess)){
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
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
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
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
                return;
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(5);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(2);
                } else if (res.contains(CommonUtils.mSuccess)){
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
                    CommonUtils.mPhoneNum = "";
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
