package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Exercise {
	private int id;
	private int examId;
	private String name;
	private String type;
	private int exerciseType;
	private int exerciseIsAlreadyPassed = 0;
	private String text;
	private String intro;
	private String teacher;
	private String timeOpen;
	private String timeClose;
	private String timeLimit;
	private String timeModified;
	private int reviewoverallfeedback;
	private ArrayList<Integer> questionIDs;
	private String password;
	private ArrayList<Question> questions;
	private HashMap<String, String> files;
	private boolean isClicked = false;
	private int duration;

	public Exercise() {
		setQuestionIds(new ArrayList<Integer>());
		setQuestions(new ArrayList<Question>());
		setFiles(new HashMap<String, String>());
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

	public ArrayList<Integer> getQuestionsId() {
		return questionIDs;
	}

	public void setQuestionIds(ArrayList<Integer> questionids) {
		this.questionIDs = questionids;
	}

	public String getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(String timeModified) {
		this.timeModified = timeModified;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public HashMap<String, String> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}

	public int getExerciseType() {
		return exerciseType;
	}

	public void setExerciseType(int exerciseType) {
		this.exerciseType = exerciseType;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getExerciseIsAlreadyPassed() {
		return exerciseIsAlreadyPassed;
	}

	public void setExerciseIsAlreadyPassed(int exerciseIsAlreadyPassed) {
		this.exerciseIsAlreadyPassed = exerciseIsAlreadyPassed;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
