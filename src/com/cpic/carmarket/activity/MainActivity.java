package com.cpic.carmarket.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.cpic.carmarket.R;
import com.cpic.carmarket.base.BaseActivity;
import com.cpic.carmarket.fragment.AnswerFragment;
import com.cpic.carmarket.fragment.FormFragment;
import com.cpic.carmarket.fragment.MessageFragment;
import com.cpic.carmarket.fragment.MineFragment;

public class MainActivity extends BaseActivity {

	// rbtn的管理类
	private RadioGroup rGroup;
	// 上一次选择的rbtn
	private RadioButton lastButton;
	// Fragment的管理类
	private FragmentManager mManager;
	// Fragment的事务类
	private FragmentTransaction mTrans;
	// 管理Fragment的List集合
	private List<Fragment> mFragList;
	// 记录上次点击返回键的时间
	private long lastTime;

	public String curFragmentTag = "";

	@Override
	protected void getIntentData(Bundle savedInstanceState) {
	}

	@Override
	protected void loadXml() {
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void initView() {
		rGroup = (RadioGroup) findViewById(R.id.activity_main_rgroup);
		lastButton = (RadioButton) findViewById(R.id.activity_main_rbtn_answer);
	}

	@Override
	protected void registerListener() {

		// TODO Auto-generated method stub
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 当前选中的radioButton
				RadioButton selectRbtn = (RadioButton) findViewById(checkedId);

				int index = Integer.parseInt(selectRbtn.getTag().toString());
				int lastIndex = Integer.parseInt(lastButton.getTag().toString());

				Fragment mFragment = mManager.findFragmentByTag(index + "");

				mTrans = mManager.beginTransaction();

				if (mFragment == null) {
					mTrans.add(R.id.activity_main_framlayout,mFragList.get(index), "" + index);
				}
				// 设置界面隐藏与显示，避免一次性加载所有界面
				mTrans.show(mFragList.get(index));
				mTrans.hide(mFragList.get(lastIndex));
				mTrans.commit();

				lastButton = selectRbtn;
			}
		});
	}
	@Override
	protected void initData() {
		initFragment();
	}
	private void initFragment() {
		// TODO Auto-generated method stub
		mFragList = new ArrayList<Fragment>();
		mFragList.add(new AnswerFragment());
		mFragList.add(new MessageFragment());
		mFragList.add(new FormFragment());
		mFragList.add(new MineFragment());

		mManager = getSupportFragmentManager();
		mTrans = mManager.beginTransaction();
		mTrans.add(R.id.activity_main_framlayout, mFragList.get(0), "0");
		mTrans.show(mFragList.get(0));
		mTrans.commit();
	}
	@Override
	public void onBackPressed() {
		// 获取本次点击的时间
		long currentTime = System.currentTimeMillis();
		long dTime = currentTime - lastTime;
		if (dTime < 2000) {
			finish();
		} else {
			Toast.makeText(this, "再按一次退出程序", 0).show();
			lastTime = currentTime;
		}
	}

}
