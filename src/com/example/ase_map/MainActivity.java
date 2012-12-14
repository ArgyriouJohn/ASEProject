package com.example.ase_map;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends MapActivity implements LocationListener 
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private String provider;
	double latitude;
    double longitude;
    Button logOutButton;
	
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        
    	Location entry = new Location(MainActivity.this);
        
        logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction("com.package.ACTION_LOGOUT");
				sendBroadcast(broadcastIntent);
				
				Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(logInIntent);
			}
		});
        
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        // Check if enabled and if not send user to the GSP settings
        if (!enabled) 
        {
        	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        	startActivity(intent);
        } 
        
        // Define the criteria how to select the location in provider -> use default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        // Initialize the location fields
        if (location != null) 
        {
          System.out.println("Provider " + provider + " has been selected.");
          //onLocationChanged(location);
        } else 
        {
          //latituteField.setText("Location not available");
          //longitudeField.setText("Location not available");
        }
        
        // GPS Location
        GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude *1E6));
        
        // Dispalay an itemizedoverlay on the map.
        List<Overlay> mapOverlays = mapView.getOverlays();	
        Drawable drawable = this.getResources().getDrawable(R.drawable.android96);
        CustomItemizedOverlay itemizedoverlay = new CustomItemizedOverlay(drawable, this);
        OverlayItem overlayitem = new OverlayItem(point," "," ");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        // Initialize map fields.
        mapController = mapView.getController();
        mapController.animateTo(point);
        mapController.setZoom(13);
        mapView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected boolean isRouteDisplayed() 
    {
        return false;
    }
    
    /* Request updates at startup */
    @Override
    protected void onResume() 
    {
      super.onResume();
      boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (!enabled) 
      {
      	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      	startActivity(intent);
      } 
      locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() 
    {
      super.onPause();
      locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location) 
    {   	
    	latitude = location.getLatitude();
    	longitude = location.getLongitude();
    	GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude *1E6));
    	
    	Location entry = new Location(MainActivity.this);
    	
    	// Update itemizedoverlay when location changes.
    	List<Overlay> mapOverlays = mapView.getOverlays();
        CustomItemizedOverlay itemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
        OverlayItem oldOverlayitem = itemizedoverlay.getItem(0);
        OverlayItem newOverlayitem = new OverlayItem(point," "," ");
        itemizedoverlay.setOverlay(oldOverlayitem,newOverlayitem);
        mapOverlays.set(0,itemizedoverlay);
    	
    	mapController.animateTo(point);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
    	// TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider) 
    {
    	Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }

    public void onProviderDisabled(String provider) 
    {
    	Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onBackPressed() {    			
		Dialog d = new Dialog(MainActivity.this);
		d.setTitle(":(");
		TextView tv = new TextView(MainActivity.this);
		tv.setText("Please use the Log Out button at the top to logout!");
		d.setContentView(tv);
		d.show();
    }

}
