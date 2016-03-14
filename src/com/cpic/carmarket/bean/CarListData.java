package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class CarListData {
	
	private String header;
	private ArrayList<CarListDataInfo> children;
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public ArrayList<CarListDataInfo> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<CarListDataInfo> children) {
		this.children = children;
	}
	public CarListData() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CarListData [header=" + header + ", children=" + children + "]";
	}
	
}
