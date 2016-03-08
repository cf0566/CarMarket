package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.FormData4;
import com.cpic.carmarket.bean.FormDataInfo4;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class FormAfterQueryActivity extends BaseActivity {

	private TextView tvCar, tvUser, tvPhone, tvAddress, tvType, tvPrice,
			tvTime, tvPay, tvNum, tvCreat,tvUserName,tvScoreTime,tvQuery,tvUserPhone;
	
	private RatingBar rBar;
	private ImageView ivBack;
	private CircleImageView ivUserIcon;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	private FormDataInfo4 data;
	
	private String order_id;

	private BitmapDisplayConfig config;

	private BitmapUtils utils;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		order_id = getIntent().getStringExtra("order_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_form_after_query);
	}
	
	@Override
	protected void initView() {
		tvCar = (TextView) findViewById(R.id.activity_form_after_query_tv_car);
		tvUser = (TextView) findViewById(R.id.activity_form_after_query_tv_user);
		tvPhone = (TextView) findViewById(R.id.activity_form_after_query_tv_phone);
		tvAddress = (TextView) findViewById(R.id.activity_form_after_query_tv_address);
		tvType = (TextView) findViewById(R.id.activity_form_after_query_tv_type);
		tvPrice = (TextView) findViewById(R.id.activity_form_after_query_tv_price);
		tvTime = (TextView) findViewById(R.id.activity_form_after_query_tv_time);
		tvPay = (TextView) findViewById(R.id.activity_form_after_query_tv_pay);
		tvNum = (TextView) findViewById(R.id.activity_form_after_query_tv_num);
		tvCreat = (TextView) findViewById(R.id.activity_form_after_query_tv_creat_time);
		tvUserName = (TextView) findViewById(R.id.activity_form_after_query_tv_user_name);
		tvScoreTime = (TextView) findViewById(R.id.activity_form_after_query_tv_score);
		tvQuery = (TextView) findViewById(R.id.activity_form_after_query_tv_user_query);
		tvUserPhone = (TextView) findViewById(R.id.activity_form_after_query_tv_user_phone);
		
		rBar = (RatingBar) findViewById(R.id.activity_form_after_query_rbar_score);
		ivUserIcon = (CircleImageView) findViewById(R.id.activity_form_after_query_iv_icon);
		ivBack = (ImageView) findViewById(R.id.activity_form_after_query_iv_back);
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
	}
	
	@Override
	protected void initData() {
		sp = PreferenceManager.getDefaultSharedPreferences(FormAfterQueryActivity.this);
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
				FormData4 obj = JSONObject.parseObject(arg0.result, FormData4.class);
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
		tvCreat.setText(data.getCreate_date());
		tvUserName.setText(data.getUser_name());
		tvScoreTime.setText(data.getEvaluation_date());
		tvQuery.setText(data.getEvaluation().getScore_content());
		tvUserPhone.setText(data.getMobile());
		double score = Double.parseDouble(data.getEvaluation().getScore());
		rBar.setRating((float)score);
		
		String url = data.getUser_img();
		loadUserIcon(url);
	}
	private void loadUserIcon(String ivUrl) {
		config = new BitmapDisplayConfig();
		 utils = new BitmapUtils(FormAfterQueryActivity.this);
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
		utils.display(ivUserIcon, ivUrl, config);
		
	}
}
