package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class MerchantInfoData {
	
	private String company_logo;
	private String store_img;
	private String on_time;
	private String company_name;
	private String company_desc;
	private String tel;
	private String address;
	private ArrayList<MerchantInfoDataInfo> business;
	public String getCompany_logo() {
		return company_logo;
	}
	public void setCompany_logo(String company_logo) {
		this.company_logo = company_logo;
	}
	public String getStore_img() {
		return store_img;
	}
	public void setStore_img(String store_img) {
		this.store_img = store_img;
	}
	public String getOn_time() {
		return on_time;
	}
	public void setOn_time(String on_time) {
		this.on_time = on_time;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_desc() {
		return company_desc;
	}
	public void setCompany_desc(String company_desc) {
		this.company_desc = company_desc;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public MerchantInfoData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArrayList<MerchantInfoDataInfo> getBusiness() {
		return business;
	}
	public void setBusiness(ArrayList<MerchantInfoDataInfo> business) {
		this.business = business;
	}
	@Override
	public String toString() {
		return "MerchantInfoData [company_logo=" + company_logo
				+ ", store_img=" + store_img + ", on_time=" + on_time
				+ ", company_name=" + company_name + ", company_desc="
				+ company_desc + ", tel=" + tel + ", address=" + address
				+ ", business=" + business + "]";
	}
	
}
