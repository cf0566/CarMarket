package com.cpic.carmarket.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cpic.carmarket.activity.LoginActivity;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;

public class MyLoginService extends Service {

	private MyConnectionListener connectionListener = null;
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	public class MyBinder extends Binder {

		public void callTest() {
			test();
		}

	}

	public void test() {
		connectionListener = new MyConnectionListener();
		EMChatManager.getInstance().addConnectionListener(connectionListener);
	}
	
	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {
		}

		@Override
		public void onDisconnected(final int error) {
			new Runnable() {
				public void run() {
					if (error == EMError.USER_REMOVED) {
						// 显示帐号已经被移除
					} else if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆
						EMChatManager.getInstance().logout();
						Intent intent = new Intent(getBaseContext(), LoginActivity.class);
						startActivity(intent);
						android.os.Process.killProcess(android.os.Process.myPid());  
					} else {
						// "连接不到聊天服务器"
					}
				}
			}.run();
		}
	}
}
