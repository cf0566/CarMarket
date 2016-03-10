package com.cpic.carmarket.bean;

public class MoneyManagerList {
	
	private int type;
	private String mark;
	private String date;
	private String money;
	private String result;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "MoneyManagerList [type=" + type + ", mark=" + mark + ", date="
				+ date + ", money=" + money + ", result=" + result + "]";
	}
	public MoneyManagerList() {
		super();
		// TODO Auto-generated constructor stub
	}
}
