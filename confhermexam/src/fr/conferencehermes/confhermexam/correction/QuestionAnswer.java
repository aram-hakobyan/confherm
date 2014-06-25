package fr.conferencehermes.confhermexam.correction;

import java.util.ArrayList;

public class QuestionAnswer {
	private int type;
	private int singleAnswerPosition;
	private ArrayList<Integer> multiAnswerPositions;
	private ArrayList<String> textAnswers;

	public QuestionAnswer() {
		type = -1;
		singleAnswerPosition = -1;
		multiAnswerPositions = new ArrayList<Integer>();
		textAnswers = new ArrayList<String>();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSingleAnswerPosition() {
		return singleAnswerPosition;
	}

	public void setSingleAnswerPosition(int singleAnswerPosition) {
		this.singleAnswerPosition = singleAnswerPosition;
	}

	public ArrayList<Integer> getMultiAnswerPositions() {
		return multiAnswerPositions;
	}

	public void setMultiAnswerPositions(ArrayList<Integer> multiAnswerPositions) {
		this.multiAnswerPositions = multiAnswerPositions;
	}

	public ArrayList<String> getTextAnswers() {
		return textAnswers;
	}

	public void setTextAnswers(ArrayList<String> textAnswers) {
		this.textAnswers = textAnswers;
	}

}
