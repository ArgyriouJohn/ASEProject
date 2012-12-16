package com.aseproject.location;

import java.io.Serializable;

/**
 * This class creates an location object used to store and retrieve location information from the server.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 */
public class Locations implements Serializable 
{
	private String username;
	private Double longitude;
	private Double latitude;
	
	/**
    * This constructor creates an location object.
    */
	public Locations() {}
	
	/**
    * This constructor creates an Review object.
    * @param username location's username
    * @param longitude location's longitude
    * @param latitude location's  latitude
    */
	public Locations(String username, Double longitude, Double latitude) 
	{
		this.username = username;
		this.longitude = longitude;
		this.latitude = latitude;
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
    * This method returns the location's longitude.
    * @return longitude. 
    */
	public Double getLongitude() 
	{
		return longitude;
	}
	
	/**
    * This method returns the location's latitude.
    * @return latitude. 
    */
	public Double getLatitude() 
	{
		return latitude;
	}
}
