//package com.nedfon.nedfon.view;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.util.AttributeSet;
//import android.view.View;
//
//import java.lang.ref.WeakReference;
//
//public class LongClickButton2 extends android.support.v7.widget.AppCompatButton {
//    /**
//     * 间隔时间（ms）
//     */
//    private long intervaltime = 50;
//    private myhandler handler;
//
//    public LongClickButton2(Context context) {
//        super(context);
//        init();
//    }
//
//    public LongClickButton2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public LongClickButton2(Context context, AttributeSet attrs, int defstyleattr) {
//        super(context, attrs, defstyleattr);
//        init();
//    }
//    /**
//     * 初始化监听
//     */
//    private void init() {
//        handler = new myhandler(this);
//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                new Thread(new longclickthread()).start();
//                return true;
//            }
//        });
//    }
//    /**
//     * 长按时，该线程将会启动
//     */
//    private class longclickthread implements Runnable {
//        private int num;
//
//        @Override
//        public void run() {
//            while (LongClickButton2.this.isPressed()) {
//                num++;
//                if (num % 5 == 0) {
//                    handler.sendEmptyMessage(1);
//                }
//                SystemClock.sleep(intervaltime / 5);
//            }
//        }
//    }
//    /**
//     * 通过handler，使监听的事件响应在主线程中进行
//     */
//    private static class myhandler extends Handler {
//        private WeakReference<LongClickButton2> ref;
//
//        myhandler(LongClickButton2 button) {
//            ref = new WeakReference<>(button);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            LongClickButton2 button = ref.get();
//            if (button != null) {
//                //直接调用普通点击事件
//                button.performClick();
//            }
//        }
//    }
//    public void setintervaltime(long intervaltime) {
//        this.intervaltime = intervaltime;
//    }
//}