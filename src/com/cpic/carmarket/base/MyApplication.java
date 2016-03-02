package com.cpic.carmarket.base;

import com.easemob.chat.EMChat;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
	/**
	 * 日志的开关，false：不打印Log；true：打印Log
	 */
	public static final boolean isDebug = false;
	
	/**
	 * 全局上下文
	 */
	private Context mContext;
	
	/**
	 * 屏幕的宽度
	 */
	public static int mDisplayWitdh;
	
	/**
	 * 屏幕的高度
	 */
	public static int mDisplayHeight;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		
		initDisplay();
		EMChat.getInstance().init(mContext);
		 
		/**
		 * debugMode == true 时为打开，sdk 会在log里输入调试信息
		 * @param debugMode
		 * 在做代码混淆的时候需要设置成false
		 */
		EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
		
	}

	private void initDisplay() {
		
	}
	
}
