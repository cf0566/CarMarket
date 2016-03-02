package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class AnswerResult {
	private int code;
	private String msg;
	private ArrayList<AnswerData> data;
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
	public ArrayList<AnswerData> getData() {
		return data;
	}
	public void setData(ArrayList<AnswerData> data) {
		this.data = data;
	}
	public AnswerResult(int code, String msg, ArrayList<AnswerData> data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public AnswerResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
