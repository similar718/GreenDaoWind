package com.citypicker.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Administrator on 2018/2/4 0004.
 */

public class GetLocation {
    private static AMapLocationClient mLocationClient;
    public static String initLocation(Context context) {
        final String[] lng = {"0.0"};
        final String[] lat = {"0.0"};
        final String[] citys = {"广州"};
        mLocationClient = new AMapLocationClient(context);
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
                        lng[0] = aMapLocation.getLongitude()+"";
                        lat[0] = aMapLocation.getLatitude()+"";
                        citys[0] = city;
                    } else {
                        //定位失败
                    }
                }
            }
        });
        return lng[0]+" "+lat[0]+" "+citys[0];
    }

}
