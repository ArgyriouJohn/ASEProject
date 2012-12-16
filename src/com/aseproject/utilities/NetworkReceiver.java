package com.aseproject.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
    
/**
 *Class which acts like a receiver. Through the whole of the application whenever the connection is lost this class will act accordingly
 *Avoiding crash of the application and unresponsiveness
 * @author Socratis Michaelides 2012
 */
public class NetworkReceiver extends BroadcastReceiver 
{
    private static final String LOGTAG = "NetworkReceiver";
	private LocationManager locationManager;
	public static final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
	Context mContext;
	
	AseMapApplication app; 

	@Override
    public void onReceive(Context ctx, Intent intent) 
	{
		Log.i(LOGTAG, "Action: " + intent.getAction());
    	if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) 
    	{
		    NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		    String typeName = info.getTypeName();
		    String subtypeName = info.getSubtypeName();
		    boolean available = info.isAvailable();
		    Log.i(LOGTAG, "Network Type: " + typeName + ", subtype: " + subtypeName+ ", available: " + available);
		    
		    app = (AseMapApplication) ctx.getApplicationContext();
	
		    if(!available)
		    {
	            app.setNetworkStatus(false);
		    	Intent i = new Intent(ctx, ConnectivityIssue.class);
		    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    	ctx.startActivity(i);
		    }
		    else 
		    {
	            app.setNetworkStatus(true);
	            Log.d(LOGTAG,"Network status =true");
		    } 
	    }
        if(intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
        {
        	locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(enabled)
            {
            	Log.d(LOGTAG,"GPS Status Changed to TRUE");
            	app.setGPSstatus(true); 	
            }
            else
            {
        	    Log.d(LOGTAG,"GPS Status Changed to FALSE");
            	app.setGPSstatus(false);
               // Intent gpsintent = new Intent(ctx, ConnectivityIssue.class);
                //gpsintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // ctx.startActivity(gpsintent);
     	    }
            // Get the location manager
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
    	}
    }
}
