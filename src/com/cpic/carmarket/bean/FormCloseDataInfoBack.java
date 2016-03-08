package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class FormCloseDataInfoBack {
	private String back_money;
	private String back_desc;
	private String back_status;
	private String back_time;
	private ArrayList<String> back_img;
	private String merchant_reason;
	private ArrayList<String> merchant_img;
	private String merchant_time;
	public String getBack_money() {
		return back_money;
	}
	public void setBack_money(String back_money) {
		this.back_money = back_money;
	}
	public String getBack_desc() {
		return back_desc;
	}
	public void setBack_desc(String back_desc) {
		this.back_desc = back_desc;
	}
	public String getBack_status() {
		return back_status;
	}
	public void setBack_status(String back_status) {
		this.back_status = back_status;
	}
	public String getBack_time() {
		return back_time;
	}
	public void setBack_time(String back_time) {
		this.back_time = back_time;
	}
	public ArrayList<String> getBack_img() {
		return back_img;
	}
	public void setBack_img(ArrayList<String> back_img) {
		this.back_img = back_img;
	}
	public String getMerchant_reason() {
		return merchant_reason;
	}
	public void setMerchant_reason(String merchant_reason) {
		this.merchant_reason = merchant_reason;
	}
	public ArrayList<String> getMerchant_img() {
		return merchant_img;
	}
	public void setMerchant_img(ArrayList<String> merchant_img) {
		this.merchant_img = merchant_img;
	}
	public String getMerchant_time() {
		return merchant_time;
	}
	public void setMerchant_time(String merchant_time) {
		this.merchant_time = merchant_time;
	}
	@Override
	public String toString() {
		return "FormCloseDataInfoBack [back_money=" + back_money
				+ ", back_desc=" + back_desc + ", back_status=" + back_status
				+ ", back_time=" + back_time + ", back_img=" + back_img
				+ ", merchant_reason=" + merchant_reason + ", merchant_img="
				+ merchant_img + ", merchant_time=" + merchant_time + "]";
	}
	public FormCloseDataInfoBack() {
		super();
		// TODO Auto-generated constructor stub
	}
}
