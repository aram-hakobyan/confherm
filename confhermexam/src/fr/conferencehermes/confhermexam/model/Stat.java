package fr.conferencehermes.confhermexam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stat {

	private int type;
	private String name;
	private String typeName;
	private String goodAnswerPerc;
	private String badAnswerPerc;
	private String passedAnswerPerc;
	private ArrayList<HashMap<String, String>> answers;
	private ArrayList<String> goodAnswers;
	private String allAnswers;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getGoodAnswerPerc() {
		return goodAnswerPerc;
	}

	public void setGoodAnswerPerc(String goodAnswerPerc) {
		this.goodAnswerPerc = goodAnswerPerc;
	}

	public String getBadAnswerPerc() {
		return badAnswerPerc;
	}

	public void setBadAnswerPerc(String badAnswerPerc) {
		this.badAnswerPerc = badAnswerPerc;
	}

	public String getPassedAnswerPerc() {
		return passedAnswerPerc;
	}

	public void setPassedAnswerPerc(String passedAnswerPerc) {
		this.passedAnswerPerc = passedAnswerPerc;
	}

	public ArrayList<HashMap<String, String>> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<HashMap<String, String>> answers) {
		this.answers = answers;
	}

	public ArrayList<String> getGoodAnswers() {
		return goodAnswers;
	}

	public void setGoodAnswers(ArrayList<String> goodAnswers) {
		this.goodAnswers = goodAnswers;
	}

	public String getAllAnswers() {
		return allAnswers;
	}

	public void setAllAnswers(String allAnswers) {
		this.allAnswers = allAnswers;
	}

}
