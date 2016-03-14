package com.cpic.carmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;

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
	
	private String mei,xiu,bao,ban,zhen,lun;
	
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
				if (mei != null && mei.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				mei = data.getStringExtra("car_name");
				sbMei.append(mei);
				tvMeirong.setText(sbMei.toString());
				break;
			case XIUFU:
				if (xiu != null && xiu.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				xiu = data.getStringExtra("car_name");
				sbXiu.append(xiu);
				tvXiufu.setText(sbXiu.toString());
				break;
			case BAOYANG:
				if (bao != null && bao.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				bao = data.getStringExtra("car_name");
				sbBao.append(bao);
				tvBaoyang.setText(sbBao.toString());
				break;
			case BANPEN:
				if (ban != null && ban.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				ban = data.getStringExtra("car_name");
				sbBan.append(ban);
				tvBanpen.setText(sbBan.toString());
				break;
			case ZHENDUAN:
				if (zhen != null && zhen.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				zhen = data.getStringExtra("car_name");
				sbZhen.append(zhen);
				tvZhenduan.setText(sbZhen.toString());
				break;
			case LUNTAI:
				if (lun != null && lun.contentEquals(data.getStringExtra("car_name"))) {
					showShortToast("该车型已选择");
					return;
				}
				lun = data.getStringExtra("car_name");
				sbLun.append(lun);
				tvLuntai.setText(sbLun.toString());
				break;

			default:
				break;
			}
		}
		
	}

}
