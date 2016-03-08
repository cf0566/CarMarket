package com.cpic.carmarket.bean;

public class FormDataInfo3 {
	
	private String car_name;
	private String order_id;
	private String order_amount;
	private String pay_id;
	private String pay_status;
	private String order_content;
	private String order_date;
	private String order_address;
	private String mobile;
	private String consignee;
	private String company_name;
	private String company_logo;
	private String order_sn;
	private String create_date;
	private String merchant_date;
	private FormDataInfoDetails back;
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
	public String getOrder_content() {
		return order_content;
	}
	public void setOrder_content(String order_content) {
		this.order_content = order_content;
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
	public String getMerchant_date() {
		return merchant_date;
	}
	public void setMerchant_date(String merchant_date) {
		this.merchant_date = merchant_date;
	}
	public FormDataInfo3() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FormDataInfoDetails getBack() {
		return back;
	}
	public void setBack(FormDataInfoDetails back) {
		this.back = back;
	}
	@Override
	public String toString() {
		return "FormDataInfo3 [car_name=" + car_name + ", order_id=" + order_id
				+ ", order_amount=" + order_amount + ", pay_id=" + pay_id
				+ ", pay_status=" + pay_status + ", order_content="
				+ order_content + ", order_date=" + order_date
				+ ", order_address=" + order_address + ", mobile=" + mobile
				+ ", consignee=" + consignee + ", company_name=" + company_name
				+ ", company_logo=" + company_logo + ", order_sn=" + order_sn
				+ ", create_date=" + create_date + ", merchant_date="
				+ merchant_date + ", back=" + back + "]";
	}
	
}
