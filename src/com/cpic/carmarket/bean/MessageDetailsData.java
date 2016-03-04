package com.cpic.carmarket.bean;

import java.util.ArrayList;


public class MessageDetailsData {
	
	private String content;
	private String order_time;
	private String car_name;
	private String checked;
	private ArrayList<String> question_img;
	private String car_logo;
	private ArrayList<String> img;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getCar_name() {
		return car_name;
	}
	public void setCar_name(String car_name) {
		this.car_name = car_name;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public ArrayList<String> getQuestion_img() {
		return question_img;
	}
	public void setQuestion_img(ArrayList<String> question_img) {
		this.question_img = question_img;
	}
	public String getCar_logo() {
		return car_logo;
	}
	public void setCar_logo(String car_logo) {
		this.car_logo = car_logo;
	}
	public ArrayList<String> getImg() {
		return img;
	}
	public void setImg(ArrayList<String> img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "MessageDetailsData [content=" + content + ", order_time="
				+ order_time + ", car_name=" + car_name + ", checked="
				+ checked + ", question_img=" + question_img + ", car_logo="
				+ car_logo + ", img=" + img + "]";
	}
	public MessageDetailsData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
