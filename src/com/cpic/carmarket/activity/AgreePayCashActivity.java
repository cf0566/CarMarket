package com.cpic.carmarket.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class AgreePayCashActivity extends BaseActivity {

	private EditText etMoney;
	private RadioGroup rgroup;
	private Button btnAgree;
	private ImageView ivBack;
	
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

	}
}
