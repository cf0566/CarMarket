package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.FormCloseActivity.MyAdapter;
import com.cpic.carmarket.activity.FormCloseActivity.MyAdapter.ViewHolder;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.AnswerDetails;
import com.cpic.carmarket.bean.AnswerDetailsData;
import com.cpic.carmarket.bean.AnswerDetailsListInfo;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.MyGridView;
import com.cpic.carmarket.view.MyListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AnswerDetailsActivity extends BaseActivity {

	private AnswerDetails details;
	private TextView tvContent, tvCar, tvOrderTime, tvCheck, tvCompany,
			tvScore, tvTime, tvAnswer, tvLiuyan;
	private ImageView  ivIcon, ivBack;
	private MyListView mlv;
	private Button btnOnline;

	private ArrayList<AnswerDetailsListInfo> questionData;
	private AnswerDetailsData data;


	private MyAdapter adapter;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;

	private BitmapUtils utils;
	private Dialog dialog;
	private RatingBar rBar;

	private String question_id, iv_icon_url, name, ease_name, img_url;

	private Intent intent;
	private BitmapDisplayConfig config;

	
	private MyGridView gv;
	private ArrayList<String> img_urls ;
	private MyAdapter2 adapter2;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		question_id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		ease_name = getIntent().getStringExtra("ease_name");
		img_url = getIntent().getStringExtra("img_url");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_answer_details);
	}

	@Override
	protected void registerListener() {
		tvLiuyan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(AnswerDetailsActivity.this,AnswerOnlineResponseActivity.class);
				intent.putExtra("question_id", question_id);
				if (data.getAnswer().getList().size() == 0) {
					intent.putExtra("pre_id", data.getAnswer().getAnswer_id());
				} else {
//					Log.i("oye", data.getAnswer().getList()
//									.get(data.getAnswer().getList().size() - 1)
//									.getId());
					intent.putExtra("pre_id",data.getAnswer().getList()
									.get(data.getAnswer().getList().size() - 1)
									.getId());
				}
				startActivity(intent);
			}
		});

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		btnOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(AnswerDetailsActivity.this,ChatActivity.class);
				intent.putExtra("chatType", 1);
				intent.putExtra("userId", ease_name);
				intent.putExtra("name", name);
				intent.putExtra("img_url", img_url);
				startActivity(intent);
			}
		});
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AnswerDetailsActivity.this, PicturePreviewActivity.class);
				intent.putExtra("url", img_urls.get(position));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initData() {
		loadDatas();
	}

	/**
	 * 下载用户头像
	 * 
	 * @param img_url
	 */
	private void loadUserIcon(String ivUrl) {
		config = new BitmapDisplayConfig();
		utils = new BitmapUtils(AnswerDetailsActivity.this);
		config.setLoadingDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		utils.display(ivIcon, ivUrl, config);
	}

	/**
	 * 上一界面回来时，重新加载页面
	 */
	@Override
	protected void onResume() {
		super.onResume();
		loadDatas();
	}

	private void loadDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(AnswerDetailsActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("question_id", question_id);
		post.send(HttpMethod.POST, UrlUtils.postUrl
				+ UrlUtils.path_answerDetail, params,
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
						if (dialog != null) {
							dialog.dismiss();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (dialog != null) {
							dialog.dismiss();
						}
						details = JSONObject.parseObject(arg0.result,
								AnswerDetails.class);
						int code = details.getCode();
						data = details.getData();
						if (code == 1) {
							tvContent.setText(data.getContent());
							tvCar.setText(data.getCar_name());
							tvCheck.setText("维修项目：" + data.getChecked());
							tvOrderTime.setText("预约时间：" + data.getOrder_time());
							tvTime.setText(data.getAnswer().getTime());
							tvCompany.setText(data.getAnswer().getName());
							tvAnswer.setText(data.getAnswer().getContent());
							tvScore.setText(data.getAnswer().getScore() + ""+ "分");
							rBar.setRating((float) data.getAnswer().getScore());
							iv_icon_url = data.getAnswer().getLogo();
							loadUserIcon(iv_icon_url);
							if (data.getQuestion_img().size()!= 0) {
								img_urls = data.getQuestion_img();
								adapter2 = new MyAdapter2();
								adapter2.steDatas(img_urls);
								gv.setAdapter(adapter2);
							}
							if (data.getAnswer().getList().size()!=0) {
								questionData = data.getAnswer().getList();
								adapter = new MyAdapter(AnswerDetailsActivity.this);
								adapter.setDatas(questionData);
								mlv.setAdapter(adapter);
							}else{
								mlv.setVisibility(View.INVISIBLE);
							}
						}
					}
				});
	}


	@Override
	protected void initView() {
		tvContent = (TextView) findViewById(R.id.activity_answer_tv_content);
		tvCar = (TextView) findViewById(R.id.activity_answer_tv_carname);
		tvOrderTime = (TextView) findViewById(R.id.activity_answer_tv_ordertime);
		tvCheck = (TextView) findViewById(R.id.activity_answer_tv_check);
		tvCompany = (TextView) findViewById(R.id.activity_answer_tv_company);
		tvScore = (TextView) findViewById(R.id.activity_answer_tv_score);
		tvTime = (TextView) findViewById(R.id.activity_answer_tv_time);
		tvAnswer = (TextView) findViewById(R.id.activity_answer_tv_answer);
		ivIcon = (ImageView) findViewById(R.id.activity_answer_iv_icon);
		mlv = (MyListView) findViewById(R.id.activity_answer_lv_question);
		tvLiuyan = (TextView) findViewById(R.id.activity_answer_btn_answer);
		btnOnline = (Button) findViewById(R.id.activity_answer_btn_online_talk);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		rBar = (RatingBar) findViewById(R.id.activity_answer_rbar);
		ivBack = (ImageView) findViewById(R.id.activity_answer_details_iv_back);
		gv = (MyGridView) findViewById(R.id.activity_answer_gv);
	}

	public class MyAdapter extends BaseAdapter {

		private static final int TYPE_ANSWER = 0;
		private static final int TYPE_QUESTION = 1;
		private static final int TYPE_COUNT = 2;

		private ArrayList<AnswerDetailsListInfo> datas;
		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		public void setDatas(ArrayList<AnswerDetailsListInfo> datas) {
			this.datas = datas;
		}

		@Override
		public int getItemViewType(int position) {
			if (datas.get(position).getType() == 1) {
				return TYPE_QUESTION;
			}
			return TYPE_ANSWER;
		}

		@Override
		public int getViewTypeCount() {
			return TYPE_COUNT;
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
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			int type = getItemViewType(position);
			if (convertView == null) {
				holder = new ViewHolder();
				if (type == TYPE_QUESTION) {
					convertView = View.inflate(context,
							R.layout.item_answer_details_list, null);
					holder.tvQuestion = (TextView) convertView
							.findViewById(R.id.item_answer_details_tv_question);
				} else if (type == TYPE_ANSWER) {
					convertView = View.inflate(context,
							R.layout.item_answer_details_list2, null);
					holder.tvAnswer = (TextView) convertView
							.findViewById(R.id.item_answer_details_tv_answer);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (type == TYPE_QUESTION) {
				holder.tvQuestion.setText(datas.get(position).getMsg());
				// Log.i("oye", datas.get(position).getMsg());
			} else if (type == TYPE_ANSWER) {
				holder.tvAnswer.setText(datas.get(position).getMsg() + "");
				// Log.i("oye", datas.get(position).getMsg());
			}
			return convertView;
		}

		class ViewHolder {
			TextView tvAnswer, tvQuestion;
		}
	}
	
	/**
	 * 图片加载
	 * @author MBENBEN
	 *
	 */
	public class MyAdapter2 extends BaseAdapter{

		private ArrayList<String> img_urls;
		
		public void steDatas(ArrayList<String> img_urls){
			this.img_urls = img_urls;
		}
		
		@Override
		public int getCount() {
			return img_urls == null ? 0 :img_urls.size();
		}

		@Override
		public Object getItem(int position) {
			return img_urls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder ;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(AnswerDetailsActivity.this, R.layout.item_select_photo_list, null);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_photo_iv_icon);
				holder.ivDel = (ImageView) convertView.findViewById(R.id.item_photo_iv_del);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ivDel.setVisibility(View.INVISIBLE);
			loadUserIcon(img_urls.get(position), holder);
			return convertView;
		}
		private void loadUserIcon(String ivUrl,ViewHolder holder) {
			config = new BitmapDisplayConfig();
			 utils = new BitmapUtils(AnswerDetailsActivity.this);
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
			config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
			utils.display(holder.ivIcon, ivUrl, config);
		}
		
		class ViewHolder{
			ImageView ivIcon,ivDel;
		}
	}
}
