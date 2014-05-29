package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

public class Profile {
	private int id;
	private static String firstName;
	private static String lastName;
	private ArrayList<String> groups;

	public Profile() {
		setGroups(new ArrayList<String>());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public static String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}

}
