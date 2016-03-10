package com.cpic.carmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class GetMoneyFromActivity extends BaseActivity{

	private TextView tvAcount,tvInfo,tvFrom;
	private EditText etName,etAcount;
	private int payType;
	private Button btnGet;
	
	private ImageView ivBack;
	
	private final static int PAY_ALI = 3;
	private final static int PAY_CAR = 1;
	
	private Intent intent;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		payType = getIntent().getIntExtra("payType", 1);
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_money_get_from);
	}

	@Override
	protected void initView() {
		tvAcount = (TextView) findViewById(R.id.activity_get_money_from_tv_acount);
		tvInfo = (TextView) findViewById(R.id.activity_get_money_from_tv_info);
		tvFrom = (TextView) findViewById(R.id.activity_get_money_from_tv_from);
		etName = (EditText) findViewById(R.id.activity_get_money_from_et_name);
		etAcount = (EditText) findViewById(R.id.activity_get_money_from_et_acount);
		ivBack = (ImageView) findViewById(R.id.activity_get_from_money_iv_back);
		btnGet = (Button) findViewById(R.id.activity_get_money_from_btn_get);
	}

	@Override
	protected void initData() {
		if (payType == PAY_ALI) {
			tvInfo.setText("请填写您的支付宝信息");
			tvAcount.setText("账号");
			etAcount.setHint("请输入您的支付宝账号");
			tvFrom.setText("支付宝提现");
		}else if (payType == PAY_CAR) {
			tvInfo.setText("请填写您的银行卡提现信息");
			tvAcount.setText("卡号");
			etAcount.setHint("请输入您本人的银行卡卡号");
			tvFrom.setText("银行卡提现");
		}
	}


	@Override
	protected void registerListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		btnGet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etAcount.getText().toString())||"".equals(etName.getText().toString())) {
					showShortToast("姓名和账号不得为空");
				}else{
					intent = new Intent();
					intent.putExtra("name", etName.getText().toString());
					intent.putExtra("acount", etAcount.getText().toString());
					intent.putExtra("payType", payType);
					setResult(RESULT_OK,intent);
					finish();
				}
			}
		});
	}
}
