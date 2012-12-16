package com.aseproject.utilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aseproject.checkin.CheckInListTask;
import com.aseproject.map.R;
import com.aseproject.places.GooglePlaces;
import com.aseproject.places.Place;
import com.aseproject.places.PlaceDetails;
import com.aseproject.places.PlacesList;
import com.aseproject.review.ReviewDialog;
import com.aseproject.review.ReviewListTask;

public class Utils 
{
	static Handler reviewThread = new Handler(); 
	static Handler checkInThread = new Handler(); 
	static String imgTemp;
	
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
				} catch (Exception e) {e.printStackTrace();}
    		}
    	}
		return placeDetails;
	}
	
	public static String createPlaceInfo(LinearLayout layout,PlaceDetails placeDetails,final Activity activity)
	{		
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

        lbl_name.setText(Html.fromHtml("<b>Name:</b> "+name));
        lbl_address.setText(Html.fromHtml("<b>Address:</b> "+address));
        lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
        lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
        
        Bundle extras = activity.getIntent().getExtras();
        String strvalue= extras.getString("username");
        final String uName = strvalue;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
	    java.util.Date utilDate = cal.getTime();
	    final java.sql.Timestamp sqlDate = new  java.sql.Timestamp(utilDate.getTime());
	    
	    final Button checkinButton =(Button) activity.findViewById(R.id.checkInButton);		
		final Button reviewButton =(Button) activity.findViewById(R.id.reviewButton);
		reviewButton.setText("Write a review...");	
		final String locationName = name;
		
		//Review Button Handler
		reviewButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				//Send to the SubmitReview Dialog the name of the place that was clicked
				Intent reviewDialogIntent = new Intent(activity.getBaseContext(), ReviewDialog.class);
				reviewDialogIntent.putExtra("locName", locationName);
				reviewDialogIntent.putExtra("userName", uName);
				activity.startActivity(reviewDialogIntent);
			}
		});
					
		//checkIn Button Handler
        checkinButton.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				if(checkinButton.getText().equals("Check In!")||checkinButton.getText().equals("Failure!"))
				{
					WebServiceConnector ws = new WebServiceConnector();								   
				    try 
				    {
						System.out.println(ws.getCheckInResponse(uName,locationName,sqlDate));
						checkinButton.setText("You have successfully checked in!");
					} catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						checkinButton.setText("Failure!");
					}
				} else {
					checkinButton.setText("Check In!");
				}
			}
        });
                              
        URL newurl;
		try 
		{
			newurl = new URL("http://maps.googleapis.com/maps/api/streetview?size=500x400&location="+latitude+","+longitude+"+&sensor=false");
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
		Runnable runnable = new Runnable() 
		{
		   @Override
		   public void run() 
		   {
			   new ReviewListTask(location,activity).execute();
			   reviewThread.postDelayed(this,4000);	
		   }
		};
		reviewThread.postDelayed(runnable, 0);
		return reviewThread;
	}
		
	public static Handler getCheckIns(final String location,final Activity activity)
	{
		Runnable runnable = new Runnable() 
		{
		   @Override
		   public void run() 
		   {
			   new CheckInListTask(location,activity).execute();
			   checkInThread.postDelayed(this,4000);	
		   }
		};
		checkInThread.postDelayed(runnable, 0);	
		return checkInThread;
	}
	
	public static Bitmap decodeImage(String input)
	{
        byte [] encodeByte=Base64.decode(input,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	}
	
	public static Bitmap resizeBitmap(Bitmap input,int nWidth, int nHeight)
	{	
		//resize img
		int width = input.getWidth();
		int height = input.getHeight();
		int newWidth = nWidth;
		int newHeight = nHeight;
				
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
			
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		//recreate the new Bitmap
		return Bitmap.createBitmap(input, 0, 0,width, height, matrix, true);				
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int pixels)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
	}
	
	public static void setImgTemp(String img)
	{
		imgTemp=img;
	}
	
	public static String getImgTemp()
	{
		return imgTemp;
	}

}

