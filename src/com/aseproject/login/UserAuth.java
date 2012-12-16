package com.aseproject.login;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class creates a UserAuth object which represents a user and is used to store and retrieve user information to and from the server.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 */
public class UserAuth implements Serializable 
{
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
	private int isInvis;
	private Timestamp checkInDate;

   /**
    * This constructor creates a UserAuth object.
    * @param none.
    */
	public UserAuth() 
	{}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's password.
    * @param user's email.
    */
	public UserAuth(String username, String password, String email) 
	{
		this.username = username;
		this.password = password;
		this.email = email;		
	}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's password.
    */
	public UserAuth(String username, String password) 
	{
		this.username = username;
		this.password = password;
	}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    */
	public UserAuth(String username)
	{
		this.username = username;
	}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's visibility status.
    */
	public UserAuth(String username, int visInvis)
	{
		this.username = username;
		this.isInvis = visInvis;
	}

   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's checkIn date.
    */
	public UserAuth(String username, Timestamp date) 
	{
		this.username = username;
		this.checkInDate = date;
	}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's first name.
    * @param user's last name.
    * @param user's gender value.
    * @param user's day of birth.
    * @param user's month of birth.
    * @param user's year of birth.
    * @param user's profile picture.
    * @param user's visibility status.
    */
	public UserAuth(String username, String first, String last, String gen, int d, int m, int y,String profilePic, int visibility) 
	{
		this.username = username;
		this.firstName = first;
		this.lastName = last;
		this.gender = gen;
		this.year = d;
		this.month = m;
		this.day = y;
		this.profilePic = profilePic;
		this.isInvis = visibility;
	}
	
   /**
    * This constructor creates a UserAuth object.
    * @param user's username.
    * @param user's first name.
    * @param user's last name.
    * @param user's gender value.
    * @param user's day of birth.
    * @param user's month of birth.
    * @param user's year of birth.
    */
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
	
	/**
    * This method returns the user's username.
    * @return username. 
    */
	public String getUsername() 
	{
		return username;
	}
	
	/**
    * This method returns the user's password.
    * @return password. 
    */
	public String getPassword()
	{
		return password;
	}
	
	/**
    * This method returns the user's email.
    * @return email. 
    */
	public String getEmail() 
	{
		return email;
	}
	
	/**
    * This method returns the user's first name.
    * @return firstname. 
    */
	public String getFirstName() 
	{
		return firstName;
	}
	
	/**
    * This method returns the user's last name.
    * @return lastname. 
    */
	public String getLastName() 
	{
		return lastName;
	}
	
	/**
    * This method returns the user's gender.
    * @return gender. 
    */
	public String getGender() 
	{
		return gender;
	}

	/**
    * This method returns the user's year of birth.
    * @return year. 
    */
	public int getYear()
	{
		return year;
	}
	
	/**
    * This method returns the user's month of birth.
    * @return month. 
    */
	public int getMonth() 
	{
		return month;
	}
	
	/**
    * This method returns the user's day of birth.
    * @return day. 
    */
	public int getDay() 
	{
		return day;
	}
	
	/**
    * This method returns the user's profile picture.
    * @return profilePic. 
    */
	public String getPicture() 
	{
		return profilePic;
	}
	
	/**
    * This method returns the user's visibility status.
    * @return isInvis. 
    */
	public int getVisibility() 
	{
		return isInvis;
	}
}