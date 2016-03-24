package com.cpic.carmarket.fragment;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.activity.AdviceActivity;
import com.cpic.carmarket.activity.LoginActivity;
import com.cpic.carmarket.activity.MerchantInfoActivity;
import com.cpic.carmarket.activity.MoneyManagerActivity;
import com.cpic.carmarket.activity.PayProtectMoneyActivity;
import com.cpic.carmarket.activity.WashCarTypeActivity;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.cpic.carmarket.view.CircleImageView;
import com.easemob.chat.EMChatManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MineFragment extends Fragment {

	private CircleImageView ivIcon;
	private BitmapDisplayConfig config;
	private BitmapUtils utils;
	private String img_url, company_name, acount, is_approve;
	private SharedPreferences sp;
	private Intent intent;

	private LinearLayout llComInfo, llCall;
	private TextView tvCompany, tvAcount, tvWash, tvCount, tvMoney, tvAdvice,
			tvBackLogin, tvIsAgree;

	private Dialog dialog;
	private TextView tvCamera, tvPhoto, tvBack;

	private static final int CAMERA = 1;
	private static final int PHOTO = 0;

	private Uri cameraUri;
	private File cameraPic;
	private PopupWindow pw;
	private int screenWidth;
	private String path;// 图片路径
	
	private HttpUtils post;
	private RequestParams params;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, null);

		initView(view);
		initData();
		registerListener();

		return view;
	}

	private void initData() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		initDatas();
	}

	private void registerListener() {
		/**
		 * 商家详情
		 */
		llComInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), MerchantInfoActivity.class);
				startActivity(intent);
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
				builder.setItems(tel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_DIAL,
								Uri.parse("tel:400-000-000"));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
				intent = new Intent(getActivity(), MoneyManagerActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 交纳保证金
		 */
		tvMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(),
						PayProtectMoneyActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 意见反馈
		 */
		tvAdvice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), AdviceActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 退出登录
		 */
		tvBackLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("是否退出登录");

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								EMChatManager.getInstance().logout();
								Intent intent = new Intent(getActivity(),LoginActivity.class);
								getActivity().finish();
								startActivity(intent);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.show();
			}
		});

		/**
		 * 更换头像
		 */
		ivIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(v);
			}
		});

	}

	private void initView(View view) {
		ivIcon = (CircleImageView) view
				.findViewById(R.id.fragment_mine_iv_icon);
		tvCompany = (TextView) view
				.findViewById(R.id.fragment_mine_tv_user_name);
		tvAcount = (TextView) view
				.findViewById(R.id.fragment_mine_tv_user_acount);
		tvWash = (TextView) view.findViewById(R.id.fragment_mine_tv_wash);
		tvCount = (TextView) view.findViewById(R.id.fragment_mine_tv_count);
		tvWash = (TextView) view.findViewById(R.id.fragment_mine_tv_wash);
		tvMoney = (TextView) view.findViewById(R.id.fragment_mine_tv_money);
		tvAdvice = (TextView) view.findViewById(R.id.fragment_mine_tv_advice);
		tvBackLogin = (TextView) view
				.findViewById(R.id.mine_fragment_tv_back_login);
		llComInfo = (LinearLayout) view
				.findViewById(R.id.fragment_mine_ll_com_info);
		llCall = (LinearLayout) view.findViewById(R.id.fragment_mine_ll_call);
		tvIsAgree = (TextView) view.findViewById(R.id.fragment_mine_tv_isagree);
		dialog = ProgressDialogHandle.getProgressDialog(getActivity(), null);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initDatas() {
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		img_url = sp.getString("logo", "");
		company_name = sp.getString("company_name", "");
		acount = sp.getString("acount", "");
		is_approve = sp.getString("is_approve", "");
		loadIvIcon(img_url);
		tvCompany.setText(company_name);
		tvAcount.setText("账号：" + acount);
		if ("0".equals(is_approve)) {
			tvIsAgree.setText("未审核");
		} else if ("1".equals(is_approve)) {
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
	private void showPopupWindow(View v) {
		View view = View.inflate(getActivity(), R.layout.popupwindow_1,null);
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

		pw = new PopupWindow(view, screenWidth * 99 / 100,LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params = getActivity().getWindow()
				.getAttributes();
		params.alpha = 0.7f;
		getActivity().getWindow().setAttributes(params);

		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(true);

		pw.setAnimationStyle(R.style.pw_anim_style);

		pw.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

		pw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params =getActivity()
						.getWindow().getAttributes();
				params.alpha = 1f;
				getActivity().getWindow().setAttributes(params);
			}
		});

	}

	/**
	 * 相机中调用
	 */
	private void getFromCamera() {
		// 通过Intent调用系统相机
		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraPic = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/userIcon.jpg");
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
	 * @param file
	 */
	private void upLoadUserIcon(File file)  {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("poster", file);
		String url = UrlUtils.postUrl+UrlUtils.path_uploadImage;
		post.send(HttpMethod.POST,url, params,new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
				if (dialog != null) {
					dialog.show();
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(getActivity(), "上传失败,请检查网络状况", 0).show();;
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
					Toast.makeText(getActivity(), "上传失败,请重试", 0).show();;
				}else if (code == 1) {
					Toast.makeText(getActivity(), "上传成功", 0).show();;
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/userIcon.jpg";
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
				ContentResolver cr = getActivity().getContentResolver();
				try {
					Bitmap b = MediaStore.Images.Media.getBitmap(cr, uri);
					Bitmap bitmap = big(b, 60, 60);
					bitmap.getByteCount();
					ivIcon.setImageBitmap(bitmap);
					// 这里开始的第二部分，获取图片的路径：
					String[] proj = { MediaStore.Images.Media.DATA };
					// 好像是android多媒体数据库的封装接口，具体的看Android文档
					Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);
					// 按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
	
}
