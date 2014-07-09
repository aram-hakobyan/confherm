package fr.conferencehermes.confhermexam.parser;

import java.util.HashMap;

public class Profile {
	private int id;
	private String firstName;
	private String lastName;
	private HashMap<String, String> groups;
	private String email;
	private String userName;
	private String authKey;
	private String information;
	public String getInformation() {
	return information;
}

public void setInformation(String information) {
	this.information = information;
}

	public Profile() {
		setGroups(new HashMap<String, String>());
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

	public String getEmailAdress() {
		return email;
	}

	public void setEmailAdress(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getAuthKey() {
		return authKey;
	}

	public HashMap<String, String> getGroups() {
		return groups;
	}

	public void setGroups(HashMap<String, String> groups) {
		this.groups = groups;
	}

}
