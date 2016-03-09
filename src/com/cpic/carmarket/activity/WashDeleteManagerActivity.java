package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.WashListActivity.WashAdapter;
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

public class WashDeleteManagerActivity extends BaseActivity {

	private String object_id;
	private ArrayList<WashListData> datas;
	private ListView lv;
	private ImageView ivBack;

	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	private CheckBox cbox;
	private Button btnDelete;
	private ArrayList<String> items = new ArrayList<String>();
	
	private String deleteItems = "";
	private WashDeleteAdapter adapter;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		object_id = getIntent().getStringExtra("object_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_wash_manager);
	}

	@Override
	protected void initView() {
		lv = (ListView) findViewById(R.id.activity_wash_manager_lv);
		ivBack = (ImageView) findViewById(R.id.activity_wash_manager_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(WashDeleteManagerActivity.this, null);
		btnDelete = (Button) findViewById(R.id.activity_wash_manager_btn_delete);
	}

	@Override
	protected void registerListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				cbox = (CheckBox) view.findViewById(R.id.item_activity_wash_delete_list_cbox);
				if (cbox.isChecked()) {
					cbox.setChecked(false);
					items.remove(datas.get(position).getAttribute_id());
				}else if (!cbox.isChecked()) {
					cbox.setChecked(true);
					items.add(datas.get(position).getAttribute_id());
				}
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteItems();
			}
		});
		
	}

	private void deleteItems() {
			post = new HttpUtils();
			params = new RequestParams();
			sp = PreferenceManager.getDefaultSharedPreferences(WashDeleteManagerActivity.this);
			String token = sp.getString("token", "");
			params.addBodyParameter("token", token);
			if (items.size()!= 0) {
				for (int i = 0; i < items.size()-1; i++) {
					deleteItems += items.get(i)+",";
				}
				deleteItems +=  items.get(items.size()-1);
				Log.i("oye", deleteItems);
			}else{
				showShortToast("请至少选择删除一项");
				return;
			}
			params.addBodyParameter("object_id", deleteItems);
			String url = UrlUtils.postUrl+UrlUtils.path_washDel;
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
					showShortToast("删除失败，请检查网络连接");
					if (dialog != null) {
						dialog.dismiss();
					}
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					JSONObject obj = JSONObject.parseObject(arg0.result);
					int code = obj.getIntValue("code");
					if (code == 1) {
						showShortToast("删除成功");
						onBackPressed();
					}else{
						showShortToast("删除失败，请重试");
					}
				}
			});
	}
	
	@Override
	protected void initData() {
		loadDatas();
	}
	private void loadDatas() {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(WashDeleteManagerActivity.this);
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
					adapter = new WashDeleteAdapter();
					datas = list.getData();
					adapter.setDatas(datas);
					lv.setAdapter(adapter);
				}else{
					showShortToast("数据加载失败！");
				}
			}
		});
	}

	public class WashDeleteAdapter extends BaseAdapter {

		private ArrayList<WashListData> data;

		public void setDatas(ArrayList<WashListData> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data == null ? 0 : data.size();
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
				convertView = View.inflate(WashDeleteManagerActivity.this,
						R.layout.item_wash_delete_list, null);
				holder.tvType = (TextView) convertView
						.findViewById(R.id.item_wash_delete_list_tv_type);
				holder.cBox = (CheckBox) convertView
						.findViewById(R.id.item_activity_wash_delete_list_cbox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvType.setText(data.get(position).getAttribute_name());

			return convertView;
		}

		class ViewHolder {
			TextView tvType;
			CheckBox cBox;
		}
	}

}
