package fr.conferencehermes.confhermexam.parser;

public class Answer {
	private int id;
	private int questionId;
	private String answer;
	private int asnwerFormat;
	private String feedback;
	private int feedbackFormat;
	private double fraction;

	public Answer() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getAsnwerFormat() {
		return asnwerFormat;
	}

	public void setAsnwerFormat(int asnwerFormat) {
		this.asnwerFormat = asnwerFormat;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getFeedbackFormat() {
		return feedbackFormat;
	}

	public void setFeedbackFormat(int feedbackFormat) {
		this.feedbackFormat = feedbackFormat;
	}

	public double getFraction() {
		return fraction;
	}

	public void setFraction(double fraction) {
		this.fraction = fraction;
	}

}