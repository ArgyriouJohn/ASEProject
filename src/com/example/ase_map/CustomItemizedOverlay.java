package com.example.ase_map;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	
	public CustomItemizedOverlay(Drawable defaultMarker) 
	{
		super(boundCenterBottom(defaultMarker));
	}
	
	public CustomItemizedOverlay(Drawable defaultMarker, Context context,LinearLayout placeLayoutDetails,PlaceDetails placeDetails,PlacesList nearPlaces,GooglePlaces googlePlaces) 
	{
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.placeLayoutDetails =placeLayoutDetails;
		this.placeDetails=placeDetails;
		this.googlePlaces =googlePlaces;
	}
	
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);
	    Drawable icon = mContext.getResources().getDrawable(R.drawable.android96);
	    mOverlays.get(0).setMarker(boundCenterBottom(icon));
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
	    Drawable icon = mContext.getResources().getDrawable(R.drawable.android96);
	    mOverlays.get(0).setMarker(boundCenterBottom(icon));
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
		  OverlayItem item = mOverlays.get(index);
		  p = item.getPoint();
		  
		  if(index!=0)
		  {
			  String placeName = item.getTitle();
			  placeDetails = Utils.getPlaceDetails(placeName, nearPlaces, googlePlaces);
			  Utils.createPlaceInfo(placeLayoutDetails,placeDetails,(Activity) mContext);
			  
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
			  // Show/Hide the debubble on Tap using a counter.
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
		  return true;
	}
	
	 @Override
     public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
     {
		 super.draw(canvas, mapView, shadow); 
		 // if p!=null create the bubble
		 if(p!=null)
		 {
			 //popupObject(canvas,mapView);
		 }
		 return true;
     }
	 
	 public void popupObject(Canvas canvas, final MapView mapView)
	 {
		 // Date 
		 Date dNow = new Date( );
	     SimpleDateFormat hours = new SimpleDateFormat ("hh:mm:ss");
	     SimpleDateFormat days = new SimpleDateFormat ("dd.MM.yy");
	     
         //---translate the GeoPoint to screen pixels---
         Point screenPts = new Point();
         mapView.getProjection().toPixels(p, screenPts);
         
         //text properties
         Paint paint = new Paint();
         paint.setTextAlign(Paint.Align.CENTER);
         paint.setTextSize(20);
         paint.setARGB(150, 0, 0, 0); // alpha, r, g, b (Black, semi see-through)

         //---add the marker---
         Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sp);            
         canvas.drawBitmap(bmp, screenPts.x -85, screenPts.y -220, null);
         canvas.drawText(days.format(dNow), screenPts.x, screenPts.y -145, paint);
         canvas.drawText(hours.format(dNow), screenPts.x, screenPts.y -165, paint);
         canvas.drawText("Lat: "+String.valueOf(mapView.getMapCenter().getLatitudeE6()), screenPts.x, screenPts.y -105, paint);
         canvas.drawText("Lon: "+String.valueOf(mapView.getMapCenter().getLongitudeE6()), screenPts.x, screenPts.y -125, paint); 
         //translateAnimation(mapView,p,mContext);
         InputStream is = mContext.getResources().openRawResource(R.drawable.cat);
         Movie mMovie = Movie.decodeStream(is);
         mMovie.draw(canvas,screenPts.x,screenPts.y);
         MapView.LayoutParams screenParameters = new MapView.LayoutParams(
     		    MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, p, 0,
     		    0, MapView.LayoutParams.LEFT | MapView.LayoutParams.BOTTOM);
        // mapView.addView(imageView, screenParameters);
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
}

