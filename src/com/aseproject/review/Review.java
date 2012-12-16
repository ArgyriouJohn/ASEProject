package com.aseproject.review;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class creates an Review object used to store and retrieve review information from the server.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 */
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
	
	/**
    * This constructor creates an Review object.
    * @param username user's username
    * @param location review's location 
    * @param reviewText review's text
    * @param rating review's ratings
    * @param likes review's likes
    * @param dislikes review's dislikes
    */	
	public Review(String username, String location, String reviewText, int rating,int likes,int dislikes) 
	{
		this.username = username;
		this.location = location;
		this.reviewText = reviewText;
		this.rating = rating;	
		this.likes = likes;
		this.dislikes = dislikes;
	}
	
	/**
    * This constructor creates an Review object.
    * @param username user's username
    * @param location review's location
    * @param timeDate Time and date. 
    * @param reviewText review's text
    * @param rating review's ratings
    * @param likes review's likes
    * @param dislikes review's dislikes
    */	
	public Review(String username, String location, String reviewText, int rating,int likes,int dislikes,Timestamp timeDate) 
	{
		this.username = username;
		this.location = location;
		this.reviewText = reviewText;
		this.rating = rating;	
		this.likes = likes;
		this.dislikes = dislikes;
		this.timeDate = timeDate;
	}
	
	/**
    * This constructor creates an Review object.
    * @param username user's username
    * @param timeDate Time and date. 
    */	
	public Review(String username, Timestamp timeDate) 
	{
		this.username = username;
		this.timeDate = timeDate;
	}
	
	/**
    * This constructor creates an Review object.
    * @param username user's username
    * @param location review's location
    * @param timeDate Time and date. 
    * @param reviewText review's text
    * @param rating review's ratings
    * @param likes review's likes
    * @param dislikes review's dislikes
    * @param profilePic user's profile picture. 
    */	
	public Review(String username, String location, String reviewText, int rating, int likes, int dislikes, String profilePic,Timestamp timeDate) 
	{
		this.username = username;
		this.location = location;
		this.reviewText = reviewText;
		this.rating = rating;	
		this.likes = likes;
		this.dislikes = dislikes;
		this.profilePic = profilePic;
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
    * This method returns the review's location.
    * @return location. 
    */
	public String getLocation()
	{
		return location;
	}
	
	/**
    * This method returns the review's text.
    * @return A review's text. 
    */	
	public String getReviewText() 
	{
		return reviewText;
	}
	
	/**
    * This method returns the review's ratings.
    * @return A review's ratings. 
    */
	public int getRating() 
	{
		return rating;
	}
	
	/**
    * This method returns the review's likes.
    * @return A review's likes. 
    */	
	public int getLikes() 
	{
		return likes;
	}
	
	/**
    * This method returns the review's dislikes.
    * @return A review's dislikes. 
    */
	public int getDislikes() 
	{
		return dislikes;
	}
	
	/**
    * This method returns the user's Profile picture in String format.
    * @return Profile picture in String format. 
    */
	public String getProfPic() 
	{
		return profilePic;
	}
	
	/**
    * This method returns the checkin's Time and date.
    * @return Time and date. 
    */
	public Timestamp getTimeDate() 
	{
		return timeDate;
	}
}
