package com.nedfon.nedfon.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.CountDownTimerUtils;
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
import com.citypicker.CheckPermissionsListener;
import com.citypicker.CityPickerActivity;
import com.citypicker.model.LocateState;
import com.citypicker.utils.GetLocation;
import com.citypicker.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener ,CheckPermissionsListener{

    private TextView mBackTv;
    private RelativeLayout mRegisterRl;
    private Button mSendinvalidBtn;
    private EditText mIvaliedEt,mPwdEt,mPhoneEt;
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

    private String lng = "";
    private String lat = "";
    private String citys = "";
    private AMapLocationClient mLocationClient;

    //高德定位所需要的权限
    protected final String[] neededPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int REQUEST_CODE = 2333;

    private CheckPermissionsListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(RegisterActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mBackTv = this.findViewById(R.id.activity_register_back_tv);
        mRegisterRl = this.findViewById(R.id.activity_register_sent_rl);
        mSendinvalidBtn = this.findViewById(R.id.activity_register_send_btn);
        mIvaliedEt = this.findViewById(R.id.activity_register_invalid_et);
        mPwdEt = this.findViewById(R.id.activity_register_pwd_et);
        mPhoneEt = this.findViewById(R.id.activity_register_phone_et);
        mRemovePhoneIv = this.findViewById(R.id.activity_register_remove_phone_iv);

        mBackTv.setOnClickListener(this);
        mRegisterRl.setOnClickListener(this);
        mSendinvalidBtn.setOnClickListener(this);
        mRemovePhoneIv.setOnClickListener(this);

        initLocation();
        //请求权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mLocationClient.startLocation();
        }else {
            requestPermissions(this, neededPermissions, this);
        }
    }
    private void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String location = StringUtils.extractLocation(city, district);

                        lng = aMapLocation.getLongitude()+"";
                        lat = aMapLocation.getLatitude()+"";
                        citys = location;
                        Log.e("oooooooooo","success lng = "+lng+" lat = "+lat+" city = "+citys);
                    } else {
                        //定位失败
                        Log.e("oooooooooo","failed lng = "+lng+" lat = "+lat+" city = "+citys);
                    }
                }
            }
        });
    }

    public void requestPermissions(Activity activity, String[] permissions, CheckPermissionsListener listener){
        if (activity == null) return;
        mListener = listener;
        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);
        if (!deniedPermissions.isEmpty()){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }else{
            //所有权限都已经同意了
            mListener.onGranted();
        }
    }
    private List<String> findDeniedPermissions(Activity activity, String... permissions){
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                List<String> deniedPermissions = new ArrayList<>();
                int length = grantResults.length;
                for (int i = 0; i < length; i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        //该权限被拒绝了
                        deniedPermissions.add(permissions[i]);
                    }
                }
                if (deniedPermissions.size() > 0){
                    mListener.onDenied(deniedPermissions);
                }else{
                    mListener.onGranted();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_register_back_tv: //返回
                RegisterActivity.this.finish();
                break;

            case R.id.activity_register_sent_rl: //注册
                register();
                break;

            case R.id.activity_register_send_btn: //发送验证码
                sendInvalid();
                break;

            case R.id.activity_register_remove_phone_iv: //清除手机号码
                mPhoneEt.setText("");
                break;
        }
    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PHONE_IS_NOT_NULL:
                    ToastUtils.show(RegisterActivity.this,"电话号码不能为空");
                    break;
                case SEND_INVALID_FAILED :
                    ToastUtils.show(RegisterActivity.this,"发送验证码失败！");
                    break;
                case SEND_INVALID_SUCCESS :
                    ToastUtils.show(RegisterActivity.this,"发送验证码成功");
                    break;
                case INPUT_PHONE_NUMBER :
                    ToastUtils.show(RegisterActivity.this,"请输入正确的电话号码！");
                    break;
                case SERVER_EXCEPTION :
                    ToastUtils.show(RegisterActivity.this,"服务器异常");
                    break;
                case REGISTER_NOT_NULL :
                    ToastUtils.show(RegisterActivity.this,"手机号码/密码/验证码不能为空！");
                    break;
                case REGISTER_FAILED :
                    ToastUtils.show(RegisterActivity.this,"注册失败！");
                    break;
                case REGISTER_SUCCESS :
                    ToastUtils.show(RegisterActivity.this,"注册成功！");
                    RegisterActivity.this.finish();
                    break;
                case OTHER_ERROR :
                    ToastUtils.show(RegisterActivity.this,"其他错误！");
                    break;
            }
        }
    };

    private void register(){
        String phone = mPhoneEt.getText().toString();
        String pwd = mPwdEt.getText().toString();
        String invalid = mIvaliedEt.getText().toString();
        if (phone.equals("") || "".equals(pwd) || "".equals(invalid)){
            mHandler.sendEmptyMessage(REGISTER_NOT_NULL);
            return;
        }
        if (CommonUtils.isPhone(phone)){
            doVerifyReqisterPost(phone,pwd,invalid,lat,lng,citys);
        } else {
            mHandler.sendEmptyMessage(INPUT_PHONE_NUMBER);
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
            CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(mSendinvalidBtn, 60000, 1000);
            mCountDownTimerUtils.start();
        } else {
            mHandler.sendEmptyMessage(INPUT_PHONE_NUMBER);
        }
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();

    private void doRegisterQequestInvalidIDGet(String phone){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
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
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(SEND_INVALID_FAILED);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(SERVER_EXCEPTION);
                } else if (res.contains(":1,")){
                    mHandler.sendEmptyMessage(SEND_INVALID_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(OTHER_ERROR);
                }
            }
        });
    }

    private void doVerifyReqisterPost(String phone,String pwd,String verifycode,String lat,String lng,String city){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        RequestBody requestBody = requestBodyBuilder
                .add("phone",phone)
                .add("pwd",pwd)
                .add("verifyCode",verifycode)
                .add("lat",lat)
                .add("lng",lng)
                .add("city",city).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(CommonUtils.localhost+"mobileapi/verifyRegister").post(requestBody).build();
//        Request request = builder.url(CommonUtils.localhost+"mobileapi/verifyReqister?phone=" +
//                ""+phone+"&pwd="+pwd+"&verifyCode="+verifycode+"&lat="+lat+"&lng="+lng+"&city="+city).post(requestBody).build();
        executeVerifyReqisterRequest(request);
    }

    private void executeVerifyReqisterRequest(Request request) {
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
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(":0,")){
                    mHandler.sendEmptyMessage(REGISTER_FAILED);
                } else if (res.equals("") || null == res) {
                    mHandler.sendEmptyMessage(SERVER_EXCEPTION);
                } else if (res.contains(":1,")){
                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(OTHER_ERROR);
                }
            }
        });
    }

    @Override
    public void onGranted() {
        mLocationClient.startLocation();
    }

    @Override
    public void onDenied(List<String> permissions) {
        Toast.makeText(this, "权限被禁用，请到设置里打开", Toast.LENGTH_SHORT).show();
    }
}
