package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

public class CorrectionAnswer {
	private int questionId;
	private int questionType;
	private ArrayList<String> answers;

	public CorrectionAnswer() {
		answers = new ArrayList<String>();
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

}
