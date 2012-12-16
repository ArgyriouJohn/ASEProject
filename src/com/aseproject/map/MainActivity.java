package com.aseproject.map;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import com.aseproject.facebook.FbShare;
import com.aseproject.location.LocationsHandling;
import com.aseproject.login.LoginActivity;
import com.aseproject.login.UserAuth;
import com.aseproject.places.GooglePlaces;
import com.aseproject.places.Place;
import com.aseproject.places.PlaceDetails;
import com.aseproject.places.PlacesList;
import com.aseproject.profile.ProfileActivity;
import com.aseproject.utilities.AseMapApplication;
import com.aseproject.utilities.User;
import com.aseproject.utilities.Utils;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * This class is the main class that extends the mapMactivity class.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 * @author Socratis Michaelides 2012
 * @see CustomItemizedOverlay
 */
public class MainActivity extends MapActivity implements LocationListener 
{
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	
	private String provider;
	private String posStatus;
	
	double latitude = 50.867714;
    double longitude = -0.087478;
    String TAG = "DEBUG";
    
    GooglePlaces googlePlaces = new GooglePlaces();
    PlacesList nearPlaces;
    PlaceDetails placeDetails;
    
    ImageButton logOutButton;
    ImageButton showNearLocButton;
    ImageButton accountInfoButton, fbButton;
    ImageButton visInvis;
    
    //fb pic
    Bitmap bmap;
    Button checkInButton;
    
    TextView date;
    ListView placesListView;
    LinearLayout placeLayoutDetails;
    
    Date dNow;
	
