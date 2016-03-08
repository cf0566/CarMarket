package com.cpic.carmarket.bean;

import java.util.ArrayList;

public class FormDataInfoDetails4 {
	
	private String score;
	private String score_content;
	private ArrayList<String> img;
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getScore_content() {
		return score_content;
	}
	public void setScore_content(String score_content) {
		this.score_content = score_content;
	}
	public ArrayList<String> getImg() {
		return img;
	}
	public void setImg(ArrayList<String> img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return "FormDataInfoDetails4 [score=" + score + ", score_content="
				+ score_content + ", img=" + img + "]";
	}
	public FormDataInfoDetails4() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
