package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class FormData2 {
	private int code;
	private String msg;
	private FormDataInfo2 data;
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
	public FormData2() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FormDataInfo2 getData() {
		return data;
	}
	public void setData(FormDataInfo2 data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "FormData2 [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	
}
