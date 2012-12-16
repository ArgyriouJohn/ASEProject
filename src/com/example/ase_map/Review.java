package com.example.ase_map;

import java.io.Serializable;

public class Review implements Serializable 
{

	private String username;
	private String location;
	private String reviewText;
	private int rating;
	private int likes;
	private int dislikes;
	
	public Review(String user, String loc, String revText, int rate,int likes,int dislikes) 
	{
		this.username = user;
		this.location = loc;
		this.reviewText = revText;
		this.rating = rate;	
		this.likes = likes;
		this.dislikes = dislikes;
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
}
