package com.example.ase_map;

import java.io.Serializable;
import java.sql.Timestamp;

public class CheckIn implements Serializable {
	
	private String username;
	private String location;
	private Timestamp timeDate;

	public CheckIn(String user, String loc, Timestamp tD) {
		
		this.username = user;
		this.location = loc;
		this.timeDate = tD;
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getLocation() {
		return location;
	}
	
	public Timestamp getTimeDate() {
		return timeDate;
	}

}
