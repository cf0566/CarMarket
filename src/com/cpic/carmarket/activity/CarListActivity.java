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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.CarList;
import com.cpic.carmarket.bean.CarListData;
import com.cpic.carmarket.bean.CarListDataInfo;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CarListActivity extends BaseActivity {

	private ExpandableListView elv;
	private ImageView ivBack;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private CarAdapter adapter;
	
	private ArrayList<CarListData> groups;
	private ArrayList<ArrayList<CarListDataInfo>> child;
	
	private ArrayList<CarListDataInfo> temp;
	
	private Intent intent;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_car_list);
	}

	@Override
	protected void initView() {
		elv = (ExpandableListView) findViewById(R.id.activity_car_list_elv);
		ivBack = (ImageView) findViewById(R.id.activity_car_list_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(CarListActivity.this, null);
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
		
		elv.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				intent = new Intent();
				intent.putExtra("car_name", child.get(groupPosition).get(childPosition).getCategory_name());
				intent.putExtra("car_id", child.get(groupPosition).get(childPosition).getCategory_id());
				setResult(RESULT_OK,intent);
				finish();
				return false;
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
		sp = PreferenceManager.getDefaultSharedPreferences(CarListActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		String url = UrlUtils.postUserUrl+UrlUtils.path_carCategories;
		post.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {

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
				showShortToast("获取数据失败，请检查网络连接");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				CarList list = JSONObject.parseObject(arg0.result, CarList.class);
				int code = list.getCode();
				if (code == 1) {
					groups = new ArrayList<CarListData>();
					groups = list.getData();
					child = new ArrayList<ArrayList<CarListDataInfo>>();
					temp = new ArrayList<CarListDataInfo>();
					adapter = new CarAdapter();
					for (int i = 0; i < groups.size(); i++) {
						temp = groups.get(i).getChildren();
						child.add(temp);
					}
					adapter.setDatas(groups, child);
					elv.setAdapter(adapter);
					for (int i = 0; i < groups.size(); i++) {
						elv.expandGroup(i);
					}
				}else{
					showShortToast("获取数据失败");
				}
			}
		});
	}
	
	
	
	public class CarAdapter extends BaseExpandableListAdapter{
		
		private ArrayList<CarListData> titleList = null;
		private ArrayList<ArrayList<CarListDataInfo>> contentList = null;
		
		public void setDatas(ArrayList<CarListData> Title , ArrayList<ArrayList<CarListDataInfo>> Content){
			this.titleList = Title;
			this.contentList = Content;
		}
		
		@Override
		public int getGroupCount() {
			return titleList == null ? 0 :titleList.size();
		}
		@Override
		public int getChildrenCount(int groupPosition) {
			return contentList.get(groupPosition).size();
		}
		@Override
		public Object getGroup(int groupPosition) {
			return titleList.get(groupPosition);
		}
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return contentList.get(groupPosition).get(childPosition);
		}
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		@Override
		public boolean hasStableIds() {
			return false;
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CarListActivity.this, R.layout.item_carlist_group, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView)convertView.findViewById(R.id.item_carlist_group_tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(titleList.get(groupPosition).getHeader());
			
			return convertView;
		}
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(CarListActivity.this, R.layout.item_carlist_child, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView)convertView.findViewById(R.id.item_carlist_child_tv_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvContent.setText(contentList.get(groupPosition).get(childPosition).getCategory_name());
			return convertView;
		}
		class ViewHolder{
			TextView tvTitle;
			TextView tvContent;
			ImageView ivIcon;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
	
}
