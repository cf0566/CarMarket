package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class FormCloseData {
	
	private int code ;
	private String msg;
	private FormCloseDataInfo data;
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
	public FormCloseDataInfo getData() {
		return data;
	}
	public void setData(FormCloseDataInfo data) {
		this.data = data;
	}
	public FormCloseData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "FormCloseData [code=" + code + ", msg=" + msg + ", data="
				+ data + "]";
	}
}
