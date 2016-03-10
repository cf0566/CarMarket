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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.MoneyManager;
import com.cpic.carmarket.bean.MoneyManagerData;
import com.cpic.carmarket.bean.MoneyManagerList;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MoneyManagerActivity extends BaseActivity{
	
	private TextView tvMoney;
	private Button btnGet;
	private ListView lv;
	private ImageView ivBack;
	private MoneyManagerData money;
	private ArrayList<MoneyManagerList> datas;
	private MoneyAdapter adapter ;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private Intent intent;
	
	private String sumMoney;

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_money_manager);
	}

	@Override
	protected void initView() {
		tvMoney = (TextView) findViewById(R.id.activity_money_manager_tv_money);
		btnGet = (Button) findViewById(R.id.activity_money_manager_btn_get_money);
		lv = (ListView) findViewById(R.id.activity_money_manager_lv);
		ivBack = (ImageView) findViewById(R.id.activity_money_manager_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(MoneyManagerActivity.this, null);
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
		
		btnGet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(MoneyManagerActivity.this, GetMoneyActivity.class);
				intent.putExtra("money", sumMoney);
				startActivity(intent);
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
		sp = PreferenceManager.getDefaultSharedPreferences(MoneyManagerActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		String url = UrlUtils.postUrl+UrlUtils.path_tradeList;
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
				showShortToast(UrlUtils.loading_failure);
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				money = JSONObject.parseObject(arg0.result, MoneyManager.class).getData();
				sumMoney = money.getBalance();
				tvMoney.setText("¥"+sumMoney);
				datas = money.getTradeList();
				adapter = new MoneyAdapter();
				adapter.setDatas(datas);
				lv.setAdapter(adapter);
			}
		});
	}

	public class MoneyAdapter extends BaseAdapter{
		
		private ArrayList<MoneyManagerList> datas;
		
		public void setDatas(ArrayList<MoneyManagerList> datas){
			this.datas = datas;
		}
		@Override
		public int getCount() {
			return datas == null ? 0 :datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder ;
			if (convertView == null ) {
				convertView = View.inflate(MoneyManagerActivity.this, R.layout.item_money_manager_list, null);
				holder = new ViewHolder();
				holder.tvTime = (TextView) convertView.findViewById(R.id.item_manager_tv_time);
				holder.tvType = (TextView) convertView.findViewById(R.id.item_manager_tv_type);
				holder.tvtrade = (TextView) convertView.findViewById(R.id.item_manager_tv_trade);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTime.setText(datas.get(position).getDate());
			if (1==datas.get(position).getType()) {
				holder.tvType.setText("提现");
				holder.tvtrade.setText("-"+datas.get(position).getMoney());
			}else if (2 == datas.get(position).getType()) {
				holder.tvType.setText("转入");
				holder.tvtrade.setText("+"+datas.get(position).getMoney());
			}
			
			return convertView;
		}
		
		class ViewHolder{
			TextView tvType,tvTime,tvtrade;
		}
	}
}
