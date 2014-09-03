package fr.conferencehermes.confhermexam.parser;

public class Answer {
	private int id;
	private int order;
	private int questionId;
	private String answer;
	private int asnwerFormat;
	private String feedback;
	private int feedbackFormat;
	private double fraction;
	private int IS_GOOD_ANSWER = 0;

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

	public int getIS_GOOD_ANSWER() {
		return IS_GOOD_ANSWER;
	}

	public void setIS_GOOD_ANSWER(int iS_GOOD_ANSWER) {
		IS_GOOD_ANSWER = iS_GOOD_ANSWER;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
