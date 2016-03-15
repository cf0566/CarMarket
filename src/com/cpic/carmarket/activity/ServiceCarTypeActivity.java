package com.cpic.carmarket.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.MerchantInfoDataInfo;
import com.cpic.carmarket.bean.PostCarListInfo;

public class ServiceCarTypeActivity extends BaseActivity {

	private CheckBox boxWash, boxMeirong, boxXiufu, boxBaoyang, boxBanpen,
			boxZhenduan, boxLuntai, boxErshou;
	private TextView tvMeirong, tvXiufu, tvBaoyang, tvBanpen, tvZhenduan,
			tvLuntai;
	private Button btnEnsure;

	private ImageView ivBack;
	private Intent intent;
	
	private final static int MEIRONG = 0;
	private final static int XIUFU = 1;
	private final static int BAOYANG = 2;
	private final static int BANPEN = 3;
	private final static int ZHENDUAN = 4;
	private final static int LUNTAI = 5;
	
	private StringBuilder sbMei = new StringBuilder();
	private StringBuilder sbXiu = new StringBuilder();
	private StringBuilder sbBao = new StringBuilder();
	private StringBuilder sbBan = new StringBuilder();
	private StringBuilder sbZhen = new StringBuilder();
	private StringBuilder sbLun = new StringBuilder();
	
	private StringBuilder sbMei_id = new StringBuilder();
	private StringBuilder sbXiu_id = new StringBuilder();
	private StringBuilder sbBao_id = new StringBuilder();
	private StringBuilder sbBan_id = new StringBuilder();
	private StringBuilder sbZhen_id = new StringBuilder();
	private StringBuilder sbLun_id = new StringBuilder();
	
	private String mei,xiu,bao,ban,zhen,lun;
	private String mei_id,xiu_id,bao_id,ban_id,zhen_id,lun_id;
	
