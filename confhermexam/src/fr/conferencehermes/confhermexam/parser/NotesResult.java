package fr.conferencehermes.confhermexam.parser;

public class NotesResult {
	private String student_id;
	private String name;
	private int score;
	private String rank;
	public static int moyenne_score;
	public static int median_score;

	public NotesResult() {

	}

	public int getMedianScore() {
		return median_score;
	}

	public void setMedianScore(int median_score) {
		this.median_score = median_score;
	}

	public int getMonenneScore() {
		return moyenne_score;
	}

	public void setMonenneScore(int moyenne_score) {
		this.moyenne_score = moyenne_score;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getStudentId() {
		return student_id;
	}

	public void setStudentId(String student_id) {
		this.student_id = student_id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getStudentName() {
		return name;
	}

	public void setStudentName(String name) {
		this.name = name;
	}

}
