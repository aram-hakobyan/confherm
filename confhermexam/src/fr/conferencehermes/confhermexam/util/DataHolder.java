package fr.conferencehermes.confhermexam.util;

import java.util.ArrayList;

import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.Training;

public class DataHolder {
	private static DataHolder instance;
	
	private ArrayList<Exam> exams;
	private ArrayList<Exercise> exercises;
	private ArrayList<Training> trainings;

	private DataHolder() {
		setExams(new ArrayList<Exam>());
		setExercises(new ArrayList<Exercise>());
		setTrainings(new ArrayList<Training>());
	}

	public static DataHolder getInstance() {
		if (instance == null) {
			instance = new DataHolder();
		}
		return instance;

	}

	public ArrayList<Exam> getExams() {
		return exams;
	}

	public void setExams(ArrayList<Exam> exams) {
		this.exams = exams;
	}

	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}

	public ArrayList<Training> getTrainings() {
		return trainings;
	}

	public void setTrainings(ArrayList<Training> trainings) {
		this.trainings = trainings;
	}

}
