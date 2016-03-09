package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class WashList {
	private ArrayList<WashListData> data;
	private String msg;
	private int code;
	public ArrayList<WashListData> getData() {
		return data;
	}
	public void setData(ArrayList<WashListData> data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public WashList() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "WashList [data=" + data + ", msg=" + msg + ", code=" + code
				+ "]";
	}
	
}
