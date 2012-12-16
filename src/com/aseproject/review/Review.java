package com.aseproject.review;

import java.io.Serializable;
import java.sql.Timestamp;

public class Review implements Serializable 
{
	private String username;
	private String location;
	private String reviewText;
	private int rating;
	private int likes;
	private int dislikes;
	private String profilePic;
	private Timestamp timeDate;
	
	public Review(String user, String loc, String revText, int rate,int likes,int dislikes) 
	{
		this.username = user;
		this.location = loc;
		this.reviewText = revText;
		this.rating = rate;	
		this.likes = likes;
		this.dislikes = dislikes;
	}
	
	public Review(String user, String loc, String revText, int rate,int likes,int dislikes,Timestamp timeDate) 
	{
		this.username = user;
		this.location = loc;
		this.reviewText = revText;
		this.rating = rate;	
		this.likes = likes;
		this.dislikes = dislikes;
		this.timeDate = timeDate;
	}
	
	public Review(String user, String loc, String revText, int rate, int likes, int dislikes, String profilePic,Timestamp timeDate) 
	{
		this.username = user;
		this.location = loc;
		this.reviewText = revText;
		this.rating = rate;	
		this.likes = likes;
		this.dislikes = dislikes;
		this.profilePic = profilePic;
		this.timeDate = timeDate;
	}
	
	public String getUsername() 
	{
		return username;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public String getReviewText() 
	{
		return reviewText;
	}
	
	public int getRating() 
	{
		return rating;
	}
	
	public int getLikes() 
	{
		return likes;
	}
	
	public int getDislikes() 
	{
		return dislikes;
	}
	
	public String getProfPic() 
	{
		return profilePic;
	}
	
	public Timestamp getTimeDate() 
	{
		return timeDate;
	}
}
