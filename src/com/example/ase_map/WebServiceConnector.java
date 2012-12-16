package com.example.ase_map;

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


public class WebServiceConnector
{
	private static final String NAMESPACE = "http://pkg";
	private static String URL = "http://54.243.147.122:8080/ASEServer/services/Android?wsdl"; 
	private static final String METHOD_NAME = "login";
	private static final String METHOD_NAME2 = "getLocations";
	private static final String METHOD_NAME3 = "checkIn";
	private static final String METHOD_NAME4 = "getCheckIns";
	private static final String METHOD_NAME5 = "getReview";
	private static final String METHOD_NAME6 = "review";
	private static final String SOAP_ACTION ="http://pkg/login";
	private static final String SOAP_ACTION2 = "http://pkg/getLocations";
	private static final String SOAP_ACTION3 = "http://pkg/checkIn";
	private static final String SOAP_ACTION4 = "http://pkg/getCheckIns";
	private static final String SOAP_ACTION5 = "http://pkg/getReview";
	private static final String SOAP_ACTION6 = "http://pkg/review";

			
	private String initializeStuff(Object o, String methodName, String soapAction) throws IOException {
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
	
	public String getLoginResponse(String username, String password, String email) throws IOException
	{
		UserAuth authData = new UserAuth(username, password, email);
	    return initializeStuff(authData, METHOD_NAME, SOAP_ACTION);
	}
	
	public String getLocResponse(String username, Double longitude, Double latitude) throws IOException
	{
		LocShare locData = new LocShare(username, longitude, latitude); 
	    return initializeStuff(locData, METHOD_NAME2, SOAP_ACTION2);
	}
	
	public String getCheckInResponse(String username, String location, Timestamp timeDate) throws IOException
	{
		CheckIn checkInData = new CheckIn(username, location, timeDate);		
		return initializeStuff(checkInData, METHOD_NAME3, SOAP_ACTION3);		
	}
	
	public String getReviewResponse(String username, String location, String reviewText, int rating) throws IOException
	{		 
		Review reviewData = new Review(username, location, reviewText, rating); 		
	    return initializeStuff(reviewData, METHOD_NAME6, SOAP_ACTION6);
	}
	
	public ArrayList<CheckIn> getCheckInsResponse(String location) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4); 

		CheckIn checkInData = new CheckIn("",location,null); 
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
	
	public ArrayList<Review> getReviewsResponse(String location) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5); 

		Review reviewData = new Review("", location, "", 5); 
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
}