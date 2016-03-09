package com.cpic.carmarket.bean;

public class WashListData {
	
	private String attribute_name;
	private String attribute_id;
	private String cost;
	public String getAttribute_name() {
		return attribute_name;
	}
	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}
	public String getAttribute_id() {
		return attribute_id;
	}
	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	@Override
	public String toString() {
		return "WashListData [attribute_name=" + attribute_name
				+ ", attribute_id=" + attribute_id + ", cost=" + cost + "]";
	}
	public WashListData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
