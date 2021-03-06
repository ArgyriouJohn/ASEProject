package com.aseproject.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Base64;

import com.aseproject.checkin.CheckIn;
import com.aseproject.location.Locations;
import com.aseproject.login.UserAuth;
import com.aseproject.review.Review;

/**
* This class defines all client methods to relevant web service methods.
* @author John Argyriou 2012
* @author Thanos Irodotou 2012
*/
public class WebServiceConnector
{
	private static final String NAMESPACE = "http://server.aseserver.com";
	private static String URL = "http://54.243.147.122:8080/ASEServer/services/ASEProjectServices?wsdl"; 
	private static final String METHOD_NAME = "loginOrRegister";
	private static final String METHOD_NAME1 = "updateProfile";
	private static final String METHOD_NAME2 = "addLocations";
	private static final String METHOD_NAME3 = "addCheckIn";
	private static final String METHOD_NAME4 = "getCheckIns";
	private static final String METHOD_NAME5 = "getReviews";
	private static final String METHOD_NAME6 = "addReview";
	private static final String METHOD_NAME7 = "deleteProfile";
	private static final String METHOD_NAME8 = "retrieveProfile";
	private static final String METHOD_NAME9 = "addVisibility";
	private static final String METHOD_NAME10 = "deleteCheckIn";
	private static final String METHOD_NAME11 = "deleteReview";
	private static final String SOAP_ACTION = NAMESPACE+"/"+METHOD_NAME;
	private static final String SOAP_ACTION1 = NAMESPACE+"/"+METHOD_NAME1;
	private static final String SOAP_ACTION2 = NAMESPACE+"/"+METHOD_NAME2;
	private static final String SOAP_ACTION3 = NAMESPACE+"/"+METHOD_NAME3;
	private static final String SOAP_ACTION4 = NAMESPACE+"/"+METHOD_NAME4;
	private static final String SOAP_ACTION5 = NAMESPACE+"/"+METHOD_NAME5;
	private static final String SOAP_ACTION6 = NAMESPACE+"/"+METHOD_NAME6;
	private static final String SOAP_ACTION7 = NAMESPACE+"/"+METHOD_NAME7;
	private static final String SOAP_ACTION8 = NAMESPACE+"/"+METHOD_NAME8;
	private static final String SOAP_ACTION9 = NAMESPACE+"/"+METHOD_NAME9;
	private static final String SOAP_ACTION10 = NAMESPACE+"/"+METHOD_NAME10;
	private static final String SOAP_ACTION11 = NAMESPACE+"/"+METHOD_NAME11;
	
	
	private String initializeConnection(Object o, String methodName, String soapAction) throws IOException 
	{
		SoapObject request = new SoapObject(NAMESPACE, methodName); 	  
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(o);
		out.flush();
	    os.close();
	    byte [] data = out.toByteArray();		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		request.addProperty("data",data);
       	new MarshalBase64().register(envelope);   //serialization
     	envelope.bodyOut = request;
     	envelope.dotNet = true; 
     	envelope.setOutputSoapObject(request);
     	envelope.setAddAdornments(false);
     	envelope.implicitTypes= true;
       	envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.setOutputSoapObject(request);	
		
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);
		androidHttpTransport.debug = true;//NEW ADDED
		String output ="empty";		
	    try 
	    {
	    	androidHttpTransport.call(soapAction, envelope);
	    	SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
	    	output = result.toString();
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return output;
	}
	
    /**
    * This method is used to login users.
    * @param username
    * @param password
    * @param email
    * @return debbuging information.
    */
	public String getLoginResponse(String username, String password, String email) throws IOException
	{
		UserAuth authData = new UserAuth(username, password, email);
	    return initializeConnection(authData, METHOD_NAME, SOAP_ACTION);
	}
	
