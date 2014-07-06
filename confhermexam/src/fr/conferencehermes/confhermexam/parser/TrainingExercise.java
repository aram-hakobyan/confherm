package fr.conferencehermes.confhermexam.parser;

public class TrainingExercise {
	private int exercise_id;
	private String title;
	private int duration;

	public TrainingExercise() {

	}

	public int getExercise_id() {
		return exercise_id;
	}

	public void setExercise_id(int exercise_id) {
		this.exercise_id = exercise_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
