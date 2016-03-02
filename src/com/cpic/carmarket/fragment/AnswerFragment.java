package com.cpic.carmarket.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.cpic.carmarket.activity.ChatActivity;
import com.cpic.carmarket.bean.AnswerData;
import com.cpic.carmarket.bean.AnswerResult;
import com.cpic.carmarket.utils.UrlUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AnswerFragment extends Fragment{

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
				intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra("userId", datas.get(position).getEase_name());
				intent.putExtra("userName", datas.get(position).getUser_name());
				intent.putExtra("chatType", 1);
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
		post.send(HttpMethod.POST, UrlUtils.postUrl+UrlUtils.path_answerList, params, new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getActivity(), "数据获取失败，请检查网络连接", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
				AnswerResult res = JSONObject.parseObject(arg0.result, AnswerResult.class);
				int code = res.getCode();
				if (code == 0) {
					Toast.makeText(getActivity(), "数据获取失败", 0).show();
				}else if (code == 1) {
					datas = res.getData();
					adapter = new AnswerAdapter(getActivity());
					adapter.setDatas(datas);
					plv.setAdapter(adapter);
				}
			}
		});
	}
	
	private void initView(View view) {
		plv = (PullToRefreshListView) view.findViewById(R.id.fragment_answer_plv);
	}
	/**
	 * 列表适配器
	 * @author MBENBEN
	 *
	 */
	public class AnswerAdapter extends BaseAdapter{

		private Context context;
		private ArrayList<AnswerData> datas;
		
		@Override
		public int getCount() {
			return datas == null ? 0 :datas.size();
		}
		
		public void setDatas(ArrayList<AnswerData> datas){
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
				convertView = View.inflate(context, R.layout.item_answer_list, null);
				holder = new ViewHolder();
				holder.tvUserName = (TextView) convertView.findViewById(R.id.item_answer_tv_user_name);
				holder.tvCarType = (TextView) convertView.findViewById(R.id.item_answer_tv_car_type);
				holder.tvQuestion = (TextView) convertView.findViewById(R.id.item_answer_tv_reason);
				holder.tvMessage = (TextView) convertView.findViewById(R.id.item_answer_tv_newmsg);
				holder.tvRepair = (TextView) convertView.findViewById(R.id.item_answer_tv_repair_type);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_answer_iv_icon);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			holder.tvUserName.setText(datas.get(position).getUser_name());
			holder.tvCarType.setText(datas.get(position).getCar_name());
			holder.tvQuestion.setText(datas.get(position).getContent());
			holder.tvRepair.setText(datas.get(position).getDim_name());
			
			
			return convertView;
		}
		class ViewHolder{
			TextView tvUserName,tvCarType,tvQuestion,tvMessage,tvRepair;
			ImageView ivIcon;
		}
	}
}

