package com.example.ase_map;

import java.io.Serializable;

public class UserAuth implements Serializable {

	private String username;
	private String password;
	private String email;
	
	public UserAuth() {				
	}
	
	public UserAuth(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public UserAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}

}
