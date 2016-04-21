package com.cpic.carmarket.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.activity.ChatActivity;
import com.cpic.carmarket.bean.EaseUser;
import com.cpic.carmarket.bean.EaseUserInfo;
import com.cpic.carmarket.utils.UrlUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyApplication extends Application{
	/**
	 * 日志的开关，false：不打印Log；true：打印Log
	 */
	public static final boolean isDebug = false;
	/**
	 * 全局上下文
	 */
	private Context mContext;
	private static MyApplication instance;
	public final String PREF_USERNAME = "username";
	
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	/**
	 * 屏幕的宽度
	 */
	public static int mDisplayWitdh;
	
	/**
	 * 屏幕的高度
	 */
	public static int mDisplayHeight;
	
	private ArrayList<EaseUserInfo> datas;
	@Override
	public void onCreate() {
		super.onCreate();
	    instance = this;
		mContext = getApplicationContext();
		hxSDKHelper.onInit(mContext);
		EMChat.getInstance().init(mContext);
		/**
		 * debugMode == true 时为打开，sdk 会在log里输入调试信息
		 * @param debugMode
		 * 在做代码混淆的时候需要设置成false
		 */
		EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		//设置notification点击listener
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
		 
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(mContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if(chatType == ChatType.Chat){ //单聊信息
					String user = message.getFrom();
					loadInfo(user,intent);
				}
				return intent;
			}

			private void loadInfo(String user,final Intent intent) {
				HttpUtils post = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("user", user);
				String url = UrlUtils.postUrl+UrlUtils.path_getUserImg;
				post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						EaseUser user = JSONObject.parseObject(arg0.result, EaseUser.class);
						int code = user.getCode();
						if (code == 1) {
							datas = user.getData();
							intent.putExtra("userId", datas.get(0).getEase_user());
							intent.putExtra("name", datas.get(0).getUser_name());
							intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
						}else{
						}
					}
				});
			}
		});
		
		
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
		 
		if (processAppName == null ||!processAppName.equalsIgnoreCase("com.cpic.carmarket")) {
		    Log.e("oye", "enter the service process!");
		    //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
		 
		    // 则此application::onCreate 是被service 调用的，直接返回
		    return;
		}
		
		
	}
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	public static MyApplication getInstance() {
		return instance;
	}
	
	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final boolean isGCM,final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(isGCM,emCallBack);
	}
}
