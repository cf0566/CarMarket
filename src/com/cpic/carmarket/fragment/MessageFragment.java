package com.cpic.carmarket.fragment;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
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
import com.cpic.carmarket.activity.MessageDetailsActivity;
import com.cpic.carmarket.activity.NewMessageActivity;
import com.cpic.carmarket.bean.MessageData;
import com.cpic.carmarket.bean.MessageDataInfo;
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

public class MessageFragment extends Fragment {

	private PullToRefreshListView plv;
	private TextView tvMsg;
	private ArrayList<MessageDataInfo> datas;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private AnswerAdapter adapter;
	private Dialog dialog;

	private Intent intent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		initView(view);
		loadDatas();
		
		registerListener();
		return view;
	}

	private void registerListener() {
		/**
		 * 信息监听跳转
		 */
		tvMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), NewMessageActivity.class);
				startActivity(intent);
			}
		});
		
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				intent = new Intent(getActivity(), MessageDetailsActivity.class);
				intent.putExtra("question_id", datas.get(position-1).getQuestion_id());
				startActivity(intent);
			}
		});
		
		
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
				String token = sp.getString("token", "");
				post = new HttpUtils();
				params = new RequestParams();
				params.addBodyParameter("token", token);
				post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_messageList,
						params, new RequestCallBack<String>() {

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								super.onStart();
								if (dialog != null) {
									dialog.show();
								}
							}
							@Override
							public void onFailure(HttpException arg0, String arg1) {
								Toast.makeText(getActivity(), "数据获取失败，请检查网络连接", 0)
										.show();
								if (dialog != null) {
									dialog.dismiss();
								}
								plv.onRefreshComplete();
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								plv.onRefreshComplete();
								if (dialog != null) {
									dialog.dismiss();
								}
								datas = JSONObject.parseObject(arg0.result,
										MessageData.class).getData();
								adapter = new AnswerAdapter(getActivity());
								adapter.setDatas(datas);
								plv.setAdapter(adapter);
							}
						});
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				
			}
		});
		
	}

	private void loadDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		post.send(HttpMethod.POST,
				UrlUtils.postUrl + UrlUtils.path_messageList, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
						if (dialog != null) {
							dialog.show();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getActivity(), "数据加载失败，请检查网络连接", 0)
								.show();
						if (dialog != null) {
							dialog.dismiss();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (dialog != null) {
							dialog.dismiss();
						}
						datas = JSONObject.parseObject(arg0.result,
								MessageData.class).getData();
						adapter = new AnswerAdapter(getActivity());
						adapter.setDatas(datas);
						plv.setAdapter(adapter);

					}

				});
	}

	private void initView(View view) {
		plv = (PullToRefreshListView) view.findViewById(R.id.fragment_message_plv);
		tvMsg = (TextView) view.findViewById(R.id.fragment_massage_msg_tv);
		dialog = ProgressDialogHandle.getProgressDialog(getActivity(), null);
	}

	/**
	 * 列表适配器
	 * 
	 * @author MBENBEN
	 *
	 */
	public class AnswerAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<MessageDataInfo> datas;
		private BitmapDisplayConfig config;
		private BitmapUtils utils;

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		public void setDatas(ArrayList<MessageDataInfo> datas) {
			this.datas = datas;
		}

		public AnswerAdapter(Context context) {
			this.context = context;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context,R.layout.item_fragment_massage_list, null);
				holder = new ViewHolder();
				holder.tvUserName = (TextView) convertView.findViewById(R.id.item_massage_tv_user_name);
				holder.tvCarType = (TextView) convertView.findViewById(R.id.item_massage_tv_car_type);
				holder.tvQuestion = (TextView) convertView.findViewById(R.id.item_massage_tv_reason);
				holder.tvRepair = (TextView) convertView.findViewById(R.id.item_massage_tv_check);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_massage_iv_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			holder.tvUserName.setText(datas.get(position).getUser_name());
			holder.tvCarType.setText(datas.get(position).getCar_name());
			holder.tvQuestion.setText(datas.get(position).getContent());
			holder.tvRepair.setText(datas.get(position).getDim_name());
			String ivUrl = datas.get(position).getUser_img();
			/**
			 * 下载用户头像
			 * 
			 * @param img_url
			 */
			loadBitmap(holder, ivUrl);
			return convertView;
		}

		private void loadBitmap(final ViewHolder holder, String ivUrl) {
			config = new BitmapDisplayConfig();
			utils = new BitmapUtils(context);
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
			config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
			utils.display(holder.ivIcon, ivUrl, config);
		}
		class ViewHolder {
			TextView tvUserName, tvCarType, tvQuestion, tvRepair;
			ImageView ivIcon;
		}
	}

}