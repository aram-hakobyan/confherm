package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class Question {
	private int id;
	private int exerciseId;
	private String name;
	private String questionText;
	private String type;
	private String createdBy;
	private ArrayList<Answer> answers;
	private String correction;
	private String inputCount;
	private HashMap<String, String> files;
	private HashMap<String, String> correctionFiles;

	public Question() {
		setAnswers(new ArrayList<Answer>());
		setFiles(new HashMap<String, String>());
		setCorrectionFiles(new HashMap<String, String>());
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

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	public String getCorrection() {
		return correction;
	}

	public void setCorrection(String string) {
		this.correction = string;
	}

	public String getInputCount() {
		return inputCount;
	}

	public void setInputCount(String inputCount) {
		this.inputCount = inputCount;
	}

	public HashMap<String, String> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, String> files) {
		this.files = files;
	}

	public HashMap<String, String> getCorrectionFiles() {
		return correctionFiles;
	}

	public void setCorrectionFiles(HashMap<String, String> correctionFiles) {
		this.correctionFiles = correctionFiles;
	}

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

}
