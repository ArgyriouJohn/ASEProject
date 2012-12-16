package com.aseproject.checkin;

import java.io.Serializable;
import java.sql.Timestamp;

public class CheckIn implements Serializable 
{	
	private String username;
	private String location;
	private Timestamp timeDate;
	private String profilePic;

	public CheckIn(String user, String loc, Timestamp tD) 
	{		
		this.username = user;
		this.location = loc;
		this.timeDate = tD;		
	}
	
	public CheckIn(String user, String loc, Timestamp tD, String profilePic) 
	{		
		this.username = user;
		this.location = loc;
		this.timeDate = tD;	
		this.profilePic = profilePic;
	}
	
	public String getUsername() 
	{
		return username;
	}
	
	public String getLocation() 
	{
		return location;
	}
	
	public Timestamp getTimeDate() 
	{
		return timeDate;
	}
	
	public String getProfilePic() 
	{
		return profilePic;
	}
}
