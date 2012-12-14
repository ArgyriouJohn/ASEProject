package com.example.ase_map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.serialization.PropertyInfo;



public class WebServiceConnector
{
	private static final String NAMESPACE = "http://pkg";
	private static String URL = "http://54.243.147.122:8080/ASEServer/services/Android?wsdl"; 
	//private static String URL = "http://10.0.2.2:8080/ASEServer/services/Android?wsdl"; 
	private static final String METHOD_NAME = "getData";
	private static final String SOAP_ACTION ="http://pkg/getData";
 
	public String getResponse(String username, String password, String email) throws IOException
	{
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
		PropertyInfo propInfo=new PropertyInfo();

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
}
	