    /**
    * Initialize variables.
    * @param Bundle savedInstanceState
    */
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Set loose policy.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        
        //Tab View Start
        TabHost tabHost=(TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        
        // set tab icons.
        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Check Ins", getResources().getDrawable(R.drawable.checkin));

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Reviews", getResources().getDrawable(R.drawable.review));
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
       
        //Retrieve and show date.
        dNow = new Date( );
        SimpleDateFormat days = new SimpleDateFormat ("dd.MM.yy");
	    date = (TextView) findViewById(R.id.textView1);
	    date.setText(days.format(dNow));
	    
	    //set up facebook data
		//check if logged in with fb
	    if(getIntent().hasExtra("fbBitmap") || (((AseMapApplication) getApplication()).getFBloginStatus() == true))
	    {   //get the active session
	    	final Session session = Session.getActiveSession();
	    	if (session != null && session.isOpened()) 
	    	{
	           Request request = Request.newMeRequest(session, new Request.GraphUserCallback() 
	           {
	               @Override
	               public void onCompleted(GraphUser user, Response response) 
	               {
	                   if (session == Session.getActiveSession()) 
	                   {
	                       if (user != null) {}
	                   }
	           }});
	           Request.executeBatchAsync(request);
	    	}
	     }
	    
	    // if there is na interet connection
        if (isOnline())
        {
            System.out.println("INTERNET's FINE"); 
        }
        else 
        { 
            try 
            {
            	// if not , show alert window.
            	new AlertDialog.Builder(getBaseContext()).setTitle("Info").setMessage("No internet connection."+"\n"
            		+ "Please check your internet settings!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
            }
            catch(Exception e) 
            {
            	Log.d(Constants.ACCOUNT_NAME, "Show Dialog: "+e.getMessage());
            }
        }
                       
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
        } 
        
        // GPS Location
        GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude *1E6));
        
        // Dispalay an itemizedoverlay on the map.
        final List<Overlay> mapOverlays = mapView.getOverlays();
        Bundle extras = getIntent().getExtras();
        final String username= extras.getString("username");
        Drawable drawable = this.getResources().getDrawable(R.drawable.bmarker);
        final CustomItemizedOverlay itemizedoverlay = new CustomItemizedOverlay(username,drawable, this,placeLayoutDetails,placeDetails, googlePlaces);
        OverlayItem overlayitem = new OverlayItem(point," "," ");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
                       
     // Getting listview
        placesListView = (ListView) findViewById(R.id.listView1);
        placeLayoutDetails = (LinearLayout) findViewById(R.id.LinearLayout2);
        placeLayoutDetails.setVisibility(View.INVISIBLE);
        
        placesListView.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
        	{
        	    Button checkinButton = (Button) findViewById(R.id.checkInButton);		
        		String placeName = ((TextView) view).getText().toString();
        		placeDetails = Utils.getPlaceDetails(placeName, nearPlaces, googlePlaces);
        		
        		// set selected marker red and all others blue. 
        		List<Overlay> mapOverlays = mapView.getOverlays();
                CustomItemizedOverlay updatedItemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
             	updatedItemizedoverlay.setClickedMarker(position+1);
                mapOverlays.set(0,updatedItemizedoverlay);

                checkinButton.setText("Check In!");
                
        		if(placeLayoutDetails.getVisibility()==View.INVISIBLE)
        		{
        			// show animated list.
	        		TranslateAnimation anim = new TranslateAnimation(300,0,0,0);
	            	anim.setDuration(1000);
	            	anim.setFillAfter(true);
	            	placeLayoutDetails.startAnimation(anim);
        		}
        		placeLayoutDetails.setVisibility(View.VISIBLE);
        		Utils.createPlaceInfo(placeLayoutDetails,placeDetails,MainActivity.this);
        		
        		//Initialize lists.
        		ListView checkInListView = (ListView) findViewById(R.id.CheckInListView);
        		ListView reviewListView = (ListView) findViewById(R.id.ReviewListView);
    			ArrayAdapter<String> loading = new ArrayAdapter<String>(MainActivity.this,R.layout.list_item);
    			loading.add("Loading , please wait!");
    			checkInListView.setAdapter(loading);
    			reviewListView.setAdapter(loading);
    			
  			  	// update review thread!      		
  			  	Utils.getReviews(false,placeName,MainActivity.this).removeCallbacksAndMessages(null);
        		Utils.getReviews(true,placeName,MainActivity.this);
  			  
        		// update chechIn thread!
        		Utils.getCheckIns(false,placeName,MainActivity.this).removeCallbacksAndMessages(null);
        		Utils.getCheckIns(true,placeName,MainActivity.this);
             }
        });
        
        //Visible/Invisible Button Handler
        visInvis = (ImageButton) findViewById(R.id.visInvisImmageButton);
        User entry = new User(MainActivity.this);
	    entry.open();
	    final UserAuth user = entry.retrieveProfileInfo(username);
	    final int userVisibility = user.getVisibility();
	    entry.close();
	    System.out.println(userVisibility);
	    
	    // change icon on visibility change
	    if(userVisibility == 0) 
	    {
	    	visInvis.setBackgroundResource(R.drawable.vis);
	    } 
	    else 
	    {
	    	visInvis.setBackgroundResource(R.drawable.invis);
	    }
	    
        visInvis.setOnClickListener(new View.OnClickListener()
        {	
			@Override
			public void onClick(View v) 
			{
				// animate icon
				AlphaAnimation alphaDown = new AlphaAnimation(1.0f, 0.3f);
			    AlphaAnimation alphaUp = new AlphaAnimation(0.3f, 1.0f);
			    alphaDown.setDuration(1000);
			    alphaUp.setDuration(500);
			    alphaDown.setFillAfter(true);
			    alphaUp.setFillAfter(true);
			    v.startAnimation(alphaUp);		 
			    
			    User entry = new User(MainActivity.this);
			    entry.open();
			    final UserAuth user = entry.retrieveProfileInfo(username);
			    if(user.getVisibility() == 0) // promt message on visibility change
			    {
			    	entry.setVisibility(username, 1);
			    	visInvis.setBackgroundResource(R.drawable.invis);
				    Toast.makeText(getApplicationContext(), "Your details are now invisible to others!", Toast.LENGTH_LONG).show();
			    } 
			    else 
			    {
			    	entry.setVisibility(username, 0);
			    	visInvis.setBackgroundResource(R.drawable.vis);
				    Toast.makeText(getApplicationContext(), "Your details are now visible to others!", Toast.LENGTH_LONG).show();
			    }
			    entry.close();
			}
		}); 
        
        
        fbButton = (ImageButton) findViewById(R.id.fb_btn);
        fbButton.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				// go to facebook share window.
				Intent i = new Intent(MainActivity.this, FbShare.class);
		        startActivity(i);			
			}
        });
        
        // activate/deavtivate fb button.
	    if(getIntent().hasExtra("fbLogin"))
	    {
	    	fbButton.setEnabled(true);
	    }
	    else
	    {
	    	fbButton.setEnabled(false);
	    }

        accountInfoButton = (ImageButton) findViewById(R.id.showProfileImageButton);
        accountInfoButton.setOnClickListener(new View.OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				//animate icon.
				AlphaAnimation alphaDown = new AlphaAnimation(1.0f, 0.3f);
			    AlphaAnimation alphaUp = new AlphaAnimation(0.3f, 1.0f);
			    alphaDown.setDuration(1000);
			    alphaUp.setDuration(500);
			    alphaDown.setFillAfter(true);
			    alphaUp.setFillAfter(true);
			    v.startAnimation(alphaUp);
			    
			    // go to profile window.
		        Bundle extras = getIntent().getExtras();
		        String strvalue= extras.getString("username");
		        String intent = "MainActivity";
				Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);				
				profileIntent.putExtra("message", strvalue);
				profileIntent.putExtra("intent", intent);
				profileIntent.putExtra("visibility", user.getVisibility());
				startActivity(profileIntent);			
			}
		});
        
        posStatus = "Show";
        showNearLocButton = (ImageButton) findViewById(R.id.imageButton1);
        showNearLocButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {   
            	//animate icon.
				AlphaAnimation alphaDown = new AlphaAnimation(1.0f, 0.3f);
			    AlphaAnimation alphaUp = new AlphaAnimation(0.3f, 1.0f);
			    alphaDown.setDuration(1000);
			    alphaUp.setDuration(500);
			    alphaDown.setFillAfter(true);
			    alphaUp.setFillAfter(true);
			    v.startAnimation(alphaUp);	
			    
            	List<Overlay> mapOverlays = mapView.getOverlays();
                CustomItemizedOverlay itemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
            	
            	if(posStatus.equals("Show")||posStatus.equals("Update"))
            	{	
            		// show animated palces list.
	            	try 
	            	{
						nearPlaces = googlePlaces.search(latitude, longitude,500,null);
						posStatus="Hide";
					} 
	            	catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                ArrayList<String> placesNames = new ArrayList<String>();
	                itemizedoverlay.clear();
	                	                
	            	for(Place place : nearPlaces.results)
	            	{
	            		// get places information.
	            		placesNames.add(place.name);
	            		GeoPoint geoPoint = new GeoPoint((int) (place.geometry.location.lat * 1E6),(int) (place.geometry.location.lng * 1E6));
	            		
	            		OverlayItem overlayitem = new OverlayItem(geoPoint, place.name,place.vicinity);
	            		itemizedoverlay.addOverlay(overlayitem);
	            	}
	            	itemizedoverlay.setPlaceDetails(placeDetails);
	            	itemizedoverlay.setNearPlaces(nearPlaces);
	            	itemizedoverlay.lockMarkers(true);
	            	mapOverlays.set(0,itemizedoverlay);
	            	 	
	            	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.list_item, placesNames);
	            	placesListView.setAdapter(adapter); 
	            	
	            	// animate the list.
	            	TranslateAnimation anim = new TranslateAnimation(0,0,-1000,0);
	            	anim.setDuration(placesListView.getAdapter().getCount()*100);
	            	anim.setFillAfter(true);
	            		            	
	            	placesListView.startAnimation(anim);
            	}
            	else if(posStatus.equals("Hide"))
            	{   
            		// hide animated list.
            		if(placeLayoutDetails.getVisibility()==View.VISIBLE)
	        		{
            			//animate the list.
	            		TranslateAnimation anim2 = new TranslateAnimation(0,-1000,0,0);
	            		anim2.setDuration(1000);
		            	anim2.setFillAfter(true);
		            	placeLayoutDetails.startAnimation(anim2);
	        		}
	            	placeLayoutDetails.setVisibility(View.INVISIBLE);
	            	
	            	// all Markers become blue.
	            	itemizedoverlay.loseFocusMarker();
	            	itemizedoverlay.lockMarkers(false);
	            	mapOverlays.set(0,itemizedoverlay);
            		
            		TranslateAnimation anim1 = new TranslateAnimation(0,1000,0,0);
            		anim1.setDuration(1000);
	            	anim1.setFillAfter(true);
	            	anim1.setAnimationListener(new AnimationListener() 
	            	{
	            	    public void onAnimationEnd(Animation animation) 
	            	    {
	            	    	// upadate list parameters.
	            	    	posStatus="Show";
	    	            	ArrayList<String> placesNames = new ArrayList<String>();
	    	            	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.list_item, placesNames);
	    	            	placesListView.setAdapter(adapter); 
	            	    }
	            	    public void onAnimationStart(Animation animation){}
						public void onAnimationRepeat(Animation animation){}
	            	});
	            	placesListView.startAnimation(anim1);
	            }
            }
        });
        
        logOutButton = (ImageButton) findViewById(R.id.imageButton2);
        logOutButton.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				//animate icon.
				AlphaAnimation alphaDown = new AlphaAnimation(1.0f, 0.3f);
			    AlphaAnimation alphaUp = new AlphaAnimation(0.3f, 1.0f);
			    alphaDown.setDuration(1000);
			    alphaUp.setDuration(500);
			    alphaDown.setFillAfter(true);
			    alphaUp.setFillAfter(true);
			    v.startAnimation(alphaUp);
			    
				if(((AseMapApplication) getApplication()).getFBloginStatus() == false)	
				{			    
					 // Create new entry for the database and when the location is changed put those values in the db.
			        LocationsHandling locEntry = new LocationsHandling(MainActivity.this);
			        Bundle extras = getIntent().getExtras();
			        String strvalue= extras.getString("username");        
			        locEntry.open();
			        locEntry.createEntry(strvalue, longitude, latitude);
			        System.out.println("Locs coming from user: " +strvalue);
			        locEntry.clearDb();
			        System.out.println("Local db on logOut: " +locEntry.getData());
			        locEntry.close();    	
	
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.package.ACTION_LOGOUT");
					sendBroadcast(broadcastIntent);
					
					// go to logout window.
					Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(logInIntent);
				}
				else
				{
					 // Create new entry for the database and when the location is changed put those values in the db.
					LocationsHandling locEntry = new LocationsHandling(MainActivity.this);
			        Bundle extras = getIntent().getExtras();
			        String strvalue= extras.getString("username");        
			        locEntry.open();
			        locEntry.createEntry(strvalue, longitude, latitude);
			        System.out.println("Locs coming from user: " +strvalue);
			        locEntry.clearDb();
			        System.out.println("Local db on logOut: " +locEntry.getData());
			        locEntry.close();    	
	
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction("com.package.ACTION_LOGOUT");
					sendBroadcast(broadcastIntent);
					
					// facebook , go to logout window.
					Intent i = new Intent(MainActivity.this, LoginActivity.class);
					i.putExtra("fbLogout", "logout");
					startActivity(i);	
				}		
			}
		});
   
        // Initialize map fields.
        mapController = mapView.getController();
        mapController.animateTo(point);
        mapController.setZoom(17);
        mapView.invalidate();
    }
    
    
    /**
    * This method creates a menu.
    * @param Menu menu
    * @return status
    */    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
	* This method dispalys a route.
	* @return status
	*/ 
    @Override
    protected boolean isRouteDisplayed() 
    {
        return false;
    }
    
    /**
	* This method describes aht happens when the activity resumes.
	*/ 
    @Override
    protected void onResume() 
    {
       /* Request updates at startup */
      super.onResume();
      boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      if (!enabled) 
      {
    	// if there is no internet , go to the settings screen
      	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      	startActivity(intent);
      } 
      locationManager.requestLocationUpdates(provider, 400, 1, this);
      
      // update profile pic. 
      if(Utils.getImgTemp()!=null && !Utils.getImgTemp().equals(""))
      {
		  List<Overlay> mapOverlays = mapView.getOverlays();
		  CustomItemizedOverlay itemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
		  itemizedoverlay.setPicturePic(Utils.getImgTemp());
		  mapOverlays.set(0,itemizedoverlay);
      }  
    }

    /**
   	* This method describes aht happens when the activity pauses.
   	*/ 
    @Override
    protected void onPause() 
    {
      /* Remove the locationlistener updates when Activity is paused */
      super.onPause();
      locationManager.removeUpdates(this);
      Utils.setImgTemp(null);
    }

    /**
   	* This method describes what  happens when the location changes.
   	* @param Location location
   	*/
    public void onLocationChanged(Location location) 
    {   
    	// define a threshold of location cahnge
    	if(Math.abs(latitude - location.getLatitude()) > 0.0005 || Math.abs(longitude - location.getLongitude()) > 0.0005)
    	{
    		// get coordinates.
	    	latitude = location.getLatitude();
	    	longitude = location.getLongitude();    	
	    	GeoPoint point = new GeoPoint((int)(latitude * 1E6), (int)(longitude *1E6));    
	    	 	
	    	// Update itemizedoverlay when location changes.
	    	List<Overlay> mapOverlays = mapView.getOverlays();
	        CustomItemizedOverlay itemizedoverlay = (CustomItemizedOverlay) mapOverlays.get(0);
	    	
	        // Create new entry for the database and when the location is changed put those values in the local db.
	        LocationsHandling locEntry = new LocationsHandling(MainActivity.this);
	        Bundle extras = getIntent().getExtras();
	        String strvalue= extras.getString("username");        
	        locEntry.open();
	        locEntry.createLocalEntry(strvalue, longitude, latitude);
	        System.out.println("Local db on change: " + locEntry.getData());
	        locEntry.close();    	
	    	
	        OverlayItem oldOverlayitem = itemizedoverlay.getItem(0);
	        OverlayItem newOverlayitem = new OverlayItem(point," "," ");
	        itemizedoverlay.setOverlay(oldOverlayitem,newOverlayitem);
	        mapOverlays.set(0,itemizedoverlay);
	        
	        // set updated flag.
	        if(placesListView.getAdapter()!=null)
	        {
	        	if(!placesListView.getAdapter().isEmpty())
	            {
	        		posStatus="Update";
	            }
	        }
	    	
	    	mapController.animateTo(point);
    	}
    }

    /**
   	* This method describes what  happens when the status changes.
   	* @param String provider
   	* @param int status
   	* @param Bundle extras
   	*/
    public void onStatusChanged(String provider, int status, Bundle extras) 
    {
    	// TODO Auto-generated method stub
    }

    /**
   	* This method alerts if provider is disabled.
   	* @param provider
   	*/
    public void onProviderEnabled(String provider) 
    {
    	Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }
    
    /**
   	* This method alerts if provider is enabled.
   	* @param provider
   	*/
    public void onProviderDisabled(String provider) 
    {
    	Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }
    
    /**
   	* This method checks if internet or gprs is online.
   	* @return status
   	*/
    public boolean isOnline() 
    {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable())
        {
            Toast.makeText(getBaseContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
    return true; 
    }
      
    /**
   	* This method describes what  happens when the back key is pressed.
   	*/
    @Override
    public void onBackPressed() 
    {
    	// user must logout first.
		new AlertDialog.Builder(MainActivity.this).setTitle(" ").setMessage("Please use the Log Out button at the top to logout first!").setIcon(R.drawable.warning).setNeutralButton("Close", null).show();  			        
    }
}
