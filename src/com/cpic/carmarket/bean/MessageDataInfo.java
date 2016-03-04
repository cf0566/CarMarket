package com.cpic.carmarket.bean;

public class MessageDataInfo {
	
	private String question_id;
	private String content;
	private String status;
	private String dim_id;
	private String dim_name;
	private String car_id;
	private String car_name;
	private String user_name;
	private String user_img;
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "MessageDataInfo [question_id=" + question_id + ", content="
				+ content + ", status=" + status + ", dim_id=" + dim_id
				+ ", dim_name=" + dim_name + ", car_id=" + car_id
				+ ", car_name=" + car_name + ", user_name=" + user_name
				+ ", user_img=" + user_img + "]";
	}
	public MessageDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
