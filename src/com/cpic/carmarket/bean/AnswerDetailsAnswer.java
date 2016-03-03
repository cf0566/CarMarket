package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class AnswerDetailsAnswer {
	private String name;
    private String content;
    private String answer_id;
    private String time;
    private String logo;
    private double score;
    private ArrayList<AnswerDetailsListInfo> list;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public ArrayList<AnswerDetailsListInfo> getList() {
		return list;
	}
	public void setList(ArrayList<AnswerDetailsListInfo> list) {
		this.list = list;
	}
	public AnswerDetailsAnswer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}
	@Override
	public String toString() {
		return "AnswerDetailsAnswer [name=" + name + ", content=" + content
				+ ", answer_id=" + answer_id + ", time=" + time + ", logo="
				+ logo + ", score=" + score + ", list=" + list + "]";
	}
	
}
