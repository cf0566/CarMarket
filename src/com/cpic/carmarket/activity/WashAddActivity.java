package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
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

public class WashAddActivity extends BaseActivity{

	private TextView tvAdd,tvSelect;
	private ImageView ivBack;
	private EditText etMoney;
	
	private PopupWindow pw;
	private int screenWidth;
	private ArrayList<String> datas;
	private String object_id;
	
	private ListView lv;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		DisplayMetrics metrics = new DisplayMetrics();
		WashAddActivity.this.getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		object_id = getIntent().getStringExtra("object_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_add_wash_service);
	}

	@Override
	protected void initView() {
		tvAdd = (TextView) findViewById(R.id.activity_add_wash_tv_add);
		tvSelect = (TextView) findViewById(R.id.activity_add_wash_tv_select);
		etMoney = (EditText) findViewById(R.id.activity_add_wash_service_et_money);
		ivBack = (ImageView) findViewById(R.id.activity_wash_add_iv_back);
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
				showPopupWindow();
			}
		});
		tvAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addService();
			}

		
		});
	}

	@Override
	protected void initData() {
		datas = new ArrayList<String>();
		if ("102".equals(object_id)) {
			datas.add("标准洗车");
			datas.add("精细洗车");
		}else if ("103".equals(object_id)) {
			datas.add("车内清洁");
			datas.add("车外清洁");
		}
	}
	
	private void addService() {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(WashAddActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		if ("标准洗车".equals(tvSelect.getText().toString())) {
			params.addBodyParameter("object_id", "103");
		}else if ("精细洗车".equals(tvSelect.getText().toString())) {
			params.addBodyParameter("object_id", "104");
		}else if ("车内清洁".equals(tvSelect.getText().toString())) {
			params.addBodyParameter("object_id", "101");
		}else if ("车外清洁".equals(tvSelect.getText().toString())) {
			params.addBodyParameter("object_id", "102");
		}else{
			showShortToast("请选择服务类型");
			return;
		}
		if ("".equals(etMoney.getText().toString())) {
			showShortToast("请输入金额");
			return;
		}
		params.addBodyParameter("cost", etMoney.getText().toString());
		String url = UrlUtils.postUrl+UrlUtils.path_washAdd;
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
				showShortToast("添加失败，请检查网络状况");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					showShortToast("添加成功");
					onBackPressed();
				}else{
					showShortToast("添加失败");
				}
				
			}
		});
		
	}
	
	private void showPopupWindow() {
		View view = View.inflate(WashAddActivity.this, R.layout.popupwindow_wash_select, null);
		lv = (ListView) view.findViewById(R.id.popupwindow_wash_lv);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(WashAddActivity.this, android.R.layout.simple_list_item_1);
		
		for (int i = 0; i < datas.size(); i++) {
			adapter.add(datas.get(i));
		}
		lv.setAdapter(adapter);
		
		pw = new PopupWindow(view, screenWidth/10*3, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		
		WindowManager.LayoutParams params = WashAddActivity.this.getWindow().getAttributes();
		params.alpha = 1f;

		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(true);
		
		pw.showAsDropDown(tvSelect);
		
		pw.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = WashAddActivity.this.getWindow().getAttributes();
				params.alpha = 1f;
				WashAddActivity.this.getWindow().setAttributes(params);
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tvSelect.setText(datas.get(position));
				pw.dismiss();
			}
		});
		
	}

}
