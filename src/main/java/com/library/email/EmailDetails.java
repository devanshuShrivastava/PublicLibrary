package com.library.email;

public class EmailDetails {
	private String userEmailID;
	private String adminEmailID="devanshu010193@gmail.com";
	private String adminPassword="PopMom123";
	private String body;
	private String subject;

	public String getAdminEmailID() {
		return adminEmailID;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setUserEmailID(String userEmailID) {
		this.userEmailID = userEmailID;
	}

	public String getUserEmailID() {
		return userEmailID;
	}

}
