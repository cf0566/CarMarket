package com.cpic.carmarket.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="Contants") //注解中的值代表表名
public class Contants {
	@Id(column="_id")
	private int id;
	@Column(column="userId")
	private String userId;
	@Column(column="userName")
	private String userName;
	@Column(column="imgUrl")
	private String imgUrl;
	@Column(column="lastTalk")
	private String lastTalk;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLastTalk() {
		return lastTalk;
	}
	public void setLastTalk(String lastTalk) {
		this.lastTalk = lastTalk;
	}
	@Override
	public String toString() {
		return "Contants [id=" + id + ", userId=" + userId + ", userName="
				+ userName + ", imgUrl=" + imgUrl + ", lastTalk=" + lastTalk
				+ "]";
	}
	public Contants() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contants(int id, String userId, String userName, String imgUrl,
			String lastTalk) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.imgUrl = imgUrl;
		this.lastTalk = lastTalk;
	}
}
