package com.example.ase_map;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    GooglePlaces googlePlaces = new GooglePlaces();
    PlacesList nearPlaces;
    PlaceDetails placeDetails;
    Button button;
    
    // Places Listview
    ListView lv;
    LinearLayout ll;

    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        
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
        
     // Getting listview
        lv = (ListView) findViewById(R.id.listView1);
        
        ll = (LinearLayout) findViewById(R.id.LinearLayout2);
        ll.setVisibility(View.INVISIBLE);
        
        lv.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
        	{
	        		String placeName = ((TextView) view).getText().toString();
	        		for(Place place : nearPlaces.results)
	            	{
	            		if(place.name.equals(placeName))
	            		{
	            			try {
	            				placeDetails = googlePlaces.getPlaceDetails(place.reference);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            		}
	            	}
	        		 
	        		ll.setVisibility(View.VISIBLE);
	        		 
	        		TranslateAnimation anim = new TranslateAnimation(300,0,0,0);
	            	anim.setDuration(1000);
	            	anim.setFillAfter(true);
	            	ll.startAnimation(anim);
	            	
					String name = placeDetails.result.name;
                    String address = placeDetails.result.formatted_address;
				    String phone = placeDetails.result.formatted_phone_number;
                    String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                    String longitude = Double.toString(placeDetails.result.geometry.location.lng);
                    String imgURL = placeDetails.result.icon;
                    
                    TextView lbl_name = (TextView) findViewById(R.id.name);
                    TextView lbl_address = (TextView) findViewById(R.id.address);
                    TextView lbl_phone = (TextView) findViewById(R.id.phone);
                    TextView lbl_location = (TextView) findViewById(R.id.location);
                    ImageView lbl_img= (ImageView) findViewById(R.id.imageView1);
                    
                    name = name == null ? "Not present" : name; // if name is null display as "Not present"
                    address = address == null ? "Not present" : address;
                    phone = phone == null ? "Not present" : phone;
                    latitude = latitude == null ? "Not present" : latitude;
                    longitude = longitude == null ? "Not present" : longitude;

                    lbl_name.setText(name);
                    lbl_address.setText(address);
                    lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                    lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                    
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
             }
        });
        
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {              	
            	button.setTextColor(-16777216);           	
            	if(button.getText().equals("Show Nearest Places")||button.getText().equals("Update Nearest Places"))
            	{	
	            	try 
	            	{
						nearPlaces = googlePlaces.search(latitude, longitude,500,null);
						button.setText("Hide Nearest Places");
					} 
	            	catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                ArrayList<String> placesNames = new ArrayList<String>();
	            	for(Place place : nearPlaces.results)
	            	{
	            		placesNames.add(place.name);
	            	}
	            	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.list_item, placesNames);
	            	// Assign adapter to ListView
	            	lv.setAdapter(adapter); 

	            	TranslateAnimation anim = new TranslateAnimation(0,0,-1000,0);
	            	anim.setDuration(lv.getAdapter().getCount()*100);
	            	anim.setFillAfter(true);
	            	lv.setAnimation(anim);
            	}
            	else if(button.getText().equals("Hide Nearest Places"))
            	{   
            		TranslateAnimation anim2 = new TranslateAnimation(0,-1000,0,0);
            		anim2.setDuration(1000);
	            	anim2.setFillAfter(true);
	            	ll.startAnimation(anim2);
            		
            		TranslateAnimation anim1 = new TranslateAnimation(0,1000,0,0);
            		anim1.setDuration(1000);
	            	anim1.setFillAfter(true);
	            	anim1.setAnimationListener(new AnimationListener() 
	            	{
	            	    public void onAnimationEnd(Animation animation) 
	            	    {
	            	    	button.setText("Show Nearest Places");
	    	            	ArrayList<String> placesNames = new ArrayList<String>();
	    	            	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.list_item, placesNames);
	    	            	lv.setAdapter(adapter); 
	            	    }
	            	    public void onAnimationStart(Animation animation){}
						public void onAnimationRepeat(Animation animation){}
	            	});
	            	lv.startAnimation(anim1);
	            }
            }
        });
        
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
    	
    	// Update itemizedoverlay when location changes.
    	List<Overlay> mapOverlays = mapView.getOverlays();
        CustomItemizedOverlay itemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
        OverlayItem oldOverlayitem = itemizedoverlay.getItem(0);
        OverlayItem newOverlayitem = new OverlayItem(point," "," ");
        itemizedoverlay.setOverlay(oldOverlayitem,newOverlayitem);
        mapOverlays.set(0,itemizedoverlay);
        WebServiceConnector ws = new WebServiceConnector();
        /*try 
        {
			System.out.println(ws.getResponse());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        if(lv.getAdapter()!=null)
        {
        	if(!lv.getAdapter().isEmpty())
            {
        		button.setText("Update Nearest Places");
        		button.setTextColor(-65536);
            }
        }
        
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
}
