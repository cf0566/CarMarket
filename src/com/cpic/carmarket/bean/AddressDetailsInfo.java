package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class AddressDetailsInfo {
	
	private ArrayList<Object> address_components;
	private String formatted_address;
	private Object geometry;
	private String place_id;
	private ArrayList<Object> types;
	public ArrayList<Object> getAddress_components() {
		return address_components;
	}
	public void setAddress_components(ArrayList<Object> address_components) {
		this.address_components = address_components;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public Object getGeometry() {
		return geometry;
	}
	public void setGeometry(Object geometry) {
		this.geometry = geometry;
	}
	public String getPlace_id() {
		return place_id;
	}
	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}
	public ArrayList<Object> getTypes() {
		return types;
	}
	public void setTypes(ArrayList<Object> types) {
		this.types = types;
	}
	@Override
	public String toString() {
		return "AddressDetailsInfo [address_components=" + address_components
				+ ", formatted_address=" + formatted_address + ", geometry="
				+ geometry + ", place_id=" + place_id + ", types=" + types
				+ "]";
	}
	public AddressDetailsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