	 /**
    * This method is used to update users personal information.
    * @param username
    * @param firstName
    * @param lastName
    * @param gender
    * @param day
    * @param month
    * @param year
    * @param image
    * @param visibility
    * @return debbuging information.
    */
	public String getUpdateResponse(String username, String firstName, String lastName, String gender, int day, int month, int year, String image, int visibility) throws IOException
	{
		UserAuth updateData = new UserAuth(username, firstName, lastName, gender, day, month, year, image, visibility);
		return initializeConnection(updateData, METHOD_NAME1, SOAP_ACTION1);
	}
	
	/**
    * This method is used to delete a user's account.
    * @param username
    * @return debbuging information.
	*/
	public String getDeleteResponse(String username) throws IOException
	{
		UserAuth updateData = new UserAuth(username);
		return initializeConnection(updateData, METHOD_NAME7, SOAP_ACTION7);
	}
	
	/**
    * This method is used to delete a user's check in.
    * @param username
    * @param date
    * @return debbuging information.
	*/
	public String getDeleteCheckInResponse(String username, Timestamp date) throws IOException
	{
		CheckIn checkInData = new CheckIn(username, date);
		return initializeConnection(checkInData, METHOD_NAME10, SOAP_ACTION10);
	}
	
	/**
    * This method is used to delete a user's review.
    * @param username
    * @param date
    * @return debbuging information.
	*/
	public String getDeleteReviewResponse(String username, Timestamp date) throws IOException
	{
		Review reviewData = new Review(username, date);
		return initializeConnection(reviewData, METHOD_NAME11, SOAP_ACTION11);
	}
	
	/**
    * This method is used to change a user's visibility.
    * @param username
    * @param visibility
    * @return debbuging information.
	*/
	public String getVisibilityChangeResponse(String username, int visibility) throws IOException
	{
		UserAuth updateData = new UserAuth(username, visibility);
		return initializeConnection(updateData, METHOD_NAME9, SOAP_ACTION9);
	}
	
	/**
    * This method is used to store a user's location.
    * @param username
    * @param longitude
    * @param latitude
    * @return debbuging information.
	*/
	public String getLocResponse(String username, Double longitude, Double latitude) throws IOException
	{
		Locations locData = new Locations(username, longitude, latitude); 
	    return initializeConnection(locData, METHOD_NAME2, SOAP_ACTION2);
	}
	
	/**
    * This method is used to store a user's check in.
    * @param username
    * @param longitude
    * @param timeDate
    * @return debbuging information.
	*/
	public String getCheckInResponse(String username, String location, Timestamp timeDate) throws IOException
	{
		CheckIn checkInData = new CheckIn(username, location, timeDate);		
		return initializeConnection(checkInData, METHOD_NAME3, SOAP_ACTION3);		
	}
	
	/**
    * This method is used to store a user's review.
    * @param username
    * @param location
    * @param reviewText
    * @param rating
    * @param likes
    * @param dislikes
    * @param sqlDate
    * @return debbuging information.
	*/
	public String getReviewResponse(String username, String location, String reviewText, int rating,int likes,int dislikes,Timestamp sqlDate) throws IOException
	{		 
		Review reviewData = new Review(username, location, reviewText, rating,likes,dislikes,sqlDate); 		
	    return initializeConnection(reviewData, METHOD_NAME6, SOAP_ACTION6);
	}
	
