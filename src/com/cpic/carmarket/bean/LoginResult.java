package com.cpic.carmarket.bean;

public class LoginResult {
	private int code;
	private String msg;
	private LoginData data;
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
	public LoginData getData() {
		return data;
	}
	public void setData(LoginData data) {
		this.data = data;
	}
	public LoginResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LoginResult(int code, String msg, LoginData data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
}
