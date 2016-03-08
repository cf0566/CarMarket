package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class FormData3 {
	private int code;
	private String msg;
	private ArrayList<FormDataInfo3> data;
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
	public ArrayList<FormDataInfo3> getData() {
		return data;
	}
	public void setData(ArrayList<FormDataInfo3> data) {
		this.data = data;
	}
	public FormData3() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "FormData [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	
}
