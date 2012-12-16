package com.aseproject.checkin;

import java.sql.Timestamp;

import android.graphics.Bitmap;

public class CheckInBitmap extends CheckIn
{	
	private Bitmap profilePicBitmamp;
	
	public CheckInBitmap(String user, String loc, Timestamp tD,String profilePic) 
	{
		super(user, loc, tD, profilePic);
	}
	
	public Bitmap getProfilePicBitmap() 
	{
		return profilePicBitmamp;
	}
	
	public void setProfilePicBitmap(Bitmap profilePicBitmamp) 
	{
		this.profilePicBitmamp =profilePicBitmamp;
	}
}
