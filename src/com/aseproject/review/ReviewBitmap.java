package com.aseproject.review;

import java.sql.Timestamp;

import android.graphics.Bitmap;

public class ReviewBitmap extends Review 
{
	private Bitmap profilePicBitmamp;
	
	public ReviewBitmap(String user, String loc, String revText, int rate,int likes, int dislikes, String profilePic,Timestamp timeDate) 
	{
		super(user, loc, revText, rate, likes, dislikes, profilePic,timeDate);
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
