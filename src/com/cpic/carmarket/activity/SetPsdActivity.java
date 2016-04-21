package com.cpic.carmarket.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.utils.MD5Utils;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SetPsdActivity extends BaseActivity{

	private EditText etFirst,etSecond;
	private String first,second,mobile,checknum;
	private Button btnLogin;
	private ImageView ivBack;
	private HttpUtils post;
	private RequestParams params;
	private Intent intent;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		mobile = getIntent().getStringExtra("mobile");
		checknum = getIntent().getStringExtra("checknum");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_set_psd);
	}

	@Override
	protected void initView() {
		etFirst = (EditText) findViewById(R.id.activity_setpsd_et_first);
		etSecond = (EditText) findViewById(R.id.activity_setpsd_et_two);
		btnLogin = (Button) findViewById(R.id.activity_setpsd_btn_login);
		ivBack = (ImageView) findViewById(R.id.activity_setpsd_iv_back);
	}

	@Override
	protected void registerListener() {
		/**
		 * 监听输入字体变化
		 */
		etFirst.addTextChangedListener(new TextWatcher() {

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
				first = s.toString();
			}
		});
		etSecond.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				second = s.toString();
			}
		});
		/**
		 * 返回键监听
		 */
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (first==null||second==null) {
					showShortToast("密码不得为空");
				}else if(!first.equals(second)) {
					showShortToast("两次输入的密码不一致，请重新输入");
				}else{
					params = new RequestParams();
					post = new HttpUtils();
					params.addBodyParameter("mobile", mobile);
					params.addBodyParameter("pwd", new MD5Utils().getMD5(second));
					params.addBodyParameter("code", checknum);
					post.send(HttpMethod.POST, UrlUtils.postUrl+UrlUtils.path_updatePwd, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							showShortToast("获取失败，请检查网络连接");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							JSONObject obj = JSONObject.parseObject(arg0.result);
							int code = obj.getIntValue("code");
							if (code == 1) {
								showShortToast("修改密码成功");
								SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SetPsdActivity.this);
								final SharedPreferences.Editor editor=sp.edit();
								editor.putString("acount", mobile);
								editor.putString("psd", second);
								editor.commit();
								intent = new Intent(SetPsdActivity.this, LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
								startActivity(intent);
								finish();
							}else{
								showShortToast("修改密码失败，请确认该用户是否存在");
							}
						}
					});
				}
			}
		});
	}

	@Override
	protected void initData() {
	}
}
