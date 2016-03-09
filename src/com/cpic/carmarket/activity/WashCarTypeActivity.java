package com.cpic.carmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class WashCarTypeActivity extends BaseActivity{

	private TextView tvNext;
	private ImageView ivBack;
	private RadioGroup rGroup;
	private String object_id = "103";
	private Intent intent;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_wash_car_type);
	}

	@Override
	protected void initView() {
		tvNext = (TextView) findViewById(R.id.activity_wash_car_tv_next);
		ivBack = (ImageView) findViewById(R.id.activity_wash_car_type_iv_back);
		rGroup = (RadioGroup) findViewById(R.id.activity_wash_car_type_rgroup);
	}

	@Override
	protected void registerListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		/**
		 * 选择类型
		 */
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.activity_wash_car_type_rbtn_in:
					object_id = "103";
					break;
				case R.id.activity_wash_car_type_rbtn_out:
					object_id = "102";
					break;

				default:
					break;
				}
			}
		});
		
		/**
		 * 下一步
		 */
		tvNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(WashCarTypeActivity.this, WashListActivity.class);
				intent.putExtra("object_id", object_id);
				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void initData() {
		
	}
}
