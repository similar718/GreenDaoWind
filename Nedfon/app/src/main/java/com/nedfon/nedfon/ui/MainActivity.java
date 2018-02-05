package com.nedfon.nedfon.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.StatusBarCompat;

public class MainActivity extends Activity implements OnClickListener{
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();

    private LinearLayout mTabMain;
    private LinearLayout mTabDynamic;
    private LinearLayout mTabFriend;
    private LinearLayout mTabMe;

    private ImageView mMainImg;
    private ImageView mDynamicImg;
    private ImageView mFriendImg;
    private ImageView mMeImg;

    private TextView mMainTxt;
    private TextView mDynamicTxt;
    private TextView mFriendTxt;
    private TextView mMeTxt;

    private LocalActivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(MainActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_main);

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        initView();
        initEvents();
    }

    private void initEvents() {
        //给底部的4个LinearLayout即4个导航控件添加点击事件
        mTabMain.setOnClickListener(this);
        mTabDynamic.setOnClickListener(this);
        mTabFriend.setOnClickListener(this);
        mTabMe.setOnClickListener(this);
        //viewpager滑动事件
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {//当viewpager滑动时，对应的底部导航按钮的图片要变化
                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem) {
                    case 0:
                        mMainImg.setImageResource(R.drawable.device_bind_selected);
//                        mMainTxt.setTextColor(Color.parseColor("#ff2700"));
                        break;
                    case 1:
                        mDynamicImg.setImageResource(R.drawable.device_bind_selected);
//                        mDynamicTxt.setTextColor(Color.parseColor("#ff2700"));
                        break;
                    case 2:
                        mFriendImg.setImageResource(R.drawable.device_bind_selected);
//                        mFriendTxt.setTextColor(Color.parseColor("#ff2700"));
                        break;
                    case 3:
                        mMeImg.setImageResource(R.drawable.my_icon_selected);
//                        mMeTxt.setTextColor(Color.parseColor("#ff2700"));
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView() {//初始化所有的view
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        //tabs
        mTabMain = (LinearLayout) findViewById(R.id.id_tab_main);
        mTabDynamic = (LinearLayout) findViewById(R.id.id_tab_dynamic);
        mTabFriend = (LinearLayout) findViewById(R.id.id_tab_friend);
        mTabMe = (LinearLayout) findViewById(R.id.id_tab_me);
        //imagebutton
        mMainImg = (ImageView)findViewById(R.id.id_tab_main_img);
        mDynamicImg = (ImageView)findViewById(R.id.id_tab_dynamic_img);
        mFriendImg = (ImageView)findViewById(R.id.id_tab_friend_img);
        mMeImg = (ImageView)findViewById(R.id.id_tab_me_img);

        mMainTxt = (TextView) findViewById(R.id.id_tab_main_txt);
        mDynamicTxt = (TextView) findViewById(R.id.id_tab_dynamic_txt);
        mFriendTxt = (TextView) findViewById(R.id.id_tab_friend_txt);
        mMeTxt = (TextView) findViewById(R.id.id_tab_me_txt);

        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.putExtra("id", 1);
        mViews.add(getView("QualityActivity1", intent));

        intent.setClass(this, ForgotPasswordActivity.class);
        intent.putExtra("id", 2);
        mViews.add(getView("QualityActivity2", intent));

        intent.setClass(this, FindPwdSuccessActivity.class);
        intent.putExtra("id", 3);
        mViews.add(getView("QualityActivity3", intent));

        intent.setClass(this, PersonalCenterActivity.class);
        intent.putExtra("id", 4);
        mViews.add(getView("QualityActivity5", intent));

        //初始化PagerAdapter
        mAdapter = new PagerAdapter() {
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);//从mViews这个list中根据位置取出我们需要的view
                container.addView(view);//将上述的view添加到ViewGroup中
                return view;
            }
            /*
             * isViewFromObject用来判断instantiateItem(ViewGroup, int)函数所返回来的Key与一个页面视图是否是代表
             * 的同一个视图(即它俩是否是对应的，对应的表示同一个View),如果对应的是同一个View，返回True，否则返回False
             * */
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // 返回PagerView包含的view的个数
                return mViews.size();
            }
        };
        //为ViewPager设置adapter
        mViewPager.setAdapter(mAdapter);
//        Log.e("MainActivity", "initView:  viewpager getitem"+mViewPager.getCurrentItem());
//        mViewPager.setCurrentItem(2);
//        Log.e("MainActivity", "initView:  viewpager getitem"+mViewPager.getCurrentItem());
        mViewPager.setPageTransformer(true,null);//设置切换动画
        mViewPager.setCurrentItem(3);
    }

    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();

    }
    @Override
    public void onClick(View v) {
        resetImg();//点击哪个tab,对应的颜色要变亮，因此，在点击具体的tab之前先将所有的图片都重置为未点击的状态，即暗色图片
        switch (v.getId()) {
            case R.id.id_tab_main:
                mViewPager.setCurrentItem(0);//
                mMainImg.setImageResource(R.drawable.device_bind_selected);//并将按钮颜色点亮
//                mMainTxt.setTextColor(Color.parseColor("#ff2700"));
                break;
            case R.id.id_tab_dynamic://第三个
                mViewPager.setCurrentItem(1);
                mDynamicImg.setImageResource(R.drawable.device_bind_selected);
//                mDynamicTxt.setTextColor(Color.parseColor("#ff2700"));
                break;
            case R.id.id_tab_friend://第二个
                mViewPager.setCurrentItem(2);
                mFriendImg.setImageResource(R.drawable.device_bind_selected);
//                mFriendTxt.setTextColor(Color.parseColor("#ff2700"));
                break;
            case R.id.id_tab_me:
                mViewPager.setCurrentItem(3);
                mMeImg.setImageResource(R.drawable.my_icon_selected);
//                mMeTxt.setTextColor(Color.parseColor("#ff2700"));
                break;
            default:
                break;
        }
    }

    /*
     * 将所有的图片设置为暗色
     * */
    private void resetImg() {
        mMainImg.setImageResource(R.drawable.device_bind_unselect);
        mDynamicImg.setImageResource(R.drawable.device_bind_unselect);
        mFriendImg.setImageResource(R.drawable.device_bind_unselect);
        mMeImg.setImageResource(R.drawable.my_icon_unselect);

//        mMainTxt.setTextColor(Color.parseColor("#aaaaaa"));
//        mDynamicTxt.setTextColor(Color.parseColor("#aaaaaa"));
//        mFriendTxt.setTextColor(Color.parseColor("#aaaaaa"));
//        mMeTxt.setTextColor(Color.parseColor("#aaaaaa"));
    }
}
