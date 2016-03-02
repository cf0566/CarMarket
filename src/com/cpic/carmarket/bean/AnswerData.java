package com.cpic.carmarket.bean;

public class AnswerData {
	
	private String question_id;
	private String dim_id;
	private String dim_name;
	private String car_id;
	private String car_name;
	private String car_logo;
	private String user_name;
	private String user_img;
	private String ease_name;
	private String content;
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getDim_id() {
		return dim_id;
	}
	public void setDim_id(String dim_id) {
		this.dim_id = dim_id;
	}
	public String getDim_name() {
		return dim_name;
	}
	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}
	public String getCar_id() {
		return car_id;
	}
	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}
	public String getCar_name() {
		return car_name;
	}
	public void setCar_name(String car_name) {
		this.car_name = car_name;
	}
	public String getCar_logo() {
		return car_logo;
	}
	public void setCar_logo(String car_logo) {
		this.car_logo = car_logo;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public String getEase_name() {
		return ease_name;
	}
	public void setEase_name(String ease_name) {
		this.ease_name = ease_name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "AnswerData [question_id=" + question_id + ", dim_id=" + dim_id
				+ ", dim_name=" + dim_name + ", car_id=" + car_id
				+ ", car_name=" + car_name + ", car_logo=" + car_logo
				+ ", user_name=" + user_name + ", user_img=" + user_img
				+ ", ease_name=" + ease_name + ", content=" + content + "]";
	}
	public AnswerData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AnswerData(String question_id, String dim_id, String dim_name,
			String car_id, String car_name, String car_logo, String user_name,
			String user_img, String ease_name, String content) {
		super();
		this.question_id = question_id;
		this.dim_id = dim_id;
		this.dim_name = dim_name;
		this.car_id = car_id;
		this.car_name = car_name;
		this.car_logo = car_logo;
		this.user_name = user_name;
		this.user_img = user_img;
		this.ease_name = ease_name;
		this.content = content;
	}
	
}
