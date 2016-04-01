package com.cpic.carmarket.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
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
import com.bumptech.glide.Glide;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.AnswerDetailsActivity;
import com.cpic.carmarket.activity.ChatActivity;
import com.cpic.carmarket.activity.NewMessageActivity;
import com.cpic.carmarket.bean.AnswerData;
import com.cpic.carmarket.bean.AnswerResult;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.CircleImageView;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AnswerFragment extends Fragment {

	private PullToRefreshListView plv;
	private TextView tvMsg;
	private ArrayList<AnswerData> datas;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private AnswerAdapter adapter;
	private Dialog dialog;
	private ArrayList<EMConversation> conversationList = new ArrayList<EMConversation>();
	private ImageView ivTis;

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
				intent.putExtra("name", datas.get(position - 1).getUser_name());
				intent.putExtra("ease_name", datas.get(position - 1)
						.getEase_name());
				intent.putExtra("img_url", datas.get(position - 1)
						.getUser_img());
				startActivity(intent);
			}
		});
		tvMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), NewMessageActivity.class);
				startActivity(intent);
			}
		});
		plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sp = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				String token = sp.getString("token", "");
				post = new HttpUtils();
				params = new RequestParams();
				params.addBodyParameter("token", token);
				post.send(HttpMethod.POST, UrlUtils.postUrl
						+ UrlUtils.path_answerList, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								super.onStart();
								if (dialog != null) {
									dialog.show();
								}
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								Toast.makeText(getActivity(), "数据获取失败，请检查网络连接",
										0).show();
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
								AnswerResult res = JSONObject.parseObject(
										arg0.result, AnswerResult.class);
								int code = res.getCode();
								if (code == 0) {
									Toast.makeText(getActivity(), "数据获取失败", 0)
											.show();
								} else if (code == 1) {
									datas = res.getData();
									adapter = new AnswerAdapter(getActivity());
									adapter.setDatas(datas);
									plv.setAdapter(adapter);
									Toast.makeText(getActivity(), "更新完成", 0)
											.show();
								}
							}
						});
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
			}
		});
	}

	private void initData() {
		loadDatas();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (conversationList.size() != 0) {
			conversationList.clear();
		}
		conversationList.addAll(loadConversationsWithRecentChat());
		if (conversationList.size() != 0) {
			if (conversationList.get(0).getUnreadMsgCount() != 0) {
				ivTis.setVisibility(View.VISIBLE);
			} else {
				ivTis.setVisibility(View.INVISIBLE);
			}
		}
	}

	private void loadDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		post.send(HttpMethod.POST, UrlUtils.postUrl + UrlUtils.path_answerList,
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
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						if (dialog != null) {
							dialog.dismiss();
						}
						// Log.i("oye", arg0.result);
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
		tvMsg = (TextView) view.findViewById(R.id.fragment_answer_msg_tv);
		dialog = ProgressDialogHandle.getProgressDialog(getActivity(), null);
		ivTis = (ImageView) view.findViewById(R.id.fragment_answer_msg_iv_tis);
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
		private BitmapDisplayConfig config;

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
				convertView = View.inflate(context, R.layout.item_answer_list,
						null);
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
				holder.ivIcon = (CircleImageView) convertView
						.findViewById(R.id.item_answer_iv_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvUserName.setText(datas.get(position).getUser_name());
			holder.tvCarType.setText(datas.get(position).getCar_name());
			holder.tvQuestion.setText(datas.get(position).getContent());
			holder.tvRepair.setText(datas.get(position).getDim_name());
			for (int i = 0; i < conversationList.size(); i++) {
				if (datas.get(position).getEase_name()
						.equals(conversationList.get(i).getUserName())) {
					int count = conversationList.get(i).getUnreadMsgCount();
					if (count != 0) {
						holder.tvMessage.setText("新消息(" + count + ")");
						holder.tvMessage.setVisibility(View.VISIBLE);
					} else {
						holder.tvMessage.setVisibility(View.INVISIBLE);
					}
				}
			}
			String ivUrl = datas.get(position).getUser_img();
			/**
			 * 下载用户头像
			 * 
			 * @param img_url
			 */
//			loadBitmap(holder, ivUrl);
			Glide.with(context).load(ivUrl).placeholder(R.drawable.empty_photo).fitCenter().into(holder.ivIcon);
			holder.ivIcon.setTag(R.id.image_tag, position);
			
			return convertView;
		}

//		private void loadBitmap(final ViewHolder holder, String ivUrl) {
//			config = new BitmapDisplayConfig();
//			utils = new BitmapUtils(context);
//			config.setLoadingDrawable(getResources().getDrawable(
//					R.drawable.empty_photo));
//			config.setLoadFailedDrawable(getResources().getDrawable(
//					R.drawable.empty_photo));
//			utils.display(holder.ivIcon, ivUrl, config);
//		}

		class ViewHolder {
			TextView tvUserName, tvCarType, tvQuestion, tvMessage, tvRepair;
			CircleImageView ivIcon;
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation
							.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList,
				new Comparator<Pair<Long, EMConversation>>() {
					@Override
					public int compare(final Pair<Long, EMConversation> con1,
							final Pair<Long, EMConversation> con2) {

						if (con1.first == con2.first) {
							return 0;
						} else if (con2.first > con1.first) {
							return 1;
						} else {
							return -1;
						}
					}

				});
	}
}
