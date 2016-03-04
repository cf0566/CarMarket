package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class MessageData {
	private ArrayList<MessageDataInfo> data;

	public ArrayList<MessageDataInfo> getData() {
		return data;
	}

	public void setData(ArrayList<MessageDataInfo> data) {
		this.data = data;
	}

	public MessageData(ArrayList<MessageDataInfo> data) {
		super();
		this.data = data;
	}

	public MessageData() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MessageData [data=" + data + "]";
	}
	
}
