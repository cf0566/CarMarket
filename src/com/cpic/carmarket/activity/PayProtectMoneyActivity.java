package com.cpic.carmarket.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class PayProtectMoneyActivity extends BaseActivity {

	private ImageView ivBack;
	private Button btnAgree;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_pay_protect_money);
	}

	@Override
	protected void initView() {
		ivBack  = (ImageView) findViewById(R.id.activity_pay_protect_iv_back);
		btnAgree = (Button) findViewById(R.id.activity_pay_protect_btn_agree);
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
		 * 同意支付监听
		 */
		btnAgree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	protected void initData() {

	}

}
