package com.example.ase_map;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Utils 
{
	public static PlaceDetails getPlaceDetails(String placeName,PlacesList nearPlaces,GooglePlaces googlePlaces)
	{
		PlaceDetails placeDetails = new PlaceDetails();
		for(Place place : nearPlaces.results)
    	{
    		if(place.name.equals(placeName))
    		{
    			try 
    			{
    				placeDetails = googlePlaces.getPlaceDetails(place.reference);
				} catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
		return placeDetails;
	}
	
	public static String createPlaceInfo(LinearLayout layout,PlaceDetails placeDetails,final Activity activity)
	{
		final Button checkinButton =(Button) activity.findViewById(R.id.button1);
		checkinButton.setText("Check In!");
		
		String name = placeDetails.result.name;
        String address = placeDetails.result.formatted_address;
	    String phone = placeDetails.result.formatted_phone_number;
        String latitude = Double.toString(placeDetails.result.geometry.location.lat);
        String longitude = Double.toString(placeDetails.result.geometry.location.lng);
        
        TextView lbl_name = (TextView) activity.findViewById(R.id.name);
        TextView lbl_address = (TextView) activity.findViewById(R.id.address);
        TextView lbl_phone = (TextView) activity.findViewById(R.id.phone);
        TextView lbl_location = (TextView) activity.findViewById(R.id.location);
        ImageView lbl_img= (ImageView) activity.findViewById(R.id.imageView1);
        
        name = name == null ? "Not present" : name; // if name is null display as "Not present"
        address = address == null ? "Not present" : address;
        phone = phone == null ? "Not present" : phone;
        latitude = latitude == null ? "Not present" : latitude;
        longitude = longitude == null ? "Not present" : longitude;

        lbl_name.setText(name);
        lbl_address.setText(address);
        lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
        lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
        
        final String sqlLocation = name;
        Bundle extras = activity.getIntent().getExtras();
        String strvalue= extras.getString("username");
        final String uName = strvalue;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
	    java.util.Date utilDate = cal.getTime();
	    final java.sql.Timestamp sqlDate = new  java.sql.Timestamp(utilDate.getTime());
	    			
        checkinButton.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				if(checkinButton.getText().equals("Check In!")||checkinButton.getText().equals("Failure!"))
				{
					WebServiceConnector ws = new WebServiceConnector();								   
				    try 
				    {
						System.out.println(ws.getCheckInResponse(uName,sqlLocation,sqlDate));
						checkinButton.setText("Success!");
					} catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						checkinButton.setText("Failure!");
					}
				}
			}
        });
                              
        URL newurl;
		try 
		{
			newurl = new URL("http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+latitude+","+longitude+"+&sensor=false");
			//newurl = new URL("http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+name+"+&heading=235&sensor=false");
			Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 
			lbl_img.setImageBitmap(bitmap);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
	
	public static Handler getReviews(final String location,final Activity activity)
	{
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() 
		{
		   @Override
		   public void run() 
		   {
				   new ReviewListTask(location,activity).execute();
				   handler.postDelayed(this,5000);	
		   }
		};
		handler.postDelayed(runnable, 0);	
		return handler;
	}
	
	public static Handler getCheckIns(final String location,final Activity activity)
	{
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() 
		{
		   @Override
		   public void run() 
		   {
				   new CheckInListTask(location,activity).execute();
				   handler.postDelayed(this,5000);	
		   }
		};
		handler.postDelayed(runnable, 0);	
		return handler;
	}
}
