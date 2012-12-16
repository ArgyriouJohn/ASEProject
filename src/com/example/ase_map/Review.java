package com.example.ase_map;

import java.io.Serializable;

public class Review implements Serializable {

	private String username;
	private String location;
	private String reviewText;
	private int rating;
	
	public Review(String user, String loc, String revText, int rate) {
		this.username = user;
		this.location = loc;
		this.reviewText = revText;
		this.rating = rate;		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getReviewText() {
		return reviewText;
	}
	
	public int getRating() {
		return rating;
	}

}
