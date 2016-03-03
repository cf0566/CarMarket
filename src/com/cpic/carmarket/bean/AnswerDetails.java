package com.cpic.carmarket.bean;

public class AnswerDetails {
	
	private int code;
	private String msg;
	private AnswerDetailsData data;
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
	public AnswerDetailsData getData() {
		return data;
	}
	public void setData(AnswerDetailsData data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AnswerDetails [code=" + code + ", msg=" + msg + ", data="
				+ data + "]";
	}
	public AnswerDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
