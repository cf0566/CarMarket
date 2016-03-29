package com.cpic.carmarket.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.AddressDetailsInfo;
import com.cpic.carmarket.bean.AddressDetailsResults;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 获取当前位置信息
 * 
 * @author MBENBEN
 *
 */
public class DetailAddressActivity extends BaseActivity {

	private LocationManager locationManager;
	private String locationProvider;
	private EditText et;
	private String Latitude,Longitude;
	
	private HttpUtils post;
	private SharedPreferences sp;
	private Dialog dialog;
	private AddressDetailsResults data;
	private Intent intent;
	
	private ImageView ivBack;
	
	private Button btnSubmit;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {

	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_details_address);
	}

	@Override
	protected void initView() {
		et = (EditText) findViewById(R.id.activity_details_address_et);
		dialog = ProgressDialogHandle.getProgressDialog(DetailAddressActivity.this, null);
		btnSubmit = (Button) findViewById(R.id.activity_details_address_btn_submit);
		ivBack = (ImageView) findViewById(R.id.activity_address_iv_back);
	}

	@Override
	protected void registerListener() {
		/**
		 * 确认提交地址
		 */
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.putExtra("address", et.getText().toString());
				intent.putExtra("Latitude", Latitude);
				intent.putExtra("Longitude", Longitude);
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void initData() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 获取所有可用的位置提供器
		List<String> providers = locationManager.getProviders(true);
		if (providers.contains(LocationManager.GPS_PROVIDER)) {
			// 如果是GPS
			locationProvider = LocationManager.GPS_PROVIDER;
		} else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
			// 如果是Network
			locationProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
			return;
		}
		// 获取Location
		Location location = locationManager
				.getLastKnownLocation(locationProvider);
		if (location != null) {
			// 不为空,显示地理位置经纬度
			showLocation(location);
		}
		// 监视地理位置变化
		locationManager.requestLocationUpdates(locationProvider, 3000, 1,locationListener);
		loadData();
	}

	/**
	 * 显示地理位置经度和纬度信息
	 * 
	 * @param location
	 */
	private void showLocation(Location location) {
		Latitude = location.getLatitude()+"";
		Longitude = location.getLongitude()+"";
	}
	
	/**
	 * 根据网络经纬度获取网络地址
	 */
	private void loadData() {
		post = new HttpUtils();
		String url = "http://maps.google.cn/maps/api/geocode/json?latlng="+Latitude+","+Longitude+"&sensor=true&language=zh-CN";
		post.send(HttpMethod.GET, url, new RequestCallBack<String>() {

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
				data = JSONObject.parseObject(arg0.result, AddressDetailsResults.class);
				ArrayList<AddressDetailsInfo> list = data.getResults();
				String str = list.get(0).getFormatted_address();
				String result = str.substring(0, str.indexOf(" ")); 
				et.setText(result);
			}
		});
	}

	/**
	 * LocationListern监听器 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
	 */

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle arg2) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location location) {
			// 如果位置发生变化,重新显示
			showLocation(location);

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			// 移除监听器
			locationManager.removeUpdates(locationListener);
		}
	}

}
