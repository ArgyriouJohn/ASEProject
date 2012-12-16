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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.login.UserAuth;
import com.aseproject.places.GooglePlaces;
import com.aseproject.places.PlaceDetails;
import com.aseproject.places.PlacesList;
import com.aseproject.utilities.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * This class creates all the custom items that are pictured on the map.
 * @author John Argyriou 2012
 */
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
	
    /**
    * This constructor creates an CustomItemizedOverlay object.
    * @param Drawable defaultMarker.
    */
	public CustomItemizedOverlay(Drawable defaultMarker) 
	{
		super(boundCenterBottom(defaultMarker));
	}
	
    /**
    * This constructor creates an CustomItemizedOverlay object.
    * @param username a urer's username.
    * @param Drawable defaultMarker.
    * @param Context context
    * @param LinearLayout placeLayoutDetails
    * @param PlaceDetails placeDetails
    * @param GooglePlaces googlePlaces
    */
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
		
		if(((AseMapApplication) mContext.getApplicationContext()).getFBloginStatus() == false)
		{
	        // get profile pic from server.
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
		else
		{			
			 // get profile pic from facebook and show it on map.
			 Bundle extras = ((Activity) mContext).getIntent().getExtras();
		     String strvalue= extras.getString("fbBitmap");
			 Utils.setImgTemp(strvalue);
			 userPic = Utils.getImgTempBmp();
		}		
	}
	
	/**
    * This method adds an overlay to the overlay list.
    * @param DOverlayItem overlay
    */
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);	
    
	    if(userPic!=null)
	    {
	    	// draw profile pic
	    	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);	    	
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	    }
	    else
	    {
	    	// if user has no profile pic , use default.
	    	mOverlays.get(0).setMarker(boundCenterBottom(mContext.getResources().getDrawable(R.drawable.android96)));
	    }
	    populate();
	}
	
	/**
    * This method replaces an overlay with a new one.
    * @param OverlayItem oldOverlay
    *  @param OverlayItem newOverlay
    */
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
	    	// draw profile pic
	    	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	    }
	    else
	    {
	    	// if user has no profile pic , use default.
	    	mOverlays.get(0).setMarker(boundCenterBottom(mContext.getResources().getDrawable(R.drawable.android96)));
	    }
	    populate();
	}
	
	/**
    * This method returns an overlay item.
    * @return OverlayItem 
    */
	@Override
	protected OverlayItem createItem(int i) 
	{
		return mOverlays.get(i);
	}
	
	/**
    * This method the overlay items size.
    * @return OverlayItem size.
    */
	@Override
	public int size() 
	{
		return mOverlays.size();
	}
	
	/**
    * This method defines what will happen in case you click on a overlay item.
    * @param index
    * @return status.
    */
	@Override
	protected boolean onTap(int index) 
	{
	    Button checkinButton = (Button) ((Activity) mContext).findViewById(R.id.checkInButton);	
		if(lock==false) //if you can tap on an item.
		{
		  //get item's coordinates	
		  OverlayItem item = mOverlays.get(index);
		  p = item.getPoint();
		  checkinButton.setText("Check In!");
		  
		  if(index!=0) //for any other marker , than the user's
		  {
			  showBubble = false;
			  
			  // show place information.
			  String placeName = item.getTitle();
			  placeDetails = Utils.getPlaceDetails(placeName, nearPlaces, googlePlaces);
			  Utils.createPlaceInfo(placeLayoutDetails,placeDetails,(Activity) mContext);
			  
			  //Initialize lists.
      		  ListView checkInListView = (ListView) ((Activity) mContext).findViewById(R.id.CheckInListView);
      		  ListView reviewListView = (ListView) ((Activity) mContext).findViewById(R.id.ReviewListView);
  			  ArrayAdapter<String> loading = new ArrayAdapter<String>((Activity) mContext,R.layout.list_item);
  			  loading.add("Loading , please wait!");
  			  checkInListView.setAdapter(loading);
  			  reviewListView.setAdapter(loading);
  			
			  // update review thread!
			  Utils.getReviews(false,placeName,(Activity) mContext).removeCallbacksAndMessages(null);
      		  Utils.getReviews(true,placeName,(Activity) mContext);
			  
      		  // update chechIn thread!
      		  Utils.getCheckIns(false,placeName,(Activity) mContext).removeCallbacksAndMessages(null);
      		  Utils.getCheckIns(true,placeName,(Activity) mContext);
    		  
      		  // set clicked marker red and all others blue.
      		  setClickedMarker(index);
			  
			  placeLayoutDetails =  (LinearLayout) ((Activity)mContext).findViewById(R.id.LinearLayout2);
			  if(placeLayoutDetails.getVisibility()==View.INVISIBLE)
      		  {
				  // show animated place information window.
				  TranslateAnimation anim = new TranslateAnimation(-1000,0,0,0);
	        	  anim.setDuration(1000);
	        	  anim.setFillAfter(true);

	        	  placeLayoutDetails.setVisibility(View.VISIBLE);
	        	  placeLayoutDetails.startAnimation(anim);
	        	  focucedPlace=item.getPoint();
      		  }
			  else
			  {
				  // hide animated place information window.
				  TranslateAnimation anim = new TranslateAnimation(0,-1000,0,0);
	        	  anim.setDuration(1000);
	        	  anim.setFillAfter(true);
	        	  
	        	  if(focucedPlace==p)
	        	  {
	        		  // on hide draw marker blue again.
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
	
	/**
    * This method defines what will happen in case you click on a overlay item.
    * @param index
    * @return status.
    */
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
	 
	 /**
    * This method creates the popup bubble..
    * @param Canvas canvas
    * @param MapView mapView
    */
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
         
        
         // draw text and icon.
         canvas.drawBitmap(bmp, screenPts.x -80, screenPts.y -265, null);
         canvas.drawText("Lat: "+String.valueOf(mapView.getMapCenter().getLatitudeE6()), screenPts.x, screenPts.y -165, paint);
         canvas.drawText("Lon: "+String.valueOf(mapView.getMapCenter().getLongitudeE6()), screenPts.x, screenPts.y -190, paint); 
         
         if(((AseMapApplication) mContext.getApplicationContext()).getFBloginStatus() == true)
         {
        	 // if user uses facebook , get his username.
        	Bundle extras = ((Activity) mContext).getIntent().getExtras();
 	        String strvalue= extras.getString("username");
      	    canvas.drawText(strvalue,screenPts.x -2, screenPts.y -215, paint1);
      	 }
         else
         {
        	 canvas.drawText(username,screenPts.x -2, screenPts.y -215, paint1);
         }

	  }
	 
	 /**
     * This method updates placeDetails.
     * @param PlaceDetails placeDetails
     */
	 public void setPlaceDetails(PlaceDetails placeDetails)
	 {
		 this.placeDetails = placeDetails;
	 }
	 
	 /**
     * This method returns placeDetails.
     * @return PlaceDetails placeDetails
     */
	 public PlaceDetails getPlaceDetails()
	 {
		 return placeDetails;
	 }
	 
	 /**
     * This method updates nearPlaces.
     * @param PlacesList nearPlaces
     */
	 public void setNearPlaces(PlacesList nearPlaces)
	 {
		 this.nearPlaces = nearPlaces;
	 }
	 
	 /**
     * This method returns placeDetails.
     * @return PlaceDetails nearPlaces
     */
	 public PlacesList getNearPlaces()
	 {
		 return nearPlaces;
	 }
	 
	 /**
     * This method draws a marker red.
     * @param int index
     */
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
	 
	 /**
     * This method deletes all items , except from the first one.
     */
	 public void clear()
	 {
		  OverlayItem item = mOverlays.get(0);
		  mOverlays.clear();
		  mOverlays.add(item);
		  populate();
	 }
	 
	 /**
     * This method draws a marker blue.
     */
	 public void loseFocusMarker()
	 {
		 for(int i=1;i<mOverlays.size();i++)
		  {
			 Drawable icon1 = mContext.getResources().getDrawable(R.drawable.bmarker);
			 mOverlays.get(i).setMarker(boundCenterBottom(icon1));
		  }	
	 }
	 
	 /**
     * This method locks/unlocks markers actions.
     * @param boolean flag
     */
	 public void lockMarkers(boolean flag)
	 {
		 lock=flag;
	 }
	 
	 /**
     * This sets the user's profile picture.
     * @param String pic
     */
	 public void setPicturePic(String pic)
	 {
		 	userPic = Utils.decodeImage(pic); 
		 	Bitmap resizedPic = Utils.resizeBitmap(userPic, 110, 110);
		    Bitmap roundedPic = Utils.getRoundedCornerBitmap(resizedPic,48);
	    	mOverlays.get(0).setMarker(boundCenterBottom(new BitmapDrawable(mContext.getResources(),roundedPic)));
	 }
}

