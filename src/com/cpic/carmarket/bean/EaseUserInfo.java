package com.cpic.carmarket.bean;

public class EaseUserInfo {

	private String user_id;
	private String ease_user;
	private String img;
	private String user_name;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getEase_user() {
		return ease_user;
	}
	public void setEase_user(String ease_user) {
		this.ease_user = ease_user;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	@Override
	public String toString() {
		return "EaseUserInfo [user_id=" + user_id + ", ease_user=" + ease_user
				+ ", img=" + img + ", user_name=" + user_name + "]";
	}
	public EaseUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
