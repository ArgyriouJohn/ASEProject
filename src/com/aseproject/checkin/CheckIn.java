package com.aseproject.checkin;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class creates an CheckIn object used to store and retrieve checkin information from the server.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 */
public class CheckIn implements Serializable 
{	
	private String username;
	private String location;
	private Timestamp timeDate;
	private String profilePic;

   /**
    * This constructor creates an CheckIn object.
    * @param user user's username
    * @param location checkin's location
    * @param timeDate Time and date. 
    */
	public CheckIn(String username, String location, Timestamp timeDate) 
	{		
		this.username = username;
		this.location = location;
		this.timeDate = timeDate;		
	}
	
	/**
    * This constructor creates an CheckIn object.
    * @param user user's username
    * @param location checkin's location
    * @param timeDate Time and date. 
    * @param profilePic Profile picture in String format.
    */
	public CheckIn(String username, String location, Timestamp timeDate, String profilePic) 
	{		
		this.username = username;
		this.location = location;
		this.timeDate = timeDate;	
		this.profilePic = profilePic;
	}
	
	/**
    * This constructor creates an CheckIn object.
    * @param user user's username
    * @param timeDate Time and date. 
    */
	public CheckIn(String username, Timestamp timeDate) 
	{
		this.username = username;
		this.timeDate = timeDate;
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
    * This method returns the checkin's location.
    * @return location. 
    */
	public String getLocation() 
	{
		return location;
	}
	
	/**
    * This method returns the checkin's Time and date.
    * @return Time and date. 
    */
	public Timestamp getTimeDate() 
	{
		return timeDate;
	}
	
   /**
    * This method returns the user's Profile picture in String format.
    * @return Profile picture in String format. 
    */
	public String getProfilePic() 
	{
		return profilePic;
	}
}
