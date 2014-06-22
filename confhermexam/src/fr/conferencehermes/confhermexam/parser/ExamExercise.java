package fr.conferencehermes.confhermexam.parser;

public class ExamExercise {
	public static String created_by;
	public static String exam_name;
	private int exercise_id;
	private String exercise_name;
	private boolean past_exercise;

	public ExamExercise() {

	}

	public String getCreatedBy() {
		return created_by;
	}

	public void setCreatedBy(String created_by) {
		this.created_by = created_by;
	}

	public String getExamName() {
		return exam_name;
	}

	public void setExamName(String exam_name) {
		this.exam_name = exam_name;
	}

	public int getExersiceId() {
		return exercise_id;
	}

	public void setExerciseId(int exercise_id) {
		this.exercise_id = exercise_id;
	}

	public String getExersiceName() {
		return exercise_name;
	}

	public void setExersiceName(String exercise_name) {
		this.exercise_name = exercise_name;
	}

	public boolean getPastExersice() {
		return past_exercise;
	}

	public void setPastExersice(boolean past_exercise) {
		this.past_exercise = past_exercise;
	}

}
