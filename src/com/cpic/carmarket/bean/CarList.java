package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class CarList {
	
	private int code;
	private String msg;
	private ArrayList<CarListData> data;
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
	public ArrayList<CarListData> getData() {
		return data;
	}
	public void setData(ArrayList<CarListData> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "CarList [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
}
