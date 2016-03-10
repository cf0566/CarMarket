package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class MoneyManagerData {
	
	private String balance;
	private ArrayList<MoneyManagerList> tradeList;
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public ArrayList<MoneyManagerList> getTradeList() {
		return tradeList;
	}
	public void setTradeList(ArrayList<MoneyManagerList> tradeList) {
		this.tradeList = tradeList;
	}
	public MoneyManagerData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "MoneyManagerData [balance=" + balance + ", tradeList="
				+ tradeList + "]";
	}
	
}
