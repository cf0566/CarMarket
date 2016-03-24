package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.AnswerDetails;
import com.cpic.carmarket.bean.MessageDetails;
import com.cpic.carmarket.bean.MessageDetailsData;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MessageDetailsActivity extends BaseActivity {

	private MessageDetails details;
	private MessageDetailsData data;
	private TextView tvContent, tvCar, tvOrderTime, tvCheck;
	private ImageView ivQuestion1, ivQuestion2, ivBack;
	private Button btnOnline;
	private LinearLayout ll;

	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;

	private BitmapUtils utils;
	private Dialog dialog;

	private String question_id;
	private BitmapDisplayConfig config;

	private Intent intent;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		question_id = getIntent().getStringExtra("question_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_message_details);
	}

	@Override
	protected void initView() {
		tvContent = (TextView) findViewById(R.id.activity_message_tv_content);
		tvCar = (TextView) findViewById(R.id.activity_message_tv_carname);
		tvOrderTime = (TextView) findViewById(R.id.activity_message_tv_ordertime);
		tvCheck = (TextView) findViewById(R.id.activity_message_tv_check);
		ivQuestion1 = (ImageView) findViewById(R.id.activity_message_iv_question01);
		ivQuestion2 = (ImageView) findViewById(R.id.activity_message_iv_question02);
		btnOnline = (Button) findViewById(R.id.activity_message_btn_onlinetalk);
		ivBack = (ImageView) findViewById(R.id.activity_message_details_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		ll = (LinearLayout) findViewById(R.id.activity_message_ll);
	}

	@Override
	protected void registerListener() {
		btnOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(MessageDetailsActivity.this,
						AnswerOnlineResponseActivity.class);
				intent.putExtra("question_id", question_id);
				intent.putExtra("pre_id", 0 + "");
				startActivity(intent);
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
	}

	@Override
	protected void initData() {
		loadDatas();
	}

	private void loadDatas() {
		sp = PreferenceManager
				.getDefaultSharedPreferences(MessageDetailsActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("question_id", question_id);
		post.send(HttpMethod.POST, UrlUtils.postUrl
				+ UrlUtils.path_messageDetail, params,
				new RequestCallBack<String>() {

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
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (dialog != null) {
							dialog.dismiss();
						}
						details = JSONObject.parseObject(arg0.result,
								MessageDetails.class);
						int code = details.getCode();
						if (code == 1) {
							data = details.getData();
							tvContent.setText(data.getContent());
							tvCar.setText(data.getCar_name());
							tvOrderTime.setText("预约时间：" + data.getOrder_time());
							tvCheck.setText("维修项目:" + data.getChecked());
							if (data.getImg().size() == 1) {
								ivQuestion1.setVisibility(View.VISIBLE);
								loadQuestion1(data.getQuestion_img().get(0));
							} else if (data.getImg().size() >= 2) {
								ivQuestion1.setVisibility(View.VISIBLE);
								ivQuestion2.setVisibility(View.VISIBLE);
								loadQuestion1(data.getQuestion_img().get(0));
								loadQuestion2(data.getQuestion_img().get(1));
							} else {
								ll.setVisibility(View.GONE);
							}
							ivListener();
						} else {
							showShortToast("暂无数据");
						}
					}

					private void ivListener() {
						if (data.getImg().size() == 1) {
							ivQuestion1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									intent = new Intent(MessageDetailsActivity.this,
											PicturePreviewActivity.class);
									intent.putExtra("url", data.getQuestion_img().get(0));
									startActivity(intent);
								}
							});
						} else if (data.getImg().size() >= 2) {
							ivQuestion1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									intent = new Intent(MessageDetailsActivity.this,
											PicturePreviewActivity.class);
									intent.putExtra("url", data.getQuestion_img().get(0));
									startActivity(intent);
								}
							});
							ivQuestion2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									intent = new Intent(MessageDetailsActivity.this,PicturePreviewActivity.class);
									intent.putExtra("url", data.getQuestion_img().get(1));
									startActivity(intent);
								}
							});
						}
					}
				});
	}

	/**
	 * 下载问题1
	 * 
	 * @param img_url
	 */
	private void loadQuestion1(String img_url) {
		config = new BitmapDisplayConfig();
		utils = new BitmapUtils(MessageDetailsActivity.this);
		config.setLoadingDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		utils.display(ivQuestion1, img_url, config);
	}

	/**
	 * 下载问题2
	 * 
	 * @param img_url
	 */
	private void loadQuestion2(String img_url) {
		config = new BitmapDisplayConfig();
		utils = new BitmapUtils(MessageDetailsActivity.this);
		config.setLoadingDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		utils.display(ivQuestion2, img_url, config);
	}

}
