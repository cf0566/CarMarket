package com.cpic.carmarket.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.broadcast.SMSBroadcastReceiver;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ForgetPsdActivity extends BaseActivity {

	private EditText etMobile, etCheck;
	private Button btnNext;
	private ImageView ivBack;
	private String mobile, checknum;
	private HttpUtils post;
	private TextView tvGetnum;
	private RequestParams params;
	private Intent intent;
	private Dialog dialog;
	int count = 30;
	private TimeCount time;

	private SMSBroadcastReceiver mSMSBroadcastReceiver;

	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

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
		time = new TimeCount(60000, 1000);
	}

	@Override
	protected void registerListener() {

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
				String str = etMobile.getText().toString();
				if ("".equals(etMobile.getText().toString())) {
					showLongToast("手机号码不得为空");
					return;
				}
				if (str.length() != 11) {
					showLongToast("手机号码格式不正确");
					return;
				}
				getNum();
			}
		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(etMobile.getText().toString())
						||"".equals(etCheck.getText().toString())) {
					showLongToast("手机号码或验证码不得为空");
				} else {
					intent = new Intent(ForgetPsdActivity.this,SetPsdActivity.class);
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
		params.addBodyParameter("mobile", etMobile.getText().toString());
		post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_getCode,
				params, new RequestCallBack<String>() {

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
						int code = obj.getIntValue("code");
						if (code == 1) {
							time.start();
							showShortToast("获取成功，请注意查收信息");
						} else {
							showShortToast("获取失败，请检查手机号码是否输入错误");
						}
					}
				});

	}

	@Override
	protected void initData() {
		   mSMSBroadcastReceiver = new SMSBroadcastReceiver();

	        //实例化过滤器并设置要过滤的广播
	        IntentFilter intentFilter = new IntentFilter(ACTION);
	        intentFilter.setPriority(Integer.MAX_VALUE);
	        //注册广播
	        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

	        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
	            @Override
	            public void onReceived(String message) {
	            	etCheck.setText(message);
	            }
	        });
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			tvGetnum.setText("获取验证码");
			tvGetnum.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			tvGetnum.setClickable(false);// 防止重复点击
			tvGetnum.setText(millisUntilFinished / 1000 + "s" + "重新验证");
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		 //注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
	}

}
