package com.nedfon.nedfon.utils;

import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.GetPersonInfoBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class CommonUtils {

    public static final String localhost = "http://111.231.234.151:9090/"; // TODO 更换地址http://39.108.215.34:8080/
//    public static final String localhost = "http://111.231.234.151:8234/";

    public static String token = "";
    public static String deviceSN = "";
    public static String[] mDeviceList = null;
    public static DeviceInfo bean = null;
    public static GetPersonInfoBean info = null;

    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }
}
