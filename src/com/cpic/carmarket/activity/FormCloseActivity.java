package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.app.Dialog;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.FormCloseData;
import com.cpic.carmarket.bean.FormCloseDataInfo;
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

public class FormCloseActivity extends BaseActivity {

	private TextView tvCar, tvUser, tvPhone, tvAddress, tvType, tvPrice,
			tvTime, tvPay, tvNum, tvCreat,tvBackTime,tvNotAgreeTime,tvReason,tvIsAgree;
	
	
	private MyGridView gv;
	private MyAdapter adapter;
	private ArrayList<String> img_url ;
	
	private LinearLayout ll;
	
	private ImageView ivBack,ivReason;
	
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	private FormCloseDataInfo data;
	
	private String order_id,url;

	private BitmapDisplayConfig config;

	private BitmapUtils utils;
	
	private Intent intent;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		order_id = getIntent().getStringExtra("order_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_form_close);
	}
	
	@Override
	protected void initView() {
		tvCar = (TextView) findViewById(R.id.activity_form_close_tv_car);
		tvUser = (TextView) findViewById(R.id.activity_form_close_tv_user);
		tvPhone = (TextView) findViewById(R.id.activity_form_close_tv_phone);
		tvAddress = (TextView) findViewById(R.id.activity_form_close_tv_address);
		tvType = (TextView) findViewById(R.id.activity_form_close_tv_type);
		tvPrice = (TextView) findViewById(R.id.activity_form_close_tv_price);
		tvTime = (TextView) findViewById(R.id.activity_form_close_tv_time);
		tvPay = (TextView) findViewById(R.id.activity_form_close_tv_pay);
		tvNum = (TextView) findViewById(R.id.activity_form_close_tv_num);
		tvCreat = (TextView) findViewById(R.id.activity_form_close_tv_creat_time);
		gv = (MyGridView) findViewById(R.id.activity_form_close_gv);
		tvBackTime = (TextView) findViewById(R.id.activity_form_close_tv_back_time);
		tvNotAgreeTime = (TextView) findViewById(R.id.activity_form_close_tv_not_agree_time);
		tvReason = (TextView) findViewById(R.id.activity_form_close_tv_reason);
		tvIsAgree =  (TextView) findViewById(R.id.activity_form_close_tv_isagree);
		ll = (LinearLayout) findViewById(R.id.activity_form_close_ll);
		ivBack = (ImageView) findViewById(R.id.activity_form_close_iv_back);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
	}

	@Override
	protected void registerListener() {
		
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
				Intent intent = new Intent(FormCloseActivity.this, PicturePreviewActivity.class);
				intent.putExtra("url", img_url.get(position));
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void initData() {
		sp = PreferenceManager.getDefaultSharedPreferences(FormCloseActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", order_id);
		String url = UrlUtils.postUrl+UrlUtils.path_orderDetail;
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
				showShortToast(UrlUtils.loading_failure);
				if (dialog != null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				FormCloseData obj = JSONObject.parseObject(arg0.result, FormCloseData.class);
				int code= obj.getCode();
				if (code == 1) {
					data = obj.getData();
					loadDatas();
				}else{
					showShortToast("获取数据失败"+code);
				}
			}
		});
	}
	private void loadDatas() {
		tvCar.setText(data.getCar_name());
		tvUser.setText("收件人："+data.getUser_name());
		tvPhone.setText(data.getMobile());
		tvAddress.setText(data.getOrder_address());
		tvType.setText(data.getDim_name());
		tvPrice.setText(data.getOrder_amount());
		tvTime.setText(data.getOrder_date());
		if ("1".equals(data.getPay_id())) {
			tvPay.setText("支付宝支付");
		}else{
			tvPay.setText("微信支付");
		}
		tvNum.setText(data.getOrder_sn());
		tvCreat.setText(data.getCreate_date());
		if ("1".equals(data.getBack().getBack_status())) {
			ll.setVisibility(View.INVISIBLE);
			tvBackTime.setText(data.getBack().getMerchant_time());
		}else if ("2".equals(data.getBack().getBack_status())){
			tvBackTime.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
			tvIsAgree.setText("商家不同意退款原因");
			tvNotAgreeTime.setText(data.getBack().getMerchant_time());
			tvReason.setText(data.getBack().getMerchant_reason());
			if (data.getBack().getMerchant_img().size()!=0) {
				img_url = data.getBack().getMerchant_img();
				adapter = new MyAdapter();
				adapter.steDatas(img_url);
				gv.setAdapter(adapter);
			}
		}
	}
	
	public class MyAdapter extends BaseAdapter{

		private ArrayList<String> img_url;
		
		public void steDatas(ArrayList<String> img_url){
			this.img_url = img_url;
		}
		
		@Override
		public int getCount() {
			return img_url == null ? 0 :img_url.size();
		}

		@Override
		public Object getItem(int position) {
			return img_url.get(position);
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
				convertView = View.inflate(FormCloseActivity.this, R.layout.item_select_photo_list, null);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_photo_iv_icon);
				holder.ivDel = (ImageView) convertView.findViewById(R.id.item_photo_iv_del);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ivDel.setVisibility(View.INVISIBLE);
			loadUserIcon(img_url.get(position), holder);
			return convertView;
		}
		private void loadUserIcon(String ivUrl,ViewHolder holder) {
			config = new BitmapDisplayConfig();
			 utils = new BitmapUtils(FormCloseActivity.this);
			config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
			config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
			utils.display(holder.ivIcon, ivUrl, config);
		}
		
		class ViewHolder{
			ImageView ivIcon,ivDel;
		}
	}

}
