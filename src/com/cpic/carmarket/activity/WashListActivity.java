package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.WashList;
import com.cpic.carmarket.bean.WashListData;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class WashListActivity extends BaseActivity{

	private String object_id;
	private ArrayList<WashListData> datas;
	private ListView lv;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	
	private TextView tvAdd,tvManager;
	private ImageView ivBack;
	
	private Dialog dialog;
	
	private WashAdapter adapter;
	
	private Intent intent;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		object_id = getIntent().getStringExtra("object_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_wash_list);
	}

	@Override
	protected void initView() {
		lv = (ListView) findViewById(R.id.activity_wash_list_lv);
		tvAdd = (TextView) findViewById(R.id.activity_wash_list_tv_add_service);
		ivBack = (ImageView) findViewById(R.id.activity_wash_list_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		tvManager = (TextView) findViewById(R.id.activity_wash_list_tv_manager);
	}

	@Override
	protected void registerListener() {
		
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		tvAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(WashListActivity.this, WashAddActivity.class);
				intent.putExtra("object_id", object_id);
				startActivity(intent);
			}
		});
		tvManager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(WashListActivity.this, WashDeleteManagerActivity.class);
				intent.putExtra("object_id", object_id);
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	protected void initData() {
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadDatas();
	}
	private void loadDatas() {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(WashListActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("object_id", object_id);
		String url = UrlUtils.postUrl+UrlUtils.path_washList;
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
				showShortToast(UrlUtils.loading_failure);
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				WashList list = JSONObject.parseObject(arg0.result, WashList.class);
				int code = list.getCode();
				if (code == 1) {
					adapter = new WashAdapter();
					datas = list.getData();
					adapter.setDatas(datas);
					lv.setAdapter(adapter);
				}else{
					showShortToast("数据加载失败！");
				}
			}
		});
	}

	public class WashAdapter extends BaseAdapter{
		
		private ArrayList<WashListData> data;

		public void setDatas(ArrayList<WashListData> data){
			this.data = data;
		}
		
		@Override
		public int getCount() {
			return data == null ? 0 :data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(WashListActivity.this, R.layout.item_wash_list, null);
				holder.tvType = (TextView) convertView.findViewById(R.id.item_activity_wash_list_tv_type);
				holder.tvPrice = (TextView) convertView.findViewById(R.id.item_activity_wash_list_tv_price);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvType.setText(data.get(position).getAttribute_name());
			holder.tvPrice.setText("¥:"+data.get(position).getCost());
			
			return convertView;
		}
		
		class ViewHolder{
			TextView tvType,tvPrice;
		}
	}
	
}
