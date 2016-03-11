package com.cpic.carmarket.bean;

public class MerchantInfo {
	
	private int code;
	private String msg;
	private MerchantInfoData data;
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
	public MerchantInfoData getData() {
		return data;
	}
	public void setData(MerchantInfoData data) {
		this.data = data;
	}
	public MerchantInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "MerchantInfo [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}
	
}
