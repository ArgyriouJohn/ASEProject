package com.aseproject.map;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.aseproject.login.UserAuth;
import com.aseproject.places.GooglePlaces;
import com.aseproject.places.PlaceDetails;
import com.aseproject.places.PlacesList;
import com.aseproject.utilities.User;
import com.aseproject.utilities.Utils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomItemizedOverlay extends ItemizedOverlay
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private GeoPoint p;
	private Integer counter;
	private LinearLayout placeLayoutDetails;
	private PlaceDetails placeDetails;
	private PlacesList nearPlaces;
	private GooglePlaces googlePlaces;
	private GeoPoint focucedPlace;
	private String username;
	
	private boolean lock;
	private boolean showBubble;
	
	Bitmap userPic;
	
	public CustomItemizedOverlay(Drawable defaultMarker) 
	{
		super(boundCenterBottom(defaultMarker));
	}
	
	public CustomItemizedOverlay(String username , Drawable defaultMarker, Context context,LinearLayout placeLayoutDetails,PlaceDetails placeDetails,GooglePlaces googlePlaces) 
	{
		super(boundCenterBottom(defaultMarker));
		this.username=username;
		this.mContext = context;
		this.placeLayoutDetails =placeLayoutDetails;
		this.placeDetails=placeDetails;
		this.googlePlaces =googlePlaces;
		lock=false;
		showBubble=true;
		
        // get profile pic from server!
        User entry = new User((Activity) mContext);
        entry.open();
        Bundle extras = ((Activity) mContext).getIntent().getExtras();
        String strvalue= extras.getString("username");
        UserAuth user = entry.retrieveProfileInfo(strvalue);
        if(user.getPicture()!=null)
	    {
        	userPic = Utils.decodeImage(user.getPicture()); 
	    }
        entry.close();
	}
	
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);	
    
	    if(userPic!=null)
	    {
	    	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);	    	
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	    }
	    else
	    {
	    	mOverlays.get(0).setMarker(boundCenterBottom(mContext.getResources().getDrawable(R.drawable.android96)));
	    }
	    populate();
	}
	
	public void setOverlay(OverlayItem oldOverlay,OverlayItem newOverlay) 
	{
		int index = mOverlays.indexOf(oldOverlay);
	    mOverlays.set(index,newOverlay);
	    if(counter == null)
		{
			  counter=1;
		}
	    else if(counter%2 != 0)
		{   // experimental!!! in case the bubble is open!
			this.onTap(index);
			this.onTap(index);
		}    
	    
	    if(userPic!=null)
	    {
	    	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	    }
	    else
	    {
	    	mOverlays.get(0).setMarker(boundCenterBottom(mContext.getResources().getDrawable(R.drawable.android96)));
	    }
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) 
	{
		return mOverlays.get(i);
	}
	
	@Override
	public int size() 
	{
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) 
	{
		if(lock==false)
		{
		  OverlayItem item = mOverlays.get(index);
		  p = item.getPoint();
		  
		  if(index!=0)
		  {
			  showBubble = false;
			  
			  String placeName = item.getTitle();
			  placeDetails = Utils.getPlaceDetails(placeName, nearPlaces, googlePlaces);
			  Utils.createPlaceInfo(placeLayoutDetails,placeDetails,(Activity) mContext);
			  
			  // update review thread!
			  Utils.getReviews(placeName,(Activity) mContext).removeCallbacksAndMessages(null);
      		  Utils.getReviews(placeName,(Activity) mContext);
			  
      		  // update chechIn thread!
      		  Utils.getCheckIns(placeName,(Activity) mContext).removeCallbacksAndMessages(null);
      		  Utils.getCheckIns(placeName,(Activity) mContext);
    		  
      		  // set clicked marker red and all others blue.
      		  setClickedMarker(index);
			  
			  placeLayoutDetails =  (LinearLayout) ((Activity)mContext).findViewById(R.id.LinearLayout2);
			  if(placeLayoutDetails.getVisibility()==View.INVISIBLE)
      		  {
				  TranslateAnimation anim = new TranslateAnimation(-1000,0,0,0);
	        	  anim.setDuration(1000);
	        	  anim.setFillAfter(true);

	        	  placeLayoutDetails.setVisibility(View.VISIBLE);
	        	  placeLayoutDetails.startAnimation(anim);
	        	  focucedPlace=item.getPoint();
      		  }
			  else
			  {
				  TranslateAnimation anim = new TranslateAnimation(0,-1000,0,0);
	        	  anim.setDuration(1000);
	        	  anim.setFillAfter(true);
	        	  
	        	  if(focucedPlace==p)
	        	  {
	        		  Drawable icon1 = mContext.getResources().getDrawable(R.drawable.bmarker);
	    			  mOverlays.get(index).setMarker(boundCenterBottom(icon1));
		        	  placeLayoutDetails.setVisibility(View.INVISIBLE);
		        	  placeLayoutDetails.startAnimation(anim);
	        	  }
	        	  else
	        	  {
	        		  focucedPlace=item.getPoint();
	        	  }
			  }
		  }
		  else
		  {
			  showBubble = true;
			  // Show/Hide the bubble on Tap using a counter.
			  if(counter == null)
			  {
				  counter=1;
			  }
			  else if(counter%2 != 0)
			  {
				  p = null; // if p=null , bubble gets removed. draw listens to that.
				  counter++;
			  }
			  else 
			  {
				  counter++;
			  }
		  }
		}
		return true;
	}
	
	 @Override
     public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
     {	
		 super.draw(canvas, mapView, shadow); 
		 if(showBubble==true)
		 {			 			 
			 // create the bubble
			 if(p!=null)
			 {
				 popupObject(canvas,mapView);
			 }
		 }
		 return true;
     }
	 
	 public void popupObject(Canvas canvas, final MapView mapView)
	 {	     
         //---translate the GeoPoint to screen pixels---
         Point screenPts = new Point();
         mapView.getProjection().toPixels(p, screenPts);
         
         //text properties
         Typeface tf = Typeface.create("Helvetica",Typeface.BOLD);
         Paint paint = new Paint();
         Paint paint1 = new Paint();
         paint1.setTypeface(tf);
         paint1.setTextSize(22);    
         paint1.setARGB(150, 0, 0, 0); // alpha, r, g, b (Black, semi see-through)
         paint1.setTextAlign(Paint.Align.CENTER);
         paint.setTextAlign(Paint.Align.CENTER);
         paint.setTextSize(18);        
         paint.setARGB(150, 0, 0, 0); // alpha, r, g, b (Black, semi see-through)

         //---add the marker---
         Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sp);    
         
        
         
         canvas.drawBitmap(bmp, screenPts.x -80, screenPts.y -265, null);
         canvas.drawText("Lat: "+String.valueOf(mapView.getMapCenter().getLatitudeE6()), screenPts.x, screenPts.y -165, paint);
         canvas.drawText("Lon: "+String.valueOf(mapView.getMapCenter().getLongitudeE6()), screenPts.x, screenPts.y -190, paint);   
         canvas.drawText(username,screenPts.x -2, screenPts.y -215, paint1);
	  }
	 
	 public void setPlaceDetails(PlaceDetails placeDetails)
	 {
		 this.placeDetails = placeDetails;
	 }
	 
	 public PlaceDetails getPlaceDetails()
	 {
		 return placeDetails;
	 }
	 
	 public void setNearPlaces(PlacesList nearPlaces)
	 {
		 this.nearPlaces = nearPlaces;
	 }
	 
	 public PlacesList getNearPlaces()
	 {
		 return nearPlaces;
	 }
	 
	 public void setClickedMarker(int index)
	 {
		  Drawable icon = mContext.getResources().getDrawable(R.drawable.rmarker);
		  mOverlays.get(index).setMarker(boundCenterBottom(icon));
		  
		  for(int i=1;i<mOverlays.size();i++)
		  {
			  if(i!=index)
			  {
				  Drawable icon1 = mContext.getResources().getDrawable(R.drawable.bmarker);
				  mOverlays.get(i).setMarker(boundCenterBottom(icon1));
			  }
		  }	
		  populate();
	 }	
	 	 
	 public void clear()
	 {
		  OverlayItem item = mOverlays.get(0);
		  mOverlays.clear();
		  mOverlays.add(item);
		  populate();
	 }
	 
	 public void loseFocusMarker()
	 {
		 for(int i=1;i<mOverlays.size();i++)
		  {
			 Drawable icon1 = mContext.getResources().getDrawable(R.drawable.bmarker);
			 mOverlays.get(i).setMarker(boundCenterBottom(icon1));
		  }	
	 }
	 
	 public void lockMarkers(boolean flag)
	 {
		 lock=flag;
	 }
	 
	 public void setPicturePic(String pic)
	 {
		 	userPic = Utils.decodeImage(pic); 
		 	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	 }
}

