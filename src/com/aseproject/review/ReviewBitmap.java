package com.aseproject.review;

import java.sql.Timestamp;

import com.aseproject.checkin.CheckIn;

import android.graphics.Bitmap;

/**
 * This class creates a ReviewBitmap object used store a user's profile pic in a Bitmap format too.
 * @author John Argyriou 2012
 * @see Review
 */
public class ReviewBitmap extends Review 
{
	private Bitmap profilePicBitmamp;
	
    /**
    * This constructor creates a Review object.
    * @param user user's username
    * @param loc review's location
    * @param revText review's supporting text
    * @param rate review's rating
    * @param likes review's likes
    * @param dslikes review's dislikes    
    * @param profilePic profile picture
    * @param timeData review's timestamp.
    */
	public ReviewBitmap(String user, String loc, String revText, int rate,int likes, int dislikes, String profilePic,Timestamp timeDate) 
	{
		super(user, loc, revText, rate, likes, dislikes, profilePic,timeDate);
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
		this.profilePicBitmamp = profilePicBitmamp;
	}
}
