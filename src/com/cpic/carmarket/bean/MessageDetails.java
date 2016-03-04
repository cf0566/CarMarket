package com.cpic.carmarket.bean;

public class MessageDetails {
	
	private int code;
	private String msg;
	private MessageDetailsData data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public MessageDetailsData getData() {
		return data;
	}
	public void setData(MessageDetailsData data) {
		this.data = data;
	}
	public MessageDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MessageDetails(int code, String msg, MessageDetailsData data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	@Override
	public String toString() {
		return "MessageDetails [code=" + code + ", msg=" + msg + ", data="
				+ data + "]";
	}
	
}
