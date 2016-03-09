package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.LoginResult;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
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
	private TextView tvRequest, tvForget;
	private String acount, psd;
	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;
	private Intent intent;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		EMChat.getInstance().init(this);
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
					showShortToast("请同意协议");
				}else{
					login();
				}
			}
		});
		tvRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		tvForget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(LoginActivity.this, ForgetPsdActivity.class);
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
		params.addBodyParameter("pwd", psd);
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
				LoginResult res = JSONObject.parseObject(arg0.result,LoginResult.class);
				int code = res.getCode();
				if (code == 0) {
					showLongToast("用户不存在或密码错误");
				}else if (code == 1) {
					showLongToast("登录成功");
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
					EMChatManager.getInstance().login(res.getData().getEase_user(), 
							res.getData().getEase_pwd(), new EMCallBack() {
								
								@Override
								public void onSuccess() {
									intent=new Intent(LoginActivity.this,MainActivity.class);
									startActivity(intent);
									finish();
								}
								
								@Override
								public void onProgress(int arg0, String arg1) {
									
								}
								
								@Override
								public void onError(int arg0, String arg1) {
									showShortToast("环信接入失败");
								}
							});
					
				}
			}
		});
	}
	@Override
	protected void initData() {
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
