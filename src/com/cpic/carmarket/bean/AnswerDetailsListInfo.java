package com.cpic.carmarket.bean;

public class AnswerDetailsListInfo {
		private int type;
	    private String msg;
	    private String id;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public AnswerDetailsListInfo() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "AnswerDetailsListInfo [type=" + type + ", msg=" + msg
					+ ", id=" + id + "]";
		}
	    
}
