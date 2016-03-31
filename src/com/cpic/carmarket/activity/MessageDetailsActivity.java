package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.AnswerDetailsActivity.MyAdapter2;
import com.cpic.carmarket.activity.AnswerDetailsActivity.MyAdapter2.ViewHolder;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.AnswerDetails;
import com.cpic.carmarket.bean.MessageDetails;
import com.cpic.carmarket.bean.MessageDetailsData;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.MyGridView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MessageDetailsActivity extends BaseActivity {

	private MessageDetails details;
	private MessageDetailsData data;
	private TextView tvContent, tvCar, tvOrderTime, tvCheck;
	private ImageView ivBack;
	private Button btnOnline;

	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;

	private BitmapUtils utils;
	private Dialog dialog;

	private String question_id;
	private BitmapDisplayConfig config;

	private Intent intent;

	private MyGridView gv;
	private ArrayList<String> img_urls ;
	private MyAdapter2 adapter2;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		question_id = getIntent().getStringExtra("question_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_message_details);
	}

	@Override
	protected void initView() {
		tvContent = (TextView) findViewById(R.id.activity_message_tv_content);
		tvCar = (TextView) findViewById(R.id.activity_message_tv_carname);
		tvOrderTime = (TextView) findViewById(R.id.activity_message_tv_ordertime);
		tvCheck = (TextView) findViewById(R.id.activity_message_tv_check);
		btnOnline = (Button) findViewById(R.id.activity_message_btn_onlinetalk);
		ivBack = (ImageView) findViewById(R.id.activity_message_details_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		gv = (MyGridView) findViewById(R.id.activity_message_gv);
	}

	@Override
	protected void registerListener() {
		btnOnline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(MessageDetailsActivity.this,
						AnswerOnlineResponseActivity.class);
				intent.putExtra("question_id", question_id);
				intent.putExtra("pre_id", 0 + "");
				startActivity(intent);
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MessageDetailsActivity.this, PicturePreviewActivity.class);
				intent.putExtra("url", img_urls.get(position));
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	protected void initData() {
		loadDatas();
	}

	private void loadDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(MessageDetailsActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("question_id", question_id);
		post.send(HttpMethod.POST, UrlUtils.postUrl
				+ UrlUtils.path_messageDetail, params,
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
								MessageDetails.class);
						int code = details.getCode();
						if (code == 1) {
							data = details.getData();
							tvContent.setText(data.getContent());
							tvCar.setText(data.getCar_name());
							tvOrderTime.setText("预约时间：" + data.getOrder_time());
							tvCheck.setText("维修项目:" + data.getChecked());
							if (data.getImg().size()!= 0) {
								img_urls = data.getQuestion_img();
								adapter2 = new MyAdapter2();
								adapter2.steDatas(img_urls);
								gv.setAdapter(adapter2);
							}
						} else {
							showShortToast("暂无数据");
						}
					}
				});
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
				convertView = View.inflate(MessageDetailsActivity.this, R.layout.item_select_photo_list, null);
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
			 utils = new BitmapUtils(MessageDetailsActivity.this);
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
			config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
			utils.display(holder.ivIcon, ivUrl, config);
		}
		class ViewHolder{
			ImageView ivIcon,ivDel;
		}
	}

}
