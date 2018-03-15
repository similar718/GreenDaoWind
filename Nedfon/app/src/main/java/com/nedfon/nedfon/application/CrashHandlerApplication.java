package com.nedfon.nedfon.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nedfon.nedfon.utils.CrashHandler;

public class CrashHandlerApplication extends Application {
	
	private static CrashHandlerApplication mInstance;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
	    super.onCreate();
	    mInstance = this;
	    //Set exception handling procedures for the application here, and then our program 
	    //can catch the exception that is not handled
	    CrashHandler crashHandler = CrashHandler.getInstance();
	    crashHandler.init(this);

//		// 初始化ACRA
//		ACRA.init(this);
	}
	public static CrashHandlerApplication getInstance() {
	    return mInstance;
	}
}
