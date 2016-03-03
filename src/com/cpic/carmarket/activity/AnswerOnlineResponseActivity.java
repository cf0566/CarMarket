package com.cpic.carmarket.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class AnswerOnlineResponseActivity extends BaseActivity{

	private EditText etContent;
	private Button btnSubmit;
	private ImageView ivBack;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private String question_id,pre_id;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		question_id = getIntent().getStringExtra("question_id");
		pre_id = getIntent().getStringExtra("pre_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_answer_online_response);
	}

	@Override
	protected void initView() {
		etContent = (EditText) findViewById(R.id.activity_answer_online_response_et_content);
		ivBack = (ImageView) findViewById(R.id.activity_answer_online_response_iv_back);
		btnSubmit = (Button) findViewById(R.id.activity_answer_online_response_btn_submit);
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
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etContent.getText().toString())||etContent.getText().toString().isEmpty()) {
					showShortToast("留言不得为空");
				}else{
					post = new HttpUtils();
					params = new RequestParams();
					sp = PreferenceManager.getDefaultSharedPreferences(AnswerOnlineResponseActivity.this);
					String token = sp.getString("token", "");
					params.addBodyParameter("token", token);
					params.addBodyParameter("pre_id", pre_id);
					params.addBodyParameter("question_id", question_id);
					params.addBodyParameter("content", etContent.getText().toString());
					post.send(HttpMethod.POST, UrlUtils.postUrl+UrlUtils.path_answer,params ,new RequestCallBack<String>() {
						
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
							showShortToast("提交失败，请检查网络状况");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							if (dialog != null) {
								dialog.dismiss();
							}
							JSONObject obj = JSONObject.parseObject(arg0.result);
							int code = obj.getIntValue("code");
							if (code == 1) {
								showShortToast("提交成功");
								finish();
							}else{
								showShortToast("提交失败，请重新提交");
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
