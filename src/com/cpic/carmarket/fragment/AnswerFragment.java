package com.cpic.carmarket.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.AnswerDetailsActivity;
import com.cpic.carmarket.activity.ChatActivity;
import com.cpic.carmarket.bean.AnswerData;
import com.cpic.carmarket.bean.AnswerResult;
import com.cpic.carmarket.utils.UrlUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AnswerFragment extends Fragment {

	private PullToRefreshListView plv;
	private ArrayList<AnswerData> datas;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private AnswerAdapter adapter;

	private Intent intent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_answer, null);
		initView(view);
		initData();
		registerListener();
		return view;
	}

	private void registerListener() {
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(getActivity(), AnswerDetailsActivity.class);
				intent.putExtra("id", datas.get(position - 1).getQuestion_id());
				startActivity(intent);
			}
		});
	}

	private void initData() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_answerList,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getActivity(), "数据获取失败，请检查网络连接", 0)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						AnswerResult res = JSONObject.parseObject(arg0.result,
								AnswerResult.class);
						int code = res.getCode();
						if (code == 0) {
							Toast.makeText(getActivity(), "数据获取失败", 0).show();
						} else if (code == 1) {
							datas = res.getData();
							adapter = new AnswerAdapter(getActivity());
							adapter.setDatas(datas);
							plv.setAdapter(adapter);
						}
					}
				});
	}

	private void initView(View view) {
		plv = (PullToRefreshListView) view
				.findViewById(R.id.fragment_answer_plv);
	}

	/**
	 * 列表适配器
	 * 
	 * @author MBENBEN
	 *
	 */
	public class AnswerAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<AnswerData> datas;
		private BitmapUtils utils;

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		public void setDatas(ArrayList<AnswerData> datas) {
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
				convertView = View.inflate(context, R.layout.item_answer_list,null);
				holder = new ViewHolder();
				holder.tvUserName = (TextView) convertView
						.findViewById(R.id.item_answer_tv_user_name);
				holder.tvCarType = (TextView) convertView
						.findViewById(R.id.item_answer_tv_car_type);
				holder.tvQuestion = (TextView) convertView
						.findViewById(R.id.item_answer_tv_reason);
				holder.tvMessage = (TextView) convertView
						.findViewById(R.id.item_answer_tv_newmsg);
				holder.tvRepair = (TextView) convertView
						.findViewById(R.id.item_answer_tv_repair_type);
				holder.ivIcon = (ImageView) convertView
						.findViewById(R.id.item_answer_iv_icon);
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
			loadBitmap(holder,ivUrl);

			return convertView;
		}

		private void loadBitmap(final ViewHolder holder ,String ivUrl) {
			 utils = new BitmapUtils(context);
			// 设置是否支持本地缓存
			utils.configDiskCacheEnabled(true);
			// 设置是否支持内存缓存
			utils.configMemoryCacheEnabled(true);
			// 设置图片的最大宽高
			utils.configDefaultBitmapMaxSize(200, 200);
			// 设置图片的默认颜色处理方式
			utils.configDefaultBitmapConfig(Config.ARGB_8888);
			// 设置缓存的有效期
			utils.configDefaultCacheExpiry(10000);
			// 设置联网读取图片的超时时间
			utils.configDefaultConnectTimeout(10000);
			utils.display(holder.ivIcon, ivUrl, new BitmapLoadCallBack<View>() {

				@Override
				public void onLoadCompleted(View arg0, String arg1,
						Bitmap arg2, BitmapDisplayConfig arg3,
						BitmapLoadFrom arg4) {
					holder.ivIcon.setImageBitmap(arg2);
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					holder.ivIcon.setImageResource(R.drawable.touxiang);
				}
			});
			
		}

		class ViewHolder {
			TextView tvUserName, tvCarType, tvQuestion, tvMessage, tvRepair;
			ImageView ivIcon;
		}
	}
}
