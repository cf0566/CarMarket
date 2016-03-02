package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ForgetPsdActivity extends BaseActivity{

	private EditText etMobile,etCheck;
	private Button btnNext;
	private ImageView ivBack;
	private String mobile,checknum;
	private HttpUtils post;
	private TextView tvGetnum;
	private RequestParams params;
	private Intent intent;
	private Dialog dialog;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_forget_psd);
	}

	@Override
	protected void initView() {
		etMobile = (EditText) findViewById(R.id.activity_forget_et_mobile);
		etCheck = (EditText) findViewById(R.id.activity_forget_et_checknum);
		tvGetnum = (TextView) findViewById(R.id.activity_forget_btn_getnum);
		btnNext = (Button) findViewById(R.id.activity_forget_btn_next);
		ivBack = (ImageView) findViewById(R.id.activity_forget_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
	}

	@Override
	protected void registerListener() {
		/**
		 * 监听输入字体变化
		 */
		etMobile.addTextChangedListener(new TextWatcher() {

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
				mobile = s.toString();
			}
		});
		etCheck.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				checknum = s.toString();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		/**
		 * 获取验证码
		 */
		tvGetnum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getNum();
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checknum.isEmpty()||mobile.isEmpty()) {
					showLongToast("手机号码或验证码不得为空");
				}else{
					intent = new Intent(ForgetPsdActivity.this, SetPsdActivity.class);
					intent.putExtra("checknum", checknum);
					intent.putExtra("mobile", mobile);
					startActivity(intent);
				}
			}
		});
	}

	private void getNum() {
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("mobile", mobile);
		post.send(HttpMethod.POST, UrlUtils.postUrl+UrlUtils.path_getCode, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (dialog != null) {
					dialog.dismiss();
				}
				showShortToast("获取失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getInteger("code");
				if (code == 1) {
					showShortToast("获取成功，请注意查收信息");
				}else{
					showShortToast("获取失败，请检查手机号码是否输入错误");
				}
			}
		});
		
	}
	@Override
	protected void initData() {
		
	}

}
