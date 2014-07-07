package fr.conferencehermes.confhermexam.util;

import java.util.ArrayList;

import fr.conferencehermes.confhermexam.parser.Correction;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.Training;
import fr.conferencehermes.confhermexam.parser.TrainingExercise;

public class DataHolder {
	private static DataHolder instance;

	private ArrayList<Exam> exams;
	private ArrayList<Exercise> exercises;
	private ArrayList<TrainingExercise> trainingExercises;
	private ArrayList<Training> trainings;
	private ArrayList<DownloadInstance> downloads;
	private ArrayList<Correction> corrections;
	private long millisUntilFinished;
	private int[] downloadPercents;

	private DataHolder() {
		setExams(new ArrayList<Exam>());
		setExercises(new ArrayList<Exercise>());
		setTrainings(new ArrayList<Training>());
		setTrainingExercises(new ArrayList<TrainingExercise>());
		setDownloads(new ArrayList<DownloadInstance>());
		setCorrections(new ArrayList<Correction>());

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

	public ArrayList<TrainingExercise> getTrainingExercises() {
		return trainingExercises;
	}

	public void setTrainingExercises(
			ArrayList<TrainingExercise> trainingExercises) {
		this.trainingExercises = trainingExercises;
	}

	public ArrayList<DownloadInstance> getDownloads() {
		return downloads;
	}

	public void setDownloads(ArrayList<DownloadInstance> downloads) {
		this.downloads = downloads;
	}

	public ArrayList<Correction> getCorrections() {
		return corrections;
	}

	public void setCorrections(ArrayList<Correction> corrections) {
		this.corrections = corrections;
	}

	public long getMillisUntilFinished() {
		return millisUntilFinished;
	}

	public void setMillisUntilFinished(long millisUntilFinished) {
		this.millisUntilFinished = millisUntilFinished;
	}

	public int[] getDownloadPercents() {
		return downloadPercents;
	}

	public void setDownloadPercents(int[] downloadPercents) {
		this.downloadPercents = downloadPercents;
	}

}
