package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

public class Event {
	private int id;
	private String name;
	private int validation;
	private String access_notes;
	private int testId;
	private long creationDate;
	private long lastEditTime;
	private String author;
	private ArrayList<Exam> exams;

	public Event() {
		setExams(new ArrayList<Exam>());
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

	public int getValidation() {
		return validation;
	}

	public void setValidation(int validation) {
		this.validation = validation;
	}

	public String getAccess_notes() {
		return access_notes;
	}

	public void setAccess_notes(String access_notes) {
		this.access_notes = access_notes;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ArrayList<Exam> getExams() {
		return exams;
	}

	public void setExams(ArrayList<Exam> exams) {
		this.exams = exams;
	}

	public long getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(long lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}
