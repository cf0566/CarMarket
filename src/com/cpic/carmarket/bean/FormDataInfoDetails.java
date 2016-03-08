package com.cpic.carmarket.bean;

import java.util.ArrayList;
public class FormDataInfoDetails {
	private String back_type;
	private String back_money;
	private String back_desc;
	private String back_status;
	private String back_time;
	private ArrayList<String> back_img;
	public String getBack_type() {
		return back_type;
	}
	public void setBack_type(String back_type) {
		this.back_type = back_type;
	}
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
	public FormDataInfoDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "FormDataInfoDetails [back_type=" + back_type + ", back_money="
				+ back_money + ", back_desc=" + back_desc + ", back_status="
				+ back_status + ", back_time=" + back_time + ", back_img="
				+ back_img + "]";
	}
	
}
