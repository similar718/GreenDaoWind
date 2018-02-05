//package com.nedfon.nedfon.ui;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.fragment.DataSearchFragment;
////import com.nedfon.nedfon.fragment.DeviceBindFragment;
//import com.nedfon.nedfon.fragment.DeviceSetInternetFragment;
//import com.nedfon.nedfon.fragment.PersonalCenterFragment;
//import com.nedfon.nedfon.utils.StatusBarCompat;
//
//public class MainFragmentActivity extends AppCompatActivity implements View.OnClickListener {
//    private LinearLayout ll_index;
//    private LinearLayout ll_licai;
//    private LinearLayout ll_mine;
//    private LinearLayout ll_min;
//
//    private ImageView mDataQueryIv,mDeviceSetInternetIv,mDeviceBindIv,mPersonalInfoIv;
//
//    private DataSearchFragment datasearchFragment;
//    private DeviceSetInternetFragment deviceinternetFragment;
////    private DeviceBindFragment devicebindFragment;
//    private PersonalCenterFragment personalcenterFragment;
//
//
////    private DeviceFragement mDeviceFragment;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(MainFragmentActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_main_fragment);
//
//        ll_index = (LinearLayout) findViewById(R.id.ll_index);
//        ll_licai = (LinearLayout) findViewById(R.id.ll_licai);
//        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
//        ll_min = (LinearLayout) findViewById(R.id.ll_min);
//
//        mDataQueryIv = this.findViewById(R.id.data_query_iv);
//        mDeviceSetInternetIv = this.findViewById(R.id.device_set_internet_iv);
//        mDeviceBindIv = this.findViewById(R.id.device_bind_iv);
//        mPersonalInfoIv = this.findViewById(R.id.personal_info_iv);
//
//        datasearchFragment = new DataSearchFragment();
//        deviceinternetFragment = new DeviceSetInternetFragment();
////        devicebindFragment = new DeviceBindFragment();
//        personalcenterFragment = new PersonalCenterFragment();
////        mDeviceFragment = new DeviceFragement();
//
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(R.id.fl_top, datasearchFragment, "datasearch");
//        ft.add(R.id.fl_top, deviceinternetFragment, "deviceinternet");
//        ft.add(R.id.fl_top, devicebindFragment, "devicebind");
//        ft.add(R.id.fl_top, personalcenterFragment, "personalcenter");
//
////        ft.add(R.id.fl_top, mDeviceFragment,"device");
//
//        ft.hide(datasearchFragment);
//        ft.hide(deviceinternetFragment);
//        ft.hide(personalcenterFragment);
////        ft.hide(mDeviceFragment);
//        ft.show(devicebindFragment).commit();
//
//        setClickListener();
//    }
//
//    private void viewdefualtStatus(){
//        mDataQueryIv.setImageResource(R.drawable.data_query);
//        mDeviceSetInternetIv.setImageResource(R.drawable.set_internet_unselect);
//        mDeviceBindIv.setImageResource(R.drawable.device_bind_unselect);
//        mPersonalInfoIv.setImageResource(R.drawable.my_icon_unselect);
//    }
//
//    //注册点击监听
//    private void setClickListener() {
//        ll_index.setOnClickListener(this);
//        ll_licai.setOnClickListener(this);
//        ll_mine.setOnClickListener(this);
//        ll_min.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        switch (v.getId()) {
//            case R.id.ll_index:
//                showDeviceSearchFragment();
//                break;
//            case R.id.ll_licai:
//                showDeviceSetInternetFragment();
//                break;
//            case R.id.ll_mine:
//                showDeviceBindFragment();
//                break;
//            case R.id.ll_min:
//                showPersonalCenterFragment();
//                break;
//        }
//    }
//
//    public void showDeviceSearchFragment(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        datasearchFragment = (DataSearchFragment) fm.findFragmentByTag("datasearch");
//        ft.hide(deviceinternetFragment);
//        ft.hide(devicebindFragment);
//        ft.hide(personalcenterFragment);
////        ft.hide(mDeviceFragment);
//        ft.show(datasearchFragment).commit();
//        viewdefualtStatus();
//        mDataQueryIv.setImageResource(R.drawable.data_query_selected);
//    }
//
//    public void showDeviceSetInternetFragment(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        deviceinternetFragment = (DeviceSetInternetFragment) fm.findFragmentByTag("deviceinternet");
//        ft.hide(datasearchFragment);
//        ft.hide(devicebindFragment);
//        ft.hide(personalcenterFragment);
////        ft.hide(mDeviceFragment);
//        ft.show(deviceinternetFragment).commit();
//        viewdefualtStatus();
//        mDeviceSetInternetIv.setImageResource(R.drawable.set_internet_selected);
//    }
//
//    public void showDeviceBindFragment(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        devicebindFragment = (DeviceBindFragment) fm.findFragmentByTag("devicebind");
//        ft.hide(datasearchFragment);
//        ft.hide(deviceinternetFragment);
//        ft.hide(personalcenterFragment);
////        ft.hide(mDeviceFragment);
//        ft.show(devicebindFragment).commit();
//        viewdefualtStatus();
//        mDeviceBindIv.setImageResource(R.drawable.device_bind_selected);
//    }
//
//    public void showPersonalCenterFragment(){
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        personalcenterFragment = (PersonalCenterFragment) fm.findFragmentByTag("personalcenter");
//        ft.hide(datasearchFragment);
//        ft.hide(deviceinternetFragment);
//        ft.hide(devicebindFragment);
////        ft.hide(mDeviceFragment);
//        ft.show(personalcenterFragment).commit();
//        viewdefualtStatus();
//        mPersonalInfoIv.setImageResource(R.drawable.my_icon_selected);
//    }
//
////    public void showDeviceFragment(){
////        FragmentManager fm = getFragmentManager();
////        FragmentTransaction ft = fm.beginTransaction();
////        mDeviceFragment = (DeviceFragement) fm.findFragmentByTag("device");
////        ft.hide(datasearchFragment);
////        ft.hide(deviceinternetFragment);
////        ft.hide(devicebindFragment);
////        ft.hide(personalcenterFragment);
////        ft.show(mDeviceFragment).commit();
////        viewdefualtStatus();
////        mDeviceBindIv.setImageResource(R.drawable.device_bind_selected);
////    }
//}
