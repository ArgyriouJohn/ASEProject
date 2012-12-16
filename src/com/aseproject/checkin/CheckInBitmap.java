package com.aseproject.checkin;

import java.sql.Timestamp;

import android.graphics.Bitmap;

/**
 * This class creates an CheckInBitmap object used store a users profile pic in a Bitmap format too.
 * @author John Argyriou 2012
 * @see CheckIn
 */
public class CheckInBitmap extends CheckIn
{	
	private Bitmap profilePicBitmamp;
	
    /**
    * This constructor creates an CheckIn object.
    * @param username user's username
    * @param location checkin's location
    * @param timeDate Time and date. 
    * @param profilePic profile picture.
    */
	public CheckInBitmap(String username, String location, Timestamp timeDate,String profilePic) 
	{
		super(username, location, timeDate, profilePic);
	}
	
	/**
    * This method returns the user's profile pic in Bitmap format.
    * @return profilePicBitmamp. 
    */
	public Bitmap getProfilePicBitmap() 
	{
		return profilePicBitmamp;
	}
	
	/**
    * This sets a profile pic image in Bitmap format.
    * @param profilePicBitmamp pic image in Bitmap format.
    */
	public void setProfilePicBitmap(Bitmap profilePicBitmamp) 
	{
		this.profilePicBitmamp =profilePicBitmamp;
	}
}
