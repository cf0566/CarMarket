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
	private double latitude ;
	private double longitude ;
	
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
				intent.putExtra("Latitude", latitude+"");
				intent.putExtra("Longitude", longitude+"");
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
		  if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
	            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
	            if(location != null){  
	                latitude = location.getLatitude();  
	                longitude = location.getLongitude();  
	                }  
	        }else{  
	            LocationListener locationListener = new LocationListener() {  
	                  
	                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
	                @Override  
	                public void onStatusChanged(String provider, int status, Bundle extras) {  
	                      
	                }  
	                  
	                // Provider被enable时触发此函数，比如GPS被打开  
	                @Override  
	                public void onProviderEnabled(String provider) {  
	                      
	                }  
	                  
	                // Provider被disable时触发此函数，比如GPS被关闭   
	                @Override  
	                public void onProviderDisabled(String provider) {  
	                      
	                }  
	                  
	                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
	                @Override  
	                public void onLocationChanged(Location location) {  
	                    if (location != null) {     
	                        Log.e("Map", "Location changed : Lat: "    
	                        + location.getLatitude() + " Lng: "    
	                        + location.getLongitude());     
	                    }  
	                }  
	            };  
	            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
	            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
	            if(location != null){     
	                latitude = location.getLatitude(); //经度     
	                longitude = location.getLongitude(); //纬度  
	            }     
	        }  
		  
		if ("0.0".equals(latitude+"")||"0.0".equals(longitude+"")) {
			  showShortToast("定位失败，请检查GPS是否打开");
			  latitude = 0;
			  longitude = 0;
		}else if (latitude!=0.0 ||longitude !=0.0 ) {
			loadData();
		}
	}

	
	
	/**
	 * 根据网络经纬度获取网络地址
	 */
	private void loadData() {
		post = new HttpUtils();
		String url = "http://maps.google.cn/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true&language=zh-CN";
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


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
