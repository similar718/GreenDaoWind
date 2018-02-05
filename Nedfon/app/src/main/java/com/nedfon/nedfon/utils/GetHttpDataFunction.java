package com.nedfon.nedfon.utils;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class GetHttpDataFunction {

    private static String localhost = "http://111.231.234.151:9090/mobileapi/register?phone=";

//    private static final String register_url_get = "http://localhost/mobileapi/register?phone=";
//    private static final String verifyReqister_url_post = "http://localhost/mobileapi/verifyReqister";
//    private static final String login_url_post = "http://localhost/mobileapi/login";
//    private static final String resetPwd_url_post = "http://localhost/mobileapi/resetPwd";
    private static final String certificateUpdate_url_get = "http://localhost/mobileapi/certificateUpdate?token=";
//    private static final String deviceList_url_get = "http://localhost/mobileapi/deviceList?token=";
    private static final String deviceInfo_url_get = "http://localhost/mobileapi/deviceInfo?token=";
//    private static final String controlWindCmd_url_get = "http://localhost/mobileapi/controlWindCmd?token=";
//    private static final String controlTimerCmd_url_get = "http://localhost/mobileapi/controlTimerCmd?token=";
//    private static final String controlThresholdCmd_url_get = "http://localhost/mobileapi/controlThresholdCmd?token=";
//    private static final String getCurrentWeather_url_get = "http://localhost/mobileapi/getCurrentWeather?token=";
    private static final String bindingDevice_url_get = "http://localhost/mobileapi/bindingDevice?token=";

    private static OkHttpClient okhttpclient = new OkHttpClient();
//    //注册请求验证码
//    public static String RegisterQequestInvalidID(String phone){
//        return doRegisterQequestInvalidIDGet(phone);
//    }
//    //验证码注册/登录
//    public static String VerifyReqister(String phone,String pwd,String verifycode,String lat,String lng,String city){
//        return doVerifyReqisterPost(phone,pwd,verifycode,lat,lng,city);
//    }
//    //登录
//    public static String Login(String phone,String pwd){
//        return doLoginPost(phone,pwd);
//    }

//    //重置密码
//    public static String ResetPwd(String phone,String pwd,String verifycode){
//        return doResetPwdPost(phone,pwd,verifycode);
//    }

    //请求更新会话状态
    public static String CertificateUpdate(String token){
        return doCertificateUpdateGet(token);
    }

//    //获取用户设备列表
//    public static String DeviceList(String token){
////        return doDeviceListGet(token);
////    }

    //获取单台设备状态
    public static String DeviceInfo(String token,String deviceSN){
        return doDeviceInfoGet(token,deviceSN);
    }

    //控制风机
//    public static String ControlWindCmd(String token,String deviceSN,String fanModel,String fanLevel,String fanOnOff,String ionsOnOff){
//        return doControlWindCmdGet(token,deviceSN,fanModel,fanLevel,fanOnOff,ionsOnOff);
//    }

    //增加或修改定时开关机
//    public static String ControlTimerCmd(String token,String opentime,String closetime,String setting){
//        return doControlTimerCmdGet(token,opentime,closetime,setting);
//    }
//
//    //控制阀值
//    public static String ControlThresholdCmd(String token,String deviceSN,String temp,String sweet,String pm25){
//        return doControlThresholdCmdGet(token,deviceSN,temp,sweet,pm25);
//    }
//
//    //请求当天天气信息
//    public static String GetCurrentWeather(String token){
//        return doGetCurrentWeatherGet(token);
//    }

    //绑定设备
    public static String BindingDevice(String token,String deviceSN){
        return doBindingDeviceGet(token,deviceSN);
    }

//    private static String doRegisterQequestInvalidIDGet(String phone){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(register_url_get +phone).get().build();
//        return executeRequest(request);
//    }

//    private static String doVerifyReqisterPost(String phone,String pwd,String verifycode,String lat,String lng,String city){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("pwd",pwd)
//                .add("verifyCode",verifycode)
//                .add("lat",lat)
//                .add("lng",lng)
//                .add("city",city).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(verifyReqister_url_post).post(requestBody).build();
//        return executeRequest(request);
//    }
//
//    private static String doLoginPost(String phone,String pwd){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(login_url_post).post(requestBody).build();
//        return executeRequest(request);
//    }
//
//    private static String doResetPwdPost(String phone,String pwd,String verifycode){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("verifyCode",verifycode)
//                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(resetPwd_url_post).post(requestBody).build();
//        return executeRequest(request);
//    }

    private static String doCertificateUpdateGet(String token){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("verifyCode",verifycode)
//                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(certificateUpdate_url_get+token).get().build();
        return executeRequest(request);
    }

//    private static String doDeviceListGet(String token){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
////        RequestBody requestBody = requestBodyBuilder
////                .add("phone",phone)
////                .add("verifyCode",verifycode)
////                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(deviceList_url_get+token).get().build();
//        return executeRequest(request);
//    }

    private static String doDeviceInfoGet(String token,String deviceSN){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("verifyCode",verifycode)
//                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(deviceInfo_url_get+token+"&deviceSN="+deviceSN).get().build();
        return executeRequest(request);
    }

//    private static String doControlWindCmdGet(String token,String deviceSN,String fanModel,String fanLevel,String fanOnOff,String ionsOnOff){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
////        RequestBody requestBody = requestBodyBuilder
////                .add("phone",phone)
////                .add("verifyCode",verifycode)
////                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(controlWindCmd_url_get+token+"&deviceSN="+deviceSN+"&fanModel="+fanModel+"&fanLevel="+fanLevel+"&fanOnOff="+fanOnOff+"&ionsOnOff="+ionsOnOff).get().build();
//        return executeRequest(request);
//    }

//    private static String doControlTimerCmdGet(String token,String opentime,String closetime,String setting){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
////        RequestBody requestBody = requestBodyBuilder
////                .add("phone",phone)
////                .add("verifyCode",verifycode)
////                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(controlTimerCmd_url_get+token+"&opentime="+opentime+"&closetime="+closetime+"&setting="+setting).get().build();
//        return executeRequest(request);
//    }

//    private static String doControlThresholdCmdGet(String token,String deviceSN,String temp,String sweet,String pm25){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
////        RequestBody requestBody = requestBodyBuilder
////                .add("phone",phone)
////                .add("verifyCode",verifycode)
////                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(controlThresholdCmd_url_get+token+"&deviceSN="+deviceSN+"&temp="+temp+"&sweet="+sweet+"&pm25="+pm25).get().build();
//        return executeRequest(request);
//    }

//    private static String doGetCurrentWeatherGet(String token){
//        //1.拿到OkHttpClient对象
//        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
//        //2.构造Request
////        RequestBody requestBody = requestBodyBuilder
////                .add("phone",phone)
////                .add("verifyCode",verifycode)
////                .add("pwd",pwd).build();
//        Request.Builder builder = new Request.Builder();
//        Request request = builder.url(getCurrentWeather_url_get+token).get().build();
//        return executeRequest(request);
//    }

    private static String doBindingDeviceGet(String token,String deviceSN){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
//        RequestBody requestBody = requestBodyBuilder
//                .add("phone",phone)
//                .add("verifyCode",verifycode)
//                .add("pwd",pwd).build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(bindingDevice_url_get+token+"&deviceSN="+deviceSN).get().build();
        return executeRequest(request);
    }

    private static String executeRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        final String[] object = {""};
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
                Log.i("GetHttpDataFunction","executeVerifyReqisterRequest onResponse:"+res);
                object[0] = res;
            }
        });
        return object[0];
    }
}
