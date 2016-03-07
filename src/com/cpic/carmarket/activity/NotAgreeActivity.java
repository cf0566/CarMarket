package com.cpic.carmarket.activity;

import java.io.File;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.utils.ProgressDialogHandle;
import com.cpic.carmarket.utils.UrlUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NotAgreeActivity extends BaseActivity {

	private RelativeLayout relShow;
	private PopupWindow pw;
	private int screenWidth;
	private TextView tvCamera, tvPhoto, tvBack;
	private ImageView ivAdd,ivPic,ivDelete;

	private EditText etContent;
	private HttpUtils post;
	private RequestParams params;
	private SharedPreferences sp;
	private Dialog dialog;
	
	private static final int CAMERA = 1;
	private static final int PHOTO = 0;
	
	private Uri cameraUri;
	private File cameraPic;
	private Intent intent;
	
	private Button btnSubmit;
	
	private String path,order_id;
	
	private boolean isDelete = false;
	
	@Override
	protected void getIntentData(Bundle savedInstanceState) {
		order_id = getIntent().getStringExtra("order_id");
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_not_agree);
	}

	@Override
	protected void initView() {
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		ivAdd = (ImageView) findViewById(R.id.activity_not_agree_iv_add);
		ivPic = (ImageView) findViewById(R.id.activity_not_agree_iv_pic);
		ivDelete = (ImageView) findViewById(R.id.activity_not_agree_iv_delete);
		dialog = ProgressDialogHandle.getProgressDialog(NotAgreeActivity.this, null);
		etContent = (EditText) findViewById(R.id.activity_not_agree_et_content);
		btnSubmit = (Button) findViewById(R.id.activity_not_agree_btn_submit);
	}

	@Override
	protected void registerListener() {
		ivAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(v);
			}

		});
		
		ivDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isDelete = true;
				ivDelete.setVisibility(View.GONE);
				ivPic.setVisibility(View.GONE);
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("".equals(etContent.getText().toString())) {
					showShortToast("请填写不同意退款原因！");
				}else{
					 upLoadUserIcon(new File(path));
				}
			}
		});
	}
	private void upLoadUserIcon(File file)  {
		post = new HttpUtils();
		params = new RequestParams();
		sp = PreferenceManager.getDefaultSharedPreferences(NotAgreeActivity.this);
		String token = sp.getString("token", "");
		params.addBodyParameter("token", token);
		params.addBodyParameter("order_id", order_id);
		params.addBodyParameter("status", UrlUtils.STATUS_NOTAGREE);
		params.addBodyParameter("content", etContent.getText().toString());
		params.addBodyParameter("count", "1");
		params.addBodyParameter("poster0", file);
		String url = UrlUtils.postUrl+ UrlUtils.path_orderAction;
		post.send(HttpMethod.POST, url , params, new RequestCallBack<String>() {

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
					dialog.dismiss();;
				}
				JSONObject obj = JSONObject.parseObject(arg0.result);
				int code = obj.getInteger("code");
				if (code == 0) {
					showShortToast("提交失败,请重试");
				}else if (code == 1) {
					showShortToast("提交成功");
				}
			}
		});
	}

	private void showPopupWindow(View v) {
		View view = View.inflate(NotAgreeActivity.this, R.layout.popupwindow_1,
				null);
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

		pw = new PopupWindow(view, screenWidth * 99/100,
				LayoutParams.WRAP_CONTENT);
		pw.setFocusable(true);
		WindowManager.LayoutParams params = NotAgreeActivity.this.getWindow()
				.getAttributes();
		params.alpha = 0.7f;
		NotAgreeActivity.this.getWindow().setAttributes(params);

		pw.setBackgroundDrawable(new ColorDrawable());
		pw.setOutsideTouchable(true);

		pw.setAnimationStyle(R.style.pw_anim_style);

		pw.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

		pw.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = NotAgreeActivity.this
						.getWindow().getAttributes();
				params.alpha = 1f;
				NotAgreeActivity.this.getWindow().setAttributes(params);
			}
		});

	}
	/**
	 * 相机中调用
	 */
	private void getFromCamera() {
		// 通过Intent调用系统相机
		 	intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraPic = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/error.jpg");
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

	@Override
	protected void initData() {

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/error.jpg";
			Bitmap temp = BitmapFactory.decodeFile(cameraUri.getPath());
			Bitmap bitmap = big(temp, 60, 60);
			ivPic.setImageBitmap(bitmap);
			ivPic.setVisibility(View.VISIBLE);
			ivDelete.setVisibility(View.VISIBLE);
//			upLoadUserIcon(new File(Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + "/usericon.PNG"));
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
					ivPic.setImageBitmap(bitmap);
					ivPic.setVisibility(View.VISIBLE);
					ivDelete.setVisibility(View.VISIBLE);
					//这里开始的第二部分，获取图片的路径：
					String[] proj = {MediaStore.Images.Media.DATA};
					//好像是android多媒体数据库的封装接口，具体的看Android文档
					Cursor cursor = managedQuery(uri, proj, null, null, null);
					//按我个人理解 这个是获得用户选择的图片的索引值
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					//将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					//最后根据索引值获取图片路径
					path = cursor.getString(column_index);
//					Log.i("oye", path);
					//上传头像
//					upLoadUserIcon(new File(path));
					
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
