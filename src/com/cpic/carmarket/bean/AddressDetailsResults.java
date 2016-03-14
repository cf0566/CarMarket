package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class AddressDetailsResults {
	private ArrayList<AddressDetailsInfo> results;
	private String status;
	public ArrayList<AddressDetailsInfo> getResults() {
		return results;
	}
	public void setResults(ArrayList<AddressDetailsInfo> results) {
		this.results = results;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AddressDetailsResults [results=" + results + ", status="
				+ status + "]";
	}
	public AddressDetailsResults() {
		super();
		// TODO Auto-generated constructor stub
	}
}
