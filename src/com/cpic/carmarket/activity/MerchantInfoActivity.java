package com.cpic.carmarket.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.bean.MerchantInfo;
import com.cpic.carmarket.bean.MerchantInfoDataInfo;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.CircleImageView;
import com.cpic.carmarket.view.MyListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MerchantInfoActivity extends BaseActivity {

	private CircleImageView ivIcon;

	private MyListView mlv;

	private ImageView ivBack;
	private LinearLayout llIcon, llAddress, llTime, llChange, llMobile;

	private EditText etContent;

	private TextView tvAddress, tvTime, tvname, tvMobile;
	private Button btnSubmit;

	private ArrayList<MerchantInfoDataInfo> datas;

	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;

	private BitmapUtils utils;
	private Dialog dialog;

	private MercAdapter adapter;

	private BitmapDisplayConfig config;

	private TextView tvCamera, tvPhoto, tvBack;

	private static final int CAMERA = 1;
	private static final int ADDRESS = 2;
	private static final int CAR_LIST = 3;
	private static final int PHOTO = 0;

	private Uri cameraUri;
	private File cameraPic;
	private Intent intent;
	private PopupWindow pw, pwMobile;
	private int screenWidth;
	private String path;// 图片路径

	private String address;

	private TimePicker tpAm, tpPm;
	private Button submitTime;

	private String Latitude, Longitude;// 上个页面返回的经纬度

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_merchant_info);
	}

	@Override
	protected void initView() {
		llIcon = (LinearLayout) findViewById(R.id.activity_merchant_ll_icon);
		llAddress = (LinearLayout) findViewById(R.id.activity_merchant_ll_address);
		llTime = (LinearLayout) findViewById(R.id.activity_merchant_ll_time);
		llMobile = (LinearLayout) findViewById(R.id.activity_merchant_ll_mobile);
		ivIcon = (CircleImageView) findViewById(R.id.activity_merchant_iv_icon);
		ivBack = (ImageView) findViewById(R.id.activity_merchant_iv_back);
		llChange = (LinearLayout) findViewById(R.id.activity_merchant_ll_change);
		mlv = (MyListView) findViewById(R.id.activity_merchant_mlv);
		tvAddress = (TextView) findViewById(R.id.activity_merchant_tv_address);
		tvTime = (TextView) findViewById(R.id.activity_merchant_tv_time);
		tvname = (TextView) findViewById(R.id.activity_merchant_tv_name);
		tvMobile = (TextView) findViewById(R.id.activity_merchant_tv_mobile);
		dialog = ProgressDialogHandle.getProgressDialog(this, null);
		etContent = (EditText) findViewById(R.id.activity_merchant_et_store_intro);
		btnSubmit = (Button) findViewById(R.id.activity_merchant_btn_submit);
	}

	@Override
	protected void registerListener() {

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		llIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(v);
			}
		});
		/**
		 * 地址
		 */
		llAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(MerchantInfoActivity.this,
						DetailAddressActivity.class);
				startActivityForResult(intent, ADDRESS);
				;
			}
		});
		llTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePop(v);
			}
		});
		/**
		 * 更改服务车型
		 */
		llChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(MerchantInfoActivity.this,
						ServiceCarTypeActivity.class);
				startActivityForResult(intent, CAR_LIST);
			}
		});

		/**
		 * 更改手机号码
		 */
		llMobile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMobilePop(v);
			}

		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitDatas();
			}
		});
	}

	private void submitDatas() {

		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("address", tvAddress.getText().toString());
		if (Latitude == null) {
			Latitude = "0";
		}
		if (Longitude == null) {
			Longitude = "0";
		}
		sp = PreferenceManager
				.getDefaultSharedPreferences(MerchantInfoActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("lat", Latitude);
		params.addBodyParameter("lng", Longitude);
		params.addBodyParameter("company_desc", etContent.getText().toString());
		params.addBodyParameter("tel", tvMobile.getText().toString());
		params.addBodyParameter("on_time", tvTime.getText().toString());
		params.addBodyParameter("business", TasktoJson(datas));
		Log.i("oye", TasktoJson(datas));
		String url = UrlUtils.postUrl + UrlUtils.path_modifyInfo;
		post.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			private Handler handler;

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
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getIntValue("code");
				if (code == 1) {
					showRegisterSuccess();
					new Handler().postDelayed(new Runnable(){   

					    public void run() {   
					    	MerchantInfoActivity.this.finish();
					    }   

					 }, 500);   
				}
			}
		});
	}


	private void showRegisterSuccess() {
		View view = View.inflate(MerchantInfoActivity.this,
				R.layout.pop_wait_submit, null);
		pwMobile = new PopupWindow(view, screenWidth * 3 / 5,
				LayoutParams.WRAP_CONTENT);
		pwMobile.setFocusable(true);
		WindowManager.LayoutParams params = MerchantInfoActivity.this
				.getWindow().getAttributes();
		params.alpha = 0.7f;
		MerchantInfoActivity.this.getWindow().setAttributes(params);
		pwMobile.setAnimationStyle(R.style.pw_anim_style1);
		pwMobile.setBackgroundDrawable(new ColorDrawable());
		pwMobile.setOutsideTouchable(true);

		pwMobile.showAtLocation(view, Gravity.TOP, 0, 350);
		pwMobile.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MerchantInfoActivity.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				MerchantInfoActivity.this.getWindow().setAttributes(params);
			}
		});
	}

	/**
	 * 将数组转化成json字符串
	 */
	public String TasktoJson(List<MerchantInfoDataInfo> data) {
		String jsonresult = "";
		JSONArray array = new JSONArray();
		for (int i = 0; i < data.size(); i++) {
			org.json.JSONObject petobj = new org.json.JSONObject();
			try {
				petobj.put("dim_name", data.get(i).getDim_name());
				petobj.put("car_category", data.get(i).getCar_category());
				petobj.put("category_name", data.get(i).getCategory_name());
				array.put(petobj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		jsonresult = array.toString();
		return jsonresult;
	}

	/**
	 * 修改号码的淡出匡
	 */
	private void showMobilePop(View v) {
		View view = View.inflate(MerchantInfoActivity.this,
				R.layout.popup_mobile, null);
		final EditText et = (EditText) view.findViewById(R.id.popup_mobile_et);
		Button btnDel = (Button) view.findViewById(R.id.popup_mobile_btn_del);
		Button btnEn = (Button) view.findViewById(R.id.popup_mobile_btn_ensure);

		pwMobile = new PopupWindow(view, screenWidth * 4 / 5,
				LayoutParams.WRAP_CONTENT);
		pwMobile.setFocusable(true);
		WindowManager.LayoutParams params = MerchantInfoActivity.this
				.getWindow().getAttributes();
		params.alpha = 0.7f;
		MerchantInfoActivity.this.getWindow().setAttributes(params);

		pwMobile.setBackgroundDrawable(new ColorDrawable());
		pwMobile.setOutsideTouchable(true);

		pwMobile.showAtLocation(v, Gravity.CENTER, 0, 0);

		btnDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwMobile.dismiss();
			}
		});

		btnEn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvMobile.setText(et.getText().toString());
				pwMobile.dismiss();
			}
		});
		pwMobile.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MerchantInfoActivity.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				MerchantInfoActivity.this.getWindow().setAttributes(params);
			}
		});

	}

	private void showTimePop(View v) {
		View view = View.inflate(MerchantInfoActivity.this,
				R.layout.popupwindow_time_pick, null);
		tpAm = (TimePicker) view.findViewById(R.id.popupwindow_time_am);
		tpPm = (TimePicker) view.findViewById(R.id.popupwindow_time_pm);
		submitTime = (Button) view
				.findViewById(R.id.popupwindow_time_btn_ensure);
		tpAm.setIs24HourView(true);
		tpPm.setIs24HourView(true);

		submitTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tpAm.getCurrentHour() > tpPm.getCurrentHour()) {
					showShortToast("开始时间不得大于结束时间");
					return;
				}
				if (tpAm.getCurrentHour() == tpPm.getCurrentHour()) {
					if (tpAm.getCurrentMinute() >= tpPm.getCurrentMinute()) {
						showShortToast("开始时间不得大于结束时间");
						return;
					}
				}
				StringBuilder sb = new StringBuilder();
				String strAmH = "";
				String strAmM = "";
				String strPmH = "";
				String strPmM = "";

				if (tpAm.getCurrentHour().toString().length() == 1) {
					strAmH = "0" + tpAm.getCurrentHour().toString();
				} else {
					strAmH = tpAm.getCurrentHour().toString();
				}
				if (tpAm.getCurrentMinute().toString().length() == 1) {
					strAmM = "0" + tpAm.getCurrentMinute().toString();
				} else {
					strAmM = tpAm.getCurrentMinute().toString();
				}
				if (tpPm.getCurrentHour().toString().length() == 1) {
					strPmH = "0" + tpPm.getCurrentHour().toString();
				} else {
					strPmH = tpPm.getCurrentHour().toString();
				}
				if (tpPm.getCurrentMinute().toString().length() == 1) {
					strPmM = "0" + tpPm.getCurrentMinute().toString();
				} else {
					strPmM = tpPm.getCurrentMinute().toString();
				}

				sb.append(strAmH + ":");
				sb.append(strAmM + "~");
				sb.append(strPmH + ":");
				sb.append(strPmM);
				tvTime.setText(sb.toString());
				pw.dismiss();
			}
		});

		pw = new PopupWindow(view, screenWidth, LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params = MerchantInfoActivity.this
				.getWindow().getAttributes();
		params.alpha = 0.7f;
		MerchantInfoActivity.this.getWindow().setAttributes(params);

		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(true);

		pw.setAnimationStyle(R.style.pw_anim_style);

		pw.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

		pw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MerchantInfoActivity.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				MerchantInfoActivity.this.getWindow().setAttributes(params);
			}
		});

	}

	@Override
	protected void initData() {
		loadDatas();
	}

	private void loadDatas() {
		sp = PreferenceManager
				.getDefaultSharedPreferences(MerchantInfoActivity.this);
		String token = sp.getString("token", "");
		post = new HttpUtils();
		params = new RequestParams();
		params.addBodyParameter("token", token);
		String url = UrlUtils.postUrl + UrlUtils.path_merchantInfo;
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
				if (dialog != null) {
					dialog.dismiss();
				}
				showShortToast("获取数据失败，请检查网络连接！");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				MerchantInfo info = JSONObject.parseObject(arg0.result,
						MerchantInfo.class);
				int code = info.getCode();
				if (code == 1) {
					datas = info.getData().getBusiness();
					adapter = new MercAdapter();
					adapter.setDatas(datas);
					mlv.setAdapter(adapter);
					tvAddress.setText(info.getData().getAddress());
					tvname.setText(info.getData().getCompany_name());
					tvMobile.setText(info.getData().getTel());
					tvTime.setText(info.getData().getOn_time());
					etContent.setText(info.getData().getCompany_desc());
					String img_url = info.getData().getStore_img();
					loadIvIcon(img_url);
				} else {
					showShortToast("获取数据失败");
				}
			}
		});
	}

	/**
	 * 下载门面
	 * 
	 * @param img_url
	 */
	private void loadIvIcon(String img_url) {
		config = new BitmapDisplayConfig();
		utils = new BitmapUtils(MerchantInfoActivity.this);
		config.setLoadingDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		config.setLoadFailedDrawable(getResources().getDrawable(
				R.drawable.empty_photo));
		utils.display(ivIcon, img_url, config);
	}

	private void showPopupWindow(View v) {
		View view = View.inflate(MerchantInfoActivity.this,
				R.layout.popupwindow_1, null);
		tvCamera = (TextView) view.findViewById(R.id.btn_camera);
		tvPhoto = (TextView) view.findViewById(R.id.btn_photo);
		tvBack = (TextView) view.findViewById(R.id.btn_back);
		tvCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFromCamera();
				pw.dismiss();
			}
		});
		tvPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFromPhoto();
				pw.dismiss();
			}
		});
		tvBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});

		pw = new PopupWindow(view, screenWidth * 99 / 100,
				LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params = MerchantInfoActivity.this
				.getWindow().getAttributes();
		params.alpha = 0.7f;
		MerchantInfoActivity.this.getWindow().setAttributes(params);

		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(true);

		pw.setAnimationStyle(R.style.pw_anim_style);

		pw.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

		pw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = MerchantInfoActivity.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				MerchantInfoActivity.this.getWindow().setAttributes(params);
			}
		});

	}

	/**
	 * 相机中调用
	 */
	private void getFromCamera() {
		// 通过Intent调用系统相机
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraPic = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/store.jpg");
		cameraUri = Uri.fromFile(cameraPic);
		// 指定照片拍摄后的存储位置
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, CAMERA);
	}

	private void getFromPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO);
	}

	/**
	 * 通过获取文件名上传图片的文件流
	 * 
	 * @param file
	 */
	private void upLoadUserIcon(File file) {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager
				.getDefaultSharedPreferences(MerchantInfoActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("poster", file);
		String url = UrlUtils.postUrl + UrlUtils.path_uploadStoreImg;
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
				showShortToast("上传失败,请检查网络状况");
				if (dialog == null) {
					dialog.dismiss();
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getInteger("code");
				if (code == 0) {
					showShortToast("上传失败,请重试");
				} else if (code == 1) {
					showShortToast("上传成功");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ADDRESS && resultCode == RESULT_OK) {
			if (data != null) {
				address = data.getStringExtra("address");
				tvAddress.setText(address);
			}
		}

		if (requestCode == CAR_LIST && resultCode == RESULT_OK) {
			if (data != null) {
				datas = data.getParcelableArrayListExtra("car_list");
				adapter.setDatas(datas);
				adapter.notifyDataSetChanged();
			}
		}

		if (requestCode == CAMERA) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/store.jpg";
			if (!cameraUri.getPath().isEmpty()) {
				Bitmap temp = BitmapFactory.decodeFile(cameraUri.getPath());
				Bitmap bitmap = big(temp, 60, 60);
				ivIcon.setImageBitmap(bitmap);
				upLoadUserIcon(new File(path));
			}

			// upLoadUserIcon(new File(Environment.getExternalStorageDirectory()
			// .getAbsolutePath() + "/usericon.PNG"));
		} else if (requestCode == PHOTO) {
			if (data != null) {
				Uri uri = data.getData();
				// 因为相册出返回的uri路径是ContentProvider开放的路径，不是直接的sd卡具体路径
				// 因此无法通过decodeFile方法解析图片
				// 必须通过ContentResolver对象读取图片
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap b = MediaStore.Images.Media.getBitmap(cr, uri);
					Bitmap bitmap = big(b, 60, 60);
					bitmap.getByteCount();
					ivIcon.setImageBitmap(bitmap);
					// 这里开始的第二部分，获取图片的路径：
					String[] proj = { MediaStore.Images.Media.DATA };
					// 好像是android多媒体数据库的封装接口，具体的看Android文档
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径
					path = cursor.getString(column_index);
					// Log.i("oye", path);
					// 上传头像
					// upLoadUserIcon(new File(path));
					upLoadUserIcon(new File(path));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Bitmap big(Bitmap b, float x, float y) {
		int w = b.getWidth();
		int h = b.getHeight();
		float sx = (float) x / w;// 要强制转换，不转换我的在这总是死掉。
		float sy = (float) y / h;
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
		return resizeBmp;
	}

	public class MercAdapter extends BaseAdapter {

		private ArrayList<MerchantInfoDataInfo> datas;

		public void setDatas(ArrayList<MerchantInfoDataInfo> datas) {
			this.datas = datas;
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(MerchantInfoActivity.this,
						R.layout.item_merchant_select_list, null);
				holder = new ViewHolder();
				holder.tvCar = (TextView) convertView
						.findViewById(R.id.item_merchant_tv_car);
				holder.tvCheck = (TextView) convertView
						.findViewById(R.id.item_merchant_tvcheck);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvCar.setText(datas.get(position).getCategory_name());
			holder.tvCheck.setText(datas.get(position).getDim_name());
			return convertView;
		}

		class ViewHolder {
			TextView tvCheck, tvCar;
		}
	}
}
