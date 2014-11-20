package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Correction {
	private String questionId;
	private String text;
	private ArrayList<String> answersArray;
	private HashMap<String, String> files;
	private String questionPoint;

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

	public HashMap<String, String> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}

	public String getQuestionPoint() {
		return questionPoint;
	}

	public void setQuestionPoint(String questionPoint) {
		this.questionPoint = questionPoint;
	}

}
