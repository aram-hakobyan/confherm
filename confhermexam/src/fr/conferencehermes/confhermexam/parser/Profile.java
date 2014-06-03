package fr.conferencehermes.confhermexam.parser;

import java.util.HashMap;

public class Profile {
	private int id;
	private String firstName;
	private String lastName;
	private HashMap<String,String> groups;

	public Profile() {
		setGroups(new HashMap<String,String>());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public HashMap<String,String> getGroups() {
		return groups;
	}

	public void setGroups(HashMap<String,String> groups) {
		this.groups = groups;
	}

}
