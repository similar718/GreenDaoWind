//package com.nedfon.nedfon.ui;
//
//import android.content.Intent;
//import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.adapter.MessageCenterAdapter;
//import com.nedfon.nedfon.bean.MessageCenterBean_test;
//import com.nedfon.nedfon.utils.StatusBarCompat;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MessageCenterActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//    private ListView mListView;
//
//
//    private MessageCenterAdapter mAdapter;
//    private List<MessageCenterBean_test> mList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(MessageCenterActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_message_center);
//
//        initData();
//        initView();
//    }
//
//    private void initData() {
//        mList = new ArrayList<>();
//        mList.clear();
//        for (int i=0;i<10;i++){
//            MessageCenterBean_test bean = new MessageCenterBean_test();
//            bean.title = "绿岛风住宅全热新风系统解决方案";
//            bean.time = "2018-01-2"+i;
//            bean.content = "1.换风、除臭、除尘功能。取自高空的新鲜空气，经过滤、除尘、灭菌、加热/降温、加湿/除湿等处理过程，以较低风风速，从房间一侧送风口不间断送出，同个合理的气流组织，新鲜空气经过人的活动区域，带走人员活动产生的扬尘，气味等，从排风口派出，达到净化室内空气的目的。 \n \n2.湿度调节功能。有效调节室内空气湿度，是居室时刻保持干爽、舒适的状态，对西安冬季的干燥空气有加湿的作用。\n\n3.节能。不用开窗既可获得新鲜空气，减少室内热损失，节省能源。\n\n4.过滤有害物质。祛除室内装饰造成的可能长时间存在的有害气体。";
//            //1.换风、除臭、除尘功能。取自高空的新鲜空气，经过滤、除尘、灭菌、加热/降温、加湿/除湿等处理过程，以较低风风速，从房间一侧送风口不间断送出，同个合理的气流组织，新鲜空气经过人的活动区域，带走人员活动产生的扬尘，气味等，从排风口派出，达到净化室内空气的目的。 \n \n2.湿度调节功能。有效调节室内空气湿度，是居室时刻保持干爽、舒适的状态，对西安冬季的干燥空气有加湿的作用。\n\n3.节能。不用开窗既可获得新鲜空气，减少室内热损失，节省能源。\n\n4.过滤有害物质。祛除室内装饰造成的可能长时间存在的有害气体。
//            mList.add(bean);
//        }
//    }
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_message_center_back_tv);
//        mListView = this.findViewById(R.id.activity_message_center_lv);
//
//        mAdapter = new MessageCenterAdapter(this);
//        mAdapter.setData(mList);
//        mListView.setAdapter(mAdapter);
//
//        mBackTv.setOnClickListener(this);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemDeviceOptionClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                MessageCenterBean_test data = mList.get(position);
//                Intent intent = new Intent(MessageCenterActivity.this,MessageCenterDetailActivity.class);
//                intent.putExtra("title",data.title);
//                intent.putExtra("time",data.time);
//                intent.putExtra("content",data.content);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_message_center_back_tv:
//                this.finish();
//                break;
//            default:
//                break;
//        }
//    }
//}
