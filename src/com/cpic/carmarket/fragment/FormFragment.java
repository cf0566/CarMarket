package com.cpic.carmarket.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cpic.carmarket.R;

public class FormFragment  extends Fragment{

	private RadioGroup rGroup;
	private RadioButton rbtnWait;
	private FragmentManager fm;
	private FragmentTransaction trans;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_form, null);
		initView(view);
		registerListener();
		initDatas();
		return view;
	}
	private void initDatas() {
		fm = getChildFragmentManager();
		trans = fm.beginTransaction();
		trans.replace(R.id.form_framlayout,new FormWaitServiceFragment());
		trans.commit();
	}
	private void registerListener() {
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.fragment_form_rbtn_wait_service:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormWaitServiceFragment());
					trans.commit();
					break;
				case R.id.fragment_form_rbtn_on_service:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormOnServiceFragment());
					trans.commit();
					break;
				case R.id.fragment_form_rbtn_after_service:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormAfterServiceFragment());
					trans.commit();
					break;
				case R.id.fragment_form_rbtn_wait_query:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormWaitQueryFragment());
					trans.commit();
					break;
				case R.id.fragment_form_rbtn_after_query:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormAfterQueryFragment());
					trans.commit();
					break;
				case R.id.fragment_form_rbtn_off:
					fm = getChildFragmentManager();
					trans = fm.beginTransaction();
					trans.replace(R.id.form_framlayout,new FormCloseFragment());
					trans.commit();
					break;
				default:
					break;
				}
			}
		});
	}
	private void initView(View view) {
		rGroup = (RadioGroup) view.findViewById(R.id.fragment_form_rgroup);
		rbtnWait = (RadioButton) view.findViewById(R.id.fragment_form_rbtn_wait_service);
	}
}
