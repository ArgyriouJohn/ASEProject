package com.aseproject.login;

import java.io.Serializable;

public class UserAuth implements Serializable {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String gender;
	private int year;
	private int month;
	private int day;	
	private String profilePic;
	
	public UserAuth() {				
	}
	
	public UserAuth(String username, String password, String email, String first, String last) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = first; 
		this.lastName = last;
	}
	
	public UserAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public UserAuth(String username) {
		this.username = username;
	}
	
	public UserAuth(String username, String first, String last, String gen, int d, int m, int y,String profilePic) 
	{
		this.username = username;
		this.firstName = first;
		this.lastName = last;
		this.gender = gen;
		this.year = d;
		this.month = m;
		this.day = y;
		this.profilePic = profilePic;
	}
	
	public UserAuth(String username, String first, String last, String gen, int d, int m, int y) 
	{
		this.username = username;
		this.firstName = first;
		this.lastName = last;
		this.gender = gen;
		this.year = d;
		this.month = m;
		this.day = y;
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
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public String getPicture() {
		return profilePic;
	}

}