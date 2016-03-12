package com.cpic.carmarket.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RequestLoadingActivity extends BaseActivity {

	private EditText etName, etTel, etComName, etComAddress, etTis;
	private ImageView ivBack, ivTel;
	private Button btnSubmit;
	private CheckBox cBox;
	private TextView tvAgree;

	private HttpUtils post;
	private RequestParams params;
	private Dialog dialog;

	private Intent intent;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_request_loading);
	}

	@Override
	protected void initView() {
		etName = (EditText) findViewById(R.id.activity_request_et_name);
		etTel = (EditText) findViewById(R.id.activity_request_et_tel);
		etComName = (EditText) findViewById(R.id.activity_request_et_comname);
		etComAddress = (EditText) findViewById(R.id.activity_request_et_comaddress);
		etTis = (EditText) findViewById(R.id.activity_request_et_tis);

		ivBack = (ImageView) findViewById(R.id.activity_loading_iv_back);
		ivTel = (ImageView) findViewById(R.id.activity_loading_iv_kefu);

		btnSubmit = (Button) findViewById(R.id.activity_request_btn_submit);
		cBox = (CheckBox) findViewById(R.id.activity_request_cbox_agree);
		tvAgree = (TextView) findViewById(R.id.activity_request_tv_text);

		dialog = ProgressDialogHandle.getProgressDialog(
				RequestLoadingActivity.this, null);
	}

	@Override
	protected void registerListener() {
		/**
		 * 返回监听
		 */
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		/**
		 * 客服热线
		 */
		ivTel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RequestLoadingActivity.this);
				String[] tel = { "400-000-000  呼叫>>" };
				builder.setTitle("联系客服");
				builder.setMessage("400-000-000  呼叫>>");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(Intent.ACTION_DIAL,
										Uri.parse("tel:400-000-000"));
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitInfo();
			}

		});

		tvAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RequestLoadingActivity.this,
						WebAgreeActivity.class);
				startActivity(intent);
			}
		});
	}

	private void submitInfo() {
		if ("".equals(etName.getText().toString())) {
			showShortToast("姓名不得为空");
			return;
		}
		if ("".equals(etTel.getText().toString())) {
			showShortToast("联系方式不得为空");
			return;
		}
		if ("".equals(etComName.getText().toString())) {
			showShortToast("公司名称不得为空");
			return;
		}
		if ("".equals(etComAddress.getText().toString())) {
			showShortToast("公司地址不得为空");
			return;
		}
		if (!cBox.isChecked()) {
			showShortToast("请同意爱车通商家入驻协议");
			return;
		}
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("contact_user", etName.getText().toString());
		params.addBodyParameter("contact_mobile", etTel.getText().toString());
		params.addBodyParameter("company_name", etComName.getText().toString());
		params.addBodyParameter("address", etComAddress.getText().toString());
		params.addBodyParameter("company_desc", etTis.getText().toString());
		String url = UrlUtils.postUserUrl + UrlUtils.path_applyIn;
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
				if (dialog != null) {
					dialog.dismiss();
				}
				showShortToast("提交失败，请检查网络连接");
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
				} else {
					showShortToast("提交失败");
				}
			}
		});
	}

	@Override
	protected void initData() {

	}

}
