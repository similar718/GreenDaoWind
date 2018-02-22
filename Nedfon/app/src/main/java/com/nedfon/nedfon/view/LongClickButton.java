package com.nedfon.nedfon.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import java.lang.ref.WeakReference;

/**
     * 长按连续响应的button
     * created by admin on 15-6-1.
     */
    public class LongClickButton extends android.support.v7.widget.AppCompatImageView {
    /**
     * 长按连续响应的监听，长按时将会多次调用该接口中的方法直到长按结束
     */
    private longclickrepeatlistener repeatlistener;
    /**
     * 间隔时间（ms）
     */
    private long intervaltime;
    private myhandler handler;

    public LongClickButton(Context context) {
        super(context);
        init();
    }

    public LongClickButton(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongClickButton(Context context, android.util.AttributeSet attrs, int defstyleattr) {
        super(context, attrs, defstyleattr);
        init();
    }

    /**
     * 初始化监听
     */
    private void init() {
        handler = new myhandler(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread(new longclickthread()).start();
                return true;
            }
        });
    }

    /**
     * 长按时，该线程将会启动
     */
    private class longclickthread implements Runnable {
        private int num;
        @Override
        public void run() {
            while (LongClickButton.this.isPressed()) {
                num++;
                if (num % 5 == 0) {
                    handler.sendEmptyMessage(1);
                }
                SystemClock.sleep(intervaltime / 5);
            }
        }
    }

    /**
     * 通过handler，使监听的事件响应在主线程中进行
     */
    private static class myhandler extends Handler {
        private WeakReference<LongClickButton> ref;
        myhandler(LongClickButton button) {
            ref = new WeakReference<>(button);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LongClickButton button = ref.get();
            if (button != null && button.repeatlistener != null) {
                button.repeatlistener.repeataction();
            }
        }
    }

    /**
     * 设置长按连续响应的监听和间隔时间，长按时将会多次调用该接口中的方法直到长按结束
     *
     * @param listener     监听
     * @param intervaltime 间隔时间（ms）
     */
    public void setlongclickrepeatlistener(longclickrepeatlistener listener, long intervaltime) {
        this.repeatlistener = listener;
        this.intervaltime = intervaltime;
    }

    /**
     * 设置长按连续响应的监听（使用默认间隔时间100ms），长按时将会多次调用该接口中的方法直到长按结束
     *
     * @param listener 监听
     */
    public void setlongclickrepeatlistener(longclickrepeatlistener listener) {
        setlongclickrepeatlistener(listener, 100);
    }

    public interface longclickrepeatlistener {
        void repeataction();
    }
}
