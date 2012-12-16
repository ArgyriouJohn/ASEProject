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
	//private static String URL = "http://10.0.2.2:8080/ASEServer/services/Android?wsdl"; 
	private static final String METHOD_NAME = "getData";
	private static final String METHOD_NAME2 = "getLoc";
	private static final String METHOD_NAME3 = "checkIn";
	private static final String METHOD_NAME4 = "getCheckIns";
	private static final String SOAP_ACTION ="http://pkg/getData";
	private static final String SOAP_ACTION2 = "http://pkg/getLoc";
	private static final String SOAP_ACTION3 = "http://pkg/checkIn";
	private static final String SOAP_ACTION4 = "http://pkg/getCheckIns";
 
	public String getLoginResponse(String username, String password, String email) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 

		UserAuth authData = new UserAuth(username, password, email);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(authData);
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
	    	androidHttpTransport.call(SOAP_ACTION, envelope);
	    	SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
	    	output = result.toString();
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return output;
	}
	
	public String getLocResponse(String username, Double longitude, Double latitude) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2); 

		LocShare locData = new LocShare(username, longitude, latitude); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(locData);
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
	    	androidHttpTransport.call(SOAP_ACTION2, envelope);
	    	SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
	    	output = result.toString();
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return output;
	}
	
	public String getCheckInResponse(String username, String location, Timestamp timeDate) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3); 

		CheckIn checkInData = new CheckIn(username, location, timeDate); 
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
		String output ="empty";
	    try 
	    {
	    	androidHttpTransport.call(SOAP_ACTION3, envelope);
	    	SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
	    	output = result.toString();
		} 
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	    return output;
	}
	
	public ArrayList<CheckIn> getCheckInsResponse(String username, String location, Timestamp timeDate) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4); 

		CheckIn checkInData = new CheckIn(username, location, timeDate); 
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
}