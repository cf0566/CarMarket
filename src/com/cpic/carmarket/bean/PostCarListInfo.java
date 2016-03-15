package com.cpic.carmarket.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PostCarListInfo implements Parcelable{
	
	private String object_id;
	private String car_category;
	private String category_name;
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
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
	public PostCarListInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PostCarListInfo [object_id=" + object_id + ", car_category="
				+ car_category + ", category_name=" + category_name + "]";
	}
	public PostCarListInfo(String object_id, String car_category,
			String category_name) {
		super();
		this.object_id = object_id;
		this.car_category = car_category;
		this.category_name = category_name;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(object_id);
		dest.writeString(category_name);
		dest.writeString(car_category);
	}
	
	public static final  Parcelable.Creator<PostCarListInfo> CREATOR = new Parcelable.Creator<PostCarListInfo>() {
		
		// 反序列化，将对象的信息从内存中读取出来
		@Override
		public PostCarListInfo createFromParcel(Parcel source) {
			// 读取的顺序一定要和写入的顺序一致
			String object_id = source.readString();
			String category_name = source.readString();
			String car_category = source.readString();
			return new PostCarListInfo(object_id, category_name,car_category);
		}

		@Override
		public PostCarListInfo[] newArray(int size) {
			return new PostCarListInfo[size];
		}
	};
	
}
