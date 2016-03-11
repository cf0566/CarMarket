package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.pingplusplus.android.PaymentActivity;

public class AgreePayCashActivity extends BaseActivity {

	private EditText etMoney;
	private RadioGroup rgroup;
	private Button btnAgree;
	private ImageView ivBack;
	private String payType = "1";
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private String data;
	
	private Intent intent;
	
	private final static int RESULT_PAY_CODE = 0;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_agree_pay_cash);
	}

	@Override
	protected void initView() {
		etMoney = (EditText) findViewById(R.id.activity_agree_pay_cash_et_money);
		btnAgree = (Button) findViewById(R.id.activity_agree_pay_cash_btn_ensure);
		ivBack = (ImageView) findViewById(R.id.activity_agree_pay_iv_back);
		rgroup = (RadioGroup) findViewById(R.id.activity_agree_pay_cash_rgroup);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
	}

	@Override
	protected void registerListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		rgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.activity_agree_pay_cash_rbtn_zhifubao:
					payType = "1";
					break;
				case R.id.activity_agree_pay_cash_rbtn_weixin:
					payType = "2";
					break;

				default:
					break;
				}
			}
		});
		
		btnAgree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etMoney.getText().toString())) {
					showShortToast("请您输入金额");
					return;
				}
				getCharge();
			}
		});
	}
	private void getCharge() {
		sp = PreferenceManager.getDefaultSharedPreferences(AgreePayCashActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("pay_id", payType);
		params.addBodyParameter("margin", etMoney.getText().toString());
		String url = UrlUtils.postUrl+UrlUtils.path_marginSubmit;
		post.send(HttpMethod.POST, url ,params, new RequestCallBack<String>() {

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
				showLongToast("支付失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					data = obj.getString("data");
					intent = new Intent(AgreePayCashActivity.this, PaymentActivity.class);
					intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
					startActivityForResult(intent, RESULT_PAY_CODE);
					
				}else{
					showLongToast("支付失败");
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 if (requestCode == RESULT_PAY_CODE) {
		        if (resultCode == RESULT_OK) {
		            String result = data.getExtras().getString("pay_result");
		            /* 处理返回值
		             * "success" - 支付成功
		             * "fail"    - 支付失败
		             * "cancel"  - 取消支付
		             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
		             */
//		            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//		            String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
		            if ("success".equals(result)) {
						finish();
						showShortToast("支付成功");
					}else if ("fail".equals(result)) {
						showShortToast("支付失败");
					}else if ("cancel".equals(result)) {
						showShortToast("支付取消");
					}else if ("invalid".equals(result)) {
						showShortToast("支付插件未安装");
					}
		        }
		    }
	}

	@Override
	protected void initData() {

	}
}
