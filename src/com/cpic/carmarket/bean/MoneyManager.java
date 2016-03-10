package com.cpic.carmarket.bean;

public class MoneyManager {
	
	private MoneyManagerData data;

	public MoneyManagerData getData() {
		return data;
	}

	public void setData(MoneyManagerData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MoneyManager [data=" + data + "]";
	}

	public MoneyManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
