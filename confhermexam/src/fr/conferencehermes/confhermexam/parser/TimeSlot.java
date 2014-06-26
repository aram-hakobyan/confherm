package fr.conferencehermes.confhermexam.parser;

public class TimeSlot {
	private int timeslot_id;
	private int test_id;
	private String test_name;
	private long start_date;
	private long end_date;
	private String academy;
	private String place;
	private String room;
	private int status;

	public TimeSlot() {

	}

	public int getTimeslot_id() {
		return timeslot_id;
	}

	public void setTimeslot_id(int timeslot_id) {
		this.timeslot_id = timeslot_id;
	}

	public int getTest_id() {
		return test_id;
	}

	public void setTest_id(int test_id) {
		this.test_id = test_id;
	}

	public String getTest_name() {
		return test_name;
	}

	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}

	public long getStart_date() {
		return start_date;
	}

	public void setStart_date(long start_date) {
		this.start_date = start_date;
	}

	public long getEnd_date() {
		return end_date;
	}

	public void setEnd_date(long end_date) {
		this.end_date = end_date;
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
