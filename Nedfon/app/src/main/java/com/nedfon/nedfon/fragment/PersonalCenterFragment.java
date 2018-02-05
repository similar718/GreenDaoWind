//package com.nedfon.nedfon.fragment;
//
//import android.app.Fragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.ui.MessageCenterActivity;
//import com.nedfon.nedfon.ui.MyInformationActivity;
//import com.nedfon.nedfon.utils.GetHttpDataFunction;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by Administrator on 2018/1/26 0026.
// */
//
//public class PersonalCenterFragment extends Fragment implements View.OnClickListener {
//
//    private RelativeLayout mExitRl;
//    private ImageView mHeadIconIv;
//    private TextView mUserNameTv;
//    private TextView mPhoneNumberTv;
//    private RelativeLayout mInfoRl,mMsgRl,mUpdatePwdRl;
//    private ImageView mInfoIv,mMsgIv,mUpdatePwdIv;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
//        initview(view);
//        return view;
//    }
//
//    public void initview(View view) {
//        mExitRl = view.findViewById(R.id.fragment_personal_center_exit_rl);
//        mHeadIconIv = view.findViewById(R.id.fragment_personal_center_head_img);
//        mUserNameTv = view.findViewById(R.id.fragment_personal_center_name_tv);
//        mPhoneNumberTv = view.findViewById(R.id.fragment_personal_center_phone_number_tv);
//
//        mInfoRl = view.findViewById(R.id.fragment_personal_center_info_rl);
//        mMsgRl = view.findViewById(R.id.fragment_personal_center_msg_rl);
//        mUpdatePwdRl = view.findViewById(R.id.fragment_personal_center_update_pwd_rl);
//
//        mInfoIv = view.findViewById(R.id.fragment_personal_center_info_right_iv);
//        mMsgIv = view.findViewById(R.id.fragment_personal_center_msg_right_iv);
//        mUpdatePwdIv = view.findViewById(R.id.fragment_personal_center_update_pwd_right_iv);
//
//        mExitRl.setOnClickListener(this);
//
//        mInfoRl.setOnClickListener(this);
//        mMsgRl.setOnClickListener(this);
//        mUpdatePwdRl.setOnClickListener(this);
//        mMsgIv.setOnClickListener(this);
//        mUpdatePwdIv.setOnClickListener(this);
//        mInfoIv.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.fragment_personal_center_exit_rl://退出 //TODO 我这边做数据库的操作
//                break;
//            case R.id.fragment_personal_center_info_rl://个人信息RL TODO
//                Intent information = new Intent(getActivity(), MyInformationActivity.class);
//                startActivity(information);
//                break;
//            case R.id.fragment_personal_center_msg_rl://消息中心RL TODO
//                Intent msgcenter = new Intent(getActivity(), MessageCenterActivity.class);
//                startActivity(msgcenter);
//                break;
//            case R.id.fragment_personal_center_update_pwd_rl://修改密码RL TODO
//                break;
//            case R.id.fragment_personal_center_info_right_iv://个人中心IV
//                Intent information1 = new Intent(getActivity(), MyInformationActivity.class);
//                startActivity(information1);
//                break;
//            case R.id.fragment_personal_center_msg_right_iv://消息中心IV
//                Intent msgcenter1 = new Intent(getActivity(), MessageCenterActivity.class);
//                startActivity(msgcenter1);
//                break;
//            case R.id.fragment_personal_center_update_pwd_right_iv://修改密码IV
//                break;
//            default:
//                break;
//        }
//    }
//}
