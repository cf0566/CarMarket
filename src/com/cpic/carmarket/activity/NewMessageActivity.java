package com.cpic.carmarket.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.EaseUser;
import com.cpic.carmarket.bean.EaseUserInfo;
import com.cpic.carmarket.utils.DateUtils;
import com.cpic.carmarket.utils.SmileUtils;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.CircleImageView;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewMessageActivity extends BaseActivity{

	private ImageView ivBack;
	private ArrayList<EMConversation> conversationList = new ArrayList<EMConversation>();
	private ArrayList<EaseUserInfo> info;
	private NewAdapter adapter;
	private PullToRefreshListView plv;
	private Intent intent;
	private HttpUtils post ;
	private RequestParams params;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_new_massage);
	}

	@Override
	protected void initView() {
		ivBack = (ImageView) findViewById(R.id.activity_newmsg_iv_back);
		plv = (PullToRefreshListView) findViewById(R.id.activity_newmsg_plv);
	}

	@Override
	protected void registerListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		plv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
				intent = new Intent(NewMessageActivity.this,ChatActivity.class);
				post = new HttpUtils();
				params = new RequestParams();
				params.addBodyParameter("user", conversationList.get(position-1).getUserName());
				String url = UrlUtils.postUrl + UrlUtils.path_getUserImg;
				post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						EaseUser user = JSONObject.parseObject(arg0.result, EaseUser.class);
						int code = user.getCode();
						if (code == 1) {
							info = user.getData();
							if (info.size()!=0) {
								intent.putExtra("name", info.get(0).getUser_name());
								intent.putExtra("img_url", info.get(0).getImg());
								intent.putExtra("userId", conversationList.get(position-1).getUserName());
								startActivity(intent);
							}else{
								intent.putExtra("userId", conversationList.get(position-1).getUserName());
								startActivity(intent);
							}
						}
					}
				});
				
			}
		});
	}

	@Override
	protected void initData() {
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter = new NewAdapter();
		adapter.setDatas(conversationList);
		plv.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (conversationList.size()!=0) {
			conversationList.clear();
		}
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter.notifyDataSetChanged();
	}
	
	public class NewAdapter extends BaseAdapter{

		private ArrayList<EMConversation> datas;
		private ArrayList<EaseUserInfo> info;
		private BitmapDisplayConfig config;
		private BitmapUtils utils;
		
		public void setDatas(ArrayList<EMConversation> datas){
			this.datas = datas;
		}
		
		@Override
		public int getCount() {
			return datas == null ? 0 :datas.size();
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
				convertView = View.inflate(NewMessageActivity.this, R.layout.item_contants_list, null);
				holder = new ViewHolder();
				holder.tvName = (TextView) convertView.findViewById(R.id.item_contants_tv_user_name);
				holder.tvTime = (TextView) convertView.findViewById(R.id.item_contants_tv_time);
				holder.tvLastTalk = (TextView) convertView.findViewById(R.id.item_contants_tv_content);
				holder.tvTis = (TextView) convertView.findViewById(R.id.item_contants_tv_tis);
				holder.ivIcon = (CircleImageView) convertView.findViewById(R.id.item_contants_iv_icon);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			getUserInfo(position,holder);
			
			holder.tvLastTalk.setText(datas.get(position).getLastMessage().getBody().toString());
			holder.tvTime.setText(DateUtils.getTimestampString(new Date(datas.get(position).getLastMessage().getMsgTime())));
			
			EMMessage lastMessage = datas.get(position).getLastMessage();
			holder.tvLastTalk.setText(SmileUtils.getSmiledText(NewMessageActivity.this, 
					getMessageDigest(lastMessage, (NewMessageActivity.this))),
					BufferType.SPANNABLE);
			if (datas.get(position).getUnreadMsgCount() == 0) {
				holder.tvTis.setVisibility(View.INVISIBLE);
			}else{
				holder.tvTis.setText(datas.get(position).getUnreadMsgCount()+"");
			}
			return convertView;
		}

		private void getUserInfo(int position,final ViewHolder holder) {
			post = new HttpUtils();
			params = new RequestParams();
			params.addBodyParameter("user", datas.get(position).getUserName());
			String url = UrlUtils.postUrl + UrlUtils.path_getUserImg;
			post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					EaseUser user = JSONObject.parseObject(arg0.result, EaseUser.class);
					int code = user.getCode();
					if (code == 1) {
						info = user.getData();
						if (info.size()!=0) {
							holder.tvName.setText(info.get(0).getUser_name());
//							loadIvIcon(info.get(0).getImg(), holder);
							Glide.with(getApplicationContext()).load(info.get(0).getImg()).placeholder(R.drawable.empty_photo).fitCenter().into(holder.ivIcon);
						}
					}
				}
			});
		}
//		private void loadIvIcon(String img_url,ViewHolder holder) {
//			config = new BitmapDisplayConfig();
//			utils = new BitmapUtils(NewMessageActivity.this);
//			config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
//			config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
//			utils.display(holder.ivIcon, img_url, config);
//		}
		class ViewHolder{
			TextView tvName,tvTime,tvLastTalk,tvTis;
			CircleImageView ivIcon;
		}
	}
	
	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture) +imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			
			if(((DemoHXSDKHelper)HXSDKHelper.getInstance()).isRobotMenuMessage(message)){
				digest = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getRobotMenuMessageDigest(message);
			}else if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL,false)){
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
			}else{
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			return "";
		}

		return digest;
	}
	
	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}
	
	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
		 * 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 
		 * 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					//if(conversation.getType() != EMConversationType.ChatRoom){
						sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
					//}
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
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

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
