package com.backend.shoppingcar.dto;

public class Login {
	
	private String username;
	private String password;
	private String role;
	private boolean active;
	
	public Login(){
		
	}
	
	public Login(String username, String password, String role, boolean active) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.active = active;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	

}