	private ArrayList<MerchantInfoDataInfo> datas;
	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_service_car_type);
	}

	@Override
	protected void initView() {
		boxWash = (CheckBox) findViewById(R.id.activity_service_car_cbox_wash_car);
		boxMeirong = (CheckBox) findViewById(R.id.activity_service_car_cbox_meirong);
		boxXiufu = (CheckBox) findViewById(R.id.activity_service_car_cbox_xiufu);
		boxBaoyang = (CheckBox) findViewById(R.id.activity_service_car_cbox_baoyang);
		boxBanpen = (CheckBox) findViewById(R.id.activity_service_car_cbox_banpen);
		boxZhenduan = (CheckBox) findViewById(R.id.activity_service_car_cbox_zhenduan);
		boxLuntai = (CheckBox) findViewById(R.id.activity_service_car_cbox_luntai);
		boxErshou = (CheckBox) findViewById(R.id.activity_service_car_cbox_ershou);

		tvMeirong = (TextView) findViewById(R.id.activity_service_car_tv_meirong);
		tvXiufu = (TextView) findViewById(R.id.activity_service_car_tv_xiufu);
		tvBaoyang = (TextView) findViewById(R.id.activity_service_car_tv_baoyang);
		tvBanpen = (TextView) findViewById(R.id.activity_service_car_tv_banpen);
		tvZhenduan = (TextView) findViewById(R.id.activity_service_car_tv_zhenduan);
		tvLuntai = (TextView) findViewById(R.id.activity_service_car_tv_luntai);

		btnEnsure = (Button) findViewById(R.id.activity_service_car_btn_ensure);
		ivBack = (ImageView) findViewById(R.id.activity_service_car_iv_back);
	}

	@Override
	protected void registerListener() {

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		tvMeirong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxMeirong.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, MEIRONG);
			}
		});
		tvXiufu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxXiufu.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, XIUFU);
			}
		});
		tvBaoyang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxBaoyang.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, BAOYANG);
			}
		});
		tvBanpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxBanpen.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, BANPEN);
			}
		});
		tvZhenduan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxZhenduan.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, ZHENDUAN);
			}
		});
		tvLuntai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!boxLuntai.isChecked()) {
					showShortToast("请先选择服务类型");
					return;
				}
				intent = new Intent(ServiceCarTypeActivity.this, CarListActivity.class);
				startActivityForResult(intent, LUNTAI);
			}
		});
		
		/**
		 * 确认服务类型
		 */
		btnEnsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ensureDatas();
			}

		});
	}
	
	private void ensureDatas() {
		
		datas = new ArrayList<MerchantInfoDataInfo>();
		if (boxWash.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxWash.getText().toString(), "", ""));
		}
		if (boxMeirong.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxMeirong.getText().toString(), tvMeirong.getText().toString(), sbMei_id.toString()));
		}
		if (boxXiufu.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxXiufu.getText().toString(), tvXiufu.getText().toString(), sbXiu_id.toString()));
		}
		if (boxBaoyang.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxBaoyang.getText().toString(), tvBaoyang.getText().toString(), sbBao_id.toString()));
		}
		if (boxBanpen.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxBanpen.getText().toString(), tvBanpen.getText().toString(), sbBan_id.toString()));
		}
		if (boxZhenduan.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxZhenduan.getText().toString(), tvZhenduan.getText().toString(), sbZhen_id.toString()));
		}
		if (boxLuntai.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxLuntai.getText().toString(), tvLuntai.getText().toString(), sbLun_id.toString()));
		}
		if (boxErshou.isChecked()) {
			datas.add(new MerchantInfoDataInfo("",boxErshou.getText().toString(), "", ""));
		}
		if (datas.size() == 0) {
			showShortToast("服务类型不得为空");
		}else{
			intent = new Intent();
			intent.putParcelableArrayListExtra("car_list", datas);
			setResult(RESULT_OK, intent);
			finish(); 
		}
	}
	
	
	@Override
	protected void initData() {
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case MEIRONG:
				if (sbMei.toString() != null && sbMei.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				/**
				 * 车型拼接
				 */
				if (mei == null && data.getStringExtra("car_name")!=null) {
					mei = data.getStringExtra("car_name");
					sbMei.append(mei);
				}else{
					mei = data.getStringExtra("car_name");
					sbMei.append(","+mei);
				}
				/**
				 * car_id拼接
				 */
				if (mei_id == null && data.getStringExtra("car_id")!=null) {
					mei_id = data.getStringExtra("car_id");
					sbMei_id.append(mei_id);
				}else{
					mei_id = data.getStringExtra("car_id");
					sbMei_id.append(","+mei_id);
				}
				tvMeirong.setText(sbMei.toString());
				break;
			case XIUFU:
				if (sbXiu.toString() != null && sbXiu.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				if (xiu == null && data.getStringExtra("car_name")!=null) {
					xiu = data.getStringExtra("car_name");
					sbXiu.append(xiu);
				}else{
					xiu = data.getStringExtra("car_name");
					sbXiu.append(","+xiu);
				}
				if (xiu_id == null && data.getStringExtra("car_id")!=null) {
					xiu_id = data.getStringExtra("car_id");
					sbXiu_id.append(xiu_id);
				}else{
					xiu_id = data.getStringExtra("car_id");
					sbXiu_id.append(","+xiu_id);
				}
				tvXiufu.setText(sbXiu.toString());
				break;
			case BAOYANG:
				if (sbBao.toString() != null && sbBao.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				if (bao == null && data.getStringExtra("car_name")!=null) {
					bao = data.getStringExtra("car_name");
					sbBao.append(bao);
				}else{
					bao = data.getStringExtra("car_name");
					sbBao.append(","+bao);
				}
				if (bao_id == null && data.getStringExtra("car_id")!=null) {
					bao_id = data.getStringExtra("car_id");
					sbBao_id.append(bao_id);
				}else{
					bao_id = data.getStringExtra("car_id");
					sbBao_id.append(","+bao_id);
				}
				tvBaoyang.setText(sbBao.toString());
				break;
			case BANPEN:
				if (sbBan.toString() != null && sbBan.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				if (ban == null && data.getStringExtra("car_name")!=null) {
					ban = data.getStringExtra("car_name");
					sbBan.append(ban);
				}else{
					ban = data.getStringExtra("car_name");
					sbBan.append(","+ban);
				}
				if (ban_id == null && data.getStringExtra("car_id")!=null) {
					ban_id = data.getStringExtra("car_id");
					sbBan_id.append(ban_id);
				}else{
					ban_id = data.getStringExtra("car_id");
					sbBan_id.append(","+ban_id);
				}
				tvBanpen.setText(sbBan.toString());
				break;
			case ZHENDUAN:
				if (sbZhen.toString() != null && sbZhen.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				if (zhen == null && data.getStringExtra("car_name")!=null) {
					zhen = data.getStringExtra("car_name");
					sbZhen.append(zhen);
				}else{
					zhen = data.getStringExtra("car_name");
					sbZhen.append(","+zhen);
				}
				if (zhen_id == null && data.getStringExtra("car_id")!=null) {
					zhen_id = data.getStringExtra("car_id");
					sbZhen_id.append(zhen_id);
				}else{
					zhen_id = data.getStringExtra("car_id");
					sbZhen_id.append(","+zhen_id);
				}
				tvZhenduan.setText(sbZhen.toString());
				break;
			case LUNTAI:
				if (sbLun.toString() != null && sbLun.toString().indexOf((data.getStringExtra("car_name")))!=-1) {
					showShortToast("该车型已选择");
					return;
				}
				if (lun == null && data.getStringExtra("car_name")!=null) {
					lun = data.getStringExtra("car_name");
					sbLun.append(lun);
				}else{
					lun = data.getStringExtra("car_name");
					sbLun.append(","+lun);
				}
				if (lun_id == null && data.getStringExtra("car_id")!=null) {
					lun_id = data.getStringExtra("car_id");
					sbLun_id.append(lun_id);
				}else{
					lun_id = data.getStringExtra("car_id");
					sbLun_id.append(","+lun_id);
				}
				tvLuntai.setText(sbLun.toString());
				break;

			default:
				break;
			}
		}
		
	}

}
