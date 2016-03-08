package com.cpic.carmarket.bean;

public class FormData4 {
	private int code;
	private String msg;
	private FormDataInfo4 data;
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
	public FormData4(int code, String msg, FormDataInfo4 data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public FormDataInfo4 getData() {
		return data;
	}
	public void setData(FormDataInfo4 data) {
		this.data = data;
	}
	public FormData4() {
		super();
		// TODO Auto-generated constructor stub
	}
}
