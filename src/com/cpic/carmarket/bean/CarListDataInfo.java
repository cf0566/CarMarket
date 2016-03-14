package com.cpic.carmarket.bean;

public class CarListDataInfo {
	
	private String category_id;
	private String category_name;
	private String category_img;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCategory_img() {
		return category_img;
	}
	public void setCategory_img(String category_img) {
		this.category_img = category_img;
	}
	public CarListDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CarListDataInfo [category_id=" + category_id
				+ ", category_name=" + category_name + ", category_img="
				+ category_img + "]";
	}
}
