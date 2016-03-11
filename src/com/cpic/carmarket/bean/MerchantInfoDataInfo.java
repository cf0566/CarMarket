package com.cpic.carmarket.bean;
public class MerchantInfoDataInfo {
	
	private String dim_id;
	private String dim_name;
	private String car_category;
	private String category_name;
	public String getDim_id() {
		return dim_id;
	}
	public void setDim_id(String dim_id) {
		this.dim_id = dim_id;
	}
	public String getDim_name() {
		return dim_name;
	}
	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}
	public String getCar_category() {
		return car_category;
	}
	public void setCar_category(String car_category) {
		this.car_category = car_category;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public MerchantInfoDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "MerchantInfoDataInfo [dim_id=" + dim_id + ", dim_name="
				+ dim_name + ", car_category=" + car_category
				+ ", category_name=" + category_name + "]";
	}
}
