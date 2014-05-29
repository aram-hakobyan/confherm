package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

public class Exercise {
	private int id;
	private String name;
	private String intro;
	private String timeOpen;
	private String timeClose;
	private String timeLimit;
	private String timeModified;
	private int reviewoverallfeedback;
	private ArrayList<Integer> questions;

	public Exercise() {
		setQuestions(new ArrayList<Integer>());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeClose() {
		return timeClose;
	}

	public void setTimeClose(String timeClose) {
		this.timeClose = timeClose;
	}

	public String getTimeOpen() {
		return timeOpen;
	}

	public void setTimeOpen(String timeOpen) {
		this.timeOpen = timeOpen;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getReviewoverallfeedback() {
		return reviewoverallfeedback;
	}

	public void setReviewoverallfeedback(int reviewoverallfeedback) {
		this.reviewoverallfeedback = reviewoverallfeedback;
	}

	public ArrayList<Integer> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Integer> questions) {
		this.questions = questions;
	}

	public String getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(String timeModified) {
		this.timeModified = timeModified;
	}

}
