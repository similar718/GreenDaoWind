package com.nedfon.nedfon.utils;

import com.nedfon.nedfon.bean.DeviceInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class CommonUtils {

    public static final String localhost = "http://111.231.234.151:9090/";
//    public static final String localhost = "http://111.231.234.151:8234/";

    public static String token = "";
    public static DeviceInfo bean = null;

    public static Boolean mIsLogin = false;


    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
}
