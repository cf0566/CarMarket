package com.cpic.carmarket.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.FormAfterServiceActivity;
import com.cpic.carmarket.activity.NotAgreeActivity;
import com.cpic.carmarket.bean.FormData;
import com.cpic.carmarket.bean.FormData3;
import com.cpic.carmarket.bean.FormDataInfo;
import com.cpic.carmarket.bean.FormDataInfo3;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class FormAfterServiceFragment extends Fragment {
	private PullToRefreshListView plv;
	private ArrayList<FormDataInfo3> datas;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private AfterAdapter adapter;
	private Dialog dialog;

	private Intent intent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_form_after_service, null);
		initView(view);

		registerListener();
		return view;
	}
	
	private void registerListener() {
		
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				String token = sp.getString("token", "");
				post = new HttpUtils();
				params = new RequestParams();
				params.addBodyParameter("token", token);
				params.addBodyParameter("status", UrlUtils.STATUS_AFTER);
				post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_orderList,params,new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						if (dialog != null) {
							dialog.show();
						}
					}
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getActivity(), UrlUtils.loading_failure, 0).show();
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
							datas = JSONObject.parseObject(arg0.result, FormData3.class).getData();
							adapter = new AfterAdapter();
							adapter.setDatas(datas);
							plv.setAdapter(adapter);
							Toast.makeText(getActivity(), UrlUtils.update_success, 0).show();
							plv.onRefreshComplete();
						}else{
							Toast.makeText(getActivity(), "数据获取失败", 0).show();
						}
					}
				});
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
			}
		});
		
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(getActivity(), FormAfterServiceActivity.class);
				intent.putExtra("order_id", datas.get(position-1).getOrder_id());
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initDatas();
	}
	private void initDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("status", UrlUtils.STATUS_AFTER);
		post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_orderList,params,new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getActivity(), UrlUtils.loading_failure, 0).show();
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
					datas = JSONObject.parseObject(arg0.result, FormData3.class).getData();
					adapter = new AfterAdapter();
					adapter.setDatas(datas);
					plv.setAdapter(adapter);
				}else{
					Toast.makeText(getActivity(), "数据获取失败", 0).show();
				}
			}
		});
	}
	private void initView(View view) {
		plv = (PullToRefreshListView) view.findViewById(R.id.fragment_form_after_service_plv);
		dialog = ProgressDialogHandle.getProgressDialog(getActivity(), null);
	}

	public class AfterAdapter extends BaseAdapter {

		private ArrayList<FormDataInfo3> datas;
		private BitmapDisplayConfig config;
		private BitmapUtils utils;

		public void setDatas(ArrayList<FormDataInfo3> datas) {
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.item_form_frag_after_service_list, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.item_form_after_service_iv_icon);
				holder.tvAgree = (TextView) convertView
						.findViewById(R.id.item_form_after_service_tv_agree);
				holder.tvNotAgree = (TextView) convertView
						.findViewById(R.id.item_form_after_service_tv_notagree);
				holder.tvCom = (TextView) convertView
						.findViewById(R.id.item_form_after_service_tv_com);
				holder.tvPrice = (TextView) convertView
						.findViewById(R.id.item_form_after_service_tv_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvCom.setText(datas.get(position).getCompany_name());
			holder.tvPrice.setText("¥" + datas.get(position).getOrder_amount());

			loadBitmap(holder, datas.get(position).getCompany_logo());

			holder.tvAgree.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					agreePay(position);
				}
			});
			
			holder.tvNotAgree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					intent = new Intent(getActivity(), NotAgreeActivity.class);
					intent.putExtra("order_id", datas.get(position).getOrder_id());
					getActivity().startActivity(intent);
				}
			});

			return convertView;
		}
		private void agreePay(int position) {
			sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String token = sp.getString("token", "");
			post = new HttpUtils();
			params = new RequestParams();
			params.addBodyParameter("token", token);
			params.addBodyParameter("order_id", datas.get(position).getOrder_id());
			params.addBodyParameter("status", UrlUtils.STATUS_AGREE);
			String url = UrlUtils.postUrl+UrlUtils.path_orderAction;
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
					Toast.makeText(getActivity(), "请检查网络连接，提交失败", 0).show();
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
						Toast.makeText(getActivity(), "同意退款成功", 0).show();
						initDatas();
					}else{
						Toast.makeText(getActivity(), "提交失败"+code+"", 0).show();
					}
				}
			});
		}

		private void loadBitmap(final ViewHolder holder, String ivUrl) {
			config = new BitmapDisplayConfig();
			utils = new BitmapUtils(getActivity());
			config.setLoadingDrawable(getResources().getDrawable(
					R.drawable.empty_photo));
			config.setLoadFailedDrawable(getResources().getDrawable(
					R.drawable.empty_photo));
			utils.display(holder.ivIcon, ivUrl, config);
		}

		class ViewHolder {
			ImageView ivIcon;
			TextView tvCom, tvPrice, tvAgree, tvNotAgree;
		}
	}

}
