package com.cpic.carmarket.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.base.MyApplication;
import com.cpic.carmarket.bean.LoginResult;
import com.cpic.carmarket.utils.MD5Utils;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends BaseActivity {
	private EditText etAcount, etPsd;
	private Button btnLogin;
	private CheckBox cBoxAgree;
	private TextView tvRequest, tvForget , tvText;
	private String acount, psd;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private Intent intent;
	private LoginResult res;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_login);
	}
	@Override
	protected void initView() {
		etAcount = (EditText) findViewById(R.id.activity_login_et_acount);
		etPsd = (EditText) findViewById(R.id.activity_login_et_psd);
		btnLogin = (Button) findViewById(R.id.activity_login_btn_login);
		cBoxAgree = (CheckBox) findViewById(R.id.activity_login_cbox_agree);
		tvRequest = (TextView) findViewById(R.id.activity_login_tv_request);
		tvForget = (TextView) findViewById(R.id.activity_login_tv_forget);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		tvText = (TextView) findViewById(R.id.activity_login_tv_text);
	}

	@Override
	protected void registerListener() {
		/**
		 * 监听输入字体变化
		 */
		etAcount.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				acount = s.toString();
			}

		});
		
		etPsd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				psd = s.toString();
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (acount == null || psd == null) {
					showShortToast("用户名或密码不得为空");
				}else if (!cBoxAgree.isChecked()) {
					showShortToast("请同意汽车后市场商家协议");
				}else{
					login();
				}
			}
		});
		tvRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this, RequestLoadingActivity.class);
				startActivity(intent);
			}
		});
		tvForget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this, ForgetPsdActivity.class);
				startActivity(intent);
			}
		});
		
		/**
		 * 协议监听
		 */
		tvText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this, WebAgreeActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
	private void login() {
		final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		final SharedPreferences.Editor editor=sharedPref.edit();
		editor.putString("acount", acount);
		editor.putString("psd", psd);
		editor.commit();
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", acount);
		params.addBodyParameter("pwd", new MD5Utils().getMD5(psd));
		post.send(HttpMethod.POST, UrlUtils.postUrl+UrlUtils.path_login, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				showShortToast("登录失败，请检查网络连接");
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				res = JSONObject.parseObject(arg0.result,LoginResult.class);
				int code = res.getCode();
				if (code == 0) {
					showLongToast("用户不存在或密码错误");
				}else if (code == 1) {
					final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
					SharedPreferences.Editor editor=sharedPref.edit();
					editor.putString("token", res.getData().getToken());
					editor.putString("on_time", res.getData().getOn_time());
					editor.putString("logo", res.getData().getLogo());
					editor.putString("store_img", res.getData().getStore_img());
					editor.putString("company_name", res.getData().getCompany_name());
					editor.putString("merchant_id", res.getData().getMerchant_id());
					editor.putString("tel", res.getData().getTel());
					editor.putString("user_id", res.getData().getUser_id());
					editor.putString("is_approve", res.getData().getIs_approve());
					editor.apply();
					// 进入主页面
					
					EMChat.getInstance().setAutoLogin(false);
					EMChatManager.getInstance().login(res.getData().getEase_user(), res.getData().getEase_pwd(), 
							new EMCallBack() {
								
								@Override
								public void onSuccess() {
									try {
										// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
										// ** manually load all local groups and
									    EMGroupManager.getInstance().loadAllGroups();
										EMChatManager.getInstance().loadAllConversations();
										// 处理好友和群组
										initializeContacts();
										Intent intent = new Intent(LoginActivity.this,MainActivity.class);
										startActivity(intent);
										finish();
									} catch (Exception e) {
										e.printStackTrace();
										// 取好友或者群聊失败，不让进入主页面
										runOnUiThread(new Runnable() {
											public void run() {
												dialog.dismiss();
												DemoHXSDKHelper.getInstance().logout(true,null);
												Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
											}
										});
										return;
									}
									boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
											MyApplication.currentUserNick.trim());
									if (!updatenick) {
									}
									
									if (!LoginActivity.this.isFinishing() && dialog.isShowing()) {
										dialog.dismiss();
									}
									
								}
								
								@Override
								public void onProgress(int arg0, String arg1) {
								}
								
								@Override
								public void onError(int arg0, String arg1) {
									Log.i("oye", "环信登录失败");
								}
							});
				}
			}
		});
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(R.string.Application_and_notify);
		newFriends.setNick(strChat);
		
		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);
		
		// 添加"Robot"
		User robotUser = new User();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);
		
		// 存入内存
		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	@Override
	protected void initData() {
		//使申请入驻待下划线显示
		tvRequest.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
	}
	@Override
	protected void onResume(){
		super.onResume();
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);
		String name=sharedPref.getString("acount", null);
		String password=sharedPref.getString("psd", null);
		if(name!=null)etAcount.setText(name);
		if(password!=null)etPsd.setText(password);
	}
}
