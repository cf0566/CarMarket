package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.FormData2;
import com.cpic.carmarket.bean.FormDataInfo2;
import com.cpic.carmarket.fragment.FormWaitServiceFragment.WaitAdapter;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class FormWaitServiceActivity extends BaseActivity {

	private TextView tvCar, tvUser, tvPhone, tvAddress, tvType, tvPrice,
			tvTime, tvPay, tvNum, tvCreat, tvStart;
	
	private ImageView ivBack;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	private FormDataInfo2 data;
	
	private String order_id;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		order_id = getIntent().getStringExtra("order_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_form_wait_service);
	}
	
	@Override
	protected void initView() {
		tvCar = (TextView) findViewById(R.id.activity_form_wait_service_tv_car);
		tvUser = (TextView) findViewById(R.id.activity_form_wait_service_tv_user);
		tvPhone = (TextView) findViewById(R.id.activity_form_wait_service_tv_phone);
		tvAddress = (TextView) findViewById(R.id.activity_form_wait_service_tv_address);
		tvType = (TextView) findViewById(R.id.activity_form_wait_service_tv_type);
		tvPrice = (TextView) findViewById(R.id.activity_form_wait_service_tv_price);
		tvTime = (TextView) findViewById(R.id.activity_form_wait_service_tv_time);
		tvPay = (TextView) findViewById(R.id.activity_form_wait_service_tv_pay);
		tvNum = (TextView) findViewById(R.id.activity_form_wait_service_tv_num);
		tvCreat = (TextView) findViewById(R.id.activity_form_wait_service_tv_creat_time);
		tvStart = (TextView) findViewById(R.id.activity_form_wait_service_tv_start_service);
		
		ivBack = (ImageView) findViewById(R.id.activity_form_wait_service_iv_back);
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
		
		tvStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService();
			}
		});
	}
	private void startService() {
		sp = PreferenceManager.getDefaultSharedPreferences(FormWaitServiceActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", order_id);
		params.addBodyParameter("status", UrlUtils.STATUS_SERVICE);
		String url = UrlUtils.postUrl+UrlUtils.path_orderAction;
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(FormWaitServiceActivity.this, "请检查网络连接，提交失败", 0).show();
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					Toast.makeText(FormWaitServiceActivity.this, "开始服务", 0).show();
					finish();
				}else{
					Toast.makeText(FormWaitServiceActivity.this, "提交失败"+code+"", 0).show();
				}
			}
		});
	}
	
	@Override
	protected void initData() {
		sp = PreferenceManager.getDefaultSharedPreferences(FormWaitServiceActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", order_id);
		String url = UrlUtils.postUrl+UrlUtils.path_orderDetail;
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				showShortToast(UrlUtils.loading_failure);
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				FormData2 obj = JSONObject.parseObject(arg0.result, FormData2.class);
				int code= obj.getCode();
				if (code == 1) {
					data = obj.getData();
					loadDatas();
				}else{
					showShortToast("获取数据失败"+code);
				}
			}

		});
	}
	private void loadDatas() {
		tvCar.setText(data.getCar_name());
		tvUser.setText("收件人："+data.getUser_name());
		tvPhone.setText(data.getMobile());
		tvAddress.setText(data.getOrder_address());
		tvType.setText(data.getDim_name());
		tvPrice.setText(data.getOrder_amount());
		tvTime.setText(data.getOrder_date());
		if ("1".equals(data.getPay_id())) {
			tvPay.setText("支付宝支付");
		}else{
			tvPay.setText("微信支付");
		}
		tvNum.setText(data.getOrder_sn());
		tvCreat .setText(data.getCreate_date());
	}

}
