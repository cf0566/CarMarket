package com.cpic.carmarket.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FormCloseDataInfo {
	private String car_name;
	private String order_id;
	private String order_amount;
	private String pay_id;
	private String pay_status;
	private String order_status;
	private String order_content;
	private String dim_name;
	private String order_date;
	private String order_address;
	private String mobile;
	private String consignee;
	private String company_name;
	private String user_name;
	private String user_img;
	private String company_logo;
	private String order_sn;
	private String create_date;
	private String evaluation_date;
	private ArrayList<String> evaluation;
	private FormCloseDataInfoBack back;
	public String getCar_name() {
		return car_name;
	}
	public void setCar_name(String car_name) {
		this.car_name = car_name;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	public String getPay_id() {
		return pay_id;
	}
	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrder_content() {
		return order_content;
	}
	public void setOrder_content(String order_content) {
		this.order_content = order_content;
	}
	public String getDim_name() {
		return dim_name;
	}
	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_address() {
		return order_address;
	}
	public void setOrder_address(String order_address) {
		this.order_address = order_address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_img() {
		return user_img;
	}
	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}
	public String getCompany_logo() {
		return company_logo;
	}
	public void setCompany_logo(String company_logo) {
		this.company_logo = company_logo;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getEvaluation_date() {
		return evaluation_date;
	}
	public void setEvaluation_date(String evaluation_date) {
		this.evaluation_date = evaluation_date;
	}
	public ArrayList<String> getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(ArrayList<String> evaluation) {
		this.evaluation = evaluation;
	}
	public FormCloseDataInfoBack getBack() {
		return back;
	}
	public void setBack(FormCloseDataInfoBack back) {
		this.back = back;
	}
	public FormCloseDataInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "FormCloseDataInfo [car_name=" + car_name + ", order_id="
				+ order_id + ", order_amount=" + order_amount + ", pay_id="
				+ pay_id + ", pay_status=" + pay_status + ", order_status="
				+ order_status + ", order_content=" + order_content
				+ ", dim_name=" + dim_name + ", order_date=" + order_date
				+ ", order_address=" + order_address + ", mobile=" + mobile
				+ ", consignee=" + consignee + ", company_name=" + company_name
				+ ", user_name=" + user_name + ", user_img=" + user_img
				+ ", company_logo=" + company_logo + ", order_sn=" + order_sn
				+ ", create_date=" + create_date + ", evaluation_date="
				+ evaluation_date + ", evaluation=" + evaluation + ", back="
				+ back + "]";
	}
}
