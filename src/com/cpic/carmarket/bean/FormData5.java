package com.cpic.carmarket.bean;


public class FormData5 {
	private int code;
	private String msg;
	private FormDataInfo5 data;
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
	public FormData5() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FormDataInfo5 getData() {
		return data;
	}
	public void setData(FormDataInfo5 data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "FormData5 [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	
}
