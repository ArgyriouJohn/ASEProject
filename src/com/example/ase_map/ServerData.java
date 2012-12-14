package com.example.ase_map;

import java.io.Serializable;

public class ServerData implements Serializable
{
	private String username;
	private String email;
	private double longitude;
	private double latitude;
	
	public ServerData(){}
	
	public ServerData(String username,String email,double longitude,double latitude)
	{
		this.username=username;
		this.email=email;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public double getLatitude()
	{
		return latitude;
	}

}
