package fr.conferencehermes.confhermexam.parser;

public class ExerciseAnswer {
  private int exerciseId;
  private int examId;
  private int eventId;
  private String jsonString;
  private int isSent = 0;

  public int getExerciseId() {
    return exerciseId;
  }

  public void setExerciseId(int exerciseId) {
    this.exerciseId = exerciseId;
  }

  public int getExamId() {
    return examId;
  }

  public void setExamId(int examId) {
    this.examId = examId;
  }

  public String getJsonString() {
    return jsonString;
  }

  public void setJsonString(String jsonString) {
    this.jsonString = jsonString;
  }

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public int getIsSent() {
    return isSent;
  }

  public void setIsSent(int isSent) {
    this.isSent = isSent;
  }

}
