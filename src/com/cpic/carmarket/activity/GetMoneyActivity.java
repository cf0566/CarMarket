package com.cpic.carmarket.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class GetMoneyActivity extends BaseActivity{

	private EditText etMoney;
	private TextView tvSelect,tvMoney;
	private Button btnGet;
	private ImageView ivBack;
	private String sumMoney,name,acount;
	
	private final static int PAY_ALI = 3;
	private final static int PAY_CAR = 1;
	private final static int REQUEST_CODE = 0;
	
	private Intent intent;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private int payType;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		sumMoney = getIntent().getStringExtra("money");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_get_money);
	}

	@Override
	protected void initView() {
		etMoney = (EditText) findViewById(R.id.activity_get_money_et_money);
		tvSelect = (TextView) findViewById(R.id.activity_get_money_tv_select);
		tvMoney = (TextView) findViewById(R.id.activity_get_money_tv_money);
		btnGet = (Button) findViewById(R.id.activity_get_money_btn_get);
		ivBack = (ImageView) findViewById(R.id.activity_get_money_iv_back);
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
		tvSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						GetMoneyActivity.this);
				String[] tel = { "银行卡提现","支付宝提现" };
				builder.setTitle("选择提现账户");
				builder.setItems(tel,new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								intent = new Intent(GetMoneyActivity.this, GetMoneyFromActivity.class);
								if (which == 0) {
									intent.putExtra("payType", PAY_CAR);
								}else if (which == 1) {
									intent.putExtra("payType", PAY_ALI);
								}
								startActivityForResult(intent, REQUEST_CODE);
							}
						});
				builder.show();
			}
		});
		
		btnGet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etMoney.getText().toString())) {
					showShortToast("请输入您要提现的金额");
					return;
				}
				if (Double.parseDouble(etMoney.getText().toString())>Double.parseDouble(sumMoney)) {
					showShortToast("提现金额不得大于可提现余额");
					return;
				}
				if (acount == null || name == null) {
					showShortToast("请添加您要提现的账户");
					return;
				}
				getMoney();
			}
		});
	}

	/**
	 * 提现
	 */
	private void getMoney() {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(GetMoneyActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("money", etMoney.getText().toString());
		params.addBodyParameter("pay_id", payType+"");
		params.addBodyParameter("content", name+","+acount);
		String url = UrlUtils.postUrl+UrlUtils.path_deposit;
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
				showShortToast("提现失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					showShortToast("已确认提现，我们会尽快审核");
					finish();
				}else{
					showShortToast("提现失败");
				}
				
			}
		});
		
		
	}
	@Override
	protected void initData() {
		tvMoney.setText(sumMoney);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			acount = data.getStringExtra("acount");
			name = data.getStringExtra("name");
			payType = data.getIntExtra("payType", 1);
			tvSelect.setText(acount);
		}
	}

}
