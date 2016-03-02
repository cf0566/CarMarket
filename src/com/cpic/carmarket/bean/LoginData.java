package com.cpic.carmarket.bean;

public class LoginData {
	    private String user_id;
	    private String mobile;
	    private String company_name;
	    private String logo;
	    private String store_img;
	    private String on_time;
	    private String merchant_id;
	    private String margin_url;
	    private String tel;
	    private String is_approve;
	    private String balance;
	    private String margin;
	    private String ease_user;
	    private String ease_pwd;
	    private String token;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getCompany_name() {
			return company_name;
		}
		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public String getStore_img() {
			return store_img;
		}
		public void setStore_img(String store_img) {
			this.store_img = store_img;
		}
		public String getOn_time() {
			return on_time;
		}
		public void setOn_time(String on_time) {
			this.on_time = on_time;
		}
		public String getMerchant_id() {
			return merchant_id;
		}
		public void setMerchant_id(String merchant_id) {
			this.merchant_id = merchant_id;
		}
		public String getMargin_url() {
			return margin_url;
		}
		public void setMargin_url(String margin_url) {
			this.margin_url = margin_url;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getIs_approve() {
			return is_approve;
		}
		public void setIs_approve(String is_approve) {
			this.is_approve = is_approve;
		}
		public String getBalance() {
			return balance;
		}
		public void setBalance(String balance) {
			this.balance = balance;
		}
		public String getMargin() {
			return margin;
		}
		public void setMargin(String margin) {
			this.margin = margin;
		}
		public String getEase_user() {
			return ease_user;
		}
		public void setEase_user(String ease_user) {
			this.ease_user = ease_user;
		}
		public String getEase_pwd() {
			return ease_pwd;
		}
		public void setEase_pwd(String ease_pwd) {
			this.ease_pwd = ease_pwd;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public LoginData(String user_id, String mobile, String company_name,
				String logo, String store_img, String on_time,
				String merchant_id, String margin_url, String tel,
				String is_approve, String balance, String margin,
				String ease_user, String ease_pwd, String token) {
			super();
			this.user_id = user_id;
			this.mobile = mobile;
			this.company_name = company_name;
			this.logo = logo;
			this.store_img = store_img;
			this.on_time = on_time;
			this.merchant_id = merchant_id;
			this.margin_url = margin_url;
			this.tel = tel;
			this.is_approve = is_approve;
			this.balance = balance;
			this.margin = margin;
			this.ease_user = ease_user;
			this.ease_pwd = ease_pwd;
			this.token = token;
		}
		public LoginData() {
			super();
			// TODO Auto-generated constructor stub
		}
}