	/**
    * This method is used to retrieve check ins.
    * @param username
    * @param location
    * @return ArrayList CheckIn.
	*/
	public ArrayList<CheckIn> getCheckInsResponse(String location, String username) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4); 

		CheckIn checkInData = new CheckIn(username,location,null); 
		ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(checkInData);
		out.flush();
	    os.close();
	    byte [] data = out.toByteArray();
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		request.addProperty("data",data);
       	new MarshalBase64().register(envelope);   //serialization
     	envelope.bodyOut = request;
     	envelope.dotNet = true; 
     	envelope.setOutputSoapObject(request);
     	envelope.setAddAdornments(false);
     	envelope.implicitTypes= true;
       	envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);

		androidHttpTransport.debug = true;//NEW ADDED
	    try 
	    {
	    	androidHttpTransport.call(SOAP_ACTION4, envelope);
	    	SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
	        String result = resultsRequestSOAP.toString();
	        		
	        if (result != "")
	        {
	            byte[] bloc = Base64.decode(result, Base64.DEFAULT);    
	            ByteArrayInputStream bis = null;
	            ObjectInputStream ois = null;
	            try 
	            {
	                bis = new ByteArrayInputStream(bloc);
	                ois = new ObjectInputStream(bis);
	                checkIns = (ArrayList<CheckIn>) ois.readObject();
	            } 
	            finally 
	            {
	                if (bis != null) {bis.close();}
	                if (ois != null) {ois.close();}
	            }
	        }
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return checkIns;
	}
	
	/**
    * This method is used to retrieve Reviews.
    * @param username
    * @param location
    * @return ArrayList Review.
	*/
	public ArrayList<Review> getReviewsResponse(String location, String username) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5); 

		Review reviewData = new Review(username, location, "", 5,0,0); 
		ArrayList<Review> reviews = new ArrayList<Review>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(reviewData);
		out.flush();
	    os.close();
	    byte [] data = out.toByteArray();
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		request.addProperty("data",data);
       	new MarshalBase64().register(envelope);   //serialization
     	envelope.bodyOut = request;
     	envelope.dotNet = true; 
     	envelope.setOutputSoapObject(request);
     	envelope.setAddAdornments(false);
     	envelope.implicitTypes= true;
       	envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);

		androidHttpTransport.debug = true;//NEW ADDED
	    try 
	    {
	    	androidHttpTransport.call(SOAP_ACTION5, envelope);
	    	SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
	        String result = resultsRequestSOAP.toString();
	        		
	        if (result != "")
	        {
	            byte[] bloc = Base64.decode(result, Base64.DEFAULT);    
	            ByteArrayInputStream bis = null;
	            ObjectInputStream ois = null;
	            try 
	            {
	                bis = new ByteArrayInputStream(bloc);
	                ois = new ObjectInputStream(bis);
	                reviews = (ArrayList<Review>) ois.readObject();
	            } 
	            finally 
	            {
	                if (bis != null) {bis.close();}
	                if (ois != null) {ois.close();}
	            }
	        }
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return reviews;
	}
	
	/**
    * This method is used to retrieve profiles.
    * @param username
    * @param location
    * @return UserAuth.
	*/
	public UserAuth getRetrieveProfileResponse(String username) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME8); 

		UserAuth userData = new UserAuth(username); 		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(userData);
		out.flush();
	    os.close();
	    byte [] data = out.toByteArray();
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		request.addProperty("data",data);
       	new MarshalBase64().register(envelope);   //serialization
     	envelope.bodyOut = request;
     	envelope.dotNet = true; 
     	envelope.setOutputSoapObject(request);
     	envelope.setAddAdornments(false);
     	envelope.implicitTypes= true;
       	envelope.encodingStyle = SoapEnvelope.ENC;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);

		androidHttpTransport.debug = true;//NEW ADDED
	    try 
	    {
	    	androidHttpTransport.call(SOAP_ACTION8, envelope);
	    	SoapPrimitive  resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
	        String result = resultsRequestSOAP.toString();
	        		
	        if (result != "")
	        {
	            byte[] bloc = Base64.decode(result, Base64.DEFAULT);    
	            ByteArrayInputStream bis = null;
	            ObjectInputStream ois = null;
	            try 
	            {
	                bis = new ByteArrayInputStream(bloc);
	                ois = new ObjectInputStream(bis);
	                userData = (UserAuth) ois.readObject();
	            } 
	            finally 
	            {
	                if (bis != null) {bis.close();}
	                if (ois != null) {ois.close();}
	            }
	        }
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return userData;
	}
}