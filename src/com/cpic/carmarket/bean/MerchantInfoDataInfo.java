package com.cpic.carmarket.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MerchantInfoDataInfo implements Parcelable {

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
	

	public MerchantInfoDataInfo(String dim_id, String dim_name,
			String car_category, String category_name) {
		super();
		this.dim_id = dim_id;
		this.dim_name = dim_name;
		this.car_category = car_category;
		this.category_name = category_name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(dim_id);
		dest.writeString(dim_name);
		dest.writeString(category_name);
		dest.writeString(car_category);
	}

	public static final Parcelable.Creator<MerchantInfoDataInfo> CREATOR = new Parcelable.Creator<MerchantInfoDataInfo>() {

		// 反序列化，将对象的信息从内存中读取出来
		@Override
		public MerchantInfoDataInfo createFromParcel(Parcel source) {
			// 读取的顺序一定要和写入的顺序一致
			String dim_id = source.readString();
			String dim_name = source.readString();
			String category_name = source.readString();
			String car_category = source.readString();
			return new MerchantInfoDataInfo(dim_id, dim_name, category_name, car_category);
		}

		@Override
		public MerchantInfoDataInfo[] newArray(int size) {
			return new MerchantInfoDataInfo[size];
		}
	};

}
