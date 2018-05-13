package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.adapter.MessageCenterAdapter;
import com.nedfon.nedfon.bean.GetMsgInfoAllBean;
import com.nedfon.nedfon.bean.MessageCenterBean_test;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageCenterOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private ListView mListView;


    private MessageCenterAdapter mAdapter;
    private List<MessageCenterBean_test> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText("消息中心");
        setImage(4);
        NAME = MessageCenterOkActivity.class.getSimpleName();
//        initData();
        doGetMsgInfoGet(CommonUtils.token);
        initView();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.clear();
        if (mBean == null){
            return;
        }
        for (int i=0;i<mBean.data.size();i++){
            MessageCenterBean_test bean = new MessageCenterBean_test();
            bean.title = mBean.data.get(i).attachTitle;
            bean.time = mBean.data.get(i).updateloadDate;
            bean.content = mBean.data.get(i).descNo;
            //1.换风、除臭、除尘功能。取自高空的新鲜空气，经过滤、除尘、灭菌、加热/降温、加湿/除湿等处理过程，以较低风风速，从房间一侧送风口不间断送出，同个合理的气流组织，新鲜空气经过人的活动区域，带走人员活动产生的扬尘，气味等，从排风口派出，达到净化室内空气的目的。 \n \n2.湿度调节功能。有效调节室内空气湿度，是居室时刻保持干爽、舒适的状态，对西安冬季的干燥空气有加湿的作用。\n\n3.节能。不用开窗既可获得新鲜空气，减少室内热损失，节省能源。\n\n4.过滤有害物质。祛除室内装饰造成的可能长时间存在的有害气体。
            mList.add(bean);
//            bean.title = "绿岛风住宅全热新风系统解决方案";
//            bean.time = "2018-01-2"+i;
//            bean.content = "1.换风、除臭、除尘功能。取自高空的新鲜空气，经过滤、除尘、灭菌、加热/降温、加湿/除湿等处理过程，以较低风风速，从房间一侧送风口不间断送出，同个合理的气流组织，新鲜空气经过人的活动区域，带走人员活动产生的扬尘，气味等，从排风口派出，达到净化室内空气的目的。 \n \n2.湿度调节功能。有效调节室内空气湿度，是居室时刻保持干爽、舒适的状态，对西安冬季的干燥空气有加湿的作用。\n\n3.节能。不用开窗既可获得新鲜空气，减少室内热损失，节省能源。\n\n4.过滤有害物质。祛除室内装饰造成的可能长时间存在的有害气体。";
//            //1.换风、除臭、除尘功能。取自高空的新鲜空气，经过滤、除尘、灭菌、加热/降温、加湿/除湿等处理过程，以较低风风速，从房间一侧送风口不间断送出，同个合理的气流组织，新鲜空气经过人的活动区域，带走人员活动产生的扬尘，气味等，从排风口派出，达到净化室内空气的目的。 \n \n2.湿度调节功能。有效调节室内空气湿度，是居室时刻保持干爽、舒适的状态，对西安冬季的干燥空气有加湿的作用。\n\n3.节能。不用开窗既可获得新鲜空气，减少室内热损失，节省能源。\n\n4.过滤有害物质。祛除室内装饰造成的可能长时间存在的有害气体。
//            mList.add(bean);
        }
        mAdapter.setData(mList);
    }

    private void initView() {
        mListView = this.findViewById(R.id.activity_message_center_lv);

        mAdapter = new MessageCenterAdapter(this);
        mAdapter.setData(mList);
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageCenterBean_test data = mList.get(position);
                Intent intent = new Intent(MessageCenterOkActivity.this,MessageCenterDetailOkActivity.class);
                intent.putExtra("title",data.title);
                intent.putExtra("time",data.time);
                intent.putExtra("content",data.content);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_center_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent personal = new Intent(MessageCenterOkActivity.this,PersonalCenterOkActivity.class);
        startActivity(personal);
        this.finish();
    }


    private static OkHttpClient okhttpclient = new OkHttpClient();
    /**
     *   获取系统信息
     */
    private  void doGetMsgInfoGet(String token){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //http://localhost:9090/mobileapi/getMsgInfo?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
        // eyJleHAiOjE1MTk5NTg5MjksInVzZXJuYW1lIjoiMTM1MTI3NzQ3NjAifQ.DP22dBsyMqnPoQyMw0KV51WN_OImBxI8rfphBS-eWfs
        Request request = builder.url(CommonUtils.localhost+"mobileapi/getMsgInfo?token="+token).get().build();
        executeGetMsgInfoRequest(request);
    }
    private GetMsgInfoAllBean mBean = null;

    private void executeGetMsgInfoRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res11 = "+res );
                if (res.contains(CommonUtils.mSuccess)){
                    mBean = new Gson().fromJson(res,GetMsgInfoAllBean.class);
                    mHandler.sendEmptyMessage(3);
                } else if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(1);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtils.show(MessageCenterOkActivity.this,"更改个人信息失败！");
                    break;
                case 2 :
                    ToastUtils.show(MessageCenterOkActivity.this,"其他错误");
                    break;
                case 3 :
//                    ToastUtils.show(MessageCenterOkActivity.this,"更改个人信息成功！");
//                    setBackOnClick();
                    initData();
                    break;
            }
        }
    };
}
