package com.cpic.carmarket.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.AnswerDetailsActivity;
import com.cpic.carmarket.activity.WashCarTypeActivity;
import com.cpic.carmarket.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public class MineFragment  extends Fragment{

	private CircleImageView ivIcon;
	private BitmapDisplayConfig config;
	private BitmapUtils utils;
	private String img_url,company_name,acount,is_approve;
	private SharedPreferences sp ;
	private Intent intent;
	
	private LinearLayout llComInfo,llCall;
	private TextView tvCompany,tvAcount,tvWash,tvCount,tvMoney,tvAdvice,tvBackLogin,tvIsAgree;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, null);
		
		initView(view);
		
		registerListener();
		
		return view;
	}
	
	private void registerListener() {
		/**
		 * 商家详情
		 */
		llComInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		/**
		 * 拨打热线
		 */
		llCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				String[] tel = { "400-000-000  呼叫>>" };
				builder.setTitle("联系客服");
				builder.setItems(tel,new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-000-000"));
								startActivity(intent);
							}
						});
				builder.show();
			}
		});
		/**
		 * 洗车服务及报价
		 */
		tvWash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), WashCarTypeActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 财务管理
		 */
		tvCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		/**
		 * 交纳保证金
		 */
		tvMoney.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		/**
		 * 意见反馈
		 */
		tvAdvice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		/**
		 * 退出登录
		 */
		tvBackLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	private void initView(View view) {
		ivIcon = (CircleImageView) view.findViewById(R.id.fragment_mine_iv_icon);
		tvCompany = (TextView) view.findViewById(R.id.fragment_mine_tv_user_name);
		tvAcount = (TextView) view.findViewById(R.id.fragment_mine_tv_user_acount);
		tvWash = (TextView) view.findViewById(R.id.fragment_mine_tv_wash);
		tvCount = (TextView) view.findViewById(R.id.fragment_mine_tv_count);
		tvWash = (TextView) view.findViewById(R.id.fragment_mine_tv_wash);
		tvMoney = (TextView) view.findViewById(R.id.fragment_mine_tv_money);
		tvAdvice = (TextView) view.findViewById(R.id.fragment_mine_tv_advice);
		tvBackLogin = (TextView) view.findViewById(R.id.mine_fragment_tv_back_login);
		llComInfo = (LinearLayout) view.findViewById(R.id.fragment_mine_ll_com_info);
		llCall = (LinearLayout) view.findViewById(R.id.fragment_mine_ll_call);
		tvIsAgree = (TextView) view.findViewById(R.id.fragment_mine_tv_isagree);
	}

	@Override
	public void onResume() {
		super.onResume();
		initDatas();
	}

	private void initDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		img_url = sp.getString("logo", "");
		company_name = sp.getString("company_name", "");
		acount = sp.getString("acount", "");
		is_approve = sp.getString("is_approve", "");
		loadIvIcon(img_url);
		tvCompany.setText(company_name);
		tvAcount.setText("账号："+acount);
		if ("0".equals(is_approve)) {
			tvIsAgree.setText("未审核");
		}else if("1".equals(is_approve)){
			tvIsAgree.setText("已通过");
		}
	}
	
	private void loadIvIcon(String img_url) {
		config = new BitmapDisplayConfig();
		utils = new BitmapUtils(getActivity());
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.empty_photo));
		utils.display(ivIcon, img_url, config);
	}
}
