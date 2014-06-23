package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

public class Correction {
	private String questionId;
	private String text;
	private ArrayList<String> answersArray;

	public Correction() {
		setAnswersArray(new ArrayList<String>());
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ArrayList<String> getAnswersArray() {
		return answersArray;
	}

	public void setAnswersArray(ArrayList<String> answersArray) {
		this.answersArray = answersArray;
	}

}
