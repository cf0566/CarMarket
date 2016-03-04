package com.cpic.carmarket.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class NewMessageActivity extends BaseActivity{

	private ImageView ivBack;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_new_massage);
	}

	@Override
	protected void initView() {
		ivBack = (ImageView) findViewById(R.id.activity_newmsg_iv_back);
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
