package com.skylion.cartoon.entity;

public class User {

	private String name;
	private String email;
	private boolean loggedIn = false;
	private static User instance = null;
	private int id = -1;

	public static User getInstance() {
		if (instance == null)
			instance = new User();
		return instance;
	}

	private User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

}
