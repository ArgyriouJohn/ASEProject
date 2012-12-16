package com.aseproject.places;

import org.apache.http.client.HttpResponseException;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.util.Log;
 
/** TThis class is used to get places details from the Google Places API
* @author John Argyriou 2012
* */
@SuppressWarnings("deprecation")
public class GooglePlaces 
{
    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
 
    // Google API Key
    private static final String API_KEY = "AIzaSyDXAs9MUQG2RUf4a2k9MIyvQ6Db-2PADas";
 
    // Google Places serach url's
    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
    private static final boolean PRINT_AS_STRING = false;
    
    private static final HttpTransport transport = new ApacheHttpTransport();
 
    private double _latitude;
    private double _longitude;
    private double _radius;
 
    /**
     * Searching places
     * @param latitude - latitude of place
     * @params longitude - longitude of place
     * @param radius - radius of searchable area
     * @param types - type of place to search
     * @return list of places
     * */
    public PlacesList search(double latitude, double longitude, double radius, String types) throws Exception 
    {
        this._latitude = latitude;
        this._longitude = longitude;
        this._radius = radius;
 
        try 
        {
            HttpRequestFactory httpRequestFactory = createRequestFactory(HTTP_TRANSPORT);
            HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", _latitude + "," + _longitude);
            request.getUrl().put("radius", _radius); // in meters
            request.getUrl().put("sensor", "false");
            if(types != null) request.getUrl().put("types", types);
            
            if (PRINT_AS_STRING) 
            {
                System.out.println(request.execute().parseAsString());
                return null;
            } 
            else 
            {
            	PlacesList list = request.execute().parseAs(PlacesList.class);
            	// Check log cat for places response status
            	Log.d("Places Status", "" + list.status);
            	return list;
            }
 
        } 
        catch (HttpResponseException e) 
        {
            Log.e("Error:", e.getMessage());
            return null;
        }
 }
 
    /**
     * Searching single place full details
     * @param refrence - reference id of place
     *                 - which you will get in search api request
     * */
    public PlaceDetails getPlaceDetails(String reference) throws Exception 
    {
        try 
        {
        	HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
        	HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("reference", reference);
            request.getUrl().put("sensor", "false");
 
            PlaceDetails place = request.execute().parseAs(PlaceDetails.class);
 
            return place;
 
        } 
        catch (HttpResponseException e) 
        {
            Log.e("Error in Perform Details", e.getMessage());
            throw e;
        }
    }
 
    /**
     * Creating http request Factory
     * */
    public static HttpRequestFactory createRequestFactory(final HttpTransport transport) 
    {	 
    	return transport.createRequestFactory(new HttpRequestInitializer() 
    	{
	    	public void initialize(HttpRequest request) 
	    	{
		    	GoogleHeaders headers = new GoogleHeaders();
		    	headers.setApplicationName("Google-Places-DemoApp");
		    	request.setHeaders(headers);
		    	JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
		    	request.addParser(parser);
	        }
        });
    }
}
