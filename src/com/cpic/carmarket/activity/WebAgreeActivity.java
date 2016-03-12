package com.cpic.carmarket.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

public class WebAgreeActivity extends BaseActivity{

	private ImageView ivBack;
	private String url;
	private WebView wbView;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_web_agree);
	}

	@Override
	protected void initView() {
		ivBack = (ImageView) findViewById(R.id.activity_web_back);
		wbView =  (WebView) findViewById(R.id.activity_web_agree);
		
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
		url = "http://jk1.cpioc.com/index.php?m=Admin&c=merchantApi&a=protocal";
		WebSettings webSetting = wbView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		wbView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		wbView.loadUrl(url);
	}

}
