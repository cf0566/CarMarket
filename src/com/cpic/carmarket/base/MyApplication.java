package com.cpic.carmarket.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.params.HttpParams;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.activity.ChatActivity;
import com.cpic.carmarket.activity.MainActivity;
import com.cpic.carmarket.bean.EaseUser;
import com.cpic.carmarket.bean.EaseUserInfo;
import com.cpic.carmarket.utils.UrlUtils;
import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.util.NetUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
